package edu.duke.projectTeam8;


public class DatabaseUser implements User {
  public final String netID;
  public final String email;
  public final Name name;
  public final Database database;
  public final String type;

  /**
   * Constructs a User from a Name, NetID, Email Address, type, and Database
   */
  public DatabaseUser(Name name, String netID, String emailAddress, Database database, String type) {
    this.name = name;
    this.netID = netID;
    this.email = emailAddress;
    this.database = database;
    this.type = type;
  }

  public DatabaseUser(Name name, String netID, String emailAddress, Database database) {
    this(name, netID, emailAddress, database,  null);
  }


  /**
   * Return's the user's NetID
   *
   * @return a String containing the user's NetID
   */
  public String getNetID() {
    return netID;
  }

  public Name getName() {
    return name;
  }


  public String getEmail() {
    return email;
  }

  /**
   * Modify the user's preferred name
   * @param preferredName is the new preferred name
   * @throws Exception if there is an error in the database
   */

  public void modifyPreferredName(String preferredName) throws Exception {
    database.modifyPreferredName(netID, preferredName);
    name.modifyPreferredName(preferredName);

  }



  public void updateHashSalt(String passwordHash, String salt) throws Exception {
    database.updateHashSalt(netID, passwordHash, salt);
  }

 

  public String getHashedKey() throws Exception {
    return database.getHashedKey(netID);
  }

 

  public String getSalt() throws Exception {
    return database.getSalt(netID);
  }

  public String getType(){
    return type;
  }

  @Override
    public String toString(){
        return name.toString();
    } 

    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }
        if (!(o instanceof DatabaseUser)) {
            return false;
        }
        DatabaseUser other = (DatabaseUser) o;
        return other.getNetID().equals(this.getNetID());
    }

    @Override
    public int hashCode(){
        return netID.hashCode();
    }

}
