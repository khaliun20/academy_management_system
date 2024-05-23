package edu.duke.projectTeam8;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public interface Lecture {

  public Date getDate();

  public void modifyAttendanceOfStudent(Student student, Consumer<Attendance> markAttendance) throws Exception;

  public void recordAttendance(Student student, Consumer<Attendance> markAttendance) throws Exception;

  public Map<Student, Attendance> getLectureAttendance() throws Exception;

  public String getAttendanceOfStudent(String netID) throws Exception;
  
}
