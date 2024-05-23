package edu.duke.projectTeam8;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.text.SimpleDateFormat;

import com.google.protobuf.TextFormat.ParseException;
import com.mysql.cj.jdbc.exceptions.SQLError;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import java.io.IOException;

import java.sql.*;

import java.security.NoSuchAlgorithmException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.HashMap;

public class MySQLDatabase extends AbstractDatabase {

    public MySQLDatabase() throws SQLException {
        super("jdbc", "mysql", "vcm-39854.vm.duke.edu", "3306", "ece651", "root", "rootpassword");
    }

    public void cleanTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM attendance");
            statement.executeUpdate("ALTER TABLE attendance AUTO_INCREMENT = 1");
            statement.executeUpdate("DELETE FROM enrollment");
            statement.executeUpdate("DELETE FROM courses");
            statement.executeUpdate("DELETE FROM users");
        }
    }

    public User constructUserFromResult(ResultSet result) throws SQLException {
        return new DatabaseUser(getNameFromResult(result), result.getString("net_id"), result.getString("email"), this, result.getString("type"));
    }

    public Professor constructProfessorFromResult(ResultSet result, String id_column) throws SQLException {
        return new DatabaseProfessor(getNameFromResult(result), result.getString(id_column), result.getString("email"), this);
    }

    public Student constructStudentFromResult(ResultSet result) throws SQLException {
        return new DatabaseStudent(getNameFromResult(result), result.getString("net_id"), result.getString("email"), this);
    }

    public Course constructCourseFromResult(ResultSet result) throws SQLException {
        return new DatabaseCourse(result.getString("course_id"), constructProfessorFromResult(result, "professor_id"), this);
    }

    public Lecture constructLectureFromResult(ResultSet result) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        return new DatabaseLecture(constructCourseFromResult(result), sdf.parse(result.getString("date")), this);
    }

    public Attendance constructAttendanceFromResult(ResultSet result) throws SQLException {
        Attendance attendance = new Attendance();
        Map<String, Consumer<Attendance>> map = new HashMap<>();
        map.put("Attended", (value) -> value.markAttended());
        map.put("Absent", (value) -> value.markAbsent());
        map.put("Tardy", (value) -> value.markTardy());
        String status = result.getString("attendance");
        map.get(status).accept(attendance);
        return attendance;
    }

    @Override
    public User getUser(String id) throws SQLException {
        return getPerson(id, (result) -> constructUserFromResult(result), null);
    }

    public Professor getProfessor(String id) throws SQLException {
        return getPerson(id, (result) -> constructProfessorFromResult(result, "net_id"), "p");
    }

    public Professor getProfessorByCourse(String courseID) throws SQLException {
        return getRow((result) -> constructProfessorFromResult(result, "net_id"), buildQuery("SELECT * FROM", "courses", "LEFT JOIN users ON users.net_id = courses.professor_id","WHERE course_id = '" + courseID + "'"));
    }

    public Student getStudent(String id) throws SQLException {
        return getPerson(id, (result) -> constructStudentFromResult(result), "s");
    }


    public Set<Professor> getAllProfessors() throws Exception {
        return getAllPeople((result) -> constructProfessorFromResult(result, "net_id"), "p");
    }

    public Set<Student> getAllStudents() throws Exception {
        return getAllPeople((result) -> constructStudentFromResult(result), "s");
    }

    public Set<Course> getClasses(String netID, String type, String table) throws Exception {
        String joinClause = "";
        if (table.equals("enrollment")) {
            joinClause = "LEFT JOIN courses ON courses.course_id = enrollment.course_id LEFT JOIN users ON users.net_id = courses.professor_id";
        } else {
            joinClause = "LEFT JOIN users ON users.net_id = courses.professor_id";
        }
        return getAllRows((result) -> constructCourseFromResult(result), buildQuery("SELECT * FROM", table, joinClause, "WHERE", type, "= '" + netID + "'"));
    }

    protected <T> T getPerson(String id, ThrowingFunction<ResultSet, T, SQLException> constructor, String type) throws SQLException {
        String whereClause = "WHERE net_id = '" + id + "'";
        if (type != null) {
            whereClause += " AND type = '" + type + "'";
        }
        return getRow(constructor, buildQuery("SELECT * FROM", "users", whereClause));
    }

    protected <T> T getRow(ThrowingFunction<ResultSet, T, SQLException> constructor, String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                return constructor.apply(result);
            } else {
                throw new SQLException("No results found");
            }
        }
    }

    public Set<Student> getActiveEnrollment(String courseID) throws Exception {
        return getAllRows((result) -> constructStudentFromResult(result), buildQuery("SELECT * FROM", "enrollment", "INNER JOIN users ON student_id = net_id", "WHERE course_id = '" + courseID + "'", "AND status = 1"));
    }


    protected <T> Set<T> getAllPeople(ThrowingFunction<ResultSet, T, Exception> constructor, String type) throws Exception {
        String whereClause = "";
        if (type != null) {
            whereClause = "WHERE type = '" + type + "'";
        }
        return getAllRows(constructor, buildQuery("SELECT * FROM", "users", whereClause));
    }

    protected <T> Set<T> getAllRows(ThrowingFunction<ResultSet, T, Exception> constructor, String query) throws Exception {
        Set<T> set = new HashSet<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                set.add(constructor.apply(results));
            }
        }
        return set;
    }


    @FunctionalInterface
    protected interface ThrowingFunction<I, T, E extends Exception> {
        T apply(I i) throws E;
    }
    public interface ThrowingBiFunction<I, J, T, E extends Exception> {
        T apply(I i, J j) throws E;
    }

    protected <K, V> Map<K, V> mapAllRows(ThrowingFunction<ResultSet, V, SQLException> constructor, String query, String key, ThrowingBiFunction<ResultSet, String, K, Exception> getFunction) throws Exception {
        Map<K, V> map = new HashMap<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                map.put(getFunction.apply(results, key), constructor.apply(results));
            }
        }
        return map;
    }

    protected Name getNameFromResult(ResultSet result) throws SQLException {
        String preferredName = result.getString("preferred_name");
        if (preferredName != null) {
            return new Name(result.getString("first_name"), result.getString("last_name"), result.getString("preferred_name"));
        }
        else {
            return new Name(result.getString("first_name"), result.getString("last_name"));
        }
    }

    public void modifyPreferredName(String id, String preferredName) throws SQLException {
        Map<String, String> updates = new HashMap<>();
        updates.put("preferred_name", preferredName);
        String query = buildUpdateQuery("users", "net_id", id, updates);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

    public void subscribeToWeeklyReport(String netID, String courseID, boolean value) throws SQLException {
        String query = "UPDATE enrollment SET notification = " + value + " WHERE student_id = '" + netID + "' AND course_id = '" + courseID + "'";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

    public void modifyAttendanceRecord(String netID, String courseID, String date, String attendance) throws SQLException {
        StringBuilder builder = new StringBuilder("UPDATE attendance SET attendance = '");
        builder.append(attendance);
        builder.append("' WHERE student_id = '");
        builder.append(netID);
        builder.append("' AND course_id = '");
        builder.append(courseID);
        builder.append("' AND date = '");
        builder.append(date);
        builder.append("'");
        String query = builder.toString();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

    public Attendance getAttendance(String netID, String courseID, String date) throws SQLException {
        StringBuilder builder = new StringBuilder("SELECT attendance FROM attendance WHERE student_id = '");
        builder.append(netID);
        builder.append("' AND course_id = '");
        builder.append(courseID);
        builder.append("' AND date = '");
        builder.append(date);
        builder.append("'");
        String query = builder.toString();
        return getRow((status) -> constructAttendanceFromResult(status), query);
    }

    public void addAttendanceRecord(String netID, String courseID, String date, String attendance) throws SQLException {
        StringBuilder builder = new StringBuilder("INSERT INTO attendance (course_id, student_id, attendance, date) VALUES ('");
        builder.append(courseID);
        builder.append("', '");
        builder.append(netID);
        builder.append("', '");
        builder.append(attendance);
        builder.append("', '");
        builder.append(date);
        builder.append("')");
        String query = builder.toString();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }


    public Set<Date> getValidAttendanceDates(String netID, String courseID) throws Exception, ParseException {
        StringBuilder builder = new StringBuilder("SELECT date FROM attendance WHERE student_id = '");
        builder.append(netID);
        builder.append("' AND course_id = '");
        builder.append(courseID);
        builder.append("'");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Set<Date> date = getAllRows((result) -> sdf.parse(result.getString("date")), builder.toString());
        return date;

    }


    public List<Lecture> getLecturesList(String courseID) throws Exception {
        Set<Lecture> set = getAllRows((result) -> constructLectureFromResult(result), buildQuery("SELECT DISTINCT attendance.course_id, attendance.date, professor_id, net_id, first_name, last_name, preferred_name, email FROM", "attendance", "LEFT JOIN courses ON courses.course_id = attendance.course_id LEFT JOIN users ON net_id = professor_id", "WHERE attendance.course_id = '" + courseID + "'"));
        List<Lecture> list = new ArrayList<>();
        for (Lecture lecture : set) {
            list.add(lecture);
        }
        return list;
    }

    public Map<Date, Attendance> getAttendanceMap(String netID, String courseID) throws Exception {
        String query = "SELECT date, attendance FROM attendance WHERE student_id = '" + netID + "' AND course_id = '" + courseID + "'";
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        return mapAllRows((status) -> constructAttendanceFromResult(status), query, "date", (results, key) -> sdf.parse(results.getString(key)));


    }

    public Boolean isSubscribedToWeeklyReport(String netID, String courseID) throws SQLException {
        String query = "SELECT notification FROM  enrollment WHERE student_id = '" + netID + "' AND course_id = '" + courseID + "'";
        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                return result.getBoolean("notification");
            } else {
                throw new SQLException("No results found");
            }
        }
    }

    // TODO: review the date format
    public Map<Student, Attendance> getLectureAttendance(String courseID, String date) throws Exception {
        String query = "SELECT * FROM attendance INNER JOIN users ON net_id = student_id WHERE date = '" + date + "' AND course_id = '" + courseID + "'";
        return mapAllRows((status) -> constructAttendanceFromResult(status), query, "net_id", (results, key) -> constructStudentFromResult(results));
    }


    public void updateHashSalt(String id, String passwordHash, String salt) throws SQLException {
        Map<String, String> updates = new HashMap<>();
        updates.put("hashed_key", passwordHash);
        String query = buildUpdateQuery("users", "net_id", id, updates);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
        Map<String, String> updateSalt = new HashMap<>();
        updateSalt.put("salt", salt);
        String saltQuery = buildUpdateQuery("users", "net_id", id, updateSalt);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(saltQuery);
        }
    }

    public String getSalt(String id) throws SQLException {
        return selectSingleItem("salt", "users", "net_id", id);
    }

    public String getHashedKey(String id) throws SQLException {
        return selectSingleItem("hashed_key", "users", "net_id", id);
    }

    protected String selectSingleItem(String column, String table, String primaryKeyColumn, String primaryKeyValue) throws SQLException {
        String query = buildQuery("SELECT", column, "FROM", table, "WHERE", primaryKeyColumn, " = '" + primaryKeyValue + "'");
        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                return result.getString(column);
            } else {
                throw new SQLException("No results found");
            }
        }
    }

    protected String buildUpdateQuery(String table, String primaryKeyColumn, String primaryKeyValue, Map<String, String> updates) {
        StringBuilder updateString = new StringBuilder();
        for (String column : updates.keySet()) {
            updateString.append(column);
            updateString.append(" = '");
            updateString.append(updates.get(column));
            updateString.append("'");
        }
        return buildQuery("UPDATE", table, "SET", updateString.toString(), "WHERE", primaryKeyColumn, "= '" + primaryKeyValue + "'");
    }

    protected String buildQuery(String... clauses) {
        StringBuilder builder = new StringBuilder();
        for (String clause : clauses) {
            builder.append(" ");
            builder.append(clause);
        }
        return builder.toString();
    }


    // ================================================================================================
    // ================================================================================================
    // The following methods are all for the admin app
    // ================================================================================================
    // ================================================================================================

    public void importUsers(String filePath) throws SQLException, IOException, NoSuchAlgorithmException {
        Map<String, Integer> columnIndex = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String[] headers = br.readLine().split(",");
            for (int i = 0; i < headers.length; i++) {
                columnIndex.put(headers[i].trim().toLowerCase(), i);
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String netId = values[columnIndex.get("netid")].trim();
                String firstName = values[columnIndex.get("first name")].trim();
                String lastName = values[columnIndex.get("last name")].trim();
                String type = values[columnIndex.get("type")].trim();
                String password = values[columnIndex.get("password")].trim();
                String email = values[columnIndex.get("email")].trim();

                String salt = SHA256Utils.makeSaltStr();
                String passwordHash = SHA256Utils.hashPassword(password.getBytes(StandardCharsets.UTF_8), salt);

                try (PreparedStatement pstmt = connection.prepareStatement(
                        "INSERT INTO users (net_id, first_name, last_name, preferred_name, hashed_key, salt, type, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                    pstmt.setString(1, netId);
                    pstmt.setString(2, firstName);
                    pstmt.setString(3, lastName);
                    pstmt.setNull(4, Types.VARCHAR); // preferred_name can be null
                    pstmt.setString(5, passwordHash);
                    pstmt.setString(6, salt);
                    pstmt.setString(7, type);
                    pstmt.setString(8, email);
                    pstmt.executeUpdate();
                }
            }
        }
    }

    public void importCourses(String filePath) throws SQLException, IOException {
        Map<String, Integer> columnIndex = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String[] headers = br.readLine().split(",");
            for (int i = 0; i < headers.length; i++) {
                columnIndex.put(headers[i].trim().toLowerCase(), i);
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String courseId = values[columnIndex.get("course id")].trim();
                String professorId = values[columnIndex.get("professor netid")].trim();
                if (courseId == null || professorId == null) {
                    throw new IllegalArgumentException("The CSV file is missing 'Course ID' or 'Professor netID' column.");
                }

                try (PreparedStatement pstmt = connection.prepareStatement(
                        "INSERT INTO courses (course_id, professor_id) VALUES (?, ?)")) {
                    pstmt.setString(1, courseId);
                    pstmt.setString(2, professorId);
                    pstmt.executeUpdate();
                }
            }
        }
    }

    public void importEnrollment(String filePath) throws SQLException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String[] headers = br.readLine().split(",");
            Map<String, Integer> columnIndex = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                columnIndex.put(headers[i].trim().toLowerCase(), i);
            }

            Integer courseIndex = columnIndex.get("course id");
            Integer studentIndex = columnIndex.get("student netid");
            if (courseIndex == null || studentIndex == null) {
                throw new IllegalArgumentException("The CSV file is missing 'Course ID' or 'Student netID' column.");
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String courseId = values[courseIndex].trim();
                String studentId = values[studentIndex].trim();

                String insertSql = "INSERT INTO enrollment (course_id, student_id) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
                    pstmt.setString(1, courseId);
                    pstmt.setString(2, studentId);
                    pstmt.executeUpdate();
                }
            }
        }
    }

  // ===========================================================
  public void checkCourseExists(String courseId) throws SQLException, IllegalArgumentException {
    String sql = "SELECT COUNT(*) FROM courses WHERE course_id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, courseId);
      ResultSet rs = pstmt.executeQuery();
      if (!rs.next() || rs.getInt(1) == 0) {
        throw new IllegalArgumentException("Course does not exist.");
      }
    }
  }

  public Boolean checkStudentEnrolledInClass(String courseId, String studentId)
      throws SQLException, IllegalArgumentException {
    String sql = "SELECT status FROM enrollment WHERE course_id = ? AND student_id = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, courseId);
      pstmt.setString(2, studentId);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        int status = rs.getInt("status");
        if (status != 0) {
          return true;
        }
        else {
          return false;
        }
      }
      else {
        return false;
      }
    }
  }

    public void checkStudentInClassForEnroll(String courseId, String studentId)
            throws SQLException, IllegalArgumentException {
        String sql = "SELECT status FROM enrollment WHERE course_id = ? AND student_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            pstmt.setString(2, studentId);
            ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        int status = rs.getInt("status");
        if (status != 0) {
          throw new IllegalArgumentException("Student is already actively enrolled in this course.");
        }
      }
    }
  }
  public void enrollStudentInClass(String courseId, String studentId) throws SQLException {
    String courseIdBase = courseId.split("-")[0];  

    
    String checkSectionSql = "SELECT course_id FROM enrollment WHERE student_id = ? AND course_id LIKE ? AND status = 1";
    try (PreparedStatement pstmt = connection.prepareStatement(checkSectionSql)) {
        pstmt.setString(1, studentId);
        pstmt.setString(2, courseIdBase + "%");  
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String enrolledCourseId = rs.getString("course_id");
            throw new SQLException("Student is already enrolled in an active section of this course: " + enrolledCourseId);
        }
    }


    String checkSql = "SELECT status FROM enrollment WHERE course_id = ? AND student_id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(checkSql)) {
        pstmt.setString(1, courseId);
        pstmt.setString(2, studentId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            int status = rs.getInt("status");
            if (status == 0) {
                String updateSql = "UPDATE enrollment SET status = 1 WHERE course_id = ? AND student_id = ?";
                try (PreparedStatement updatePstmt = connection.prepareStatement(updateSql)) {
                    updatePstmt.setString(1, courseId);
                    updatePstmt.setString(2, studentId);
                    updatePstmt.executeUpdate();
                }
            }
        } else {
            String insertSql = "INSERT INTO enrollment (course_id, student_id, status) VALUES (?, ?, 1)";
            try (PreparedStatement insertPstmt = connection.prepareStatement(insertSql)) {
                insertPstmt.setString(1, courseId);
                insertPstmt.setString(2, studentId);
                insertPstmt.executeUpdate();
            }
        }
    }
  }




  public void createNewClass(String courseId, String professorId) throws SQLException {
    String checkUserExistsSql = "SELECT 1 FROM users WHERE net_id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(checkUserExistsSql)) {
        pstmt.setString(1, professorId);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            throw new SQLException("No such user with provided professor ID.");
        }
    }

    String checkProfessorSql = "SELECT 1 FROM users WHERE net_id = ? AND type = 'p'";
    try (PreparedStatement pstmt = connection.prepareStatement(checkProfessorSql)) {
        pstmt.setString(1, professorId);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            throw new SQLException("Invalid professor ID: Not a professor.");
        }
    }


    String checkCourseSql = "SELECT 1 FROM courses WHERE course_id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(checkCourseSql)) {
        pstmt.setString(1, courseId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            throw new SQLException("Course ID already exists.");
        }
    }

    String sql = "INSERT INTO courses (course_id, professor_id) VALUES (?, ?)";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, courseId);
        pstmt.setString(2, professorId);
        pstmt.executeUpdate();
    }
}


  // public void enrollStudentInClass(String courseId, String studentId) throws SQLException {

  //   String checkSql = "SELECT status FROM enrollment WHERE course_id = ? AND student_id = ?";
  //   boolean isEnrolled = false;
  //   try (PreparedStatement pstmt = connection.prepareStatement(checkSql)) {
  //     pstmt.setString(1, courseId);
  //     pstmt.setString(2, studentId);
  //     ResultSet rs = pstmt.executeQuery();

  //     if (rs.next()) {
  //       isEnrolled = true;
  //       int status = rs.getInt("status");

  //       if (status == 0) {
  //         String updateSql = "UPDATE enrollment SET status = 1 WHERE course_id = ? AND student_id = ?";
  //         try (PreparedStatement updatePstmt = connection.prepareStatement(updateSql)) {
  //           updatePstmt.setString(1, courseId);
  //           updatePstmt.setString(2, studentId);
  //           updatePstmt.executeUpdate();
  //         }
  //       }
  //     } else {
  //       String insertSql = "INSERT INTO enrollment (course_id, student_id) VALUES (?, ?)";
  //       try (PreparedStatement insertPstmt = connection.prepareStatement(insertSql)) {
  //         insertPstmt.setString(1, courseId);
  //         insertPstmt.setString(2, studentId);
  //         insertPstmt.executeUpdate();
  //       }
  //     }
  //   }
  // }

    public void checkStudentInClassForDrop(String courseId, String studentId)
            throws SQLException, IllegalArgumentException {
        String sql = "SELECT status FROM enrollment WHERE course_id = ? AND student_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            pstmt.setString(2, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new IllegalArgumentException("Student is not enrolled in this course.");
            }

            int status = rs.getInt("status");
            if (status == 0) {
                throw new IllegalArgumentException("Student has already dropped this course.");
            }
        }
    }

  public void checkStudentExistsInSchool(String studentId) throws SQLException, IllegalArgumentException {
    String sql = "SELECT COUNT(*) FROM users WHERE net_id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, studentId);
      ResultSet rs = pstmt.executeQuery();
      if (!rs.next() || rs.getInt(1) == 0) {
        throw new IllegalArgumentException(
            "Student does not exist in school. If you want to add new students that is not in the school, please enroll them first.");
      }
    }
  }

    public void checkStudentNotExistInSchool(String studentId) throws SQLException, IllegalArgumentException {
        String sql = "SELECT COUNT(*) FROM users WHERE net_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) != 0) {
                throw new IllegalArgumentException("Student already exists in school.");
            }
        }
    }

    public void enrollStudentToSchool(String studentID, String firstName, String lastName, String password, String email)
            throws SQLException, NoSuchAlgorithmException {
        String salt = SHA256Utils.makeSaltStr();
        String passwordHash = SHA256Utils.hashPassword(password.getBytes(StandardCharsets.UTF_8), salt);

    try (PreparedStatement pstmt = connection.prepareStatement(
        "INSERT INTO users (net_id, first_name, last_name, preferred_name, hashed_key, salt, type, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
      pstmt.setString(1, studentID);
      pstmt.setString(2, firstName);
      pstmt.setString(3, lastName);
      pstmt.setNull(4, Types.VARCHAR); // preferred_name can be null
      pstmt.setString(5, passwordHash);
      pstmt.setString(6, salt);
      pstmt.setString(7, "s");
      pstmt.setString(8, email);
      pstmt.executeUpdate();
    }
  }

  public void enrollProfessorToSchool(String ProfID, String firstName, String lastName, String password, String email) throws SQLException, NoSuchAlgorithmException {
    String salt = SHA256Utils.makeSaltStr();
    String passwordHash = SHA256Utils.hashPassword(password.getBytes(StandardCharsets.UTF_8), salt);

    try (PreparedStatement pstmt = connection.prepareStatement(
        "INSERT INTO users (net_id, first_name, last_name, preferred_name, hashed_key, salt, type, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
      pstmt.setString(1, ProfID);
      pstmt.setString(2, firstName);
      pstmt.setString(3, lastName);
      pstmt.setNull(4, Types.VARCHAR); // preferred_name can be null
      pstmt.setString(5, passwordHash);
      pstmt.setString(6, salt);
      pstmt.setString(7, "p");
      pstmt.setString(8, email);
      pstmt.executeUpdate();
    }
  }

    public void dropStudent(String courseId, String studentId) throws SQLException {
        String sql = "UPDATE enrollment SET status = 0 WHERE course_id = ? AND student_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            pstmt.setString(2, studentId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                // Don't think this will ever be thrown cuz we have checked these.
                throw new SQLException("No rows updated. Either the course ID or the student ID does not exist.");
            }
        }
    }


}
