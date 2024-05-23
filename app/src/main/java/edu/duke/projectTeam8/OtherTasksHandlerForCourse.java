package edu.duke.projectTeam8;

import java.util.Map;

public class OtherTasksHandlerForCourse {

  // private IOHandler ioHandler;

  // public OtherTasksHandlerForCourse(IOHandler ioHandler) {
  //   this.ioHandler = ioHandler;

  // }

  // /**
  //  * Drops a student from a specified course.
  //  * 
  //  * @param course The course from which the student is being dropped.
  //  */
  // public void dropStudentFromCourse(Course course) { // TODO: Confirm if this is correct
  //   if(course.getActiveEnrollment().size() == 0) {
  //     ioHandler.printNoStudentEnrolled(course); //TODO: CONFIRM
  //     return;
  //   }

  //   String prompt = "Please enter the netID of the student you would like to drop: \n";
  //   ioHandler.printNetIDPrompt(prompt, course);
  //   Student student = ioHandler.readAndValidateStudent(course, true);
  //   course.getEnrollment().dropStudent(student);
  //   ioHandler.printStudentDropped(student, course); 
  // }

  // /**
  //  * Adds a student to a specified course.
  //  * 
  //  * @param course The course from which the student is being dropped.
  //  */

  // public void addStudentToCourse(Course course) {
  //   Boolean success = false;
  //   while (!success) {
  //     try {
  //       String prompt = "Please enter the netID of the student you would like to add: \n";
  //       ioHandler.printNetIDPrompt(prompt, course);
  //       Student student = ioHandler.readAndValidateStudentAdd(course, false);
  //       course.enrollStudent(student);
  //       ioHandler.printStudentAdded(student, course); 

  //       success = true;
  //     } catch (IllegalArgumentException e) {
  //       ioHandler.printStudentAlreadyEnrolled(course);
  //       ioHandler.readString("b", "Please type b to go back to course page\n");
  //     }
  //   }
  // }




}
