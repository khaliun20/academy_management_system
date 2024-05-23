package edu.duke.projectTeam8;

import java.sql.SQLException;
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
public class OldAttendanceRecord implements AttendanceRecord{
  private final OldStudent oldStudent;
  private final OldCourse oldCourse;
  private final Map<Date, Attendance> attendanceRecord;

  /**
   * Creates an AttendanceRecord given a Student and Course
   *
   * @param oldStudent is the Student this Attendance Record is tracking
   * @param oldCourse  is the course this Attendance Record is tracking
   */
  public OldAttendanceRecord(OldStudent oldStudent, OldCourse oldCourse) {
    this.oldStudent = oldStudent;
    this.oldCourse = oldCourse;
    attendanceRecord = new HashMap<>();
  }

  /**
   * Adds a record of a student's attendance for a specific lecture
   *
   * @param date       is the Date of the lecture being recorded
   * @param attendance is the Attendance status for the student for the lecture
   * @throws IllegalArgumentException if the date provided is already in the
   *                                  attendance record
   */
  public void addRecord(Date date, Attendance attendance) {
    if (attendanceRecord.keySet().contains(date)) {
      throw new IllegalArgumentException(
          "There was already an attendance record recorded for " + date + " for the course: " + oldCourse);
    } else {
      attendanceRecord.put(date, attendance);
    }
  }

  /**
   * Modifies a record of a student's attendance for a specific lecture
   *
   * @param date           is the Date of the lecture being modified
   * @param markAttendance is a Consumer function that marks the attendance for
   *                       the record
   * @throws IllegalArgumentException if the date provided is not already in the
   *                                  attendance record
   */
  public void modifyRecord(Date date, Consumer<Attendance> markAttendance) {
    if (!attendanceRecord.keySet().contains(date)) {
      throw new IllegalArgumentException(
          "There is not already an attendance record recorded for " + date + " for the course: " + oldCourse);
    } else {
      // This has to be fixed so that it is a map or something better
      /*
       * if (attendance == "Tardy")
       * attendanceRecord.get(date).markTardy();
       * else if (attendance == "Absent") {
       * attendanceRecord.get(date).markAbsent();
       * }
       * else if (attendance == "Attended") {
       * attendanceRecord.get(date).markAttended();
       * }
       * else {
       * throw new IllegalArgumentException(attendance +
       * " is not currently an acceptable attendance choice.");
       * }
       */
      markAttendance.accept(attendanceRecord.get(date));
    }
  }

  /**
   * Returns the Attendance Status for the student given a specific date
   *
   * @param date is the Date the attendance status is requested for
   * @return an Attendance object which defines the student's attendance for the
   *         specified date
   * @throws IllegalArgumentException if the date provided is not in the
   *                                  Attendance Record
   */
  public Attendance getAttendanceForDate(Date date) {
    if (attendanceRecord.keySet().contains(date)) {
      return attendanceRecord.get(date);
    } else {
      throw new IllegalArgumentException("There was no attendance recorded for " + date + " for the course: " + oldCourse);
    }
  }

  /**
   * Returns string representation of the Attendance Record
   * 
   * @return a string representation of the Attendance Record
   */
  @Override
  public String toString() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    StringBuilder output = new StringBuilder(oldStudent.toString());
    output.append("'s Attendance Record for Course: ");
    output.append(oldCourse);
    output.append("\n");
    for (Date eachDate : attendanceRecord.keySet()) {
      output.append("  ");
      output.append(dateFormat.format(eachDate));
      output.append(": ");
      output.append(attendanceRecord.get(eachDate));
      output.append("\n");
    }
    return output.toString();
  }

  /**
   * Get the dates for which attendance has been recorded
   * 
   * @return an iterable of dates for which attendance has been recorded
   */
  public Iterable<Date> getValidDates() {
    return attendanceRecord.keySet();
  }

  @Override
  public String getAllAttendanceRecordsAsString(){
    if (attendanceRecord.isEmpty()) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (Date date : getValidDates()) {
      sb.append(date.toString() + " : " + attendanceRecord.get(date).toString() + "\n");
    }
    return sb.toString();

  }

  /**
   * Serialize the attendance record
   *
   * @param leafConstructor     is a function that creates a serializer for a leaf
   *                            node
   * @param iterableConstructor is a function that creates a serializer for an
   *                            iterable node
   * @return a serializer for the attendance record
   */
  public Serializer serialize(BiFunction<String, Object, Serializer> leafConstructor,
      BiFunction<String, Iterable<Serializer>, Serializer> iterableConstructor) {
    Collection<Serializer> records = new ArrayList<>();
    for (Date date : attendanceRecord.keySet()) {
      Collection<Serializer> constructorArray = new ArrayList<>();
      constructorArray.add(leafConstructor.apply("date", date));
      constructorArray.add(leafConstructor.apply("attendance", attendanceRecord.get(date)));
      records.add(iterableConstructor.apply("record", constructorArray));
    }
    return iterableConstructor.apply("attendance records", records);
  }

}
