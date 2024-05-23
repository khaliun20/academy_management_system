package edu.duke.projectTeam8;

import java.sql.SQLException;

public class StubDatabaseUser extends DatabaseUser{
    private String salt;
    private String hash;

    public StubDatabaseUser(Name name, String netID, String emailAddress){
        super(name,netID,emailAddress,null,null);
        String tmp = "not a bad password";
        salt = SHA256Utils.makeSaltStr();
        hash = SHA256Utils.hashPassword(tmp,salt);
    }
    @Override
    public void updateHashSalt(String passwordHash, String salt) throws SQLException {
        this.hash = passwordHash;
        this.salt = salt;
    }
    @Override
    public String getHashedKey(){return hash;}

    @Override
    public String getSalt(){return salt;}
}
