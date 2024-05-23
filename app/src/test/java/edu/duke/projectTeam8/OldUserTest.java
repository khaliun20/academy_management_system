package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class OldUserTest {
  @Test
  public void test_createUser() {
    OldUser john = new OldUser("js101", "john@aol.com");
    assertEquals("js101", john.getNetID());
    assertEquals("john@aol.com", john.getEmail());
  }

  private <T> Integer countIterableSize(Iterable<T> items) {
    Integer count = 0;
    for(T item: items) {
      count++;
    }
    return count;
  }

  @Test
  public void test_receiveNotification() {
    OldUser john = new OldUser("js101", "john@aol.com");
    Notification test = new Notification("This is a Test!", john);
    assertEquals(0, countIterableSize(john.checkNotifications()));
    john.receiveNotification(test);
    assertEquals(1, countIterableSize(john.checkNotifications()));
    for (Notification notification: john.checkNotifications()) {
      assertEquals("This is a Test!", notification.getMessage());
    }
  }
  @Test
  public void test_serialize(){

    OldUser john = new OldUser("js101", "john@aol.com");
    assertEquals(john.serialize(),"<User><netID>js101</netID><emailAddress>john@aol.com</emailAddress></User>");
  }

  @Test
  public void test_miscellaneous() throws Exception {
    User usr = new OldUser("yz696",null);
    usr.modifyPreferredName(null);
    usr.getName();
    usr.getType();
    usr.updateHashSalt(null,null);
    usr.getSalt();
    usr.getHashedKey();
  }
}
