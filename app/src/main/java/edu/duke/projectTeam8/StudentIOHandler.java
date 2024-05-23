package edu.duke.projectTeam8;

public class StudentIOHandler  extends IOHandler{

    public StudentIOHandler(ClientCommunicationHandler clientCommunicationHandler, AcademicEnrollment academicEnrollment) {
        super(clientCommunicationHandler, academicEnrollment);
    }

public void displayStudnetCourseTaskOptions(String course) {
    clientCommunicationHandler.sendMessage ("SELECT\nPlease select from what task you want to complete for course: " + course +  "\n" + "1. View Attendance Report\n" +"2. Update Weekly Attendance Report Subscription\n3. Receive Attendance Report via Email\n\n0. Go back\n");
  }

  public void printAttendanceReportForStudent(String title, String report, String grade){
    if(report != null){
      clientCommunicationHandler.sendMessage("TEXT\n"+ title + report + grade + "\n0. Go back\n");
    }
    else{
    clientCommunicationHandler.sendMessage("TEXT\n" + title +"No attendance record to show.\n0. Go back\n");
    }
  
  }
  
  public void displaySubscriptionStatus (Boolean isSubscribed){
    if (isSubscribed){
      clientCommunicationHandler.sendMessage("SELECT\nYou are currently subscribed to weekly attendance report.\n" + "1. Keep subscription\n" + "2. Unsubscribe\n\n0. Go back\n");
    } else {
      clientCommunicationHandler.sendMessage("SELECT\nYou are currently not subscribed to weekly attendance report\n" + "1. Subscribe\n" + "2. Stay unsubscribed\n\n0. Go back \n");
    }
  }

  public void displayEmailPrompt(){
    clientCommunicationHandler.sendMessage("SELECT\nPlease select from the following options:\n1. Send Email\n0. Go back\n");
  }
}
