package edu.duke.projectTeam8;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a enrollment list of students who are enrolled in a
 * single course
 */

public class OldEnrollmentList {
  private Map<OldStudent, Boolean> courseEnrollment;
  // private final CourseID courseID;
  private final OldCourse oldCourse;

  public OldEnrollmentList(OldCourse oldCourse) {
    this.courseEnrollment = new HashMap<>();
    this.oldCourse = oldCourse;
  }

  /**
   * Creates enrollment list object
   */
  // public EnrollmentList(CourseID courseID) {
  // this.courseEnrollment = new HashMap<>();
  // this.courseID = courseID;
  // }

  /**
   * Adds student to an enrollment list if already not added
   * 
   * @param toAdd is the student to add to the enrollment list
   */

  public void addStudentToCourseEnrollment(OldStudent toAdd) {
    if (courseEnrollment.containsKey(toAdd)) {
      throw new IllegalArgumentException(toAdd + " is already on the enrollment list.");
    }
    courseEnrollment.put(toAdd, true);
  }

  public Map<OldStudent, Boolean> getCourseEnrollment() {
    return courseEnrollment;
  }


  /**
   * Gets the course ID
   * 
   * @return the course ID
   */
  public String getCourseID() {
    return oldCourse.getCourseID();
  }

  /**
   * Get the number of students enrolled in the course
   * 
   * @return the number of students enrolled in the course
   */

  public int getNumStudents() { // NOTE: This method will return a total number of students enrolled in the
                                // course, including the ones who have dropped the course
    return courseEnrollment.size();
  }

  /**
   * Drops a student from the enrollment list
   * 
   * @param oldStudent is the student to drop from the enrollment list
   */
  public void dropStudent(OldStudent oldStudent) {
    if (courseEnrollment.containsKey(oldStudent)) {
      courseEnrollment.put(oldStudent, false);
      oldStudent.dropClass(oldCourse);
    } else {
      throw new IllegalArgumentException(oldStudent.toString() + " doesn't exist in" + this.toString() + "\n");
    }
  }


  /**
   * Returns a string representation of the enrollment list
   * 
   * @return a string representation of the enrollment list
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Enrollment:\n");
    for (Map.Entry<OldStudent, Boolean> entry : courseEnrollment.entrySet()) {
      if (entry.getValue()) {
        sb.append(entry.toString());
        sb.append("\n");

      }
    }
    return sb.toString();
  }

  /**
   * Returns a hash code value for the course
   * 
   * @return a hash code value for the course
   */
  @Override
  public int hashCode() {
    return oldCourse.getCourseID().hashCode();

  }

  /**
   * Compares two enrollment lists
   * 
   * @param o is the object to compare
   * @return true if the enrollment lists are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      OldEnrollmentList e = (OldEnrollmentList) o;
      return oldCourse.getCourseID().equals(e.getCourseID());
    }
    return false;
  }

  /**
   * Serializes the enrollment list
   *
   * @return a string representation of the enrollment list
   */
  public String serialize() {
    StringBuilder sb = new StringBuilder();

    for (OldStudent s : courseEnrollment.keySet()) {
      String studentSerial = s.serialize();
      String enrollStatus = courseEnrollment.get(s) ? "enrolled" : "dropped";
      String output = studentSerial + Serializer.wrapString("enrollStatus", enrollStatus);
      sb.append(output);

    }
    String allStudentSerial = sb.toString();

    return Serializer.wrapString("EnrollmentList", allStudentSerial);
  }

}
