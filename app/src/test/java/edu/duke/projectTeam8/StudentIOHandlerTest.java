package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class StudentIOHandlerTest {
    Socket socket= new Socket("vcm-38509.vm.duke.edu",5000);
    InputStream inputStream = new ByteArrayInputStream("Hello\nWorld\r".getBytes());
    OutputStream outputStream = new ByteArrayOutputStream();
    AcademicEnrollment academicEnrollment = null;

    StudentIOHandlerTest() throws IOException {

    }
    @Test
    public void test_send(){
        ClientCommunicationHandler ccHandler = new ClientCommunicationHandler(socket,inputStream,outputStream,0,academicEnrollment);
        StudentIOHandler SIO = new StudentIOHandler(ccHandler,academicEnrollment);
        SIO.displayEmailPrompt();
        SIO.printEmailSenderror();
        SIO.printAttendanceReportForStudent("","not Null","");

        SIO.printAttendanceReportForStudent("",null,"");
        SIO.displaySubscriptionStatus(false);
        SIO.displaySubscriptionStatus(true);
        SIO.displayStudnetCourseTaskOptions("ECE100-001");
    }
}