package edu.duke.projectTeam8;

import java.sql.SQLException;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;
import java.util.function.BiFunction;

/**
 * A professor teaches a Course and takes attendance
 */
public class OldProfessor extends OldUser implements Professor{
  private final Name name;
  private Set<Course> allCours;
  /**
   * Constructs a Professor from a Name, NetID, and Email Address
   *
   * @param name is the Professor's Name
   * @param netID is a String for the Professor's Net ID
   * @param emailAddress is a String for the Professor's Email Address
   */
  public OldProfessor(Name name, String netID, String emailAddress) {
    super(netID, emailAddress);
    this.name = name;
    this.allCours = new HashSet<>();
  }

  /**
   * Constructs a simple Professor instance for testing
   */
  public OldProfessor(Name name) {
    this(name, "Test101", "Test@aol.com");
  }

  public void addCourse(OldCourse c){
    allCours.add(c);
  }
  // adding getCourses for testing controller
  public Set<Course>  getCourses(){
    return allCours;
  }

  //adding this for tesitng

  @Override
  public String toString() {
    return name.toString();
  }

  public Serializer serialize(BiFunction<String, Object, Serializer> leafConstructor, BiFunction<String, Iterable<Serializer>, Serializer> iterableConstructor) {
    Collection<Serializer> professorDetails = new ArrayList<>();
    professorDetails.add(leafConstructor.apply("first name", name.getFirstName()));
    professorDetails.add(leafConstructor.apply("last name", name.getLastName()));
    professorDetails.add(leafConstructor.apply("email", emailAddress));
    professorDetails.add(leafConstructor.apply("netID", netID));
    return iterableConstructor.apply("professor", professorDetails);
  }

  @Override
  public void modifyPreferredName(String preferredName) {
    name.modifyPreferredName(preferredName);
  }

}
