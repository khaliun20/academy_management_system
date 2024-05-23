package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginAuthenticatorSessionTest {
    @Test
    public void test_authentication() throws SQLException {


        //Successful Login
        StubIOHandler handler = new StubIOHandler();
        SaltAndHashDatabase database = new SaltAndHashDatabase();
        LoginAuthenticatorSession login = new LoginAuthenticatorSession(handler, database);
        LoginStatus temp = login.authenticate();
        assertTrue(temp.getLoginOutcome());
        assertEquals(temp.getUser().getNetID(), "backdoor");

        //Failed login due to wrong
        handler.setWrongPassword();
        login = new LoginAuthenticatorSession(handler, database);
        temp = login.authenticate();
        assertFalse(temp.getLoginOutcome());
        assertTrue(temp.getLoginAttempt() >= 3);

        handler.setWrongUsr();
        login = new LoginAuthenticatorSession(handler, database);

        assertFalse(login.authenticate().getLoginOutcome());

    }

}

class StubIOHandler extends ClientCommunicationHandler {
    private String netID;
    private String password;

    public StubIOHandler(String netID, String password) {
                super(null, new ByteArrayInputStream("placeholder".getBytes()),
                System.out, -1, null);
        this.netID = netID;
        this.password = password;
    }

    public StubIOHandler() {
        this("backdoor", "not a bad password");

    }

    public void setWrongPassword() {
        password = "a bad password";
    }

    public void setWrongUsr() {
        netID = "noSuchUser";
    }

    @Override
    public String displayAndPullStr(String displayMsg) throws IOException {
        if (displayMsg.equals("NetID:"))
            return netID;
        if (displayMsg.equals("Password:"))
            return password;
        if (displayMsg.equals("LOGIN\nWelcome to Canvas for Duke!"))
            return netID+"\n"+password;
        else
            throw new IOException();
    }

}

class SaltAndHashDatabase extends MySQLDatabase {

    public SaltAndHashDatabase() throws SQLException {
        super();

    }

    public User getUser(String netID) throws SQLException {
        if ("backdoor".equals(netID))
            return new StubDatabaseUser(null,netID, null);
        throw new SQLException();
    }


}

