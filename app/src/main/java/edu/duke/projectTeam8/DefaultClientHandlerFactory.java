package edu.duke.projectTeam8;
import java.io.IOException;
import java.net.Socket;

/**
 * This class represents a default factory for creating ClientHandlers
 */
public class DefaultClientHandlerFactory  implements ClientHandlerFactory{

    @Override
    public Runnable create(Socket clientSocket, int clientCounter, AcademicEnrollment academicEnrollment)  {
        try {
            ClientCommunicationHandler clientHandler = new ClientCommunicationHandler(clientSocket, clientSocket.getInputStream(), clientSocket.getOutputStream(), clientCounter, academicEnrollment);
            return clientHandler;
        } catch (IOException e) {
            throw new RuntimeException("Error creating client handler", e); 
        }
    }
}

