package edu.duke.projectTeam8;

import java.util.Collection;
import java.util.ArrayList;
import java.io.IOException;

/**
 * A basic Notification class that has a message and observers who get notified by the Notification
 */
public class Notification {
  private String message;
  private Collection<User> observers;

  /**
   * Constructs a Notification with a message and users
   *
   * @param message is a String containing the notification's message
   * @param users are a variable number of users who will be notified by the Notification
   */
  public Notification(String message, User... users) {
    this.message = message;
    this.observers = new ArrayList<>();
    for (User user: users) {
      observers.add(user);
    }
  }

  /**
   * Notifies the observers by having them receive the Notification
   * 
   */
  public void notifyObservers() throws IOException {
    EmailSender sender = new EmailSender();
    for (User observer: observers) {
      sender.sendEmail(observer.getEmail(), "Attendance Status Changed", message);
    }
  }

  /**
   * Returns the Notification's message
   *
   * @return a String with the message for the Notification
   */
  public String getMessage() {
    return message;
  }

}
