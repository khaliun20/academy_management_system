package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class Client {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;


    public Client(Socket socket) {
        try{ 
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            closeResources();
            e.printStackTrace();
        }
        
    }

    public void sendMessage( String message) {
        writer.print(message + "\r");
        writer.flush();
    }


    public String receiveMessage() throws IOException {
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void closeResources() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    