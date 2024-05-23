package edu.duke.projectTeam8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;


/**
 * This class represents a ClientCommunicationHandler object that handles communication between the client and the server
 */

public class ClientCommunicationHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private int clientID;
    private AcademicEnrollment academicEnrollment;

    /**
     * This constructor creates a new ClientCommunicationHandler object with a client socket, input stream, output stream, client ID, and academic enrollment
     *
     * @param clientSocket       is the client socket
     * @param inputStream        is the input stream
     * @param outputStream       is the output stream
     * @param clientID           is the client ID
     * @param academicEnrollment is the academic enrollment system
     */
    public ClientCommunicationHandler(Socket clientSocket, InputStream inputStream, OutputStream outputStream, int clientID, AcademicEnrollment academicEnrollment) {
        this.clientSocket = clientSocket;
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.writer = new PrintWriter(new OutputStreamWriter(outputStream), true);
        this.clientID = clientID;
        this.academicEnrollment = academicEnrollment;

    }

    /**
     * This method is called when the thread is started. It starts the communication with the client
     */
    // When run() returns, the thread will end
    @Override
    public void run() {

        try {
            Database database = academicEnrollment.getDatabase();
            LoginAuthenticatorSession loginAuthenticatorSession = new LoginAuthenticatorSession(this, database);
            LoginStatus loginStatus = loginAuthenticatorSession.authenticate();
            if (!loginStatus.getLoginOutcome()) {
                sendMessage("Login failed");
                closeResources();
                return;
            }

            User user = loginStatus.getUser();
            String netID = user.getNetID();
            System.out.println("You are Logged in as " +user.getName()+"("+netID+")");

            if (!academicEnrollment.isStudent(user)) {
                ProfessorIOHandler professorIoHandler = new ProfessorIOHandler(this, academicEnrollment);
                Professor p = academicEnrollment.findProfessorInDirectory(netID);
                AttendanceTakerView attendanceTakerView = new AttendanceTakerView(professorIoHandler);
                AttendanceRecordView attendanceRecordView = new AttendanceRecordView(professorIoHandler);
                ProfessorView professorView = new ProfessorView(p, professorIoHandler, attendanceTakerView, attendanceRecordView);
                professorView.showMainMenu();

            } else {

                StudentIOHandler studentIoHandler = new StudentIOHandler(this, academicEnrollment);
                Student student = academicEnrollment.getStudentByNetID(netID);
                System.out.println("Student: " + student);
                StudentView studentView = new StudentView(student, studentIoHandler);
                studentView.showMainMenu();
            }
        } catch (Exception e) {
            System.err.println("Exception thrown : " + e.getMessage());
        } finally {
            closeResources();
        }

    }

    /**
     * This method closes the resources associated with the client
     */

    public void closeResources() {
        try {
            System.out.println("Client #" + this.clientID + " disconnected");
            reader.close();
            writer.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
        }
    }

    /**
     * This method receives a message from the client and returns it as a string
     *
     * @return the message received from the client or null if the client has disconnected
     * @throws IOException if there is an error reading the message
     */
    public String receiveStringMessage() throws IOException {

        StringBuilder sb = new StringBuilder();
        int currentChar;
        while ((currentChar = reader.read()) != -1) {
            char c = (char) currentChar;
            if (c == '\r') {
                return sb.toString();
            }
            sb.append(c);
        }
        return null;

    }

    /**
     * This method sends a message to the client
     *
     * @param message the message to send to the client
     */

    public void sendMessage(String message) {
        writer.print(message + "\r");
        writer.flush();
    }

    /**
     * This method receives an integer message from the client and returns it as an integer
     */

    public int receiveIntMessage() throws IOException {
        String input = receiveStringMessage();
        return stringToInt(input);

    }

    private int stringToInt(String input) {
        input = input.trim();
        return Integer.parseInt(input);

    }

    public void clientDisplay(String msg) {
        sendMessage(msg);

    }

    public String displayAndPullStr(String displayMsg) throws IOException {
        clientDisplay(displayMsg);
        return receiveStringMessage();
    }

}
