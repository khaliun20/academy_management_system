package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseUserTest {
    MySQLTestDatabase db = new MySQLTestDatabase();
    Professor lo = db.getProfessor("lo110");
    Student john = db.getStudent("jd102");
    Course course = new DatabaseCourse("MATH502-001", lo, db);
    WeeklyTextSummary summarizer = new WeeklyTextSummary();

    DatabaseUserTest() throws SQLException, IOException, NoSuchAlgorithmException {
    }

    @Test
    public void test_getters() throws Exception {
        String postfix = "@duke.edu";
        assertEquals(john.getEmail(),john.getNetID()+postfix);
        assertEquals(lo.getEmail(),lo.getNetID()+postfix);
        String oldName = john.toString();
        john.modifyPreferredName("Jonathan");
        assertNotEquals(oldName,john.toString());

    }
    @Test
    public void test_hashKeySync() throws Exception {
        assertNotEquals(john.getHashedKey(),lo.getHashedKey());
        String hashedPassword = SHA256Utils.hashPassword("Nothing",john.getSalt());
        john.updateHashSalt(hashedPassword,john.getSalt());
        assertEquals(hashedPassword,john.getHashedKey());
        john.updateHashSalt(hashedPassword,john.getSalt());
        assertTrue(john.equals(john));
        assertFalse(john.equals(null));

    }
}