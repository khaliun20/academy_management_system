package edu.duke.projectTeam8;

import com.mysql.cj.protocol.a.authentication.Sha256PasswordPlugin;


public class ProfileChanger {

    User user;

    public ProfileChanger(User user) {
        this.user = user;
    }

    public void changePassword (String newPassword) throws Exception{
        String salt  =  SHA256Utils.makeSaltStr();
        String passwordHash = SHA256Utils.hashPassword(newPassword, salt);
        user.updateHashSalt(passwordHash, salt);

    }

    public void changePreferredName (String newPreferredName) throws Exception {
        user.modifyPreferredName(newPreferredName); 
        
    }

    
    
}
