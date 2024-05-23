package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class OldEnrollmentListTest {
    OldStudent joe = new OldStudent(new Name("Joe", "Doe", "Jack"), "jd101", "jd101@duke.edu");
    OldStudent jane = new OldStudent(new Name("Jane", "Doe", "Jenny"), "jd102", "jd102@duke.edu");
    OldStudent john = new OldStudent(new Name("John", "Doe", "Jess"), "jd103", "jd103@duke.edu");

    @Test
    public void test_serialize() {
        OldCourse oldCourse = new OldCourse(new CourseID("ECE", 110));
        OldEnrollmentList oldEnrollmentList = new OldEnrollmentList(oldCourse);
        oldEnrollmentList.addStudentToCourseEnrollment(joe);
        oldEnrollmentList.addStudentToCourseEnrollment(john);
        joe.addClass(oldCourse);
        john.addClass(oldCourse);

        String statusTrue = "<enrollStatus>enrolled</enrollStatus>";
        String statusFalse = "<enrollStatus>dropped</enrollStatus>";

        String expected1 = "<EnrollmentList>" + joe.serialize() + statusTrue + john.serialize() + statusTrue +
                "</EnrollmentList>";
        String expected2 = "<EnrollmentList>" + john.serialize() + statusTrue + joe.serialize() + statusTrue +
                "</EnrollmentList>";
        String output = oldEnrollmentList.serialize();
        assertTrue(expected1.equals(output) || expected2.equals(output));
        oldEnrollmentList.dropStudent(joe);
        output = oldEnrollmentList.serialize();
        expected1 = "<EnrollmentList>" + joe.serialize() + statusFalse + john.serialize() + statusTrue +
                "</EnrollmentList>";
        expected2 = "<EnrollmentList>" + john.serialize() + statusTrue + joe.serialize() + statusFalse +
                "</EnrollmentList>";
        assertTrue(expected1.equals(output) || expected2.equals(output));

        // TODO: adding test code for dropped students after drop function is enabled

    }

    @Test
    public void test_emptyEnrollmentlist() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList = new OldEnrollmentList(c);
        assertEquals(oldEnrollmentList.getNumStudents(), 0);
    }

    @Test
    public void test_addToEnrollmentlist() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList = new OldEnrollmentList(c);
        oldEnrollmentList.addStudentToCourseEnrollment(joe);
        oldEnrollmentList.addStudentToCourseEnrollment(john);
        assertEquals(oldEnrollmentList.getNumStudents(), 2);
    }

    @Test
    public void test_Enrollmentlist() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList = new OldEnrollmentList(c);
        oldEnrollmentList.addStudentToCourseEnrollment(joe);
        assertThrows(IllegalArgumentException.class, () -> oldEnrollmentList.addStudentToCourseEnrollment(joe));
    }

    @Test
    public void test_emptyToString() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList = new OldEnrollmentList(c);
        String expected = "Enrollment:\n";
        assertEquals(expected, oldEnrollmentList.toString());
    }

    @Test
    public void test_nonEmptyToString() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList = new OldEnrollmentList(c);
        oldEnrollmentList.addStudentToCourseEnrollment(joe);
        oldEnrollmentList.addStudentToCourseEnrollment(jane);

        String result = oldEnrollmentList.toString();
        assertTrue(result.contains("Jack Doe"));
        assertTrue(result.contains("Jenny Doe"));
        assertTrue(result.startsWith("Enrollment:\n"));
    }

    @Test
    public void test_equals() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList1 = new OldEnrollmentList(c);
        oldEnrollmentList1.addStudentToCourseEnrollment(joe);
        oldEnrollmentList1.addStudentToCourseEnrollment(jane);

        CourseID cid2 = new CourseID("ECE", 111);
        OldCourse c2 = new OldCourse(cid2);
        OldEnrollmentList oldEnrollmentList2 = new OldEnrollmentList(c2);
        oldEnrollmentList2.addStudentToCourseEnrollment(joe);
        oldEnrollmentList2.addStudentToCourseEnrollment(jane);
        assertFalse(oldEnrollmentList1.equals(oldEnrollmentList2));
    }

    @Test
    public void test_notEquals() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList1 = new OldEnrollmentList(c);
        oldEnrollmentList1.addStudentToCourseEnrollment(joe);

        CourseID cid2 = new CourseID("ECE", 111);
        OldCourse c2 = new OldCourse(cid2);
        OldEnrollmentList oldEnrollmentList2 = new OldEnrollmentList(c2);
        oldEnrollmentList2.addStudentToCourseEnrollment(joe);
        oldEnrollmentList2.addStudentToCourseEnrollment(jane);
        assertFalse(oldEnrollmentList1.equals(oldEnrollmentList2));
    }

    @Test
    public void test_notEquals2() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList1 = new OldEnrollmentList(c);
        oldEnrollmentList1.addStudentToCourseEnrollment(joe);
        oldEnrollmentList1.addStudentToCourseEnrollment(jane);

        CourseID cid2 = new CourseID("ECE", 111);
        OldCourse c2 = new OldCourse(cid2);
        OldEnrollmentList oldEnrollmentList2 = new OldEnrollmentList(c2);
        oldEnrollmentList2.addStudentToCourseEnrollment(joe);
        assertFalse(oldEnrollmentList1.equals(oldEnrollmentList2));
    }

    @Test
    public void test_notEquals3() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList1 = new OldEnrollmentList(c);
        oldEnrollmentList1.addStudentToCourseEnrollment(joe);
        oldEnrollmentList1.addStudentToCourseEnrollment(jane);

        CourseID cid2 = new CourseID("ECE", 111);
        OldCourse c2 = new OldCourse(cid2);
        OldEnrollmentList oldEnrollmentList2 = new OldEnrollmentList(c2);
        oldEnrollmentList2.addStudentToCourseEnrollment(joe);
        oldEnrollmentList2.addStudentToCourseEnrollment(john);
        assertFalse(oldEnrollmentList1.equals(oldEnrollmentList2));
    }

    @Test
    public void test_notEquals4() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList = new OldEnrollmentList(c);
        oldEnrollmentList.addStudentToCourseEnrollment(joe);
        OldStudent job = new OldStudent(new Name("Job", "Doe", "Jack"), "jd101", "jd101@duke.edu");
        assertFalse(oldEnrollmentList.equals(job));

    }

    @Test
    public void test_equalsHashcode() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList1 = new OldEnrollmentList(c);

        oldEnrollmentList1.addStudentToCourseEnrollment(joe);
        oldEnrollmentList1.addStudentToCourseEnrollment(jane);
        OldEnrollmentList oldEnrollmentList2 = new OldEnrollmentList(c);
        oldEnrollmentList2.addStudentToCourseEnrollment(joe);
        oldEnrollmentList2.addStudentToCourseEnrollment(jane);
        assertEquals(oldEnrollmentList1.hashCode(), oldEnrollmentList2.hashCode());

    }

    @Test
    public void test_notEqualsHashcode() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList1 = new OldEnrollmentList(c);

        oldEnrollmentList1.addStudentToCourseEnrollment(jane);
        oldEnrollmentList1.addStudentToCourseEnrollment(john);

        CourseID cid2 = new CourseID("ECE", 111);
        OldCourse c2 = new OldCourse(cid2);
        OldEnrollmentList oldEnrollmentList2 = new OldEnrollmentList(c2);
        oldEnrollmentList2.addStudentToCourseEnrollment(joe);
        oldEnrollmentList2.addStudentToCourseEnrollment(jane);
        assertNotEquals(oldEnrollmentList1.hashCode(), oldEnrollmentList2.hashCode());

    }

    @Test
    public void test_notEqualsHashcode2() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList1 = new OldEnrollmentList(c);
        oldEnrollmentList1.addStudentToCourseEnrollment(jane);
        CourseID cid2 = new CourseID("ECE", 111);
        OldCourse c2 = new OldCourse(cid2);
        OldEnrollmentList oldEnrollmentList2 = new OldEnrollmentList(c2);
        assertNotEquals(oldEnrollmentList1.hashCode(), oldEnrollmentList2.hashCode());

    }

    @Test
    public void test_notEqualsHashcode3() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        OldEnrollmentList oldEnrollmentList1 = new OldEnrollmentList(c);

        CourseID cid2 = new CourseID("ECE", 111);
        OldCourse c2 = new OldCourse(cid2);
        OldEnrollmentList oldEnrollmentList2 = new OldEnrollmentList(c2);
        assertNotEquals(oldEnrollmentList1.hashCode(), oldEnrollmentList2.hashCode());

    }

    @Test
    public void test_getEnrollment() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse c = new OldCourse(cid);
        // EnrollmentList enrollmentList1 = new EnrollmentList(c);
        c.enrollStudent(jane);
        Set<Student> actual = c.getActiveEnrollment();
        HashSet<OldStudent> actualHashSet = new HashSet<>();
        for (Student oldStudent : actual) {
            actualHashSet.add((OldStudent) oldStudent);
        }
        HashSet<OldStudent> expected = new HashSet<>();
        expected.add(jane);

        assertEquals(expected.size(), actualHashSet.size());
        for (OldStudent oldStudent : expected) {
            assertTrue(actualHashSet.contains(oldStudent));
        }

    }

    @Test
    public void test_getEnrollment1() {
        CourseID cid = new CourseID("ECE", 110);
        CourseID cid2 = new CourseID("ECE", 111);
        OldCourse c1 = new OldCourse(cid);
        OldCourse c2 = new OldCourse(cid2);
        OldEnrollmentList enrollmentList1 = new OldEnrollmentList(c1);

        jane.addClass(c1);
        jane.addClass(c2);
        enrollmentList1.addStudentToCourseEnrollment(jane);

        Map<OldStudent, Boolean> actual = enrollmentList1.getCourseEnrollment();
        HashSet<OldStudent> actualHashSet = new HashSet<>();
        for (OldStudent student : actual.keySet()) {
            actualHashSet.add(student);
        }

        HashSet<Student> expected = new HashSet<>();
        expected.add(jane);

        assertEquals(expected.size(), actualHashSet.size());
        for (Student student : expected) {
            assertTrue(actualHashSet.contains(student));
        }
    }

    // private void compare_enrollments(Iterable<Student> actual, HashSet<Student>
    // expected) {
    // HashSet<Student> actualHashSet = new HashSet<>();
    // for (Student student : actual) {
    // actualHashSet.add(student);
    // }
    // assertEquals(expected.size(), actualHashSet.size());
    // for (Student student : expected) {
    // assertTrue(actualHashSet.contains(student));
    // }
    // }

    @Test
    public void test_getCourseID() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse oldCourse1 = new OldCourse(cid);
        CourseID cid2 = new CourseID("ECE", 111);
        OldCourse oldCourse2 = new OldCourse(cid2);
        OldEnrollmentList oldEnrollmentList1 = new OldEnrollmentList(oldCourse1);
        assertEquals(cid.toString(), oldEnrollmentList1.getCourseID());
        assertNotEquals(cid2.toString(), oldEnrollmentList1.getCourseID());
    }

    @Test
    public void test_dropStudent() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse oldCourse1 = new OldCourse(cid);
        oldCourse1.enrollStudent(jane);
        oldCourse1.enrollStudent(joe);
        OldEnrollmentList oldEnrollmentList1 = oldCourse1.getEnrollment();
        oldEnrollmentList1.dropStudent(jane);
        assertEquals(2, oldEnrollmentList1.getNumStudents());
    }

    @Test
    public void test_dropStudent2() {
        CourseID cid = new CourseID("ECE", 110);
        OldCourse oldCourse1 = new OldCourse(cid);
        oldCourse1.enrollStudent(jane);
        OldEnrollmentList oldEnrollmentList1 = oldCourse1.getEnrollment();
        assertThrows(IllegalArgumentException.class, () -> oldEnrollmentList1.dropStudent(joe));
    }
}
