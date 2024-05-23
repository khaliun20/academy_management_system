package admin;

import edu.duke.projectTeam8.*;

import java.sql.SQLException;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import edu.duke.projectTeam8.MySQLDatabase;

public class App {

  private Scanner scanner;
  private MySQLDatabase db;

  public App() {
    this.scanner = new Scanner(System.in);
    try {
      this.db = new MySQLDatabase();
    } catch (SQLException e) {
      System.out.println("Failed to initialize the database connection: " + e.getMessage());
      this.db = null;
    }
  }

  public void run() {
    AdminGUI.main(new String[0]);
    /*
    while (true) {
      System.out.println("What would you like to do?");
      System.out.println("1. Import School List");
      System.out.println("2. Import Course List");
      System.out.println("3. Import Enrollment List");
      System.out.println("4. Add Student to Course");
      System.out.println("5. Enroll New Student to School");
      System.out.println("6. Drop Student");
      System.out.println("7. Clean Database");
      System.out.println("8. Exit");
      String option = scanner.nextLine();
      
      switch (option) {
      case "1":
        importUsersProcess();
        break;
      case "2":
        importCoursesProcess();
        break;
      case "3":
        importEnrollmentProcess();
        break;
      case "4":
        enrollExistingStudentProcess();
        break;
      case "5":
        enrollNewStudentProcess();
        break;
      case "6":
        dropStudentProcess();
        break;
      case "7":
        cleanDBProcess();
        break;
      case "8":
        exit();
        break;
      default:
        System.out.println("Invalid Choice");
      }
    }
    */
  }
  
  // ----------------------- 1 - 3 ----------------------------
  private String getValidFilePath() {
    while (true) {
      System.out.print("Please enter the file path or type 'R' to go back: \n");
      String filePath = scanner.nextLine();
      
      if ("R".equalsIgnoreCase(filePath)) {
        return null;
      }
      
      File file = new File(filePath);
      if (file.exists() && file.isFile()) {
        return filePath;
      } else {
        System.out.println("File does not exist, please enter a valid file path or 'RETURN' to go back.");
      }
    }
  }
  
  private void importUsersProcess() {
    String filePath = getValidFilePath();
    if (filePath == null) {
      System.out.println("Returning to main menu...");
      return;
    }
    try {
      db.importUsers(filePath);
      System.out.println("Users imported successfully.");
    } catch (IOException | SQLException | NoSuchAlgorithmException e) {
      System.out.println("Error importing users: " + e.getMessage());
    }
  }
  
  private void importCoursesProcess() {
    String filePath = getValidFilePath();
    if (filePath == null) {
      System.out.println("Returning to main menu...");
      return;
    }
    
    try {
      db.importCourses(filePath);
      System.out.println("Courses imported successfully.");
    } catch (IOException | SQLException e) {
      System.out.println("Error importing courses: " + e.getMessage());
    }
  }
  
  private void importEnrollmentProcess() {
    String filePath = getValidFilePath();
    if (filePath == null) {
      System.out.println("Returning to main menu...");
      return;
    }
    
    try {
      db.importEnrollment(filePath);
      System.out.println("Enrollment imported successfully.");
    } catch (IOException | SQLException e) {
      System.out.println("Error importing enrollment data: " + e.getMessage());
    }
  }
  
  // ----------------------- 4 ----------------------------
  
  private void enrollExistingStudentProcess() {
    System.out.print("Please enter the course ID or type 'R' to go back to main menu:\n");
    String courseId = scanner.nextLine();
    if ("R".equalsIgnoreCase(courseId)) {
      System.out.println("Returning to main menu...");
      return;
    }
    try {
      db.checkCourseExists(courseId);
    } catch (IllegalArgumentException | SQLException e) {
      System.out.println("Error: " + e.getMessage());
      return;
    }
    
    System.out.print("Please enter the student's netID or type 'R' to go back to main menu:\n");
    String studentId = scanner.nextLine();
    
    if ("R".equalsIgnoreCase(studentId)) {
      System.out.println("Returning to main menu...");
      return;
    }
    
    try {
      db.checkStudentExistsInSchool(studentId);
    } catch (IllegalArgumentException | SQLException e) {
      System.out.println("Error: " + e.getMessage());
      return;
    }
    try {
      db.checkStudentInClassForEnroll(courseId, studentId);
    } catch (IllegalArgumentException | SQLException e) {
      System.out.println("Error: " + e.getMessage());
      return;
    }
    
    try {
      db.enrollStudentInClass(courseId, studentId);
      System.out.println("Student enrolled in class successfully.");
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
  
  // ----------------------- 5 ----------------------------
  public String readString(String regex, String invalidMessage) {
    String read = null;
    while (read == null || read.trim().isEmpty() || !isValid(read, regex)) {
      read = scanner.nextLine();
      if (read == null || read.trim().isEmpty() || !isValid(read, regex)) {
        System.err.println(invalidMessage);
      }
    }
    return read;
  }
  
  private boolean isValid(String read, String regex) {
    if (read.matches(regex)) {
      return true;
    }
    return false;
  }
  
  private void enrollNewStudentProcess() {
    System.out.print("Please enter the student's netID\n");
    String studentID = readString("^[a-zA-Z]+[0-9]+$",
                                  "Please enter a valid netID, in the following format: xx000\n");
    try {
      db.checkStudentNotExistInSchool(studentID);
    } catch (IllegalArgumentException | SQLException e) {
      System.out.println("Error: " + e.getMessage());
      return;
    }
    System.out.print("Please enter the student's first name\n");
    String firstName = readString("^[a-zA-Z]+$", "Please enter a valid name, in the following format: John\n");
    System.out.print("Please enter the student's last name\n");
    String lastName = readString("^[a-zA-Z]+$", "Please enter a valid name, in the following format: Doe\n");
    System.out.print("Please enter the student's email\n");
    String email = readString("^[^\\.\\s][\\w\\-]+(\\.[\\w\\-]+)*@([\\w-]+\\.)+[\\w-]{2,}$",
                              "Please enter a valid email, in the following format: xxxx@xxx.xxx \n");
    String password = "00000000";
    try {
      db.enrollStudentToSchool(studentID, firstName, lastName, password, email);
      System.out.println("Student enrolled in school successfully.");
    } catch (IllegalArgumentException | SQLException | NoSuchAlgorithmException e) {
      System.out.println("Error: " + e.getMessage());
      return;
    }
  }
  
  // ----------------------- 6 ----------------------------
  private void dropStudentProcess() {
    System.out.print("Please enter the course ID or type 'R' to go back:\n");
    String courseId = scanner.nextLine();
    if ("R".equalsIgnoreCase(courseId)) {
      System.out.println("Returning to main menu...");
      return;
    }
    
    try {
      db.checkCourseExists(courseId);
    } catch (IllegalArgumentException | SQLException e) {
      System.out.println("Error: " + e.getMessage());
      return;
    }
    
    System.out.print("Please enter the student's netID or type 'R' to go back:\n");
    String studentId = scanner.nextLine();
    if ("R".equalsIgnoreCase(studentId)) {
      System.out.println("Returning to main menu...");
      return;
    }
    
    try {
      db.checkStudentInClassForDrop(courseId, studentId);
    } catch (IllegalArgumentException | SQLException e) {
      System.out.println("Error: " + e.getMessage());
      return;
    }
    
    try {
      db.dropStudent(courseId, studentId);
      System.out.println("Student dropped successfully.");
    } catch (IllegalArgumentException | SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
  
  // ----------------------- 7 ----------------------------
  private void cleanDBProcess() {
    System.out.println(
                       "Are you sure you want to clean the database? (Type Y to confirm and anything else to cancel)");
    String response = scanner.nextLine();
    if (response.equalsIgnoreCase("Y")) {
      try {
        db.cleanTables();
        System.out.println("Database cleaned successfully.");
      } catch (SQLException e) {
        System.out.println("Error cleaning the database: " + e.getMessage());
      }
    }
  }
  
  // ----------------------- 8 ----------------------------
  private void exit() {
    System.out.println("Are you sure you want to exit? (Type Y to confirm and anything else to cancel)");
    String response = scanner.nextLine();
    if (!response.equalsIgnoreCase("Y")) {
      return;
    }
    System.out.println("Exiting...");
    scanner.close();
    System.exit(0);
  }
  
  public static void main(String[] args) {
    App app = new App();
    app.run();
  }
}
