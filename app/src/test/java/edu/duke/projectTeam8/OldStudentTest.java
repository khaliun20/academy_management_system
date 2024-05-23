package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class OldStudentTest {
  @Test
  public void test_modifyPreferredName() {
    OldStudent john = new OldStudent(new Name("Jonathan", "Smith", "John"), "js101", "js101@duke.edu");
    assertEquals("John Smith", john.toString());
    john.modifyPreferredName("Jane");
    assertEquals("Jane Smith", john.toString());
  }

  @Test
  public void test_equals() {
    OldStudent john = new OldStudent(new Name("John", "Smith"), "js101", "js101@duke.edu");
    OldStudent jake = new OldStudent(new Name("Jake", "Smith"), "js102", "js102@duke.edu");
    assertFalse(john.equals(jake));
    assertTrue(john.equals(john));
    assertFalse(john.equals("John"));
  }

  @Test
  public void test_hashCode() {
    Name name1 = new Name("John", "Doe");
    String netID1 = "jd123";
    String email1 = "john.doe@example.com";

    Name name2 = new Name("Jane", "Doe");
    String netID2 = "jd123"; // same netID as student1 to test equal hashcode
    String email2 = "jane.doe@example.com";

    Name name3 = new Name("Jake", "Doe");
    String netID3 = "jd124"; // different netID to test non-equal hashcode
    String email3 = "jake.doe@example.com";

    OldStudent oldStudent1 = new OldStudent(name1, netID1, email1);
    OldStudent oldStudent2 = new OldStudent(name2, netID2, email2);
    OldStudent oldStudent3 = new OldStudent(name3, netID3, email3);

    assertEquals(oldStudent1.hashCode(), oldStudent2.hashCode());
    assertNotEquals(oldStudent1.hashCode(), oldStudent3.hashCode());

    Name name4 = new Name("Tom", "Cook");
    String netID4 = null;
    String email4 = "whatever@gmail.com";
    OldStudent oldStudent4 = new OldStudent(name4, netID4, email4);
    assertEquals(0, oldStudent4.hashCode());
  }

  @Test
  public void test_getEmail() {
    Name name1 = new Name("John", "Doe");
    String netID1 = "jd123";
    String email1 = "john.doe@example.com";
    String email2 = "wrongemail";
    OldStudent oldStudent1 = new OldStudent(name1, netID1, email1);
    assertEquals(email1, oldStudent1.getEmail());
    assertNotEquals(email2, oldStudent1.getEmail());
  }

  @Test
  public void test_addClass() {
    OldStudent john = new OldStudent(new Name("John", "Smith"), "js101", "js101@duke.edu");
    assertTrue(john.getClasses().isEmpty());
    CourseID cid = new CourseID("ECE", 110);
    OldCourse test = new OldCourse(cid);
    john.addClass(test);
    assertTrue(john.getClasses().contains(test));
    assertEquals(1, john.getClasses().size());
    assertThrows(IllegalArgumentException.class, () -> john.addClass(test));
    try {
      john.addClass(test);
    } catch (Exception e) {
      assertEquals("John Smith is already enrolled in the course: ECE110", e.getMessage());
    }
  }

  // @Disabled
  // @Test
  // public void test_requestAccessToClass() throws IOException {
  // Student john = new Student(new Name("John", "Smith"), "js101",
  // "js101@duke.edu");
  // assertTrue(john.getClasses().isEmpty());
  // Professor testProfessor = new Professor(new Name("Joan", "Kennedy"));
  // CourseID cid = new CourseID("ECE", 110);
  // Course test = new Course(cid, testProfessor);
  // Integer count = 0;
  // for (Notification note : testProfessor.checkNotifications()) {
  // count++;
  // }
  // assertEquals(0, count);
  // john.requestAccessToClass(test);
  // assertFalse(john.getClasses().contains(test));
  // assertTrue(john.getClasses().isEmpty());
  // for (Notification note : testProfessor.checkNotifications()) {
  // assertEquals("John Smith has requested enrollment in your course: ECE110",
  // note.getMessage());
  // count++;
  // }
  // assertEquals(1, count);
  // assertEquals(1, john.getClasses().size());
  // assertThrows(IllegalArgumentException.class, () -> john.addClass(test));
  /*
   * try {
   * john.addClass(test);
   * } catch (Exception e) {
   * assertEquals("John Smith is already enrolled in the course: 1",
   * e.getMessage());
   * }
   * //
   */
  // }

  @Test
  public void test_dropClass() {
    OldStudent john = new OldStudent(new Name("John", "Smith"), "js101", "js101@duke.edu");
    CourseID cid = new CourseID("ECE", 110);
    OldCourse test = new OldCourse(cid);
    john.addClass(test);
    john.dropClass(test);
    assertTrue(john.getClasses().isEmpty());
    assertThrows(IllegalArgumentException.class, () -> john.dropClass(test));
    try {
      john.dropClass(test);
    } catch (Exception e) {
      assertEquals("John Smith is not enrolled in the course: ECE110", e.getMessage());
    }
  }

  @Test
  public void test_attendanceRecord() throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    OldStudent john = new OldStudent(new Name("John", "Smith"), "js101", "js101@duke.edu");
    CourseID cid = new CourseID("ECE", 110);
    OldCourse test = new OldCourse(cid);
    Date date = dateFormat.parse("10/21/2023");
    Attendance attendance = new Attendance();
    attendance.markAbsent();
    assertThrows(IllegalArgumentException.class, () -> john.addAttendanceRecord(test, date, attendance));
    try {
      john.addAttendanceRecord(test, date, attendance);
    } catch (Exception e) {
      assertEquals("John Smith is not enrolled in the course: ECE110", e.getMessage());
    }
    john.addClass(test);
    john.addAttendanceRecord(test, date, attendance);
    String expected = "John Smith's Attendance Record for Course: ECE110\n" +
        "  10/21/2023: Absent\n";
    assertEquals(expected, john.getAttendanceRecord(test).toString());
    Date date2 = dateFormat.parse("10/22/2023");
    john.addAttendanceRecord(test, date2, attendance);
    assertEquals(attendance, john.getAttendanceRecord(test).getAttendanceForDate(date2));
  }

  // @Disabled
  // @Test
  // public void test_getMyCourseIDs() {
  // Student john = new Student(new Name("John",
  // "Smith"),"js101","js101@duke.edu");
  // CourseID cid = new CourseID("ECE", 110);
  // CourseID cid2 = new CourseID("ECE", 111);
  // Course c1 = new Course(cid);
  // Course c2 = new Course(cid2);
  // john.addClass(c2);
  // john.addClass(c1);
  // Iterable<CourseID> courseIDs = john.getMyCourseIDs();
  // HashSet<CourseID> expectedCourseIDs = new HashSet<>();
  // expectedCourseIDs.add(10);
  // expectedCourseIDs.add(11);
  // for (CourseID id : courseIDs) {
  // assertTrue(expectedCourseIDs.contains(id));
  // }
  // }

  @Test
  void testEqualsSameObject() {
    OldStudent oldStudent = new OldStudent(new Name("John", "Doe"), "netID1", "email@example.com");
    assertTrue(oldStudent.equals(oldStudent));
  }

  @Test
  void testEqualsWithNull() {
    OldStudent oldStudent = new OldStudent(new Name("John", "Doe"), "netID1", "email@example.com");
    assertFalse(oldStudent.equals(null));
  }

  @Test
  void testEqualsDifferentClass() {
    OldStudent oldStudent = new OldStudent(new Name("John", "Doe"), "netID1", "email@example.com");
    Object other = new Object();
    assertFalse(oldStudent.equals(other));
  }

  @Test
  void testEqualsWithNetIDNullBoth() {
    OldStudent oldStudent1 = new OldStudent(new Name("John", "Doe"), null, "email@example.com");
    OldStudent oldStudent2 = new OldStudent(new Name("Jane", "Doe"), null, "email@example.com");
    assertTrue(oldStudent1.equals(oldStudent2));
  }

  @Test
  void testEqualsWithNetIDNullOneSide() {
    OldStudent oldStudent1 = new OldStudent(new Name("John", "Doe"), null, "email@example.com");
    OldStudent oldStudent2 = new OldStudent(new Name("Jane", "Doe"), "netID2", "email@example.com");
    assertFalse(oldStudent1.equals(oldStudent2));
  }

  @Test
  void testEqualsWithNetIDEqual() {
    OldStudent oldStudent1 = new OldStudent(new Name("John", "Doe"), "netID1", "email@example.com");
    OldStudent oldStudent2 = new OldStudent(new Name("Jane", "Doe"), "netID1", "email@example.com");
    assertTrue(oldStudent1.equals(oldStudent2));
  }

  @Test
  void testEqualsWithNetIDDifferent() {
    OldStudent oldStudent1 = new OldStudent(new Name("John", "Doe"), "netID1", "email@example.com");
    OldStudent oldStudent2 = new OldStudent(new Name("Jane", "Doe"), "netID2", "email@example.com");
    assertFalse(oldStudent1.equals(oldStudent2));
  }

  @Test
  void test_serialize() {
    OldStudent oldStudent1 = new OldStudent(new Name("John", "Doe"), "netID1", "email@example.com");

    String expected = "<Student><User><netID>netID1</netID><emailAddress>email@example.com</emailAddress></User>" +
        "<Name><firstName>John</firstName><lastName>Doe</lastName><preferredName>John</preferredName></Name></Student>";
    assertEquals(oldStudent1.serialize(), expected);

  }

  @Test
  public void test_XMLSerialize() throws Exception {
    OldStudent john = new OldStudent(new Name("John", "Smith"), "js101", "john@aol.com");
    OldCourse oldCourse = new OldCourse(new CourseID("Test", 651));
    oldCourse.enrollStudent(john);

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date date1 = dateFormat.parse("10/21/1999");
    Date date2 = dateFormat.parse("12/22/2022");
    john.addAttendanceRecord(oldCourse, date1, new Attendance());
    john.addAttendanceRecord(oldCourse, date2, new Attendance());
    john.getAttendanceRecord(oldCourse).modifyRecord(date1, (attendance) -> attendance.markTardy());
    john.getAttendanceRecord(oldCourse).modifyRecord(date2, (attendance) -> attendance.markAbsent());

    Serializer serializedRecords = john.serialize((string1, object) -> new XMLSerializer(string1, object),
        (string2, objects) -> new XMLSerializer(string2, objects));
    String expectedRecords = "<student>\n\t<first name>John</first name>\n\t<last name>Smith</last name>\n\t<email>john@aol.com</email>\n\t<netID>js101</netID>\n\t<course list>\n\t\t<course>\n\t\t\t<course id>Test651</course id>\n\t\t\t<enrollment status>true</enrollment status>\n\t\t\t<professor>\n\t\t\t\t<first name>Test</first name>\n\t\t\t\t<last name>Professor</last name>\n\t\t\t\t<email>Test@aol.com</email>\n\t\t\t\t<netID>Test101</netID>\n\t\t\t</professor>\n\t\t\t<attendance records>\n\t\t\t\t<record>\n\t\t\t\t\t<date>Thu Dec 22 00:00:00 EST 2022</date>\n\t\t\t\t\t<attendance>Absent</attendance>\n\t\t\t\t</record>\n\t\t\t\t<record>\n\t\t\t\t\t<date>Thu Oct 21 00:00:00 EDT 1999</date>\n\t\t\t\t\t<attendance>Tardy</attendance>\n\t\t\t\t</record>\n\t\t\t</attendance records>\n\t\t</course>\n\t</course list>\n</student>";
    assertEquals(expectedRecords, serializedRecords.toString());
  }

}
