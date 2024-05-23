package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class OldLectureTest {

    @Test
    public void test_recordAttendance() throws Exception {
        AcademicEnrollmentV1 enrollment = new AcademicEnrollmentV1();
        enrollment.createStudent(new Name("John", "Smith"), "js101", "js@aol.com");
        OldStudent john = enrollment.getStudentByNetID("js101");
        CourseID cid = new CourseID("ECE", 110);
        OldCourse oldCourse = new OldCourse(cid);

        oldCourse.enrollStudent(john);
        OldLecture test = new OldLecture(oldCourse);

        test.recordAttendance(john, (attendance) -> attendance.markAbsent());
        assertEquals("Absent", test.getAttendanceOfStudent(john.getNetID()).toString());
        assertEquals("Student not in this course", test.getAttendanceOfStudent("yz696"));
    }

    @Test
    public void test_modifyAttendance() throws Exception {
        AcademicEnrollmentV1 enrollment = new AcademicEnrollmentV1();
        enrollment.createStudent(new Name("John", "Smith"), "js101", "js@aol.com");
        OldStudent john = enrollment.getStudentByNetID("js101");
        CourseID cid = new CourseID("ECE", 110);
        OldCourse oldCourse = new OldCourse(cid);
        oldCourse.enrollStudent(john);

        OldLecture test = new OldLecture(oldCourse);
        test.recordAttendance(john, (attendance) -> attendance.markAbsent());
        test.modifyAttendanceOfStudent(john, (attendance) -> attendance.markTardy());
        assertEquals("Tardy", test.getAttendanceOfStudent(john.getNetID()).toString());
        assertEquals("[Tardy]",test.getLectureAttendance().values().toString());
    }

}
