package edu.duke.projectTeam8;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class OldLecture implements Lecture {
  private OldCourse oldCourse;
  private Date date; // Do we want to define date formats?
  private Map<Student, Attendance> lectureAttendance;

  public OldLecture(OldCourse oldCourse) {
    this(oldCourse, new Date());
  }

  public OldLecture(OldCourse oldCourse, Date date) {
    this.date = date; // Does this create the current date or a null date?
    this.oldCourse = oldCourse;
    this.lectureAttendance = new HashMap<>();
    // initializeAttendance(course.getEnrollmentList());
  }


  public Date getDate() {
    return date;
  }


  public String getAttendanceOfStudent(String netID) {
    Student oldStudent = searchForStudent(netID);
    Attendance attendance = lectureAttendance.get(oldStudent);
    if (attendance == null) {
      return "Student not in this course"; // Should this be an exception instead?
    }
    return attendance.toString();
  }

  public void modifyAttendanceOfStudent(Student oldStudent, Consumer<Attendance> markAttendance) {
    markAttendance.accept(lectureAttendance.get(oldStudent)); // Should this also update the AttendanceRecord when this happens?
    ((OldAttendanceRecord)oldCourse.getAttendanceRecord(oldStudent)).modifyRecord(date, markAttendance);
  } // We will need to add notification here as well, correct?

  public void recordAttendance(Student oldStudent, Consumer<Attendance> markAttendance) throws Exception {
    Attendance attendance = new Attendance();
    markAttendance.accept(attendance);
    lectureAttendance.put(oldStudent, attendance);
    oldCourse.getAttendanceRecord(oldStudent).addRecord(date, attendance);
    oldStudent.addAttendanceRecord(oldCourse, date, attendance);
  }

  // NOTE: Dont use this, this has been moved to EnrollmentList. may delete this
  // one.
  public Student searchForStudent(String netID) { // This can be done with a Map (maybe through enrollmentList or
                                                  // AcademicEnrollment for O(1). Also, does the Professor need to be
                                                  // able to search by name instead of by netID?
    for (Student oldStudent : lectureAttendance.keySet()) {
      if (oldStudent.getNetID().equals(netID)) {
        return oldStudent;
      }
    }
    return null;
  }

  public Map<Student, Attendance> getLectureAttendance() {
    return this.lectureAttendance;
  }
}
