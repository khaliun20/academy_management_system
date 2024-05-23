import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private final String serverAddress;
    private final int serverPort;
    private BufferedReader reader;
    private PrintWriter writer;
    private BufferedReader terminalReader;
    boolean receiveSecondMessage;



    public static final int MIN_OPTION_VALUE = 1;
    public static final int MAIN_MENU_OPTIONS = 3;
    public static final int COURSE_MAIN_TAB_OPTIONS = 2;
    public static final int COURSE_SUB_TAB_OPTIONS = 5;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
       receiveSecondMessage = false;
    }

    public void connectAndReceiveText() {
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Connected to server on " + serverAddress + ":" + serverPort);
            initializeCommunication(socket);
            handleCommunication();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeCommunication(Socket socket) throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
        terminalReader = new BufferedReader(new InputStreamReader(System.in));
    }

    private void handleCommunication()throws IOException {
        
        while (true) {

            String receivedMessage = receiveMessage();

            if (receivedMessage == null) {
                System.out.println("Server closed the connection.");
                break;
            }
            System.out.println(receivedMessage);


            String messageToSend = getUserInput();
            sendMessage(messageToSend);

            if(receiveSecondMessage){
                String secondReceivedMessage = receiveMessage();
                System.out.println(secondReceivedMessage );
                receiveSecondMessage = false;
            }
        }
       
         
    }
    private String getUserInput() throws IOException {
        System.out.print("Enter message to send to server: \n");
        return terminalReader.readLine();
    }

    private void sendMessage( String message) {
        writer.print(message + "\r");
        writer.flush();
    }


    public String receiveMessage() throws IOException {
        StringBuilder sb = new StringBuilder();
        int currentChar;
        while ((currentChar = reader.read()) != -1) {
            char c = (char) currentChar;
            if (c == '$'){
                receiveSecondMessage = true;
            }
            if (c == '\r') {
                return sb.toString();
            } 
            sb.append(c);
        
        }
        return null;  //TODO: think about what to do here
    }
      
    

    


    public static void main(String[] args) {
        String serverAddress = "192.168.1.197";
        int serverPort = 5000;

        Client client = new Client(serverAddress, serverPort);
        client.connectAndReceiveText();
    }
}


