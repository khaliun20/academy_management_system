package edu.duke.projectTeam8;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ProfessorIOHandler extends IOHandler{

    public ProfessorIOHandler(ClientCommunicationHandler clientCommunicationHandler, AcademicEnrollment academicEnrollment) {
        super(clientCommunicationHandler, academicEnrollment);
    }   

    public  void  printStudentToSearchPrompt(Course course) throws Exception{
        String prompt = "SELECTSTUDENT\nPlease enter the netid of the student you want to search for: \n";

        Set<Student> students = course.getActiveEnrollment();
        StringBuilder sb = new StringBuilder(prompt);
          for (Student student : students){
            sb.append(student.getNetID()).append("\n");
          }
          sb.append("Go back");
          clientCommunicationHandler.sendMessage(sb.toString());
        }

    public void  printNoStudentEnrolled(Course course){
        clientCommunicationHandler.sendMessage("No student is enrolled in the course: " + course.toString() + ". You cannot drop a student at this time" +"\n"+ '$');
        }
    
        public void  displayAvailableTasks() {
        clientCommunicationHandler.sendMessage(
            "SELECT\nPlease select the task you want to perform:\n1. Manage Existing Courses\n2. Get Enrollment List for New Course\n3. Quit program\n");
    }


  public void printAttendanceSheet(Map<Student, Attendance> attendanceSheet, Lecture lecture) {
    int earnedScore = 0;
    int numRecords = 0;

    StringBuilder message = new StringBuilder();
    message.append("TEXT\nAttendance record for lecture on: ");
    message.append(changeDate(lecture.getDate()));
    message.append("\n\n");

    for (Map.Entry<Student, Attendance> entry : attendanceSheet.entrySet()) {
        Student student = entry.getKey();
        Attendance studentAttendance = entry.getValue();
        earnedScore += Attendance.mapStrToScore(studentAttendance.toString());
        message.append(student.getName());
        message.append(": ");
        message.append(studentAttendance.toString());
        message.append(" Score: " + Attendance.mapStrToScore(studentAttendance.toString()));
        message.append("\n");
        numRecords++;
    }

    message.append("\nOverall Score: " + (double) earnedScore / (double) numRecords);
    message.append("\n");
    message.append("\n0. Go back\n" );

    clientCommunicationHandler.sendMessage(message.toString());
}


  public void displayNoAttendanceSheetsToModify() {
    clientCommunicationHandler.sendMessage("SELECT\nThere is no previous attendance sheets to show.\n0. Go back");
  }

  public void printOldAttendanceSheetsPrompt(Course course) throws Exception {
    StringBuilder message = new StringBuilder("SELECT\nPlease choose the attentance sheet: \n");
    List<Lecture> allAttendanceSheets = course.getLecturesList();
    
    for (int i = 0; i < allAttendanceSheets.size(); i++) {
        Lecture lecture = allAttendanceSheets.get(i);
        String lectureDate = changeDate(lecture.getDate());
        message.append((i + 1)).append(". Attandance Sheet Date: ").append(lectureDate).append("\n");
    }
    message.append("\n0. Go back\n");

    clientCommunicationHandler.sendMessage(message.toString());
}

  public void  printStudentAttendancePrompt(Student student) {
    clientCommunicationHandler.sendMessage ("SELECT\nTaking attendance for: " + student.toString() + "\n" + "1. Attended\n" + "2. Tardy\n" + "3. Absent\n");
  }


  public  void  printOldStatusAndAttendancePrompt(Lecture lecture, Student student) throws Exception {
    clientCommunicationHandler.sendMessage("SELECT\nStudent " + student.toString() + " is currently marked as  "
        + lecture.getAttendanceOfStudent(student.getNetID()).toString() + " for lecture on "
        + changeDate(lecture.getDate())
        + "\n" + "1. Attended\n" + "2. Tardy\n" + "3. Absent\n");
  }

  public void  displayCourseTaskOptions() {
    clientCommunicationHandler.sendMessage ("SELECT\nPlease select from what task you want to complete: \n" + "1. Take Attendance For Today\n" +"2. Take Attendance For Another Day\n" +"3. Modify Previous Attendance Reports\n" +
     "4. View Attendance Report by Students\n" + "5. View Attendance Report by Lecture\n" +"\n0. Go back\n");
  }

  public void printNetIDPrompt(String prompt, Course course) throws Exception {
    Set<Student> students = course.getActiveEnrollment();
    StringBuilder sb = new StringBuilder(prompt).append(course.toString()).append("\n");
    
    for (Student student : students) {
        sb.append(student.getNetID()).append("\n");
    }
    sb.append("Go back");
    
    clientCommunicationHandler.sendMessage(sb.toString());
  }

public void printDatePrompt() {
  String prompt = "DATE\nPlease enter the date of the lecture: \n0. Go back";
    clientCommunicationHandler.sendMessage(prompt);
}
  
    
}
