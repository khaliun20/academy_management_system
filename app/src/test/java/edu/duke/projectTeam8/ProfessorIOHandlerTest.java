package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorIOHandlerTest {
    Socket socket = new Socket("vcm-38509.vm.duke.edu", 5000);
    InputStream inputStream = new ByteArrayInputStream("Hello\nWorld\r".getBytes());
    Database db = new MySQLTestDatabase();
    AcademicEnrollment academicEnrollment = new DatabaseAcademicEnrollment(db);


    ProfessorIOHandlerTest() throws IOException, SQLException, NoSuchAlgorithmException {
    }

    @Test
    public void printStudentToSearchPrompt() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();

        ClientCommunicationHandler ccHandler = new ClientCommunicationHandler(socket, inputStream, outputStream, 0, academicEnrollment);

        ProfessorIOHandler PIO = new ProfessorIOHandler(ccHandler, academicEnrollment);
        Course course = new DatabaseCourse("ECE100-001", db.getProfessor("lo110"), db);
        PIO.printStudentToSearchPrompt(course);
        assertEquals(outputStream.toString().length(), 115);
        ccHandler.closeResources();
    }

    @Test
    public void test_prints() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();

        ClientCommunicationHandler ccHandler = new ClientCommunicationHandler(socket, inputStream, outputStream, 0, academicEnrollment);

        ProfessorIOHandler PIO = new ProfessorIOHandler(ccHandler, academicEnrollment);
        Course course = new DatabaseCourse("ECE100-001", db.getProfessor("lo110"), db);
        PIO.printNoStudentEnrolled(course);
        PIO.displayAvailableTasks();
        PIO.printDatePrompt();
        PIO.displayAvailableTasks();
        PIO.displayNoAttendanceSheetsToModify();
        assertEquals(outputStream.toString().length(), 478);
        ccHandler.closeResources();
    }

    @Test
    public void test_printNetIDPrompt() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();

        ClientCommunicationHandler ccHandler = new ClientCommunicationHandler(socket, inputStream, outputStream, 0, academicEnrollment);

        ProfessorIOHandler PIO = new ProfessorIOHandler(ccHandler, academicEnrollment);
        Course course = new DatabaseCourse("ECE100-001", db.getProfessor("lo110"), db);
        PIO.printNetIDPrompt("A prompt", course);
        assertEquals(outputStream.toString().length(), 57);
        ccHandler.closeResources();

    }

    @Test
    public void test_printOldAttendanceSheetsPrompt() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();

        ClientCommunicationHandler ccHandler = new ClientCommunicationHandler(socket, inputStream, outputStream, 0, academicEnrollment);

        ProfessorIOHandler PIO = new ProfessorIOHandler(ccHandler, academicEnrollment);
        Course course = new DatabaseCourse("ECE100-001", db.getProfessor("lo110"), db);
        PIO.printNoStudentEnrolled(course);
        db.addAttendanceRecord("lo110", "ECE100-001", "Wed Apr 24 00:00:00 EST 2024", "Tardy");
        PIO.printOldAttendanceSheetsPrompt(course);
        assertEquals(outputStream.toString().length(), 203);
        ccHandler.closeResources();

    }

    @Test
    public void test_printStudentAttendancePrompt() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();

        ClientCommunicationHandler ccHandler = new ClientCommunicationHandler(socket, inputStream, outputStream, 0, academicEnrollment);

        ProfessorIOHandler PIO = new ProfessorIOHandler(ccHandler, academicEnrollment);
        Course course = new DatabaseCourse("ECE100-001", db.getProfessor("lo110"), db);
        PIO.printNoStudentEnrolled(course);
        db.addAttendanceRecord("lo110", "ECE100-001", "Wed Apr 24 00:00:00 EST 2024", "Tardy");
        Student student = db.getStudent("jd102");
        PIO.printOldAttendanceSheetsPrompt(course);
        assertEquals(outputStream.toString().length(), 203);
        PIO.printStudentAttendancePrompt(student);
        PIO.displayNoAttendanceSheetsToModify();
        Lecture lecture = new DatabaseLecture(course, new Date(), db);
        lecture.modifyAttendanceOfStudent(student, Attendance::markAttended);
        PIO.displayCourseTaskOptions();
        ccHandler.closeResources();

    }

    @Test
    public void test_showAttendanceSheet() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();

        ClientCommunicationHandler ccHandler = new ClientCommunicationHandler(socket, inputStream, outputStream, 0, academicEnrollment);

        ProfessorIOHandler PIO = new ProfessorIOHandler(ccHandler, academicEnrollment);
        Course course = new DatabaseCourse("ECE100-001", db.getProfessor("lo110"), db);
        PIO.printNoStudentEnrolled(course);
        db.addAttendanceRecord("lo110", "ECE100-001", "Wed Apr 24 00:00:00 EST 2024", "Tardy");
        Student student = db.getStudent("jd102");
        PIO.printOldAttendanceSheetsPrompt(course);
        assertEquals(outputStream.toString().length(), 203);
        PIO.printStudentAttendancePrompt(student);
        PIO.displayNoAttendanceSheetsToModify();
        Lecture lecture = new DatabaseLecture(course, new Date(), db);
        lecture.modifyAttendanceOfStudent(student, Attendance::markAttended);
        PIO.displayCourseTaskOptions();
        ccHandler.closeResources();

    }

    @Test
    public void test_printAttendanceSheet() throws Exception {
        InputStream inStream = new ByteArrayInputStream("".getBytes());
        OutputStream outStream = new ByteArrayOutputStream();
        ClientCommunicationHandler ccHandler = new ClientCommunicationHandler(socket, inStream, outStream, 0, academicEnrollment);
        Professor lo = db.getProfessor("lo110");
        Student john = db.getStudent("jd102");
        Course course = new DatabaseCourse("MATH502-001", lo, db);
        WeeklyTextSummary summarizer = new WeeklyTextSummary();
        Date date = summarizer.strToDate("10/22/2023 10:22:00 EDT");
        DatabaseLecture lecture = new DatabaseLecture(course, date, db);
        lecture.recordAttendance(john, Attendance::markAttended);
        ProfessorIOHandler PIO = new ProfessorIOHandler(ccHandler,academicEnrollment);
        PIO.printAttendanceSheet(lecture.getLectureAttendance(),lecture);
        String expected = "TEXT\nAttendance record for lecture on: Sun Oct 22 10:15:00 EDT 2023\n" +
                "\nJohn Doe: Attended Score: 100\n" +
                "\nOverall Score: 100.0\n" +
                "\n0. Go back\n\r";
        assertEquals(expected,outStream.toString());
        PIO.printOldStatusAndAttendancePrompt(lecture, john);
        ccHandler.closeResources();
    }
}