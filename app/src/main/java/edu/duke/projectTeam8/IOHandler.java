package edu.duke.projectTeam8;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to handle all input and output operations for the
 * AcademicEnrollment system
 */

public class IOHandler {
  protected ClientCommunicationHandler clientCommunicationHandler;
  private AcademicEnrollment academicEnrollment;
  public static final int MIN_OPTION_VALUE = 0;
  public static final int COURSE_SUB_TAB_OPTIONS = 5;

  /**
   * Constructs an IOHandler object with the specified client communication handler
   * @param clientCommunicationHandler is the client socket communication handler
   * @param academicEnrollment is the academic enrollment system
   */
  public IOHandler( ClientCommunicationHandler clientCommunicationHandler, AcademicEnrollment academicEnrollment) {
    this.clientCommunicationHandler = clientCommunicationHandler;
    this.academicEnrollment = academicEnrollment;
  }

  // *********************** READ STRING GIVEN REGEX ************************

  /**
   * This method reads a string from the client and validates it against a regex
   * @param regex is the regex pattern to validate the string against
   * @param invalidMessage is the message to send to the client if the string is invalid
   * @return the valid string read from the client
   */
  public String readString(String regex, String invalidMessage) {
    String read = null;
    while (read == null || read.trim().isEmpty() || !isValid(read, regex)) {
      try {
        read = clientCommunicationHandler.receiveStringMessage();
        if (read == null || read.trim().isEmpty() || !isValid(read, regex)) {
          clientCommunicationHandler.sendMessage(invalidMessage);
        }
      } catch (IOException e) {
        clientCommunicationHandler.sendMessage("Error reading input. Please try again.\n");
      }
    }
    return read;
  }

  private boolean isValid(String read, String regex) {
    if (read.matches(regex) || read.equals("0")) {
      return true;
    }
    return false;
  }

  // *********************** READ INTEGER INPUT ************************

  /**
   * This method reads an integer from the client and validates it against a map of
   * @param maxOptions is the maximum value the integer can be
   * @param invalidMessage is the message to send to the client if the integer is invalid
   * @return  the valid integer read from the client
   */
  public int readAndValidateInput(int maxOptions, String invalidMessage) {
    Map<Integer, Integer> options = new HashMap<>();
    for (int i = MIN_OPTION_VALUE; i <= maxOptions; i++) {
      options.put(i, i);
    }
    return readAndValidateIntegerInput(options, invalidMessage);
  }

  public int readAndValidateInputWithNoBack(int maxOptions, String invalidMessage) {
    Map<Integer, Integer> options = new HashMap<>();
    for (int i = 1;i <= maxOptions; i++) {
      options.put(i, i);
    }
    return readAndValidateIntegerInput(options, invalidMessage);
  }

  public <T> T readAndValidateIntegerInput(Map<Integer, T> options, String invalidMessage) {
    while (true) {
      try {
        Integer selectedOption = clientCommunicationHandler.receiveIntMessage();
        if (options.containsKey(selectedOption)) {
          return options.get(selectedOption);
        } else {
          clientCommunicationHandler.sendMessage(invalidMessage);
        }
      } catch (IOException e) {
        clientCommunicationHandler.sendMessage("IOException occurred while reading selections. Please try again");
      } catch (NumberFormatException e) {
        clientCommunicationHandler.sendMessage(invalidMessage);
      } catch (IllegalArgumentException e) {
        clientCommunicationHandler.sendMessage(invalidMessage);
      }
       catch (NullPointerException e) {
        clientCommunicationHandler.closeResources();
        
      }


    }
  }


  // *********************** READ NETID INPUT ************************

  /**
   * This method checks if a student can be added or dropped from a course
   * 
   * @param course course from add to or drop student from
   * @param flag   if flag is true then we check if the student is actually
   *               enrolled in the course from
   *               which it wants to drop from.
   * @return student who has been validated
   */

  public Student readAndValidateStudent(Course course, Boolean flag) {
    while (true) {
      try {
        String netID = clientCommunicationHandler.receiveStringMessage();
//        if (netID == null) {
//          throw new IllegalArgumentException("Input netID cannot be null.");
//        }
        Student student = academicEnrollment.getStudentByNetID(netID);
//        if (student == null) {
//          clientCommunicationHandler.sendMessage("This student is not registered with the school or this is not a valid netid. Please try again.\n");
//        }
        if (flag) {
          if (course.getActiveEnrollment().contains(student)) {
            return student;
          } else {
            clientCommunicationHandler.sendMessage("This student is not currently enrolled in this course.\n");
            continue;
          }

        }
        return student;
      } catch (Exception e) {
        return null;
      }
    }
  }


  //*********************** PRINTS ONLY ************************


 


  public void   printNoAttendanceSheet() {
    clientCommunicationHandler.sendMessage ("TEXT\nThere is no attendance sheet records to show.\n\n"+ "0. Go back\n");
  }


  public void  printEmailSenderror() {
    clientCommunicationHandler.sendMessage("Error sending email. Please try again later.\n");
  }



  public boolean stayConnected() throws Exception{
    clientCommunicationHandler.sendMessage("LOGOUT\nLogging out\n");
    String response = clientCommunicationHandler.receiveStringMessage();
    System.out.println(response);
    if(response.equals("LOGOUT")){
      return false;
    } else {
      return true;
    }

  }


  public void  displayAvailableCourses(String name, Map<Integer, Course> orderedCourses){
  StringBuilder sb = new StringBuilder("SELECT\nPlease select a course to manage: \n");
   for (Map.Entry<Integer, Course> entry : orderedCourses.entrySet()) {
      Integer key = entry.getKey();
      Course courseName = entry.getValue();
      sb.append(key).append(". ").append(courseName.getCourseID()).append("\n");
    }
    sb.append("\n0. Go back\n");
    clientCommunicationHandler.sendMessage(sb.toString());

}

public void printAttendanceRecordByStudent(String allRecord){

  clientCommunicationHandler.sendMessage("TEXT\nStudent attendance report:\n"+allRecord + "\n0. Go back\n");
}

public void displayMainMenu(String name){
  String prompt = "SELECT\nPlease select the task you want to perform:\n1. Update Profile Information \n2. Manage Courses \n3. Log out \n";
  clientCommunicationHandler.sendMessage(prompt);
}

public void displayProfileOptions(){
  String prompt = "SELECT\nPlease select the task you want to perform:\n1. Change Password \n2. Update My Preferred Name\n\n0. Go back\n";
  clientCommunicationHandler.sendMessage(prompt);
}
 
public void displayNameChangePrompt(String name) {
  clientCommunicationHandler.sendMessage(("ENTER\nPlease enter your new preferred name. Your full name is set as  " + name + " in the system. ") + (" \n\n0. Go back\n"));

}

public void displayPasswordChangePrompt(){
  String prompt = "ENTER\nPlease enter your new password.The new password must be at least 8 characters.\n\n0. Go back\n";
  clientCommunicationHandler.sendMessage(prompt);
}


public  void  noClassToShow() {
  clientCommunicationHandler.sendMessage("No class to show. Please parse enrollment list first\n");
}

public  String changeDate(Date date) {
  DateModifier dateModifier = new DateModifier();
    return dateModifier.changeDate(date);

  }





}