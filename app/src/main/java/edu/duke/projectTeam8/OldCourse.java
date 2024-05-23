package edu.duke.projectTeam8;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;

/**
 * A Course object that represents a school's course
 */
public class OldCourse implements Course {
  private final CourseID courseID;
  private final OldProfessor oldProfessor; // Added to work with Notification, should be added throughout
  private final OldEnrollmentList enrollment;
  private final List<Lecture> oldLectures;
  private final Map<Student, OldAttendanceRecord> attendanceRecords;

  /**
   * Constructs a Course from a Course ID. THIS IS FOR TESTING PURPOSES ONLY.
   * COURSES SHOULD BE PASSED WITH A PROFESSOR.
   *
   * @param courseID is a CourseID that defines the unique CourseID of the Course
   */
  public OldCourse(CourseID courseID) {
    this(courseID, new OldProfessor(new Name("Test", "Professor"))); // This is a temporary solution because we don't
  }

  // public Course(CourseID courseID, EnrollmentList enrolled) {
  // this(courseID);
  // this.enrollment = new EnrollmentList(this);
  // this.lectures = new ArrayList<>();
  // this.attendanceRecords = new HashMap<>();
  // }

  /**
   * Constructs a Course from a Course ID and Professor
   *
   * @param courseID  is a CourseID that defines the unique CourseID of the Course
   * @param oldProfessor is a Professor that defines the professor for the Course
   */
  public OldCourse(CourseID courseID, OldProfessor oldProfessor) {
    this.courseID = courseID;
    this.oldProfessor = oldProfessor;
    this.oldLectures = new ArrayList<>();
    this.enrollment = new OldEnrollmentList(this);
    this.attendanceRecords = new HashMap<>();
    this.oldProfessor.addCourse(this);
  }

  // Call this whenever a student is enrolled AKA in both CSVParser and Professor
  // end. Or also can add call this in public void enrollStudent(Student student)
  // Depricated
  /*
   * public void initializeAttendanceRecords(Student student) {
   * attendanceRecords.put(student, new AttendanceRecord(student, this));
   * }
   */

  /**
   * Get a set of all the students who are actively enrolled in the course AKA
   * didn't drop out.
   * 
   * @return a set of all the students who are actively enrolled in the course
   */
  public Set<Student> getActiveEnrollment() {
    Set<Student> activeOldStudents = new HashSet<>();
    Map<OldStudent, Boolean> enrollmentMap = this.enrollment.getCourseEnrollment();

    for (Map.Entry<OldStudent, Boolean> entry : enrollmentMap.entrySet()) {
      if (entry.getValue()) {
        activeOldStudents.add(entry.getKey());
      }
    }

    return activeOldStudents;
  }

  @Override
  public AttendanceRecord getAttendanceRecord(Student student) {
    return attendanceRecords.get(student);
  }



  /**
   * Add a lecture to the course
   * 
   * @param oldLecture is the lecture to add to the course
   */
  public void addLecture(OldLecture oldLecture) {
    oldLectures.add(oldLecture);
  }

  /**
   * Get a lecture by date
   * 
   * @param date is the date of the lecture
   * @return the lecture on the date
   */
  public Lecture getCertainLecture(Date date) {
    for (Lecture oldLecture : oldLectures) {
      if (oldLecture.getDate().equals(date)) {
        return oldLecture;
      }
    }
    throw new NoSuchElementException("No lecture found on date: " + date);
  }

  /**
   * Get the list of lectures for the course
   * 
   * @return the list of lectures for the course
   */
  public List<Lecture> getLecturesList() {
    return oldLectures;
  }

  /**
   * Returns the EnrollmentList with the status of all of the Students enrolled in
   * the Course
   *
   * @return Returns an EnrollmentList which has the status of all of the Students
   *         enrolled in the Course
   */
  public OldEnrollmentList getEnrollment() {
    return enrollment;
  }


  /**
   * Enrolls a Student in the Course, including initializing the AttendanceRecord
   * and
   * add the course under the student class. It also checks whether the student
   * used
   * to be in the class so that the method does not add the student again.
   * 
   * @param oldStudent is the Student to enroll in the Course
   * @throws IllegalArgumentException if the student is already enrolled in the
   *                                  course
   */
  public void enrollStudent(OldStudent oldStudent) throws IllegalArgumentException {
    Map<OldStudent, Boolean> courseEnrollment = enrollment.getCourseEnrollment();

    if (courseEnrollment.containsKey(oldStudent)) {
      if (courseEnrollment.get(oldStudent)) {
        throw new IllegalArgumentException(oldStudent + " is already enrolled in the course.");
      } else {
        courseEnrollment.put(oldStudent, true);
      }
    } else {
      enrollment.addStudentToCourseEnrollment(oldStudent);
      attendanceRecords.put(oldStudent, new OldAttendanceRecord(oldStudent, this));
    }
    oldStudent.addClass(this);
  }

  /**
   * Returns the CourseID associated with the Course
   *
   * @return Returns a CourseID object that defines the CoureID for the Course
   */
  public String getCourseID() {
    return courseID.toString();
  }

  /**
   * Gets the Professor associated with the Course
   * 
   * @return the Professor associated with the Course
   */
  public Professor getProfessor() {return oldProfessor;}

  /**
   * Gets the String of CourseID associated with the Course
   */
  @Override
  public String toString() {
    return courseID.toString();
  }

}
