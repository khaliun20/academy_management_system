package admin;

import edu.duke.projectTeam8.Database;
import edu.duke.projectTeam8.MySQLDatabase;

import java.util.function.Function;

public class DatabaseConnector {
  protected Database database;
  protected DataImporter importer;

  public DatabaseConnector() throws Exception {
    database = new MySQLDatabase();
  }

  public void setDataImporter(DataImporter importer) {
    this.importer = importer;
  }

  public void setDataImporter(Function<Database, DataImporter> importerConstructor) {
    this.importer = importerConstructor.apply(database);
  }

  public void importData() throws Exception {
    importer.importData();
  }

  public void setImporterPath(String path) {
    importer.setFilePath(path);
  }

  public void cleanDatabase() throws Exception {
    database.cleanTables();
  }

  public void checkStudentExistsInSchool(String netID) throws Exception {
    database.checkStudentExistsInSchool(netID);
  }

  public void checkCourseExists(String courseID) throws Exception {
    database.checkCourseExists(courseID);
  }

  public Boolean checkStudentEnrolled(String courseID, String studentID) throws Exception {
    return database.checkStudentEnrolledInClass(courseID, studentID);
  }

  public void enrollStudent(String courseID, String studentID) throws Exception {
    database.enrollStudentInClass(courseID, studentID);
  }

  public void dropStudent(String courseID, String studentID) throws Exception {
    database.dropStudent(courseID, studentID);
  }

  public void addStudent(String netID, String firstName, String lastName, String email) throws Exception {
    isValid(netID, "^[a-zA-Z]+[0-9]+$");
    isValid(firstName, "^[a-zA-Z]+$");
    isValid(lastName, "^[a-zA-Z]+$");
    isValid(email, "^[^\\.\\s][\\w\\-]+(\\.[\\w\\-]+)*@([\\w-]+\\.)+[\\w-]{2,}$");
    String password = "00000000";
    database.enrollStudentToSchool(netID, firstName, lastName, password, email);
  }

  public void addCourse(String courseID, String netID) throws Exception {
    isValid(courseID, "^[a-zA-Z]+[0-9]+-[0-9]+$");
    database.createNewClass(courseID, netID);
  }

  public void addProfessor(String netID, String firstName, String lastName, String email) throws Exception {
    isValid(netID, "^[a-zA-Z]+[0-9]+$");
    isValid(firstName, "^[a-zA-Z]+$");
    isValid(lastName, "^[a-zA-Z]+$");
    isValid(email, "^[^\\.\\s][\\w\\-]+(\\.[\\w\\-]+)*@([\\w-]+\\.)+[\\w-]{2,}$");
    String password = "00000000";
    database.enrollProfessorToSchool(netID, firstName, lastName, password, email);
  }

  private void isValid(String read, String regex) {
    if (read.trim().isEmpty()) {
      throw new IllegalArgumentException("Please ensure there are no blank values");
    }
    if (!read.matches(regex)) {
      throw new IllegalArgumentException(read + " does not match the expected format");
    }
  }

}
