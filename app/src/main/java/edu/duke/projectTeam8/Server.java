package edu.duke.projectTeam8;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a server that listens for incoming connections 
 * from clients and creates a new thread for each client
 */
public class Server {
    private final ServerSocket serverSocket;
    private List<Socket> allClientSockets;
    private List<Thread> allClientThreads;
    private ClientHandlerFactory clientHandlerFactory;
    private int clientCounter = 0;
    private boolean isRunning = true;
    private AcademicEnrollment academicEnrollment;  

    /**
     * Create a new Server with a server socket, client handler factory, and academic enrollment
     * @param serverSocket listens for incoming connections from clients
     * @param clientHandlerFactory creates a new thread for each client
     * @param academicEnrollment is the academic enrollment system
     */

    public Server(ServerSocket serverSocket, ClientHandlerFactory clientHandlerFactory, AcademicEnrollment academicEnrollment) {
        this.serverSocket = serverSocket;
        this.clientHandlerFactory = clientHandlerFactory;
        allClientSockets = new ArrayList<>();
        allClientThreads = new ArrayList<>();
        this.academicEnrollment = academicEnrollment;
    }
    /**
     * Start the server and listen for incoming connections 
     * from clients and create a new thread for each client
     * @throws IOException is thrown if there is an error with the server socket
     * @throws RuntimeException is thrown if there is an error with the client thread
     */
    public void startServer() throws IOException, RuntimeException {
        try {
            InetAddress ipAddress = InetAddress.getLocalHost();
            String serverIPAddress = ipAddress.getHostAddress();
            System.out.println("Server is running on IP address: " + serverIPAddress + " and port " + serverSocket.getLocalPort());

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                clientCounter++;
                System.out.println("Client #" +clientCounter +" connected from " + clientSocket.getRemoteSocketAddress());
                addClientSocket(clientSocket);
          
                    Thread clientThread = new Thread(clientHandlerFactory.create(clientSocket, clientCounter, academicEnrollment));
                    addClientThread(clientThread);
                    clientThread.start();    
            
            }

        } catch (IOException e) { // exceptions that affect the entire server not just exceptions from individual clients
            System.out.println("Error: " + e.getMessage());
            this.stopServer(); 
            throw e;
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            this.stopServer();
            throw e;
    }

    }
    /**
     * Stop the server and close the server socket and all client sockets
     * HM: Think about this more idk if we even need something like this
     * also might need to check if there are sockets and threads already closed/stopped
     */

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
            for (Socket clientSocket : allClientSockets) {
                clientSocket.close();
            }
            for (Thread clientThread : allClientThreads) {
                clientThread.interrupt();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addClientSocket(Socket clientSocket) {
        allClientSockets.add(clientSocket);
    }

    private void addClientThread(Thread clientThread) {
        allClientThreads.add(clientThread);
    }
}
