package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class ProfileChangerTest {

    Name john = new Name("John", "Doe");
    @Test
    void testChangePassword() throws Exception {

        User u = new StubDatabaseUser (john,"jd101", "jd101@duke.edu");
        ProfileChanger pc = new ProfileChanger(u);
        pc.changePassword("newtestPassword");
        assertEquals(u.getHashedKey(), SHA256Utils.hashPassword("newtestPassword", u.getSalt()));
    }

    @Test
    void testChangePreferredName() throws Exception {
        OldStudent s = new OldStudent(john, "jd101", "jd101@duke.edu");
        ProfileChanger pc = new ProfileChanger(s);
        pc.changePreferredName("Johnathan");
        assertEquals(s.toString(), "Johnathan Doe");
    }

    @Test
    void testChangePreferredName1() throws Exception {
        OldStudent s = new OldStudent(john, "jd101", "jd101@duke.edu");
        ProfileChanger pc = new ProfileChanger(s);
        pc.changePreferredName("");
        assertEquals(s.toString(), " Doe");
    }


    @Test
    void testChangePreferredName2() throws Exception {
        OldProfessor s = new OldProfessor (john, "jd101", "jd101@duke.edu");
        ProfileChanger pc = new ProfileChanger(s);
        pc.changePreferredName("Helen");
        assertEquals(s.toString(), "Helen Doe");
    }
}

   