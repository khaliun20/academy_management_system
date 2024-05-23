package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DefaultClientHandlerFactoryTest {
    Socket socket = new Socket("vcm-38509.vm.duke.edu", 5000);

    DefaultClientHandlerFactoryTest() throws IOException {
    }

    @Test
    public  void test_() throws IOException, SQLException, NoSuchAlgorithmException {
        ClientHandlerFactory factory = new DefaultClientHandlerFactory();
        Runnable runnable = factory.create(socket,0,new DatabaseAcademicEnrollment(new MySQLTestDatabase()));
        assertNotNull(runnable);

    }

}