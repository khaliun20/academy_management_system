package edu.duke.projectTeam8;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.function.Consumer;
import java.util.function.BiFunction;

/**
 * Defines an Attendance Record object which tracks attendance for one Course
 * for one Student
 */
public interface AttendanceRecord {

  /**
   * Adds a record of a student's attendance for a specific lecture
   *
   * @param date       is the Date of the lecture being recorded
   * @param attendance is the Attendance status for the student for the lecture
   * @throws  Exception if the date provided is already in the
   *                                  attendance record
   */
  public void addRecord(Date date, Attendance attendance) throws Exception;
  
  /**
   * Modifies a record of a student's attendance for a specific lecture
   *
   * @param date           is the Date of the lecture being modified
   * @param markAttendance is a Consumer function that marks the attendance for
   *                       the record
   * @throws Exception if the date provided is not already in the
   *                                  attendance record
   */
  public void modifyRecord(Date date, Consumer<Attendance> markAttendance) throws Exception;

  /**
   * Returns the Attendance Status for the student given a specific date
   *
   * @param date is the Date the attendance status is requested for
   * @return an Attendance object which defines the student's attendance for the
   *         specified date
   * @throws Exception if the date provided is not in the attendance record
   */
  public Attendance getAttendanceForDate(Date date) throws Exception;


  /**
   * Get the dates for which attendance has been recorded
   * 
   * @return an iterable of dates for which attendance has been recorded
   * @throws Exception if there are no dates in the Attendance Record
   */
  public Iterable<Date> getValidDates() throws Exception;

  /**
   * Get student attendance record for all lectures
   * @return a string representation of the student's attendance record for all lectures
   * @throws Exception if there are no dates in the Attendance Record
   */
  public String getAllAttendanceRecordsAsString() throws Exception;

}
