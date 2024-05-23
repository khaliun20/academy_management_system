package edu.duke.projectTeam8;

import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.io.IOException;

/**
 * A Student Object represents a student
 */
public class OldStudent extends OldUser implements Student {
  private final Name name;
  private Set<Course> cours;
  // Move status to Enrollment List private Status status; // Should status be its own class so we can limit the possible items for status?
  private Map<Course, AttendanceRecord> attendanceRecord; // Should the value in attendance record be boolean instead of String, or a new class?

  /**
   * Creates a Student object from a Name
   *
   * @param name is the name of the student using a Name object
   */
  public OldStudent(Name name, String netID, String email) {
    super(netID, email);
    this.name = name;
    this.cours = new HashSet<>();
    this.attendanceRecord = new HashMap<>();
  }

  /**
   * Gets the student's name
   *
   * @return the student's Name
   */
  public Name getName() {
    return name;
  }

  @Override
  public String getType() {
    return "";
  }

  @Override
  public String getHashedKey() throws SQLException {
    return "";
  }

  @Override
  public String getSalt() throws SQLException {
    return "";
  }

  @Override
  public void updateHashSalt(String passwordHash, String salt) throws SQLException {

  }

  /**
   * Modifies the Student's preferred name
   *
   * @param preferredName is a String that defines the Student's preferred name
   */
  public void modifyPreferredName(String preferredName) {
    name.modifyPreferredName(preferredName);
  }

  @Override
  public Boolean isSubscribedToWeekyReport(Course course) throws SQLException {
    return null;
  }

  @Override
  public void subscribeToWeeklyReport(Course course) throws SQLException {

  }

  @Override
  public void unsubscribeToWeeklyReport(Course course) throws SQLException {

  }

  /**
   * Returns the classes the Student is currently enrolled in.
   *
   * @return a Set of courses the student is current enrolled in.
   */
  public Set<Course> getClasses() {
    return cours;
  }

  @Override
  public AttendanceRecord getAttendanceRecord(Course course) {
    return attendanceRecord.get(course);
  }

  /**
   * Returns student's enrolled course IDs as iterable
   */
  // public Iterable<CourseID> getMyCourseIDs(){  // Why do we want to return an Iterable<Integer>?  Why can't we just use getClasses above?
  //   HashSet<CourseID> myCourseIDs = new HashSet<>(); // Why are we creating a new HashSet here when we already have a Set<course>?
  //   for (Course course: courses){
  //     myCourseIDs.add(course.getCourseID());
  //   }
  //   return myCourseIDs;

  // }


  /**
   * Adds a class to the list of classes the Student is currently enrolled in.
   *
   * @param oldCourse is the Course object the Student is enrolling in
   * @throws IllegalArgumentException if the Student is already enrolled in the course
   */
  public void addClass(OldCourse oldCourse) {
    if (cours.contains(oldCourse)) {
        throw new IllegalArgumentException(this + " is already enrolled in the course: " + oldCourse);
      }
      else {
        cours.add(oldCourse);
        attendanceRecord.put(oldCourse, new OldAttendanceRecord(this, oldCourse));
      }
  }

  /**
   * Drops a class that the Student is currently enrolled in.
   *
   * @param oldCourse is the Course object the Student is dropping
   * @throws IllegalArgumentException if the Student is not enrolled in the course
   */
  public void dropClass(OldCourse oldCourse) {
    if (cours.contains(oldCourse)) {
      cours.remove(oldCourse);
    }
    else {
      throw new IllegalArgumentException(this + " is not enrolled in the course: " + oldCourse);
    }
  }

  /**
   * Adds an Attendance to the student's attendance record for the course
   *
   * @param oldCourse is the Course object the attendance record is being added for
   * @param date is the Date object the attendance record is being added for
   * @param attendance is an Attendance object tracking whether or not the Student was in attendance
   * @throws IllegalArgumentException if the student is not enrolled in the course
   */
  public void addAttendanceRecord(Course oldCourse, Date date, Attendance attendance) throws Exception {
    if (!attendanceRecord.keySet().contains(oldCourse)) {
      if (cours.contains(oldCourse)) {
        attendanceRecord.put(oldCourse, new OldAttendanceRecord(this, (OldCourse) oldCourse));
      }
      else {
        throw new IllegalArgumentException(this + " is not enrolled in the course: " + oldCourse);
      }
    }
    attendanceRecord.get(oldCourse).addRecord(date, attendance);
  }

  public void modifyAttendanceRecord(Course oldCourse, Date date, Consumer<Attendance> markAttendance) throws Exception {
    if (!attendanceRecord.keySet().contains(oldCourse)) {
      throw new IllegalArgumentException(this + " does not have an attendance record for this course: " + oldCourse);
    }
    else {
      attendanceRecord.get(oldCourse).modifyRecord(date, markAttendance);
    }
  }

  /**
   * Returns a student's Attendance Record object for a given course
   *
   * @param oldCourse is the Course object for which the Attendance Record is being retrieved.
   * @return An AttendanceRecord object which has the records of a Student's attendance for the course
   */
  public AttendanceRecord getAttendanceRecord(OldCourse oldCourse) {
    return attendanceRecord.get(oldCourse);
  }

  @Override
  public String toString() {
    return name.toString();
  }

  @Override
  public int hashCode() {
    if (netID == null) {
      return 0;
    } else {
      return netID.hashCode();
    }
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;

    OldStudent oldStudent = (OldStudent) other;
    return netID != null ? netID.equals(oldStudent.netID) : oldStudent.netID == null;
  }
  
  public String serialize(){
    String userString = super.serialize();
    String nameString = name.serialize();

    String output = Serializer.wrapString("Student", userString+nameString);
    return output;
  }

  
  public Serializer serialize(BiFunction<String, Object, Serializer> leafConstructor, BiFunction<String, Iterable<Serializer>, Serializer> iterableConstructor) throws Exception {
    Collection<Serializer> studentDetails = new ArrayList<>();
    studentDetails.add(leafConstructor.apply("first name", getName().getFirstName()));
    studentDetails.add(leafConstructor.apply("last name", getName().getLastName()));
    studentDetails.add(leafConstructor.apply("email", emailAddress));
    studentDetails.add(leafConstructor.apply("netID", netID));
    Collection<Serializer> serializedCourses = new ArrayList<>();
    for (Course oldCourse : attendanceRecord.keySet()) {
      Collection<Serializer> courseDetails = new ArrayList<>();
      courseDetails.add(leafConstructor.apply("course id", oldCourse.getCourseID()));
      courseDetails.add(leafConstructor.apply("enrollment status", cours.contains(oldCourse)));
      courseDetails.add(((OldProfessor)oldCourse.getProfessor()).serialize(leafConstructor, iterableConstructor));
      courseDetails.add(((OldAttendanceRecord)attendanceRecord.get(oldCourse)).serialize(leafConstructor, iterableConstructor));
      serializedCourses.add(iterableConstructor.apply("course", courseDetails));
    }
    studentDetails.add(iterableConstructor.apply("course list", serializedCourses));
    return iterableConstructor.apply("student", studentDetails);
  }

}
