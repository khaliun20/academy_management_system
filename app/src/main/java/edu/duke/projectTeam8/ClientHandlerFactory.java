package edu.duke.projectTeam8;
import java.net.Socket;

/**
 * This is an interface for creating ClientHandlers
 */
public interface ClientHandlerFactory {

    /**
     * This method creates a ClientCommunicationHandler
     * @param clientSocket is the client socket
     * @param clientCounter is the client counter
     * @param academicEnrollment is the academic enrollment system
     * @return a new ClientCommunicationHandler Runnable
     */

    Runnable create(Socket clientSocket , int clientCounter,AcademicEnrollment academicEnrollment);
}
