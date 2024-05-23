package edu.duke.projectTeam8;

import java.util.Map;


public class AttendanceRecordView {
    ProfessorIOHandler ioHandler;

    public AttendanceRecordView(ProfessorIOHandler ioHandler) {
        this.ioHandler = ioHandler;
    }


    public void displayAttendanceReportByLecture(Course course) throws Exception {
 
            int totalLecturesNum = course.getLecturesList().size();
            if (totalLecturesNum <= 0) {
            ioHandler.printNoAttendanceSheet();
            ioHandler.readString("b" , "Please type b to go back to course page\n");
            
            } else {
            boolean goBack = false;
            while (!goBack) {
                ioHandler.printOldAttendanceSheetsPrompt(course);
                int maxOption = totalLecturesNum;
                String invalidMessage = "Invalid Selection. Please choose again\n";
                Integer selectedOption = ioHandler.readAndValidateInput(maxOption, invalidMessage);
                if (selectedOption == 0) {
                    goBack = true;
                    break;
                }
                showAttendanceSheet(course, selectedOption);
            }

    
    }
    }

    private void showAttendanceSheet(Course course, int selectedOption) throws Exception {
        Lecture oldLecture = course.getLecturesList().get(selectedOption - 1);
        Map<Student, Attendance> attendanceSheet = oldLecture.getLectureAttendance();
        ioHandler.printAttendanceSheet(attendanceSheet, oldLecture);
        ioHandler.readString("0" , "Please type 0 to go back to course page\n");
        
    }

    public void displayAttendanceReportByStudent(Course course) throws Exception{
        int totalLecturesNum = course.getLecturesList().size();
        if (totalLecturesNum <= 0) {
            ioHandler.printNoAttendanceSheet();
            ioHandler.readString("0" , "Please type 0 to go back to course page\n");
            return;
        }  
        boolean goBack = false;
        while(!goBack){
            String prompt = "SELECTSTUDENT\nPlease select the student netID to view report: ";
            ioHandler.printNetIDPrompt(prompt, course);
            Student student = ioHandler.readAndValidateStudent(course, true);
            if (student == null) {
                goBack = true;
                break;
            }
            displayAttendanceReportByStudentHelper(course, student);

        }
        
        
    }
    private void displayAttendanceReportByStudentHelper( Course course, Student student) throws Exception{
        AttendanceRecord attendanceRecord = course.getAttendanceRecord(student);
        String attendanceReportString = attendanceRecord.getAllAttendanceRecordsAsString();
        // Add the calulation of the attendance rate
        WeeklyTextSummary weeklyTextSummary = new WeeklyTextSummary();
        double overallGrade  = weeklyTextSummary.computerCumulativeAttendanceScore(student, course);
        String overallGradeString = String.format("%.2f", overallGrade);
        String attendanceGradeString= "Overall attendance score: " + overallGradeString + "\n";
        String gradingScale = "Attended = 100; Tardy = 80; Absent = 0\n";
  
        if (attendanceReportString == null) {
            ioHandler.printNoAttendanceSheet();
            ioHandler.readString("0" , "Please type 0 to go back to course page\n");
        } else {
            ioHandler.printAttendanceRecordByStudent(gradingScale + "\n"+ attendanceReportString+ "\n" + attendanceGradeString);
            ioHandler.readString("0" , "Please type 0 to go back to course page\n");
        }

    }

        

    
}
