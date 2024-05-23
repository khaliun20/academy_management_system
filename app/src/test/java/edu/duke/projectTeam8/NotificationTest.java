package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.io.IOException;

public class NotificationTest {
  @Test
  public void test_createNotification() {

    User testUser = new OldUser( "js101", "john@aol.com");
    Notification testNotification = new Notification("This is a Test!", testUser);
    assertEquals("This is a Test!", testNotification.getMessage());
  }

  @Test
  public void test_notifyObserver() throws IOException {

    User testUser = new OldUser( "js101", "john@aol.com");
    Notification testNotification = new Notification("This is a Test!", testUser);
    testNotification.notifyObservers();
  }


  
}
