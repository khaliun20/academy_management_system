package edu.duke.projectTeam8;

import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;

public class DatabaseLecture implements Lecture {
  private final Course course;
  private final Date date;
  private final Database database;
  
  /**
   * Creates a DatabaseLecture object
   * @param course is the course
   * @param date is the date of the lecture
   * @param database is the database
   */
  public DatabaseLecture(Course course, Date date, Database database) {
    this.course = course;
    this.date = date;
    this.database = database;
  }
  
  public Date getDate() {
    return date;
  }

  /**
   * Get the attendance of a student
   * @param netID is the netID of the student
   * @return the attendance of the student
   * @throws Exception if there is an error in the database
   */

  public String getAttendanceOfStudent(String netID) throws Exception {
    return database.getAttendance(netID,course.getCourseID(), date.toString()).toString();
  }

  /**
   * Modify the attendance of a student
   * @param student is the student
   * @param markAttendance is a Consumer function that marks the attendance for the record
   * @throws Exception if there is an error in the database
   */

  public void modifyAttendanceOfStudent(Student student, Consumer<Attendance> markAttendance) throws Exception{
    Attendance attendance = new Attendance();
    markAttendance.accept(attendance);

    database.modifyAttendanceRecord(student.getNetID(), course.getCourseID(), date.toString(), attendance.toString());
  }

  /**
   * Record the attendance of a student
   * @param student is the student
   * @param markAttendance is a Consumer function that marks the attendance for the record
   * @throws Exception if there is an error in the database
   */

  public void recordAttendance(Student student, Consumer<Attendance> markAttendance) throws Exception{
    Attendance attendance = new Attendance();
    markAttendance.accept(attendance);

    database.addAttendanceRecord(student.getNetID(), course.getCourseID(), date.toString(), attendance.toString());
  }

  /**
   * Get the map of students to attendance for the lecture
   * @return the lecture attendance
   * @throws Exception if there is an error in the database
   */

  public Map<Student, Attendance> getLectureAttendance() throws Exception{
    return database.getLectureAttendance(course.getCourseID(), date.toString());
  }

}
