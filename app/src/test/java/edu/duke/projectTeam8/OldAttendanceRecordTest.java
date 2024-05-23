package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class OldAttendanceRecordTest {
  @Test
  public void test_addRecord() throws ParseException {
    OldStudent john = new OldStudent(new Name("John", "Smith"), "js101", "js101@duke.edu");
    CourseID cid = new CourseID("ECE", 110);
    OldCourse test = new OldCourse(cid);

    OldAttendanceRecord record = new OldAttendanceRecord(john, test);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date date = dateFormat.parse("10/21/2023");
    Attendance attendance = new Attendance();
    attendance.markAbsent();
    record.addRecord(date, attendance);
    assertEquals(attendance, record.getAttendanceForDate(date));
    String expected = "John Smith's Attendance Record for Course: ECE110\n" +
        "  10/21/2023: Absent\n";
    assertEquals(expected, record.toString());
    assertThrows(IllegalArgumentException.class, () -> record.addRecord(date, attendance));
    Date date2 = dateFormat.parse("10/22/2023");
    assertThrows(IllegalArgumentException.class, () -> record.getAttendanceForDate(date2));
  }

  @Test
  public void test_modifyRecord() throws ParseException {
    OldStudent john = new OldStudent(new Name("John", "Smith"), "js101", "js101@duke.edu");
    CourseID cid = new CourseID("ECE", 110);
    OldCourse test = new OldCourse(cid);
    OldAttendanceRecord record = new OldAttendanceRecord(john, test);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date date = dateFormat.parse("10/21/2023");
    Attendance attendance = new Attendance();
    attendance.markAbsent();
    record.addRecord(date, attendance);
    Date date2 = dateFormat.parse("10/22/2023");
    record.modifyRecord(date, (attendance_value) -> attendance_value.markTardy());
    assertEquals(attendance, record.getAttendanceForDate(date));
    assertEquals("Tardy", attendance.toString());
    assertThrows(IllegalArgumentException.class,
        () -> record.modifyRecord(date2, (attendance_value) -> attendance_value.markTardy()));
    record.modifyRecord(date, (attendance_value) -> attendance_value.markAbsent());
    assertEquals(attendance, record.getAttendanceForDate(date));
    assertEquals("Absent", attendance.toString());
    record.modifyRecord(date, (attendance_value) -> attendance_value.markAttended());
    assertEquals(attendance, record.getAttendanceForDate(date));
    assertEquals("Attended", attendance.toString());
  }

  // @Test
  // public void test_XMLSerializerConstructorStringVarArgs() throws
  // ParseException {
  // Student john = new Student(new Name("John", "Smith"), "js101",
  // "john@aol.com");
  // CourseID cid = new CourseID("Test", 651);
  // Course course = new Course(cid);
  // course.enrollStudent(john);

  // AttendanceRecord records = course.getAttendanceRecord(john);
  // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
  // Date date1 = dateFormat.parse("10/21/1999");
  // Date date2 = dateFormat.parse("12/22/2022");
  // records.addRecord(date1, new Attendance());
  // records.addRecord(date2, new Attendance());
  // records.modifyRecord(date1, (attendance) -> attendance.markTardy());
  // records.modifyRecord(date2, (attendance) -> attendance.markAbsent());
  // Serializer serializedRecords = records.serialize((string1, object) -> new
  // XMLSerializer(string1, object),
  // (string2, objects) -> new XMLSerializer(string2, objects));
  // String expectedRecords = "<attendance records>\n\t<record>\n\t\t<date>Thu Dec
  // 22 00:00:00 EST
  // 2022</date>\n\t\t<attendance>Absent</attendance>\n\t</record>\n\t<record>\n\t\t<date>Thu
  // Oct 21 00:00:00 EDT
  // 1999</date>\n\t\t<attendance>Tardy</attendance>\n\t</record>\n</attendance
  // records>";
  // assertEquals(expectedRecords, serializedRecords.toString());
  // }

}
