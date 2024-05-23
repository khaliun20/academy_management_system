package edu.duke.projectTeam8;


/**
 * A Notifcation that lets the student know that ttheir attendance status has
 * been changed.
 */

public class AttendanceStatusChangeNotification extends Notification {

    /**
     * Creates a Notification for Attendance Status Change with the Student and the
    * message
    *
    * @param message is a String containing the notification's message
    * @param student is the student who will be notified by the Notification
    */
  public AttendanceStatusChangeNotification(String message, Student student) {
    super(message, student);
  }

}
