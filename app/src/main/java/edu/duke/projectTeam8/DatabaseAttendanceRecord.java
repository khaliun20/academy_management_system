package edu.duke.projectTeam8;

import java.util.Date;
import java.util.function.Consumer;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

public class DatabaseAttendanceRecord implements AttendanceRecord {
  private final Student student;
  private final Course course;
  private final Database database;

  /**
   * Creates an AttendanceRecord given a Student and Course
   *
   * @param student is the Student this Attendance Record is tracking
   * @param course  is the course this Attendance Record is tracking
   */
  public DatabaseAttendanceRecord(Student student, Course course, Database database) {
    this.student = student;
    this.course = course;
    this.database = database;
  }

  /**
   * Adds a record of a student's attendance for a specific lecture
   *
   * @param date       is the Date of the lecture being recorded
   * @param attendance is the Attendance status for the student for the lecture
   * @throws Exception if there is an error in the database
   */
  public void addRecord(Date date, Attendance attendance) throws Exception {
    database.addAttendanceRecord(student.getNetID(), course.getCourseID(), date.toString(), attendance.toString());
  }

  /**
   * Modifies a record of a student's attendance for a specific lecture
   *
   * @param date           is the Date of the lecture being modified
   * @param markAttendance is a Consumer function that marks the attendance for
   *                       the record
   * @throws Exception if there is an error in the database
   */
  public void modifyRecord(Date date, Consumer<Attendance> markAttendance) throws Exception{
    Attendance attendance = new Attendance();
    markAttendance.accept(attendance);
    database.modifyAttendanceRecord(student.getNetID(), course.getCourseID(), date.toString(), attendance.toString());
  }

  /**
   * Returns the Attendance Status for the student given a specific date
   *
   * @param date is the Date the attendance status is requested for
   * @return an Attendance object which defines the student's attendance for the
   *         specified date
   * @throws Exception if there is an error in the database
   */
  public Attendance getAttendanceForDate(Date date) throws Exception{
    return database.getAttendance(student.getNetID(), course.getCourseID(), date.toString());
  }


  /**
   * Get the dates for which attendance has been recorded
   * 
   * @return an iterable of dates for which attendance has been recorded
   * @throws Exception if there is an error in the database
   */
  public Iterable<Date> getValidDates() throws Exception{
    Set<Date> set = database.getValidAttendanceDates(student.getNetID(), course.getCourseID());
    ArrayList<Date> list = new ArrayList<>();
    for (Date date: set) {
      list.add(date);
    }
    return list; 
  }

  /**
   * Get student attendance record for all lectures
   * @return a string representation of the student's attendance record for all lectures
   * @throws Exception if there is an error in the database
   */
  public String getAllAttendanceRecordsAsString() throws Exception {
    Map<Date, Attendance> attendanceRecord = database.getAttendanceMap(student.getNetID(), course.getCourseID());
    if (attendanceRecord.isEmpty()) {
      return null;
     }
    StringBuilder sb = new StringBuilder();
    for (Date date : attendanceRecord.keySet()) {

      sb.append(new DateModifier().changeDate(date) + " : " + attendanceRecord.get(date).toString() + "\n");
    }
    return sb.toString();
  }

}
