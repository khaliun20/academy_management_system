package edu.duke.projectTeam8;

import java.util.Date;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A Student is a user who is enrolled in the university
 
 */
public class DatabaseStudent extends DatabaseUser implements Student {

  public DatabaseStudent(Name name, String netID, String emailAddress, Database database) {
    super(name, netID, emailAddress, database);
  }

  public Set<Course> getClasses() throws Exception {
    return database.getClasses(netID, "student_id", "enrollment");
  }

  public AttendanceRecord getAttendanceRecord(Course course) {
    return new DatabaseAttendanceRecord(this, course, database);
  }

  public void modifyAttendanceRecord(Course course, Date date, Consumer<Attendance> markAttendance) throws Exception{
    Attendance attendance = new Attendance();
    markAttendance.accept(attendance);
    database.modifyAttendanceRecord(netID, course.getCourseID(), date.toString(), attendance.toString());
  }

  public void addAttendanceRecord(Course course, Date date, Attendance attendance) throws Exception{
    database.addAttendanceRecord(netID, course.getCourseID(), date.toString(), attendance.toString());
  }

  public Boolean isSubscribedToWeekyReport(Course course) throws Exception{
    return database.isSubscribedToWeeklyReport(netID, course.getCourseID());
  }

  public void subscribeToWeeklyReport(Course course) throws Exception{
    database.subscribeToWeeklyReport(netID, course.getCourseID(), true);
  }

  public void unsubscribeToWeeklyReport(Course course) throws Exception {
    database.subscribeToWeeklyReport(netID, course.getCourseID(), false);
  }

   



}
