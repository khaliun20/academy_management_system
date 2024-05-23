package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.io.IOException;

public class XMLSerializerTest {
  @Test
  public void test_deprecatedFunctionality() {
    XMLSerializer serializer = new XMLSerializer();// "<?xml version=\"1.0\" encoding = \"utf-16\"?>");
    String to = serializer.serialize("to", "Tove");
    String expectedTo = "<to>Tove</to>";
    assertEquals(expectedTo, to);
    String from = serializer.serialize("from", "Jani");
    String expectedFrom = "<from>Jani</from>";
    assertEquals(expectedFrom, from);
    String heading = serializer.serialize("heading", "Reminder");
    String expectedHeading = "<heading>Reminder</heading>";
    assertEquals(expectedHeading, heading);
    String body = serializer.serialize("body", "Don't forget about me!");
    String expectedBody = "<body>Don't forget about me!</body>";
    assertEquals(expectedBody, body);
    String note = serializer.serialize("note", to, from, heading, body);
    String expectedNote = "<note>\n\t<to>Tove</to>\n\t<from>Jani</from>\n\t<heading>Reminder</heading>\n\t<body>Don't forget about me!</body>\n</note>";
    assertEquals(expectedNote, note);
    String note2 = serializer.serialize("note", body);
    String expectedNote2 = "<note>\n\t<body>Don't forget about me!</body>\n</note>";
    assertEquals(expectedNote2, note2);
  }

  @Test
  public void test_XMLSerializerConstructorAbstractObject() {
    XMLSerializerLeaf to = new XMLSerializerLeaf("to", "Tove");
    String expectedTo = "<to>Tove</to>";
    XMLSerializer toSerializer = new XMLSerializer(to);
    assertEquals(expectedTo, toSerializer.toString());
    XMLSerializerLeaf from = new XMLSerializerLeaf("from", "Jani");
    String expectedFrom = "<from>Jani</from>";
    XMLSerializer fromSerializer = new XMLSerializer(from);
    assertEquals(expectedFrom, fromSerializer.toString());
    XMLSerializerLeaf heading = new XMLSerializerLeaf("heading", "Reminder");
    XMLSerializer headingSerializer = new XMLSerializer(heading);
    String expectedHeading = "<heading>Reminder</heading>";
    assertEquals(expectedHeading, headingSerializer.toString());
    XMLSerializerLeaf body = new XMLSerializerLeaf("body", "Don't forget about me!");
    XMLSerializer bodySerializer = new XMLSerializer(body);
    String expectedBody = "<body>Don't forget about me!</body>";
    assertEquals(expectedBody, bodySerializer.toString());
    XMLSerializerParent note = new XMLSerializerParent("note", toSerializer, fromSerializer, headingSerializer,
        bodySerializer);
    XMLSerializer noteSerializer = new XMLSerializer(note);
    String expectedNote = "<note>\n\t<to>Tove</to>\n\t<from>Jani</from>\n\t<heading>Reminder</heading>\n\t<body>Don't forget about me!</body>\n</note>";
    assertEquals(expectedNote, noteSerializer.toString());
    XMLSerializer noteSerializer2 = new XMLSerializer(new XMLSerializerParent("note", bodySerializer));
    String expectedNote2 = "<note>\n\t<body>Don't forget about me!</body>\n</note>";
    assertEquals(expectedNote2, noteSerializer2.toString());
  }

  @Test
  public void test_XMLSerializerConstructorStringObject() {
    XMLSerializer to = new XMLSerializer("to", "Tove");
    String expectedTo = "<to>Tove</to>";
    assertEquals(expectedTo, to.toString());
  }

  @Test
  public void test_XMLSerializerConstructorStringVarArgs() throws ParseException {
    OldStudent john = new OldStudent(new Name("John", "Smith"), "js101", "john@aol.com");
    OldCourse course = new OldCourse(new CourseID("Test", 651));
    course.enrollStudent(john);

    OldAttendanceRecord records = new OldAttendanceRecord(john, course);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date date1 = dateFormat.parse("10/21/1999");
    Date date2 = dateFormat.parse("12/22/2022");
    records.addRecord(date1, new Attendance());
    records.addRecord(date2, new Attendance());
    records.modifyRecord(date1, (attendance) -> attendance.markTardy());
    records.modifyRecord(date2, (attendance) -> attendance.markAbsent());
    Collection<XMLSerializer> serializers = new ArrayList<>();
    for (Date date : records.getValidDates()) {
      serializers.add(new XMLSerializer("record", new XMLSerializer("date", date),
          new XMLSerializer("attendance", records.getAttendanceForDate(date))));
    }
    XMLSerializer allRecords = new XMLSerializer("attendance records", serializers);
    String expectedRecords = "<attendance records>\n\t<record>\n\t\t<date>Thu Dec 22 00:00:00 EST 2022</date>\n\t\t<attendance>Absent</attendance>\n\t</record>\n\t<record>\n\t\t<date>Thu Oct 21 00:00:00 EDT 1999</date>\n\t\t<attendance>Tardy</attendance>\n\t</record>\n</attendance records>";
    assertEquals(expectedRecords, allRecords.toString());
  }

  @Test
  public void test_XMLSerializerConstructorStringObjectMap() {
    Name john = new Name("John", "Smith");
    Map<String, Function<Name, Object>> map = new HashMap<>();
    map.put("first name", (name) -> name.getFirstName());
    map.put("last name", (name) -> name.getLastName());
    XMLSerializer<Name> serializedName = new XMLSerializer("name", john, map);
    String expectedSerialization = "<name>\n\t<last name>Smith</last name>\n\t<first name>John</first name>\n</name>";
    assertEquals(expectedSerialization, serializedName.toString());
  }

//  @Test
//  public void test_XMLSerializerConstructorStringStringIterableMap() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException {
//    AcademicEnrollment enrollment = new OldAcademicEnrollment();
//    enrollment.createStudent(new Name("John", "Smith"), "js101", "john@aol.com");
//    enrollment.createStudent(new Name("Fred", "Smith"), "fs101", "fred@aol.com");
//    enrollment.createStudent(new Name("Mike", "Smith"), "ms101", "mike@aol.com");
//    Map<String, Function<Student, Object>> dataMap = new HashMap<>();
//    dataMap.put("netID", (student) -> student.getNetID());
//    dataMap.put("first_name", (student) -> student.getName().getFirstName());
//    dataMap.put("last_name", (student) -> student.getName().getLastName());
//    dataMap.put("email", (student) -> student.getEmail());
//    Iterable<Student> students = enrollment.getStudents();
//    XMLSerializer<Student> serializedEnrollment = new XMLSerializer("enrollment", "student", students, dataMap);
//    String expectedSerialization = "<enrollment>\n\t<student>\n\t\t<netID>js101</netID>\n\t\t<last_name>Smith</last_name>\n\t\t<first_name>John</first_name>\n\t\t<email>john@aol.com</email>\n\t</student>\n\t<student>\n\t\t<netID>fs101</netID>\n\t\t<last_name>Smith</last_name>\n\t\t<first_name>Fred</first_name>\n\t\t<email>fred@aol.com</email>\n\t</student>\n\t<student>\n\t\t<netID>ms101</netID>\n\t\t<last_name>Smith</last_name>\n\t\t<first_name>Mike</first_name>\n\t\t<email>mike@aol.com</email>\n\t</student>\n</enrollment>";
//    assertEquals(expectedSerialization, serializedEnrollment.toString());
//  }

  // This test has problem.

  // @Test
  // public void test_XMLSerializeAttendanceRecord() throws IOException,
  // ParseException {
  // AcademicEnrollment enrollment = new AcademicEnrollment();
  // enrollment.createStudent(new Name("John", "Smith"), "js101", "john@aol.com");
  // enrollment.createStudent(new Name("Fred", "Smith"), "fs101", "fred@aol.com");
  // enrollment.createStudent(new Name("Mike", "Smith"), "ms101", "mike@aol.com");
  // Course course = new Course(new CourseID("Test", 651));
  // course.addLecture(course);
  // Date date1 = new Date();
  // SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
  // Date date2 = dateFormat.parse("10/21/1988");
  // Lecture lecture1 = course.getCertainLecture(date1);
  // Lecture lecture2 = course.getCertainLecture(date2);
  // for (Student student : enrollment.getStudents()) {
  // course.enrollStudent(student);
  // student.addClass(course);
  // }
  // // course.initializeAttendanceRecords();
  // course.initializeAttendanceRecords(enrollment.getStudentByNetID("js101"));
  // course.initializeAttendanceRecords(enrollment.getStudentByNetID("fs101"));
  // course.initializeAttendanceRecords(enrollment.getStudentByNetID("ms101"));

  // for (Student student : enrollment.getStudents()) {
  // lecture1.recordAttendance(student, (attendance) -> attendance.markAbsent());
  // // lecture2.recordAttendance(student, (attendance) ->
  // attendance.markAbsent());
  // }
  // Map<String, BiFunction<Course, Student, Object>> dataMap2 = new HashMap<>();
  // dataMap2.put("courseID", (thisCourse, student) -> thisCourse.getCourseID());
  // // dataMap2.put("attendance records", (thisCourse, student) -> (new
  // // XMLSerializerParent("attendance records", "attendance record",
  // // thisCourse.getAttendanceRecord(student), dataMap3)).contentToString());
  // Map<String, Function<Student, Object>> dataMap = new HashMap<>();
  // dataMap.put("netID", (student) -> student.getNetID());
  // dataMap.put("first name", (student) -> student.getName().getFirstName());
  // dataMap.put("last name", (student) -> student.getName().getLastName());
  // dataMap.put("email", (student) -> student.getEmail());
  // dataMap.put("course list",
  // (student) -> (new XMLSerializerParent("course list", "course", student,
  // student.getClasses(), dataMap2))
  // .contentToString());
  // XMLSerializer<Student> serializedEnrollment = new XMLSerializer("enrollment",
  // "student", enrollment.getStudents(),
  // dataMap);
  // // assertEquals("fake", serializedEnrollment.toString());
  // }
}
