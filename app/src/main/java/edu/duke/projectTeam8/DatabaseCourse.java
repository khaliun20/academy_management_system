package edu.duke.projectTeam8;

import java.util.Set;

import java.util.List;

public class DatabaseCourse implements Course {
  private final String courseID;

  private final Database database;

  /**
   * Constructs a Course from a Course ID and Professor
   *
   * @param courseID  is a CourseID that defines the unique CourseID of the Course
   * @param database is a Professor that defines the professor for the Course
   * @param professor is a Professor that defines the professor for the Course
   */
  public DatabaseCourse(String courseID, Professor professor, Database database) {
    this.courseID = courseID;

    this.database = database;
  }

  /**
   * Get a set of all the students who are actively enrolled in the course AKA
   * didn't drop out.
   * 
   * @return a set of all the students who are actively enrolled in the course
   * @throws Exception if there is an error in the database
   */
  public Set<Student> getActiveEnrollment() throws Exception {
    return database.getActiveEnrollment(courseID);
  }

  /**
   * Get the attendance record for a student
   * 
   * @param student is the student to get the attendance record for
   * @return the attendance record for the student
   */
  public AttendanceRecord getAttendanceRecord(Student student) {
    return new DatabaseAttendanceRecord(student, this, database);
      
  }


  /**
   * Returns the CourseID associated with the Course
   *
   * @return Returns a CourseID object that defines the CoureID for the Course
   */
  public String getCourseID() {
    return courseID;
  }

  /**
   * Gets the Professor associated with the Course
   * 
   * @return the Professor associated with the Course
   * @throws Exception if there is an error in the database
   */
  public Professor getProfessor() throws Exception {
    return database.getProfessorByCourse(courseID);
  }

  /**
   * Returns a list of all the Lectures associated with the Course
   * @throws Exception if there is an error in the database
   */

  public List<Lecture> getLecturesList() throws Exception {
    
    return database.getLecturesList(courseID);
  }


  @Override
  public String toString() {
    return courseID;
  }

}
