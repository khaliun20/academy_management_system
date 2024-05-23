package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.net.*;



public class ServerTest {


   static class TestClientHandlerFactory implements ClientHandlerFactory {
       @Override
       public Runnable create(Socket clientSocket, int clientNumber, AcademicEnrollment academicEnrollment) {
           return () -> {
               try {
                   BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                   PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                   String inputLine;
                   while ((inputLine = in.readLine()) != null) {
                       out.println(inputLine);
                   }
                   in.close();
                   out.close();
                   clientSocket.close();
               } catch (IOException e) {
               }
           };
       }
   }

   static class TestAcademicEnrollment extends AcademicEnrollmentV1 {
   }



   @Test
   public void testStopServer() throws IOException, InterruptedException {
       ServerSocket serverSocket = new ServerSocket(0); // Choose an available port
       int port = serverSocket.getLocalPort();
       ClientHandlerFactory clientHandlerFactory = new TestClientHandlerFactory();
       AcademicEnrollment academicEnrollment = new TestAcademicEnrollment();
       Server server = new Server(serverSocket, clientHandlerFactory, academicEnrollment);

       Thread serverThread = new Thread(() -> {
           try {
               server.startServer();
           } catch (IOException | RuntimeException e) {
           }
       });
       serverThread.start();

       server.stopServer();

       serverSocket.close();
       serverThread.join();
   }

   @Test
   public void testStartServer1() throws IOException, InterruptedException, RuntimeException {
   ServerSocket serverSocket = new ServerSocket(5001);
   int port = serverSocket.getLocalPort();
   ClientHandlerFactory clientHandlerFactory = new TestClientHandlerFactory();
   AcademicEnrollment academicEnrollment = new TestAcademicEnrollment();
   Server server = new Server(serverSocket, clientHandlerFactory, academicEnrollment);

   Thread serverThread = new Thread(() -> {
       try {
           server.startServer();
       } catch (IOException | RuntimeException e) {

       }
   });
   serverThread.start();

   Socket clientSocket = new Socket("localhost", port);
   PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
   BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

   String messageFromClient = "Hello, Server!";
   out.println(messageFromClient);

   String responseFromServer = in.readLine();

   assertEquals("Echo: Hello, Server!", "Echo: " + messageFromClient, responseFromServer);


   out.close();
   in.close();
   clientSocket.close();
   serverSocket.close();
   serverThread.join();
}



}