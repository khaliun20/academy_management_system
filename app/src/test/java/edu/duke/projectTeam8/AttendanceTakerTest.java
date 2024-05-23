package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AttendanceTakerTest {


    @Test
    public void test_takeAttendanceAsAttended() throws Exception {
        OldStudent s = new OldStudent(new Name("John", "Doe"), "jd101", "jd101@duke.edu");
        OldCourse c = new OldCourse(new CourseID("ECE", 110));
        Date date4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:08:00");
        c.enrollStudent(s);
        OldLecture l = new OldLecture(c, date4);
        c.addLecture(l);


        AttendanceRecord ar = s.getAttendanceRecord(c);
        assertNotNull(ar);

        AttendanceTaker at = new AttendanceTaker(s, l, 1);
        at.initalUpdateToStudentAttendanceRecord();

        assertTrue(s.getAttendanceRecord(c).getAttendanceForDate(date4).toString().equals("Attended"));
        AttendanceTaker at1 = new AttendanceTaker(s, l, 2);
        at1.modifyStudentAttendanceRecord();
        assertTrue(s.getAttendanceRecord(c).getAttendanceForDate(date4).toString().equals("Tardy"));


    }

    @Test
    public void test_takeAttendanceAsTardry() throws Exception {
        OldStudent s = new OldStudent(new Name("John", "Doe"), "jd101", "jd101@duke.edu");
        OldCourse c = new OldCourse(new CourseID("ECE", 110));
        Date date4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:08:00");
        OldLecture l = new OldLecture(c, date4);
        c.addLecture(l);
        c.enrollStudent(s);
        AttendanceRecord ar = s.getAttendanceRecord(c);
        assertNotNull(ar);

        AttendanceTaker at = new AttendanceTaker(s, l, 2);
        at.initalUpdateToStudentAttendanceRecord();
        assertTrue(s.getAttendanceRecord(c).getAttendanceForDate(date4).toString().equals("Tardy"));

        AttendanceTaker at1 = new AttendanceTaker(s, l, 1);
        at1.modifyStudentAttendanceRecord();
        assertTrue(s.getAttendanceRecord(c).getAttendanceForDate(date4).toString().equals("Attended"));

    }

    @Test
    public void test_takeAttendanceAsAbsent() throws Exception {
        OldStudent s = new OldStudent(new Name("John", "Doe"), "jd101", "jd101@duke.edu");
        OldCourse c = new OldCourse(new CourseID("ECE", 110));
        Date date4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:08:00");
        OldLecture l = new OldLecture(c, date4);
        c.addLecture(l);
        c.enrollStudent(s);
        AttendanceRecord ar = s.getAttendanceRecord(c);
        assertNotNull(ar);

        AttendanceTaker at = new AttendanceTaker(s, l, 3);
        at.initalUpdateToStudentAttendanceRecord();
        assertTrue(s.getAttendanceRecord(c).getAttendanceForDate(date4).toString().equals("Absent"));

        AttendanceTaker at1 = new AttendanceTaker(s, l, 3);
        at1.modifyStudentAttendanceRecord();
        assertTrue(s.getAttendanceRecord(c).getAttendanceForDate(date4).toString().equals("Absent"));

    }


}
