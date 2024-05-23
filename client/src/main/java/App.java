package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.Socket;

import org.checkerframework.checker.units.qual.s;

public class App extends Application {
    private Socket socket;
    private Client client;
    private Controller controller;
    


    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            Scene scene = new Scene(root,600, 400);
            scene.getStylesheets().add(getClass().getResource("/ui/style.css").toExternalForm());
    
            primaryStage.setTitle("Client Application");
            primaryStage.setScene(scene);
            primaryStage.show();
    
            new Thread(() -> {
                try {
                    Socket socket = new Socket("localhost", 5000);
                    Client client = new Client(socket);
                    System.out.println("Connected to the server");

                    Platform.runLater(() -> {
                        controller.setClient(client);
                        controller.startCommunication();
               
             
                        
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    

    
    
    public static void main(String[] args) {
        launch(args); 
    }
}