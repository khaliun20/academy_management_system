package edu.duke.projectTeam8;

public class AttendanceTaker {
    Student student; 
    Lecture lecture;
    int selectedOption;

    public AttendanceTaker(Student student, Lecture lecture, int selectedOption) {
        this.student = student;
        this.lecture = lecture;
        this.selectedOption = selectedOption;
    }

    public void initalUpdateToStudentAttendanceRecord() throws Exception{

        switch (selectedOption) {
            case 1:
              lecture.recordAttendance(student, (attendance) -> attendance.markAttended());
              break;
            case 2:
              lecture.recordAttendance(student, (attendance) -> attendance.markTardy());
              break;
            case 3:
              lecture.recordAttendance(student, (attendance) -> attendance.markAbsent());
              break;
          }
    
    }

    public void modifyStudentAttendanceRecord() throws Exception{

        switch (selectedOption) {
            case 1:
              lecture.modifyAttendanceOfStudent(student, (attendance) -> attendance.markAttended());
              break;
            case 2:
              lecture.modifyAttendanceOfStudent(student, (attendance) -> attendance.markTardy());
              break;
            case 3:
              lecture.modifyAttendanceOfStudent(student, (attendance) -> attendance.markAbsent());
              break;
          }


    }

}
