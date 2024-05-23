package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.URL;
import java.util.jar.Attributes.Name;
import java.util.Set;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.jupiter.api.Test;


public class ControllerTest {
  //   edu.duke.projectTeam8.Name joe = new edu.duke.projectTeam8.Name("joe", "biden");
  //   Professor p = new Professor(joe);
  //   ByteArrayOutputStream bytes = new ByteArrayOutputStream();

  // private MainMenuHandler createController(Professor teacher, String inputData, ByteArrayOutputStream bytes) {
  //   AcademicEnrollment academicEnrollment = new AcademicEnrollment();
  //   BufferedReader input = new BufferedReader(new StringReader(inputData));
  //   PrintStream output = new PrintStream(bytes, true);
  //   return new MainMenuHandler(teacher, input, output,academicEnrollment);

  // }
  /*
   * Test take attendance task selection
   */
  // @Test
  // public void test_invalidAndValidTaskSelectionA() throws IOException {
  //   String inputData = "c\n\ntttt\n66\n1\n1";
  //   Controller c = createController(p, inputData, bytes);
  //   c.startTaskSelection();
  //   String expectedOutput = "Please select the task you want to perform:\n1. Take Attendance\n2. Get Enrollment List for New Course\n" +
  //       "Invalid input. Please enter a valid integer.\n" +
  //       "Invalid input. Please enter a valid integer.\n"+
  //       "Invalid input. Please enter a valid integer.\n"+
  //     "Invalid Selection. Please choose again\n";
  //   assertEquals(expectedOutput, bytes.toString());

  // }

  // @Test
  //   public void test_invalidAndValidTaskSelectionB() throws IOException {
  //       String inputData = "2";
  //       Controller c = createController(p, inputData, bytes);
  //       c.startTaskSelection();
  //       String expectedOutput = "Please select the task you want to perform:\n1. Take Attendance\n2. Get Enrollment List for New Course\n";
  //       assertEquals(expectedOutput, bytes.toString());
  //   }


  // @Test
  // public void test_handleParsingOfNewCourse() throws IOException{
  //   URL resourceUrl = getClass().getClassLoader().getResource("651.csv");
  //   assertNotNull(resourceUrl);
  //   File csvFile = new File(resourceUrl.getFile());
  //   String inputData = csvFile.getAbsolutePath();

  //   Controller c = createController(p, inputData, bytes);
  //   String expectedOutput = ("Please enter path to the enrollment list file\n");
  //   c.parseEnrollmentList();
  //   Professor teacher = c.getProfessor();
  //   assertEquals(1,teacher.getCourses().size());

  // }

  // @Test
  // public void test_studentsInCourseAfterParsing() throws IOException{

  //   URL resourceUrl2 = getClass().getClassLoader().getResource("651.csv");
  //   assertNotNull(resourceUrl2);
  //   File csvFile2 = new File(resourceUrl2.getFile());
  //   String inputData = csvFile2.getAbsolutePath();


  //   Controller c = createController(p, inputData, bytes);
  //   String expectedOutput = ("Please enter path to the enrollment list file\n");
  //   c.parseEnrollmentList();
  //   Professor teacher = c.getProfessor();
  //   Set<Course> courses = teacher.getCourses();
  //   for (Course course: courses){
  //     Set<Student> students = course.getEnrollmentList();
  //     assertEquals(6, students.size());
  //   }
  // }

  






  


  
  

}


