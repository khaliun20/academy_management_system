package edu.duke.projectTeam8;

import java.util.Set;


import java.util.Map;
import java.util.List;
import java.util.Date;


public interface Database {
  
  public User getUser(String id) throws Exception;

  public Professor getProfessor(String id) throws Exception;

  public Professor getProfessorByCourse(String courseID) throws Exception;

  public String getSalt(String id) throws Exception;

  public String getHashedKey(String id) throws Exception;

  public void modifyPreferredName(String id, String preferredName) throws Exception;

  public void updateHashSalt(String id, String passwordHash, String salt) throws Exception;

  public Set<Student> getActiveEnrollment(String CourseID) throws Exception;

  public Set<Course> getClasses(String netID, String type, String table) throws Exception;
  
  public void modifyAttendanceRecord(String netID, String courseID, String date, String attendance) throws Exception;
  
  public Map<Student, Attendance> getLectureAttendance( String courseID, String date) throws Exception;

  public Attendance getAttendance(String netID, String course, String date) throws Exception;

  public void addAttendanceRecord(String netID, String courseID, String date, String attendance) throws Exception;
  
  public Map<Date, Attendance> getAttendanceMap(String netID, String courseID) throws Exception;
  
  public void subscribeToWeeklyReport(String netID, String courseID, boolean value) throws Exception;

  public Set<Date> getValidAttendanceDates(String netID, String courseID) throws Exception ;

  public List<Lecture> getLecturesList(String courseID) throws Exception;

  public Student getStudent(String id) throws Exception;

  public Set<Student> getAllStudents()throws Exception;


  public Boolean isSubscribedToWeeklyReport(String netID, String courseID) throws Exception;

  public void importUsers(String filePath) throws Exception;

  public void importCourses(String filePath) throws Exception;

  public void importEnrollment(String filePath) throws Exception;

  public void cleanTables() throws Exception;

  public void checkStudentExistsInSchool(String studentId) throws Exception;

  public void checkCourseExists(String courseId) throws Exception;

  public Boolean checkStudentEnrolledInClass(String courseId, String studentId)
    throws Exception;

  public void enrollStudentInClass(String courseId, String studentId) throws Exception;

  public void enrollStudentToSchool(String studentID, String firstName, String lastName, String password, String email)
    throws Exception;

  public void enrollProfessorToSchool(String professorID, String firstName, String lastName, String password, String email)
    throws Exception;

  public void dropStudent(String courseId, String studentId) throws Exception;

  public void createNewClass(String courseId, String professorId) throws Exception;

}
