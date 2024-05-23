package edu.duke.projectTeam8;

import java.sql.SQLException;

/**
 * A User has access to the system
 */
public interface User {


  public String getNetID();

  public String getHashedKey() throws Exception;

  public String getSalt() throws Exception;


  public String getEmail();

  public void updateHashSalt(String passwordHash, String salt) throws Exception;

  public void modifyPreferredName(String preferredName) throws Exception;

  public Name getName();

  public String getType();  

  
}
