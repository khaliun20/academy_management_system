package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
public class ClientCommunicationHandlerTest {
    Socket socket= new Socket("vcm-38509.vm.duke.edu",5000);
    AcademicEnrollment academicEnrollment = new DatabaseAcademicEnrollment(new SaltAndHashDatabase());

    public ClientCommunicationHandlerTest() throws IOException, SQLException{

    }
    @Test
    public void test_receiveString() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello\nWorld\r".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        ClientCommunicationHandler CChandler = new ClientCommunicationHandler(socket,inputStream,outputStream,0,academicEnrollment);
        String received = CChandler.receiveStringMessage();
        assertEquals("Hello\nWorld",received);
        CChandler.closeResources();

    }
    @Test
    public void test_receiveIntString() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("41952\r".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        ClientCommunicationHandler CChandler = new ClientCommunicationHandler(socket,inputStream,outputStream,0,academicEnrollment);
        int received = CChandler.receiveIntMessage();
        assertEquals(41952,received);
        CChandler.closeResources();
    }
    @Test
    public void test_DisplayAndPull() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello\nWorld\r".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        ClientCommunicationHandler CChandler = new ClientCommunicationHandler(socket,inputStream,outputStream,0,academicEnrollment);
        String received = CChandler.displayAndPullStr("Go Ahead");
        assertEquals("Hello\nWorld",received);
        CChandler.closeResources();

    }
    @Test
    public void test_Exception() {
        PrintStream orig_out = System.err;
        InputStream inputStream = new ByteArrayInputStream("Hello\nWorld\r".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        OutputStream outputStream1 = new ByteArrayOutputStream();

        ClientCommunicationHandler CChandler = new ClientCommunicationHandler(new FakeSocket(),inputStream,outputStream,0,academicEnrollment);
        System.setErr(new PrintStream(outputStream1));
        CChandler.closeResources();
        assertEquals("Error closing client socket: \n",outputStream1.toString());
        System.setErr(orig_out);


    }
    @Test
    public void test_run_success() throws Exception {

        Socket socket = new Socket("vcm-38509.vm.duke.edu", 5000);
        String inputStr = "jd102\nnew password\r3\rLOGOUT\r";

        InputStream inputStream = new ByteArrayInputStream(inputStr.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Database db = new MySQLTestDatabase();

        Student student = academicEnrollment.getStudentByNetID("jd102");

        ProfileChanger pc = new ProfileChanger(student);
        pc.changePassword("new password");

        AcademicEnrollment academicEnrollment = new DatabaseAcademicEnrollment(db);
        ClientCommunicationHandler CCHandler = new ClientCommunicationHandler(socket,inputStream,outputStream,0,academicEnrollment);
        CCHandler.run();


    }
    @Test
    public void test_run_success_Prof() throws Exception {

        Socket socket = new Socket("vcm-38509.vm.duke.edu", 5000);
        String inputStr = "lo110\nnew password\r3\rLOGOUT\r";

        InputStream inputStream = new ByteArrayInputStream(inputStr.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Database db = new MySQLTestDatabase();

        Student student = academicEnrollment.getStudentByNetID("jd102");

        ProfileChanger pc = new ProfileChanger(student);
        pc.changePassword("new password");

        AcademicEnrollment academicEnrollment = new DatabaseAcademicEnrollment(db);
        ClientCommunicationHandler CCHandler = new ClientCommunicationHandler(socket,inputStream,outputStream,0,academicEnrollment);
        CCHandler.run();


    }
    @Test
    public void test_runFail() throws Exception {

        Socket socket = new Socket("vcm-38509.vm.duke.edu", 5000);
        String inputStr = "jd102\ndafgaee\rfaf\ngeaga\rvjag\nwafgbaaaq\r";

        InputStream inputStream = new ByteArrayInputStream(inputStr.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Database db = new MySQLTestDatabase();

        Student student = academicEnrollment.getStudentByNetID("jd102");

        ProfileChanger pc = new ProfileChanger(student);
        pc.changePassword("new password");

        AcademicEnrollment academicEnrollment = new DatabaseAcademicEnrollment(db);
        ClientCommunicationHandler CCHandler = new ClientCommunicationHandler(socket,inputStream,outputStream,0,academicEnrollment);
        CCHandler.run();
        assertTrue(outputStream.toString().contains("Login failed"));


    }

    class FakeSocket extends Socket{
        @Override
        public void close() throws IOException {
            super.close();
            throw  new IOException("");

        }
    }






}
