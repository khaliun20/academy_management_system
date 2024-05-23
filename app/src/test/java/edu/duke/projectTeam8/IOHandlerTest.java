package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class IOHandlerTest {
    Socket socket = new Socket("vcm-38509.vm.duke.edu", 5000);
    Database db = new MySQLTestDatabase();
    AcademicEnrollment academicEnrollment = new DatabaseAcademicEnrollment(db);


    IOHandlerTest() throws IOException, SQLException, NoSuchAlgorithmException {
    }

    @Test
    void readString() throws Exception {
        InputStream inStream = new ByteArrayInputStream("jd102\rjd102\rLOGOUT\rno\ryz000\rmd118\r".getBytes());
        OutputStream outStream = new ByteArrayOutputStream();
        ClientCommunicationHandler ccHandler = new ClientCommunicationHandler(socket, inStream, outStream, 0, academicEnrollment);
        IOHandler ioHandler = new IOHandler(ccHandler,academicEnrollment);

        Student student = academicEnrollment.getStudentByNetID("jd102");
        Course course=null;
        for(Course c:student.getClasses()){
            course = c;
            break;
        }
        ioHandler.readAndValidateStudent(course,false);

        ioHandler.readAndValidateStudent(course,true);
        ioHandler.printEmailSenderror();
        ioHandler.printNoAttendanceSheet();
        assertFalse(ioHandler.stayConnected());
        assertTrue(ioHandler.stayConnected());
        ioHandler.readAndValidateStudent(course,true);
        ioHandler.readAndValidateStudent(course,true);

    }

}