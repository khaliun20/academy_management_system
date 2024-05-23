package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class StudentViewTest {


    StudentViewTest() throws SQLException, IOException, NoSuchAlgorithmException {
    }

    @Test
    public void test_viewProfile() throws Exception {

        Socket socket = new Socket("vcm-38509.vm.duke.edu", 5000);
        String inputStr = "jd102\nnew password\r1\r0\r" +
                "1\r1\r0\r" +
                "1\r1\rbad\r" +
                "1\r1\rbetter password\r" +
                "2\r0\r" +
                "2\rLucas\r" +
                "0\r3\rLOGOUT\r";

        InputStream inputStream = new ByteArrayInputStream(inputStr.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Database db = new MySQLTestDatabase();
        AcademicEnrollment academicEnrollment = new DatabaseAcademicEnrollment(db);

        Student student = academicEnrollment.getStudentByNetID("jd102");

        ProfileChanger pc = new ProfileChanger(student);
        pc.changePassword("new password");

        ClientCommunicationHandler CCHandler = new ClientCommunicationHandler(socket, inputStream, outputStream, 0, academicEnrollment);
        CCHandler.run();
        CCHandler.closeResources();

    }

    @Test
    public void test_viewCourse() throws Exception {

        Socket socket = new Socket("vcm-38509.vm.duke.edu", 5000);
        String inputStr = "jd102\nnew password\r2\r1\r0\r" +
                "1\r1\r0\r" +
                "1\r1\r0\r" +
                "2\r0\r"+
                "2\r1\r2\r2\r"+
                "3\r0\r"+
                "3\r1\r"+
                "0\r0\r" +

                "0\r3\rLOGOUT\r";

        InputStream inputStream = new ByteArrayInputStream(inputStr.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Database db = new MySQLTestDatabase();
        AcademicEnrollment academicEnrollment = new DatabaseAcademicEnrollment(db);

        Student student = academicEnrollment.getStudentByNetID("jd102");

        ProfileChanger pc = new ProfileChanger(student);
        pc.changePassword("new password");

        ClientCommunicationHandler CCHandler = new ClientCommunicationHandler(socket, inputStream, outputStream, 0, academicEnrollment);
        CCHandler.run();
        CCHandler.closeResources();

    }

}