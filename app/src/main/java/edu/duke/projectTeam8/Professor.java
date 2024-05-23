package edu.duke.projectTeam8;
import java.sql.SQLException;
import java.util.Set;

/**
 * A Professor is a user who teaches courses
 */
public interface Professor extends User {

  /**
   * Return the set of professor's courses
   * @return a set of courses
   * @throws Exception if there is an error in the database
   */

  public Set<Course> getCourses() throws Exception;

    
}
