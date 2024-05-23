package edu.duke.projectTeam8;

/**
 * Creates a Name object to track a person's legal and preferred names
 */
public class Name {
  private final String firstName;
  private final String lastName;
  private String preferredName;

  /**
   * Creates a Name object
   *
   * @param firstName is a String defining a person's legal first name
   * @param lastName is a String defining a person's legal last name
   * @param preferredName is a String defining a person's preferred name
   */ 
  public Name(String firstName, String lastName, String preferredName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.preferredName = preferredName;
  }

  /**
   * Creates a Name object and sets preferred name to the person's legal first name by default
   *
   * @param firstName is a String defining a person's legal first name
   * @param lastName is a String defining a person's legal last name
   */ 
  public Name(String firstName, String lastName) {
    this(firstName, lastName, firstName);
  }

  /**
   * Returns the student's first name
   *
   * @return a String with the student's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Returns the student's last name
   *
   * @return a String with the student's last name
   */
  public String getLastName() {
    return lastName;
  }
  
  /**
   * Modifies a person's preferred name
   *
   * @param preferredName is a String defining a person's preferred name
   */
  public void modifyPreferredName(String preferredName) {
    this.preferredName = preferredName;
  }

  @Override
  public String toString() {
    return preferredName + " " + lastName;
  }

  public String serialize(){

    String first = Serializer.wrapString("firstName",firstName);
    String last = Serializer.wrapString("lastName",lastName);
    String preferred = Serializer.wrapString("preferredName", preferredName);
    return Serializer.wrapString("Name", first+last+preferred);

  }

}
