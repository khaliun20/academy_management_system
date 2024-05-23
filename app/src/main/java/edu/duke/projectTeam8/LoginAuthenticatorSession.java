package edu.duke.projectTeam8;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.io.IOException;

public class LoginAuthenticatorSession {
    ClientCommunicationHandler ccHandler;
    Database mySQLDatabase;
    LoginStatus loginStatus;

    public LoginAuthenticatorSession(ClientCommunicationHandler ccHandler,
                                    Database mySQLDatabase) {
        this.ccHandler = ccHandler;
        this.mySQLDatabase = mySQLDatabase;
        this.loginStatus = new LoginStatus();
    }

    private boolean tryLoginAttempt() {
        loginStatus.addLoginAttempt();
        try {
            String login = ccHandler.displayAndPullStr("LOGIN\nWelcome to Canvas for Duke!");
            String[] loginInfo = login.split("\n");
            String netID = loginInfo[0];
            String password = loginInfo[1];
        
            System.out.println("password: " + password);

            User user = mySQLDatabase.getUser(netID);
            System.out.println("user: " + user.getNetID() + user.getSalt());
            System.out.println("user: " + user.getHashedKey());
            String computedHash = SHA256Utils.hashPassword(password, user.getSalt());

            System.out.println("computedHash: " + computedHash);
            System.out.println("salt" + user.getSalt());
            if (computedHash.equals(user.getHashedKey())) {
                loginStatus.setSuccessfulUser(user);
                loginStatus.setLoginSuccess();
                ccHandler.sendMessage("LOGINSUCCESS\n"+netID);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    public LoginStatus authenticate() {
        do {
            if (tryLoginAttempt()) {
                break;
            }
        } while (loginStatus.getLoginAttempt() < 3);
        return loginStatus;
    }

}
