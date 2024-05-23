package edu.duke.projectTeam8;

import java.util.Set;
import java.util.Date;
import java.util.function.Consumer;

import com.mysql.cj.jdbc.exceptions.SQLError;

import java.sql.SQLException;

/**
 * A Student interface  represents a student
 */
public interface Student extends User {

  /**
   * Checks if the student is subscribed to the weekly report for a given course
   * @param course is the course for which the subscription is being checked
   * @return a boolean value indicating if the student is subscribed to the weekly report
   * @throws Exception if there is an error in the database
   */

  public Boolean isSubscribedToWeekyReport(Course course) throws Exception;

  /**
   * Subscribes the student to the weekly report for a given course
   * @param course is the course for which the student is being subscribed to the weekly report
   * @throws Exception if there is an error in the database
   */

  public void subscribeToWeeklyReport(Course course) throws Exception;

  /**
   * Unsubscribes the student from the weekly report for a given course
   * @param course is the course for which the student is being unsubscribed from the weekly report
   * @throws Exception if there is an error in the database
   */

  public void unsubscribeToWeeklyReport(Course course) throws Exception;

  /**
   * Returns the classes the Student is currently enrolled in.
   *
   * @return a Set of courses the student is current enrolled in.
   */
  public Set<Course> getClasses() throws Exception;

  
  /**
   * Returns a student's Attendance Record object for a given course
   *
   * @param course is the Course object for which the Attendance Record is being retrieved.
   * @return An AttendanceRecord object which has the records of a Student's attendance for the course
   */
  public AttendanceRecord getAttendanceRecord(Course course);

  public void addAttendanceRecord(Course course, Date date, Attendance attendance) throws Exception;

  public void modifyAttendanceRecord(Course course, Date date, Consumer<Attendance> markAttendance) throws Exception;

}
