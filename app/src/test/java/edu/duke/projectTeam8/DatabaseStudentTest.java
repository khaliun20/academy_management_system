package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class DatabaseStudentTest {
    MySQLTestDatabase db = new MySQLTestDatabase();
    WeeklyTextSummary summarizer = new WeeklyTextSummary();

    public DatabaseStudentTest() throws SQLException, IOException, NoSuchAlgorithmException {
        db.cleanTables();
        db.importUsers("src/test/resources/schoolList.csv");
        db.importCourses("src/test/resources/courseList.csv");
        db.importEnrollment("src/test/resources/enrollList.csv");

    }

    private void reset() throws Exception {
        db.cleanTables();
        db.importUsers("src/test/resources/schoolList.csv");
        db.importCourses("src/test/resources/courseList.csv");
        db.importEnrollment("src/test/resources/enrollList.csv");
    }

    @Test
    public void test_getClasses() throws Exception {
        Student student = db.getStudent("jd102");
        Set<Course> classes = student.getClasses();
        Set<String> courseNames = classes.stream().map(Course::toString).collect(Collectors.toSet());
        List<String> expected = Arrays.asList(new String[]{"MATH502-001", "ECE100-001", "ECE200-001"});
        assertTrue(courseNames.containsAll(expected));
        for (Course c : classes) {
            System.out.println(c.getCourseID());
        }

    }

    @Test
    public void test_getAttendanceRecord() throws Exception {
        Student student = db.getStudent("jd102");
        List<Course> courses = student.getClasses().stream().toList();
        Course target = courses.get(0);
        String dateStr1 = "Mon Mar 04 00:00:00 EST 2024";
        String dateStr2 = "04/24/2024 00:00:00 EST";
        Date date2 = summarizer.strToDate(dateStr2);

        db.addAttendanceRecord(student.getNetID(),target.getCourseID(), dateStr1,"Tardy");
        Attendance attendance = new Attendance();
        attendance.markAttended();
        student.addAttendanceRecord(target, date2,attendance);
        String expected = "Wed Apr 24 01:00:00 EDT 2024 : Attended\n" +
                "Mon Mar 04 00:00:00 EST 2024 : Tardy\n";


        assertEquals(expected,target.getAttendanceRecord(student).getAllAttendanceRecordsAsString());
        assertEquals(expected,student.getAttendanceRecord(target).getAllAttendanceRecordsAsString());
        for (Date date :db.getAttendanceMap(student.getNetID(),target.getCourseID()).keySet()){
            student.modifyAttendanceRecord(target,date,Attendance::markAbsent);
        }
        expected = "Wed Apr 24 01:00:00 EDT 2024 : Absent\n" +
                "Mon Mar 04 00:00:00 EST 2024 : Absent\n";
        assertEquals(expected,student.getAttendanceRecord(target).getAllAttendanceRecordsAsString());

    }

    @Test
    public void test_reportSubscription() throws Exception {
        db.reset();
        Student student = db.getStudent("jd102");
        List<Course> courses = student.getClasses().stream().toList();
        Course target = courses.get(0);
        assertTrue(student.isSubscribedToWeekyReport(target));
        student.unsubscribeToWeeklyReport(target);
        assertFalse(student.isSubscribedToWeekyReport(target));
        student.subscribeToWeeklyReport(target);
        assertTrue(student.isSubscribedToWeekyReport(target));

    }
}
