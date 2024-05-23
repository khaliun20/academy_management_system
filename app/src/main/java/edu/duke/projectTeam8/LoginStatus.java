package edu.duke.projectTeam8;

public class LoginStatus {
    private User user;
    private int loginAttempt;
    private boolean loginSuccess;

    public LoginStatus(){
        user = null;
        loginAttempt = 0;
        loginSuccess = false;
    }
    public void setLoginSuccess(){
        loginSuccess = true;
    }
    public boolean getLoginOutcome(){
        return loginSuccess;
    }
    public void setSuccessfulUser(User user){
        this.user= user;
    }
    public User getUser(){return user;}
    
    public void  addLoginAttempt(){
        loginAttempt++;
    }
    public int getLoginAttempt(){
        return loginAttempt;
    }

}
