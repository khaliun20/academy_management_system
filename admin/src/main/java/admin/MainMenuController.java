package admin;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import java.io.File;

public class MainMenuController {
  protected AdminGUI view;
  protected DatabaseConnector model;

  @FXML
  protected TextField pathField;

  @FXML
  protected TextField netIDField;

  @FXML
  protected TextField courseIDField;

  @FXML
  protected TextField firstNameField;

  @FXML
  protected TextField lastNameField;

  @FXML
  protected TextField emailField;

  public MainMenuController(AdminGUI view, DatabaseConnector model) {
    this.view = view;
    this.model = model;
  }

  public void importSchoolList(ActionEvent event) {
    model.setDataImporter((database) -> new UserImporter(database));
    view.displayGetFilePathWindow();
  }

  public void importCourseList(ActionEvent event) {
    model.setDataImporter((database) -> new CourseImporter(database));
    view.displayGetFilePathWindow();
  }

  public void importEnrollmentList(ActionEvent event) {
    model.setDataImporter((database) -> new EnrollmentImporter(database));
    view.displayGetFilePathWindow();
  }

  public void submitFilePath(ActionEvent event) {
    try {
      String path = pathField.getText();
      PathValidator validator = new PathValidator(path);
      if (validator.confirmValidPath()) {
        model.setImporterPath(path);
        try {
          model.importData();
          view.displaySuccessfulAlert("Successful Import", "Your import was successful");
        }
        catch (Exception e) {
          view.displayUnsuccessfulAlert("Import was unsuccessful", e.getMessage());
        }
      }
      else {
        view.displayUnsuccessfulAlert("Import was unsuccessful", "The path is not valid");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void dropStudentMenu(ActionEvent event) {
    view.displayDropStudentMenu();
  }

  public void dropStudent(ActionEvent event) {
    String netID = netIDField.getText();
    String courseID = courseIDField.getText();
    try {
      model.checkStudentExistsInSchool(netID);
      model.checkCourseExists(courseID);
      if (model.checkStudentEnrolled(courseID, netID)) {
        model.dropStudent(courseID, netID);
        view.displaySuccessfulAlert("Dropped Successfully", "Student with netID " + netID + " was Successfully dropped from " + courseID);
      }
      else {
        throw new IllegalArgumentException("Student is not enrolled in this course");
      }
    }
    catch (Exception e) {
      view.displayUnsuccessfulAlert("Student could not be dropped", e.getMessage());
    }
  }

  public void enrollStudentMenu(ActionEvent event) {
    view.displayEnrollStudentMenu();
  }

  public void enrollStudent(ActionEvent event) {
    String netID = netIDField.getText();
    String courseID = courseIDField.getText();
    try {
      model.checkStudentExistsInSchool(netID);
      model.checkCourseExists(courseID);
      if (!model.checkStudentEnrolled(courseID, netID)) {
        model.enrollStudent(courseID, netID);
        view.displaySuccessfulAlert("Successfully Enrolled", "Student with netID " + netID + " was Successfully enrolled into " + courseID);
      }
      else {
        throw new IllegalArgumentException("Student is already enrolled in this course");
      }
    }
    catch (Exception e) {
      view.displayUnsuccessfulAlert("Student could not be enrolled", e.getMessage());
    }
  }

  public void addStudentMenu(ActionEvent event) {
    view.displayAddStudentMenu();
  }

  public void addStudent(ActionEvent event) {
    try {
      String netID = netIDField.getText();
      String firstName = firstNameField.getText();
      String lastName = lastNameField.getText();
      String email = emailField.getText();
      model.addStudent(netID, firstName, lastName, email);
      view.displaySuccessfulAlert("Successfully Added", "New Student " + firstName + " " + lastName + " was added to the school's enrollment with net ID " + netID + " and email address " + email);
    }
    catch (Exception e) {
      view.displayUnsuccessfulAlert("Student could not be added", e.getMessage());
    }
  }

  public void addProfessor(ActionEvent event) {
    try {
      String netID = netIDField.getText();
      String firstName = firstNameField.getText();
      String lastName = lastNameField.getText();
      String email = emailField.getText();
      model.addProfessor(netID, firstName, lastName, email);
      view.displaySuccessfulAlert("Successfully Added", "New Professor " + firstName + " " + lastName + " was added to the school's faculty with net ID " + netID + " and email address " + email);
    }
    catch (Exception e) {
      view.displayUnsuccessfulAlert("Professor could not be added", e.getMessage());
    }
  }

  public void addCourseMenu(ActionEvent event) {
    view.displayAddCourseMenu();
  }

  public void addCourse(ActionEvent event) {
    try {
      String netID = netIDField.getText();
      String courseID = courseIDField.getText();
      model.addCourse(courseID, netID);
      view.displaySuccessfulAlert("Successfully Added", "New Course " + courseID + " was added.");
    }
    catch (Exception e) {
      view.displayUnsuccessfulAlert("Course could not be added", e.getMessage());
    }
  }

  public void onEnter(KeyEvent event) {
    if (event.getCode().equals(KeyCode.ENTER)) {
      event.consume();
      submitFilePath(new ActionEvent());
    }
  }

  public void cleanDatabase(ActionEvent event) {
    view.cleanDatabase();
  }

  public void exit(ActionEvent event) {
    view.exit();
  }

  public void goBackToMainMenu(ActionEvent event) {
    view.displayMainMenu();
  }


  public void subFilePath() {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Select File");


      //fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));


      Stage stage = (Stage) pathField.getScene().getWindow(); 
      File file = fileChooser.showOpenDialog(stage);


      if (file != null) {
          pathField.setText(file.getAbsolutePath()); 
      }
  }


}
