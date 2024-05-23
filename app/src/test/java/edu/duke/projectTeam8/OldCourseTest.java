package edu.duke.projectTeam8;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OldCourseTest {

    @Test
    public void test_getCourseID() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        assertEquals(cid.toString(), c.getCourseID());

        CourseID cid2 = new CourseID("ECE", 120, "002");
        OldCourse c2 = new OldCourse(cid2);
        assertEquals(cid2.toString(), c2.getCourseID());
    }

    @Test
    public void test_createCourse() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse oldCourse = new OldCourse(cid);
        assertEquals("ECE110", oldCourse.toString());
    }

    @Test
    public void test_enrollStudent() throws IOException {
        AcademicEnrollmentV1 enrollment = new AcademicEnrollmentV1();
        enrollment.createStudent(new Name("John", "Smith"), "js101", "john@aol.com");
        OldStudent john = enrollment.getStudentByNetID("js101");
        CourseID cid = new CourseID("ECE", 110);
        OldCourse oldCourse = new OldCourse(cid);
        oldCourse.enrollStudent(john);
        assertNotEquals(null, john.getAttendanceRecord(oldCourse));
        assertTrue(oldCourse.getLecturesList().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> oldCourse.enrollStudent(john));
        oldCourse.getEnrollment().dropStudent(john);
        oldCourse.enrollStudent(john);
        assertEquals(oldCourse.toString(),"ECE110");
        assertEquals(oldCourse.getCourseID(),"ECE110");

        assertEquals("Test Professor",oldCourse.getProfessor().toString());
    }

    // @Test
    // public void test_addAndGetLecture() {
    // CourseID cid = new CourseID("ECE", 110);
    // Course course = new Course(cid);
    // Lecture lecture = new Lecture(course);
    // course.addLecture(course);
    // assertEquals(lecture, course.getCertainLecture(lecture.getDate()));
    // }

    // @Test
    // public void test_InitializeAttendanceRecords(){

    // }

}
