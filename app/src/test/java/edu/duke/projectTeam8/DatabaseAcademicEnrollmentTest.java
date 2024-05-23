package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseAcademicEnrollmentTest {
    MySQLDatabase db = new MySQLTestDatabase();

    DatabaseAcademicEnrollmentTest() throws SQLException, IOException, NoSuchAlgorithmException {
    }

    @Test
    public void test_all() throws Exception {
        DatabaseAcademicEnrollment academicEnrollment = new DatabaseAcademicEnrollment(db);
        assertEquals(db,academicEnrollment.getDatabase());
        assertEquals("jd102",academicEnrollment.getStudentByNetID("jd102").getNetID());
        Professor jh109 = db.getProfessor("jh109");
        assertEquals(jh109,academicEnrollment.findProfessorInDirectory("jh109"));
        assertFalse(academicEnrollment.isStudent(jh109));
        academicEnrollment.getStudents();
    }
}