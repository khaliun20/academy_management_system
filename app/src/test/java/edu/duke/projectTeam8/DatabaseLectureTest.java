package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseLectureTest {
    MySQLTestDatabase db = new MySQLTestDatabase();
    Professor lo = db.getProfessor("lo110");
    Student john = db.getStudent("jd102");
    Course course = new DatabaseCourse("MATH502-001", lo, db);
    WeeklyTextSummary summarizer = new WeeklyTextSummary();
    DatabaseLectureTest() throws SQLException, IOException, NoSuchAlgorithmException {
    }

    @Test
    public void test_all() throws Exception {

        Date date = summarizer.strToDate("10/22/2023 10:22:00 EDT");
        DatabaseLecture lecture = new DatabaseLecture(course, date, db);
        assertEquals(lecture.getDate(),date);
        assertTrue(lecture.getLectureAttendance().isEmpty());
        lecture.recordAttendance(john,Attendance::markAttended);
        assertFalse(lecture.getLectureAttendance().isEmpty());
        assertEquals(lecture.getAttendanceOfStudent(john.getNetID()),"Attended");
        lecture.modifyAttendanceOfStudent(john,Attendance::markTardy);
        assertEquals(lecture.getAttendanceOfStudent(john.getNetID()),"Tardy");


    }

}