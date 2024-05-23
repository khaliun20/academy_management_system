package edu.duke.projectTeam8;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseCourseTest {
    MySQLTestDatabase db = new MySQLTestDatabase();
    Professor lo = db.getProfessor("lo110");
    Student john = db.getStudent("jd102");
    Course course = new DatabaseCourse("MATH502-001", lo, db);
    WeeklyTextSummary summarizer = new WeeklyTextSummary();
    DatabaseCourseTest() throws SQLException, IOException, NoSuchAlgorithmException {
    }

    @Test
    public void test_getProfessor() throws Exception{
        Professor prof = course.getProfessor();
        Professor prof1  = db.getProfessorByCourse(course.getCourseID());
        assertEquals(lo,prof);
        assertEquals(prof1,prof);
    }

    @Test
    public void test_getLectureList() throws Exception {

        List<Lecture> lectureList = course.getLecturesList();
        assertTrue(lectureList.isEmpty());
        Date date = summarizer.strToDate("10/22/2023 10:22:00 EDT");
        Date date1 = summarizer.strToDate("10/22/2023 12:34:56 EDT");
        DatabaseAttendanceRecord record = new DatabaseAttendanceRecord(john, course, db);
        Attendance attendance = new Attendance();
        attendance.markAttended();
        Attendance attendance1 = new Attendance();
        attendance1.markTardy();
        record.addRecord(date, attendance);
        record.addRecord(date1, attendance1);
        HashSet<Date> actual = new HashSet<>();
        for (Lecture l: course.getLecturesList()){
            actual.add(l.getDate());
        }
        HashSet<Date> expected = new HashSet<>();
        expected.add(date);
        expected.add(date1);
        assertIterableEquals(expected,actual);
    }
    @Test
    public void test_getActiveEnrollment() throws Exception {
        db.reset();
        assertFalse(course.getActiveEnrollment().isEmpty());

//        assertIterableEquals(expected,actual);
    }
}