// This file is the controller for the GUI. It listens for messages from the server and updates the GUI accordingly. It also sends messages to the server based on user input.
/************************************************************************************************************************************************************
 *                                      COMMUNICATION PROTOCOL                                                                                              
 * 
 * Message Types: LOGIN, LOGINSUCCESS, SELECT, SELECTSTUDENT, ENTER, TEXT, DATE, LOGOUT                                                                                          
 * 
 * LOGIN: The server sends a prompt to the client and asks for a netID and password. The client displays text fields for the netID and password and send back the netID and password to the server.
 * LOGINSUCCESS: The server sends a success message (includes netid) to the client to confirm successful login.
 * SELECT: The server sends a list of **NUMBERED options*** to the client. The client displays these options as buttons. When the user clicks 
 *         a button, the client sends the corresponding option NUMBER to the server.
 * SELECTSTUDENT: The server sends a list of ***student NETIDs*** to the client. The client displays these netIDs as buttons. When the user clicks 
 *          a button, the client sends the corresponding student netID to the server.
 *               - Future improvement: Have sender send numbered options so it can be SELECT message type instead
 * ENTER : The server sends a prompt to the client and asks for text input. The client displays a text field and a submit button. When the user 
 *         enters text and clicks the submit button, the client sends the text to the server.
 * TEXT: The server sends a message to the client. The client displays the message as text.
 * DATE: The server sends a prompt to the client and asks for a date input. The client displays a date picker and a submit button. When the 
 *        user selects a date and clicks the submit button, the client sends the date to the server.
 * 
 * LOGOUT: The server sends a message to the client to confirm logout. The client displays a confirmation dialog. If the user confirms exit, GUI stops and resources are closed.
 * 
 * 
 * 
 * All message types except LOGIN, LOGINSUCCESS, LOGOUT  have at least 3 lines. 
 *       Line 1: Message Type
 *       Line 2: Message to display to user
 *       Line 3: Either other messages or Go back to previous page message



 ************************************************************************************************************************************************************/
package client;




 import javafx.application.Platform;
 import javafx.fxml.FXML;
 import javafx.fxml.FXMLLoader;
 import javafx.fxml.Initializable;
 import javafx.geometry.Insets;
 import javafx.scene.layout.VBox;
 import javafx.scene.text.Text;
 import javafx.stage.Stage;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
 import javafx.scene.control.Alert;
 import javafx.scene.control.Alert.AlertType;
 import javafx.scene.control.Button;
 import javafx.scene.control.ButtonType;
 import javafx.scene.control.ComboBox;
 import javafx.scene.control.DatePicker;
 import javafx.scene.control.Label;
 import javafx.scene.control.PasswordField;
 import javafx.scene.control.TextField;
 
 
 import java.net.URL;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.ResourceBundle;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.List;
 import java.time.ZoneId;
 import java.time.format.DateTimeFormatter;
 import java.time.format.DateTimeParseException;
 import java.io.IOException;
 import java.time.LocalDate;
 import java.time.LocalDateTime;
 import java.time.LocalTime;
 
 
 public class Controller implements Initializable {
     @FXML
     private Label messageLabel;
 
     @FXML
     private Label userNameLabel;
 
     @FXML
     private VBox optionsBox;
 
     private Client client;
 
     private boolean isConnected  = true;
 
 
     public void setClient(Client client) {
         this.client = client;
     }
 
     public void startCommunication() {
         listenForMessages();
     }
 
     private void listenForMessages() {
         new Thread(() -> {
             try {
                 while (isConnected) {
                     String message = client.receiveMessage();
                     if (message == null) {
                         Platform.runLater(() -> {
                             Alert alert = new Alert(Alert.AlertType.INFORMATION);
                             alert.setTitle("Information");
                             alert.setHeaderText("Connection Closed");
                             alert.setContentText("The connection to the server has been closed.");
                             alert.showAndWait();
                             Platform.exit();
                         });
                         client.closeResources();
                         break;
                     }
                     System.out.println("Received message: " + message);
                     handleMessage(message);
                 }
                 client.closeResources();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }).start();
     }
 
     
     private void handleMessage(String message) throws IOException {
         clearScene();
         String[] lines = message.split("\n");
         String messageType = lines[0];
         updateMessageLabel(lines);
     
         switch (messageType) {
             case "LOGIN":
                 handleLoginMessage(lines);
                 break;
             case "LOGINSUCCESS":
                 setUsernNameLabel(lines);
                 break;
             case "SELECT":
                 handleSelectionMessage(lines);
                 break;
             case "SELECTSTUDENT":
                 handleStudentSelectionMessage(lines);
                 break;
             case "ENTER":
                 handleEnterMessage(lines);
                 break;
             case "TEXT":
                 handleTextMessage(lines);
                 break;
             case "DATE":
                 handleDateMessage(lines);
                 break;
             case "LOGOUT":
                 confirmLogout();
                 break;
             default:
                 System.out.println("Unknown message type: " + messageType);
         }
     }
 
     private void handleLoginMessage(String[] lines) {
         createLoginAndSendBack(lines);
     }
     
     private void handleSelectionMessage(String[] lines) {
         createButtonsAndSendBack(lines, false);
     }
 
     private void handleStudentSelectionMessage(String[] lines) {
         createButtonsAndSendBack(lines, true);
     }
     
     
     private void handleEnterMessage(String[] lines) {
         createTextFieldAndSendBack(lines);
     }
     
     private void handleTextMessage(String[] lines) {
         createTextBodyAndSendBack(lines);
     }
     
     private void handleDateMessage(String[] lines) {
         createDatePickerAndSendBack(lines);
     }
 
     
     
     private void clearScene() {
         Platform.runLater(() -> {
             optionsBox.getChildren().clear();
         });
     }
 
  
         
 
 
     private void sendToServer(String message) {
         try {
             client.sendMessage(message);
             System.out.println("Sent message: " + message);
         } catch (Exception e) {
             e.printStackTrace();
 
         }
     }
 
     private void updateMessageLabel(String[] lines) {
 
         Platform.runLater(() -> {
             messageLabel.setText(lines[1]);
             optionsBox.setPadding(new Insets(10, 0, 0, 0));
         });
     }
 
     private void createLoginAndSendBack(String[] lines) {
         Platform.runLater(() -> {
         TextField netIDField = new TextField();
         PasswordField passwordField = new PasswordField();
 
         Label netIDLabel = new Label("NetID:");
         
         Label passwordLabel = new Label("Password:");
 
         Button loginButton = new Button("Login");
         loginButton.getStyleClass().add("styled-button");
 
         loginButton.setOnAction(event -> {
             String password = passwordField.getText();
             String netID = netIDField.getText();
             sendToServer(netID + "\n" + password);
         });
 
         // Add the components to the optionsBox
         optionsBox.getChildren().addAll(netIDLabel, netIDField, passwordLabel, passwordField, loginButton);
     });
 }
 
 private void setUsernNameLabel(String[] lines) {
     
     String netID = lines[1];
     Platform.runLater(() -> {
         userNameLabel.setText("Loggedin: " + netID);
     });
 
 }
     
 
     private void createButtonsAndSendBack(String[] lines, boolean isStudentButton) {
   
         List<Button> buttons = new ArrayList<>();
         for (int i = 2; i < lines.length -1; i++) {
             if (lines[i].trim().isEmpty()) {
                 continue;
             }
 
             String buttonText = lines[i];
             Button button = createButton(buttonText, isStudentButton);
             buttons.add(button);
         }
 
 
         Button lastButton = createButton(lines[lines.length - 1], false);
         
 
         // Sometimes the last button is not Go back so we want to make sure it doesn't get the Go back button styling
         if (!lastButton.getText().equals("Absent")) {
             lastButton.getStyleClass().add("styled-button");    
         }    
         buttons.add(lastButton);
         
         Platform.runLater(() -> optionsBox.getChildren().addAll(buttons));
 
 
     }
 
 
     
     private String cleanButtonText(String buttonText) {
         if (buttonText.matches("^\\d+\\.\\s*.*")){
             return buttonText.replaceFirst("^\\d+\\.\\s*", "").trim();
         } else {
             return buttonText;
         }
     }
 
     private void createTextBodyAndSendBack(String[] lines) {
         Platform.runLater(() -> {
             for (int i = 2; i < lines.length - 1; i++) {
                 Text text = new Text(lines[i]);
                 optionsBox.getChildren().add(text);
             }
             
             Button goBackButton = createButton(lines[lines.length - 1], false);
             goBackButton.getStyleClass().add("styled-button");
 
 
             optionsBox.getChildren().add(goBackButton);
            
         });
     }
 
 
 
     private void createTextFieldAndSendBack(String [] lines) {
         Platform.runLater(() -> {
             TextField textField = new TextField();
             Button enterButton = new Button("Submit");
             enterButton.setOnAction(event -> {
                 String text = textField.getText();
                 sendToServer(text);
             });
     
             textField.setOnAction(event -> enterButton.fire());
        
             Button goBackButton = createButton(lines[lines.length - 1], false);
             goBackButton.getStyleClass().add("styled-button");
         
             optionsBox.getChildren().addAll(textField, enterButton, goBackButton );
         });
     }
 
 
     private void createDatePickerAndSendBack(String[] lines) {
         Platform.runLater(() -> {
             DatePicker datePicker = new DatePicker();
             TextField timePicker = new TextField();
             timePicker.setPrefWidth(150);
             timePicker.setPromptText("HH:MM AM/PM");
             
             Button takeAttendanceButton = new Button("Take Attendance");
          
             takeAttendanceButton.setOnAction(e -> {
             try{
                 String timeString = timePicker.getText();
                 LocalDate localDate = datePicker.getValue();
                 
                 DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
         
                 LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.parse(timeString, timeFormatter));
         
                 Date combinedDateTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
         
                 sendToServer(combinedDateTime.toString());
            
             } catch (DateTimeParseException ex) {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("Error");
                 alert.setHeaderText("Invalid Time Format");
                 alert.setContentText("Please enter the time in HH:MM AM/PM format.");
                 alert.showAndWait();
 
              }
             });
     
             Label dateLabel = new Label("Date:");
             Label timeLabel = new Label("Time:");
             
             Button goBackButton = createButton(lines[lines.length - 1], false);
             goBackButton.getStyleClass().add("styled-button");
     
             optionsBox.getChildren().addAll(dateLabel, datePicker, timeLabel, timePicker, takeAttendanceButton, goBackButton);
         });
     } 
 
     
     private Button createButton(String buttonText, boolean isStudentButton) {
         String cleanedButtonText = cleanButtonText(buttonText);
         Button button = new Button(cleanedButtonText);
         button.setOnAction(event -> {
             try {
                 sendToServer(isStudentButton ? buttonText : String.valueOf(buttonText.charAt(0)));
                 showConfirmation(button.getText());
             } catch (Exception e) {
                 e.printStackTrace();
             }
         });
         return button;
     }
 
     private void showConfirmation(String buttonText) {
         if (buttonText.equals("Send Email")) {
             Platform.runLater(() -> {
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("Email Sent");
                 alert.setHeaderText("Email has been sent");
                 alert.setContentText("The email has been sent to the student.");
                 alert.showAndWait();
             });
            
         }
     
     }
 
 
     private void confirmLogout() {
     Platform.runLater(() -> {
         // Create an alert
         Alert alert = new Alert(AlertType.CONFIRMATION);
         alert.setTitle("Logout Confirmation");
         alert.setHeaderText(null);
         alert.setContentText("Are you sure you want to logout?");
 
         alert.showAndWait().ifPresent(response -> {
             if (response == ButtonType.OK) {
                 sendToServer("LOGOUT");
                 isConnected = false;
                 Platform.exit();
             } else {
                 sendToServer("STAY");
             }
         });
     });
 
 }
     
 
     @Override
     public void initialize(URL location, ResourceBundle resources) {
      
     }
         
         
 
 
 
     
 
     
 }