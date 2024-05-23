
package edu.duke.projectTeam8;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents client for the text-based user interface of the program
 */
public class ProfessorView {

  private Professor professor;
  private ProfessorIOHandler ioHandler;
  private AttendanceTakerView attendanceTakerView;
  private AttendanceRecordView attendanceRecordView;
  private boolean connectionActive;
  private Map<Integer, Course> orderedCourses;
  
  public static final int MAIN_MENU_OPTIONS = 3;
  public static final int PROFILE_MENU_OPTIONS = 2;
  public static final int COURSE_MENU_OPTIONS = 5;



  public ProfessorView( Professor professor, ProfessorIOHandler ioHandler,AttendanceTakerView attendanceTaker, AttendanceRecordView attendanceRecordDisplayer) throws Exception {

    this.professor = professor; 
    this.ioHandler = ioHandler;
    this.attendanceTakerView = attendanceTaker;
    this.attendanceRecordView = attendanceRecordDisplayer;
    this.connectionActive = true;

    addProfessorCourses();
    }


  private void addProfessorCourses()  throws Exception{

    if (professor.getCourses().size() == 0) {
    this.orderedCourses = null;
    }
    Set<Course> courses = professor.getCourses();
    Map<Integer, Course> orderedCourses = new HashMap<>();
    int id = 1;
    for (Course course : courses) {
      orderedCourses.put(id++, course);
    }
    this.orderedCourses = orderedCourses;
  }

  public void showMainMenu() throws Exception{

    System.out.println("Connection Status: " + connectionActive+"\n");
    while (connectionActive) { 
     int maxOptions = MAIN_MENU_OPTIONS;
     ioHandler.displayMainMenu(professor.getNetID()) ;
      String invalidMessage = "Invalid Selection. Please choose again\n";
      Integer selectedOption = ioHandler.readAndValidateInput(maxOptions, invalidMessage);
      switch (selectedOption) {
        case 1:
          updateProfile();
          break;
        case 2:
          showCourses();
          break;
        case 3:
          stopProgram();
          break;
      }
   }}


   public void updateProfile() throws Exception{
    boolean backToMainMenu = true;
    while (backToMainMenu) {
        ioHandler.displayProfileOptions();
        int maxOptions = PROFILE_MENU_OPTIONS;
        String invalidMessage = "Invalid Selection. Please choose again\n";
        Integer selectedOption = ioHandler.readAndValidateInput(maxOptions, invalidMessage);
        if (selectedOption.equals(0)) {
          backToMainMenu = false;
          break;
      }
        ProfileChanger profileChanger = new ProfileChanger(professor);
        processProfileTask(selectedOption, profileChanger); 
        
    }
  }
private void processProfileTask(int selectedOption, ProfileChanger profileChanger) throws Exception{
 
    switch (selectedOption) {
      case 1:
          ioHandler.displayPasswordChangePrompt();
          String invalidPassword = "You didn't provide at least 8 character long password. Please choose again\n";
          String newPassword = ioHandler.readString(".{8,}", invalidPassword);
          profileChanger.changePassword(newPassword);
          if (newPassword.equals("0")) {
            break;
          }

          break;
      case 2:
          String curPreferredName = professor.toString();
          ioHandler.displayNameChangePrompt(curPreferredName);
          // TODO: ADD GO BACK Button or handle on GUI
          String invalidName = "Please enter valid name. Name must consist of letters only\n";
          String preferredName = ioHandler.readString("^[a-zA-Z]+$", invalidName);
          if (preferredName.equals("0")) {
            break;
          }
         
          profileChanger.changePreferredName(preferredName);
          break;
      case 0:
          break;
        }
      }

  
  /**
   * This method displays the all the courses the teacher is teaching and 
   * prompts the teacher to choose a course to perform a task on
   */
  public void showCourses() throws Exception{
      // TOOD: check if this is what we want with the team
      boolean backToMainMenu = true;
      while(backToMainMenu) {
        if (orderedCourses == null) {
          ioHandler.noClassToShow();
          return;
        }
      ioHandler.displayAvailableCourses(professor.getNetID(), orderedCourses); // passing orderedCourses so student can use that method too.
      int maxOptions = professor.getCourses().size();
      String invalidMessage = "Invalid Selection. Please choose again\n";
      Integer selectedOption = ioHandler.readAndValidateInput(maxOptions, invalidMessage);
      if (selectedOption.equals(0)) {
        backToMainMenu = false;
        break;
      }
      processCourseTask(selectedOption);
    }

  }

  /**
   * This method displays the task options for the selected course
   * @param selectedOption is the option number selected by the teacher
   */

  public void processCourseTask(int selectedOption) throws Exception {
    Boolean backToCourseMenu = true;
    while (backToCourseMenu) {
    Course course = orderedCourses.get(selectedOption);
    ioHandler.displayCourseTaskOptions();
    int maxOptions = COURSE_MENU_OPTIONS;
    String invalidMessage = "You chose invalid course task. Please choose again\n";
    Integer selectedTask = ioHandler.readAndValidateInput(maxOptions, invalidMessage);
    switch (selectedTask) {
      case 1:
        attendanceTakerView.displayTodaysAttendance(course);
        break;
      case 2:
        attendanceTakerView.displayAnotherDayAttendance(course);
        break;
      case 3:
        attendanceTakerView.displayModifyAttendance(course);
        break;
      case 4:
        attendanceRecordView.displayAttendanceReportByStudent(course);
        break;
      case 5:
        attendanceRecordView.displayAttendanceReportByLecture(course);
        break;
      case 0:
        backToCourseMenu = false;
        break;
  
    }
    }
    }

  private void stopProgram() throws Exception {
    boolean isActive = ioHandler.stayConnected();
    connectionActive = isActive;
    if (connectionActive){
      showMainMenu();
    }
  }

  public boolean getConnectionStatus() {
    return connectionActive;
  }


}
