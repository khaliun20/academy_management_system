package admin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXMLLoader;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class AdminGUI extends Application {
  protected Parent root;
  protected Scene scene;
  protected Stage stage;
  protected MainMenuController controller;
  protected DatabaseConnector dbConnector;

  @Override
  public void init() {
    try {
      dbConnector = new DatabaseConnector();
      controller = new MainMenuController(this, dbConnector);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void start(Stage stage) {
    try {
      FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("MainMenu.fxml"));
      loader.setController(controller);
      this.root = loader.load();
      this.stage = stage;
      this.scene = new Scene(root);
      this.stage.setTitle("Admin Application");
      this.stage.setScene(scene);
      this.stage.setOnCloseRequest((event) -> {
          event.consume();
          exit();
        });
      this.stage.show();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void displayGetFilePathWindow() {
    try {
      FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("FileImport.fxml"));
      loader.setController(controller);
      root = loader.load();
      scene = new Scene(root);
      scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            controller.onEnter(event);
          }
        });
      stage.setScene(scene);
      stage.show();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void displayMainMenu() {
    try {
      FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("MainMenu.fxml"));
      loader.setController(controller);
      root = loader.load();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void displayDropStudentMenu() {
    try {
      FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("DropStudent.fxml"));
      loader.setController(controller);
      root = loader.load();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void displayEnrollStudentMenu() {
    try {
      FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("EnrollStudent.fxml"));
      loader.setController(controller);
      root = loader.load();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void displayAddStudentMenu() {
    try {
      FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("AddNewStudent.fxml"));
      loader.setController(controller);
      root = loader.load();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void displayAddCourseMenu() {
    try {
      FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("AddCourse.fxml"));
      loader.setController(controller);
      root = loader.load();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void displaySuccessfulAlert(String header, String content) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Success");
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
    displayMainMenu();
  }

  public void displayUnsuccessfulAlert(String header, String errorMessage) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(header);
    alert.setContentText(errorMessage);
    alert.showAndWait();
  }

  public void cleanDatabase() {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Clear Database");
    alert.setHeaderText("You are about to clear the database");
    alert.setContentText("Please confirm you want to clear the database");
    if (alert.showAndWait().get() == ButtonType.OK) {
      try {
        dbConnector.cleanDatabase();
        displaySuccessfulAlert("Database Cleared", "Database successfully cleared");
      }
      catch (Exception e) {
        displayUnsuccessfulAlert("Database was not cleared", e.getMessage());
      }
    }
  }

  public void exit() {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Exit Confirmation");
    alert.setHeaderText("You are about to exit");
    alert.setContentText("Please confirm you want to exit");
    if (alert.showAndWait().get() == ButtonType.OK) {
      Platform.exit();
    }
  }
  
  public static void main(String[] args) {
    launch(args);
  }
}
