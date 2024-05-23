package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceTakerViewTest {
    Socket socket = new Socket("vcm-38509.vm.duke.edu", 5000);
    Database db = new MySQLTestDatabase();
    AcademicEnrollment academicEnrollment = new DatabaseAcademicEnrollment(db);

    AttendanceTakerViewTest() throws SQLException, IOException, NoSuchAlgorithmException {
    }

    @Test
    public void test_displayTodaysAttendance() throws Exception {
        String input = "1\r1\r1\r1\rWed Apr 24 00:00:00 EST 2024\r" +
                "2\r2\r2\r"+"1\rjd102\r3\r0\r";
        InputStream inStream = new ByteArrayInputStream(input.getBytes());
        OutputStream outStream = new ByteArrayOutputStream();
        ClientCommunicationHandler ccHandler = new ClientCommunicationHandler(socket, inStream, outStream, 0, academicEnrollment);
        Professor lo = db.getProfessor("lo110");
        Student john = db.getStudent("jd102");
        Course course = new DatabaseCourse("MATH502-001", lo, db);
        ProfessorIOHandler PIO = new ProfessorIOHandler(ccHandler, academicEnrollment);
        AttendanceTakerView attendanceTakerView = new AttendanceTakerView(PIO);
        attendanceTakerView.displayModifyAttendance(course);
//        System.out.println(course.getLecturesList().get(0).getLectureAttendance().toString());
        attendanceTakerView.displayTodaysAttendance(course);
        Lecture lecture = course.getLecturesList().get(0);
        assertEquals(course.getLecturesList().size(), 1);
        String output = lecture.getLectureAttendance().toString();

        assertTrue(output.contains("Attended") && !output.contains("Tardy") && !output.contains("Absent"));
        attendanceTakerView.displayAnotherDayAttendance(course);
        List<Lecture> lectureList= course.getLecturesList();

        assertNotEquals(lectureList.get(0).getDate(),lectureList.get(1).getDate());
        assertFalse(course.getActiveEnrollment().isEmpty());

        attendanceTakerView.displayModifyAttendance(course);

//        System.out.println(course.getLecturesList());
//        System.out.println(course.getLecturesList().get(0).getLectureAttendance().toString());
//        System.out.println(course.getLecturesList().get(1).getLectureAttendance().toString());

        System.out.println();

//        System.out.println(course.getLecturesList().get(0).getDate());
//        System.out.println(course.getLecturesList().get(1).getDate());

        ccHandler.closeResources();
    }

}