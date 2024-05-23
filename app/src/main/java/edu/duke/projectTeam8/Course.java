package edu.duke.projectTeam8;


import java.util.List;

import java.util.Set;



/**
 * A Course object that represents a school's course
 */
public interface Course {


  /**
   * Get a set of all the students who are actively enrolled in the course AKA
   * didn't drop out.
   * 
   * @return a set of all the students who are actively enrolled in the course
   * @throws Exception if the students are not found
   */
  public Set<Student> getActiveEnrollment() throws Exception;

  /**
   * Get the attendance record for a student
   * 
   * @param student is the student to get the attendance record for
   * @return the attendance record for the student
   */
  public AttendanceRecord getAttendanceRecord(Student student);

 
  /**
   * Get the list of lectures for the course
   * 
   * @return the list of lectures for the course
   */
  public List<Lecture> getLecturesList() throws Exception;


  /**
   * Returns the CourseID associated with the Course
   *
   * @return Returns a CourseID object that defines the CoureID for the Course
   */
  public String getCourseID();

  /**
   * Gets the Professor associated with the Course
   * 
   * @return the Professor associated with the Course
   * @throws Exception if the Professor is not found
   */
  public Professor getProfessor() throws Exception;

}
