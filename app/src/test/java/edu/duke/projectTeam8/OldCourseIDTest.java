package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class OldCourseIDTest {
  @Test
    void testCourseIDWithSection() {
      CourseID courseID = new CourseID("MATH", 101, "01");
      assertEquals("MATH", courseID.getSubject());
      assertEquals(Integer.valueOf(101), courseID.getNumber());
      assertTrue(courseID.getSection().isPresent());
      assertEquals("01", courseID.getSection().get());
      assertEquals("MATH101-01", courseID.toString());
    }

    @Test
    void testCourseIDWithoutSection() {
      CourseID courseID = new CourseID("MATH", 101);
      assertEquals("MATH", courseID.getSubject());
      assertEquals(Integer.valueOf(101), courseID.getNumber());
      assertFalse(courseID.getSection().isPresent());
      assertEquals("MATH101", courseID.toString());
    }

    @Test
    void testEqualsAndHashCode() {
      CourseID courseID1 = new CourseID("MATH", 101, "01");
      CourseID courseID2 = new CourseID("MATH", 101, "01");
      CourseID courseID3 = new CourseID("MATH", 101);
      CourseID courseID4 = new CourseID("MATH", 101, "02");

      assertEquals(courseID1, courseID2);
      assertNotEquals(courseID1, courseID3);
      assertNotEquals(courseID1, courseID4);
      assertEquals(courseID1.hashCode(), courseID2.hashCode());
      assertNotEquals(courseID1.hashCode(), courseID3.hashCode());
      assertNotEquals(courseID1.hashCode(), courseID4.hashCode());
    }

    @Test
    void testEqualsSelfReference() {
      CourseID courseID = new CourseID("ECE", 101, "01");
      assertTrue(courseID.equals(courseID));
    }

    @Test
    void testEqualsWithNonCourseIDObject() {
      CourseID courseID = new CourseID("ECE", 101, "01");
      Object other = new Object();
      assertFalse(courseID.equals(other));
    }
}
