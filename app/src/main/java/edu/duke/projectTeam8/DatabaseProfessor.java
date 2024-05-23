package edu.duke.projectTeam8;
import java.util.Set;

/**
 * A Professor is a user who teaches courses
 
 */

public class DatabaseProfessor extends DatabaseUser implements Professor{
    public DatabaseProfessor(Name name, String netID, String emailAddress, Database database) {
        super(name, netID, emailAddress, database);
    }

    /**
     * Return the set of professor's courses 
     * @return a set of courses
     * @throws Exception if there is an error in the database
     */

    @Override
    public Set<Course> getCourses() throws Exception{
        return database.getClasses(netID, "professor_id", "courses");
    }

   

    
}


