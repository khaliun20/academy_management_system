package edu.duke.projectTeam8;

import java.sql.SQLException;
import java.util.Collection;
import java.util.ArrayList;

/**
 * A User has access to the system
 */
public class OldUser implements User{ // Probably should turn this into an interface
  protected final String netID;
  protected final String emailAddress;
  private final Collection<Notification> notifications; // Did I conflate User and Observer?  Should they be different?

  /**
   * Creates a User from a NetID and an Email Address
   *
   * @param netID is a String for the user's NetID
   * @param emailAddress is a String for the user's Email Address
   */
  public OldUser(String netID, String emailAddress) {
    this.netID = netID;
    this.emailAddress = emailAddress;
    this.notifications = new ArrayList<>();
  }

  /**
   * Return's the user's NetID
   *
   * @return a String containing the user's NetID
   */
  public String getNetID() {
    return netID;
  }

  @Override
  public String getHashedKey() throws SQLException {
    return "";
  }

  @Override
  public String getSalt() throws SQLException {
    return "";
  }

  /**
   * Return's the user's email address
   *
   * @return a String containing the user's email address
   */
  public String getEmail() {
    return emailAddress;
  }

  @Override
  public void updateHashSalt(String passwordHash, String salt) throws SQLException {

  }

  @Override
  public void modifyPreferredName(String preferredName) throws SQLException {

  }

  @Override
  public Name getName() {
    return null;
  }

  @Override
  public String getType() {
    return "";
  }

  /**
   * Queues notifications for the user
   *
   * @param notification is a Notification for the user's review
   */
  public void receiveNotification(Notification notification) {
    notifications.add(notification);
  }

  /**
   * Let's a user review their notifications
   *
   * @return an Iterable of Notifications
   */
  public Iterable<Notification> checkNotifications() {
    return notifications;
  }

  public String serialize(){

    String nid = Serializer.wrapString("netID", netID);
    String email = Serializer.wrapString("emailAddress", emailAddress);
    String output = Serializer.wrapString("User", nid+email);
    return output;
  }
  
}
