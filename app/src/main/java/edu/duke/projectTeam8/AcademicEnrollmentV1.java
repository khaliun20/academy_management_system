package edu.duke.projectTeam8;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.NoSuchElementException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.stream.Collectors;


/**
 * This is the master list of all of the enrolled students at the school
 */
public class AcademicEnrollmentV1 implements AcademicEnrollment {
    private final Map<String, OldStudent> students;
    private final String fileName;
    private final Map<String, Function<OldStudent, Object>> dataMap;
    private final Map<String, OldCourse> courseListing = new HashMap<>();
    private final Map<String, OldProfessor> professorDirectory = new HashMap<>();
    private StringCryptoUtilAES encrypter;

    /**
     * Creates the Academic Enrollment object for a school
     */
    public AcademicEnrollmentV1(String fileName) {
        this.students = new HashMap<>();
        this.fileName = fileName;
        this.dataMap = new HashMap<>(); // Is this the right place to create this Map?
        dataMap.put("netID", (student) -> student.getNetID());
        dataMap.put("first name", (student) -> student.getName().getFirstName());
        dataMap.put("last name", (student) -> student.getName().getLastName());
        dataMap.put("email", (student) -> student.getEmail());
        try {
            this.encrypter = new StringCryptoUtilAES("TempPassword");
        } catch (Exception e) {
            System.out.println("suppressed Initialization Exception");
        }
    }

    /**
     * Creates the Academic Enrollment object for a test school
     */
    public AcademicEnrollmentV1() {
        this("Test");
    }

    /**
     * Creates a student and adds the student to the enrollment
     *
     * @param name is the Name object for the student
     */
    public void createStudent(Name name, String netID, String email) throws IOException {
        OldStudent oldStudent = new OldStudent(name, netID, email);
        students.put(netID, oldStudent);
    }

    /**
     * Get a student by their netID
     *
     * @param netID is the netID of the student
     * @return the student object
     */
    public OldStudent getStudentByNetID(String netID) {
        OldStudent oldStudent = students.get(netID);
        if (oldStudent == null) {
            throw new IllegalArgumentException("There is no record of a student with netID: " + netID + "\n");
        }
        return oldStudent;
    }

    /**
     * check if a student exists in the enrollment
     *
     * @param netID is the netID of the student
     * @return true if the student exists, false otherwise
     */
    public boolean studentExists(String netID) {
        return students.containsKey(netID);
    }

    public int getNumberOfStudents() {
        return students.size();
    }

    public Set<Student> getStudents() {
        HashSet<Student> oldStudentSet = new HashSet<>();
        for (String netID : students.keySet()) {
            oldStudentSet.add(students.get(netID));
        }
        return oldStudentSet;
    }

    public Serializer serialize(BiFunction<String, Object, Serializer> leafConstructor,
                                BiFunction<String, Iterable<Serializer>, Serializer> iterableConstructor) throws Exception {
        Collection<Serializer> students = new ArrayList<>();
        for (Student oldStudent : getStudents()) {
            students.add(((OldStudent) oldStudent).serialize(leafConstructor, iterableConstructor));
        }
        return iterableConstructor.apply("enrollment", students);
    }

    public OldProfessor findProfessorInDirectory(String professorNetID) {
        return professorDirectory.get(professorNetID);
    }

    @Override
    public Boolean isStudent(User user) {
        String desiredNetID = user.getNetID();
        for (String netID : students.values().stream().map(Student::getNetID).collect(Collectors.toSet())) {
            if (desiredNetID.equals(netID)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Database getDatabase() {
        return null;
    }

    public void addProfessor(Name name, String netID, String emailAddress) {
        professorDirectory.put(netID, new OldProfessor(name, netID, emailAddress));
    }

    public String getSerializedStr() throws Exception {
        return serialize((string1, object) -> new XMLSerializer(string1, object),
                (string2, objects) -> new XMLSerializer(string2, objects)).toString();
    }

    public void saveAcademicEnrollment() throws IOException {
        saveAcademicEnrollment(fileName);
    }

    public void saveAcademicEnrollment(String fileName) throws IOException {

        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        try {
            String encryptedData = encrypter.doEncryption(getSerializedStr());
            Files.write(path, encryptedData.getBytes());

        } catch (Exception e) {
            System.err.println("Encryption error");
        }


    }

    @Deprecated
    public Exception plainTextLoadAcademicEnrollment(String fileName) throws IOException, ParseException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder xml = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                xml.append(line);
                xml.append("\n");
            }
            parseAcademicEnrollment(xml.toString());
        } catch (Exception fnfe) {
            System.err.println(fnfe.getMessage());
            return fnfe;
        }
        return null;
    }

    public void loadAcademicEnrollment() throws IOException {

        Path filePath = Paths.get(fileName);
        if (!Files.exists(filePath))
            throw new FileNotFoundException(fileName + " (No such file or directory)");

        byte[] fileBytes = Files.readAllBytes(filePath);
        if (fileBytes.length == 0) {
            System.out.println("No content in file.");
            return;
        }
        String encryptedText = new String(fileBytes);
        try {
            String xml = encrypter.doDecryption(encryptedText);
            parseAcademicEnrollment(xml);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public void parseAcademicEnrollment(String xml) throws Exception { // Code Smell: This is huge. It
        // is basically recreating every
        // instance from the file, but it
        // would be better to break this
        // up for each type.
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Map<String, Consumer<Attendance>> markAttendanceMap = new HashMap<>();
        markAttendanceMap.put("Absent", (attendance) -> attendance.markAbsent());
        markAttendanceMap.put("Attended", (attendance) -> attendance.markAttended());
        markAttendanceMap.put("Tardy", (attendance) -> attendance.markTardy());
        XMLParser parser = new XMLParser();
        ParsedClass enrollment = parser.parse(xml.toString());
        BiFunction<String, String, String> getParsedData = (student, parameter) -> enrollment.getParameter(student)
                .getParameterData(parameter);
        for (String student : enrollment.getParameters()) {
            createStudent(new Name(getParsedData.apply(student, "first name"), getParsedData.apply(student, "last name")),
                    getParsedData.apply(student, "netID"), getParsedData.apply(student, "email"));
            OldStudent oldStudentInstance = getStudentByNetID(getParsedData.apply(student, "netID"));
            ParsedClass parsedStudent = enrollment.getParameter(student);
            for (String course : parsedStudent.getParameter("course list").getParameters()) {
                ParsedClass parsedCourse = parsedStudent.getParameter("course list").getParameter(course);
                String courseID = parsedCourse.getParameterData("course id");
                OldCourse oldCourseInstance;
                if (courseListing.keySet().contains(courseID)) {
                    oldCourseInstance = courseListing.get(courseID);
                } else {
                    String subject = courseID.replaceAll("[^A-Za-z]", "");
                    Integer code = Integer.parseInt(courseID.replaceAll("[^0-9]", ""));
                    OldProfessor oldProfessor;
                    ParsedClass parsedProfessor = parsedCourse.getParameter("professor");
                    if (professorDirectory.keySet().contains(parsedProfessor.getParameterData("netID"))) {
                        oldProfessor = professorDirectory.get(parsedProfessor.getParameterData("netID"));
                    } else {
                        oldProfessor = new OldProfessor(
                                new Name(parsedProfessor.getParameterData("first name"), parsedProfessor.getParameterData("last name")),
                                parsedProfessor.getParameterData("netID"), parsedProfessor.getParameterData("email"));
                        professorDirectory.put(parsedProfessor.getParameterData("netID"), oldProfessor);
                    }
                    oldCourseInstance = new OldCourse(new CourseID(subject, code), oldProfessor); // Need to add Professor to this
                    courseListing.put(courseID, oldCourseInstance);
                }
                oldCourseInstance.enrollStudent(oldStudentInstance);
                for (String records : parsedCourse.getParameter("attendance records").getParameters()) {
                    ParsedClass parsedRecord = parsedCourse.getParameter("attendance records").getParameter(records);
                    Date date = dateFormat.parse(parsedRecord.getParameterData("date"));
                    OldLecture oldLecture;
                    try {
                        oldLecture = (OldLecture) oldCourseInstance.getCertainLecture(date);
                    } catch (NoSuchElementException e) {
                        oldLecture = new OldLecture(oldCourseInstance, date);
                        oldCourseInstance.addLecture(oldLecture); // Shouldn't have to do this (make it work in Constructor instead
                    }
                    oldLecture.recordAttendance(oldStudentInstance, markAttendanceMap.get(parsedRecord.getParameterData("attendance")));
          /*
          studentInstance.addAttendanceRecord(courseInstance, date, new Attendance());
          studentInstance.getAttendanceRecord(courseInstance).modifyRecord(date,
              markAttendanceMap.get(parsedRecord.getParameterData("attendance")));
          */
                }
            }
        }
    }

}
