package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import java.io.IOException;
import java.util.List;
import java.beans.Transient;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.file.NoSuchFileException;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class DataParserTest {

  // @Test
  // public void test_parse() throws IOException {
  // AcademicEnrollment academicEnrollment = new AcademicEnrollment();
  // URL resourceUrl = getClass().getClassLoader().getResource("ECE651.csv");
  // assertNotNull(resourceUrl, "Test file should exist");

  // File csvFile = new File(resourceUrl.getFile());
  // String csvFilePath = csvFile.getAbsolutePath();
  // // String csvFilePath =
  // "/home/ws202/project-team-8/app/src/test/java/edu/duke/TestFiles/651.csv";

  // DataParser dataParser = new DataParser(csvFilePath, academicEnrollment);
  // Course course = dataParser.parseAndEnrollStudents();

  // assertNotNull(course);
  // assertEquals("ECE651", course.toString());
  // Iterable<Student> students = course.getEnrollmentList();
  // assertNotNull(students);

  // int count = 0;
  // for (Student student : students) {
  // count++;
  // }
  // assertEquals(6, count);
  // }

  // @Test
  // public void test_parse2() throws IOException {
  // AcademicEnrollment academicEnrollment = new AcademicEnrollment();

  // // String csvFilePath1 =
  // "/home/ws202/project-team-8/app/src/test/java/edu/duke/TestFiles/651.csv";
  // // String csvFilePath2 =
  // "/home/ws202/project-team-8/app/src/test/java/edu/duke/TestFiles/1000.csv";
  // URL resourceUrl1 = getClass().getClassLoader().getResource("ECE651.csv");
  // assertNotNull(resourceUrl1, "Test file 651.csv should exist");

  // URL resourceUrl2 = getClass().getClassLoader().getResource("ECE1000.csv");
  // assertNotNull(resourceUrl2, "Test file 1000.csv should exist");

  // File csvFile1 = new File(resourceUrl1.getFile());
  // String csvFilePath1 = csvFile1.getAbsolutePath();

  // File csvFile2 = new File(resourceUrl2.getFile());
  // String csvFilePath2 = csvFile2.getAbsolutePath();
  // DataParser dataParser = new DataParser(csvFilePath1, academicEnrollment);
  // Course course = dataParser.parseAndEnrollStudents();
  // assertNotNull(course);
  // assertEquals("ECE651", course.toString());
  // Iterable<Student> students = course.getEnrollmentList();
  // assertNotNull(students);
  // int count = 0;
  // for (Student student : students) {
  // count++;
  // }
  // assertEquals(6, count);

  // DataParser dataParser2 = new DataParser(csvFilePath2, academicEnrollment);
  // Course course2 = dataParser2.parseAndEnrollStudents();
  // assertNotNull(course2);
  // assertEquals("ECE1000", course2.toString());
  // Iterable<Student> students2 = course2.getEnrollmentList();
  // assertNotNull(students2);
  // int count2 = 0;
  // for (Student student : students2) {
  // count2++;
  // }
  // assertEquals(5, count2);

  // int expectedStudentCount = 8;
  // assertEquals(expectedStudentCount, academicEnrollment.getNumberOfStudents());
  // }
  // @Test
  // public void test_CSVreadingerror() throws IOException {
  // AcademicEnrollment academicEnrollment = new AcademicEnrollment();
  // String csvFilePath1 = "nonexistent/fake.csv";

  // DataParser parser = new DataParser(csvFilePath1, academicEnrollment);
  // assertThrows(IOException.class, () -> parser.parseAndEnrollStudents());
  // }

  // @Test
  // public void test_missingNetID() throws IOException {
  // AcademicEnrollment academicEnrollment = new AcademicEnrollment();
  // URL resourceUrl = getClass().getClassLoader().getResource("60000000.csv");
  // assertNotNull(resourceUrl);
  // File csvFile = new File(resourceUrl.getFile());
  // String csvFilePath = csvFile.getAbsolutePath();
  // DataParser dataParser = new DataParser(csvFilePath, academicEnrollment);
  // assertThrows(IllegalArgumentException.class, () ->
  // dataParser.parseAndEnrollStudents());
  // }

  // @Test
  // public void test_missingname() throws IOException {
  // AcademicEnrollment academicEnrollment = new AcademicEnrollment();
  // URL resourceUrl = getClass().getClassLoader().getResource("60000001.csv");
  // assertNotNull(resourceUrl);
  // File csvFile = new File(resourceUrl.getFile());
  // String csvFilePath = csvFile.getAbsolutePath();
  // DataParser dataParser = new DataParser(csvFilePath, academicEnrollment);
  // assertThrows(IllegalArgumentException.class, () ->
  // dataParser.parseAndEnrollStudents());
  // }

  // @Test
  // public void test_filenameERROR() throws IOException {
  // AcademicEnrollment academicEnrollment = new AcademicEnrollment();
  // URL resourceUrl = getClass().getClassLoader().getResource("ECE.csv");
  // assertNotNull(resourceUrl);
  // File csvFile = new File(resourceUrl.getFile());
  // String csvFilePath = csvFile.getAbsolutePath();
  // DataParser dataParser = new DataParser(csvFilePath, academicEnrollment);
  // assertThrows(IllegalArgumentException.class, () ->
  // dataParser.parseAndEnrollStudents());
  // }

  // public void test_parsewithsection() throws IOException {
  // AcademicEnrollment academicEnrollment = new AcademicEnrollment();
  // URL resourceUrl = getClass().getClassLoader().getResource("ECE651-001.csv");
  // assertNotNull(resourceUrl, "Test file should exist");

  // File csvFile = new File(resourceUrl.getFile());
  // String csvFilePath = csvFile.getAbsolutePath();
  // DataParser dataParser = new DataParser(csvFilePath, academicEnrollment);
  // Course course = dataParser.parseAndEnrollStudents();

  // assertNotNull(course);
  // assertEquals("ECE651-001", course.toString());
  // Iterable<Student> students = course.getEnrollmentList();
  // assertNotNull(students);

  // int count = 0;
  // for (Student student : students) {
  // count++;
  // }
  // assertEquals(6, count);

  // }
  // @Test
  // public void test_parseError() throws IOException {
  // AcademicEnrollment academicEnrollment = new AcademicEnrollment();
  // URL resourceUrl1 = getClass().getClassLoader().getResource("fake.csv");
  // String filePath = null;
  // try {
  // filePath = new File(resourceUrl.toURI()).getAbsolutePath();
  // } catch (URISyntaxException e) {
  // fail("The URL to URI conversion resulted in an error: " + e.getMessage());
  // }
  // String csvFilePath
  // ="/Users/haliunaa/project-team-8/app/src/test/java/edu/duke/TestFiles/fake.csv";

  // DataParser dataParser = new DataParser(csvFilePath, academicEnrollment);
  // assertThrows(IOException.class, () -> dataParser.parseAndEnrollStudents());
  // }

  // @Test
  // public void test_parseAndEnrollStudents_Exception() {
  // AcademicEnrollment academicEnrollment = new AcademicEnrollment();

  // String csvFilePath =
  // "/home/ws202/project-team-8/app/src/test/java/edu/duke/TestFiles/incomplete.csv";
  // DataParser dataParser = new DataParser(csvFilePath, academicEnrollment);
  // assertThrows(IOException.class, () -> dataParser.parseAndEnrollStudents());

  // }

}
