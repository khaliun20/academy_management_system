package edu.duke.projectTeam8;

/**
 * This is an Abstract Class for Serialized Object creation (i.e. converting
 * object data to XML or JSON)
 */
public abstract class AbstractSerialObject {
  protected final String header;

  /**
   * Constructor to create an AbstractSerial Object
   *
   * @param header is a String which is the header for the data in this object
   */
  public AbstractSerialObject(String header) {
    this.header = header;
  }

  /**
   * Returns the header for the Serialized Object
   *
   */
  public abstract String getHeader();

  /**
   * Returns the data portion of the Serialized Object as a String which allows
   * the Object to define how its data is viewed
   *
   */
  public abstract String contentToString();
}
