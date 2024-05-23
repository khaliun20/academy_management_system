package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class MySQLDatabaseTest {
    MySQLDatabase db = new MySQLDatabase();

    public MySQLDatabaseTest() throws SQLException {

    }

    @Test
    public void test_EnrollStudentToSchool() throws Exception {
        db.cleanTables();
        db.enrollStudentToSchool("yz000", "Lucas", "Larcher",
                "I am sleepy", "la000@duke.edu");
        User me = db.getUser("yz000");
        assertEquals("yz000", me.getNetID());
        db.cleanTables();
    }

    @Test
    public void test_importUsers() {

        try {
            db.cleanTables();
            db.importUsers("src/test/resources/schoolList.csv");
            db.cleanTables();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL exception occurred: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }

    }

    @Test
    public void test_getUser() throws Exception {
        db.cleanTables();
        db.importUsers("src/test/resources/schoolList.csv");
        User me = db.getUser("jd102");
        assertEquals("jd102", me.getNetID());
        db.cleanTables();
    }


    @Test
    public void test_importCourses() {

        try {
            db.cleanTables();
            db.importUsers("src/test/resources/schoolList.csv");
            db.importCourses("src/test/resources/courseList.csv");
            db.cleanTables();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL exception occurred: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void test_importEnrollment() {
        try {
            db.cleanTables();
            db.importUsers("src/test/resources/schoolList.csv");
            db.importCourses("src/test/resources/courseList.csv");
            db.importEnrollment("src/test/resources/enrollList.csv");
            db.cleanTables();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL exception occurred: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }

    }

    @Test
    public void test_StudentAndCourseExists() {
        try {
            db.cleanTables();
            db.importUsers("src/test/resources/schoolList.csv");
            db.importCourses("src/test/resources/courseList.csv");
            db.checkCourseExists("ECE100-003");
            assertThrows(IllegalArgumentException.class,
                    () -> db.checkCourseExists("FAKE"));
            db.checkStudentExistsInSchool("tc114");
            assertThrows(IllegalArgumentException.class,
                    () -> db.checkStudentExistsInSchool("yz777"));
            db.checkStudentNotExistInSchool("yz777");
            assertThrows(IllegalArgumentException.class,
                    () -> db.checkStudentNotExistInSchool("tc114"));

            db.cleanTables();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL exception occurred: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void test_StudentAddAndDropClass() {
        try {
            db.cleanTables();
            db.importUsers("src/test/resources/schoolList.csv");
            db.importCourses("src/test/resources/courseList.csv");
            assertThrows(IllegalArgumentException.class,
                    () -> db.checkStudentInClassForDrop("ECE100-003", "jb111"));

            db.checkStudentInClassForEnroll("ECE100-003", "jb111");
            db.enrollStudentInClass("ECE100-003", "jb111");
            assertThrows(IllegalArgumentException.class,
                    () -> db.checkStudentInClassForEnroll("ECE100-003", "jb111"));
            db.checkStudentInClassForDrop("ECE100-003", "jb111");
            db.dropStudent("ECE100-003", "jb111");
            assertThrows(IllegalArgumentException.class,
                    () -> db.checkStudentInClassForDrop("ECE100-003", "jb111"));
            db.enrollStudentInClass("ECE100-003", "jb111");
            db.cleanTables();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL exception occurred: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void test_getters() {
        try {
            db.cleanTables();
            db.importUsers("src/test/resources/schoolList.csv");
            db.importCourses("src/test/resources/courseList.csv");
            db.importEnrollment("src/test/resources/enrollList.csv");

            Professor prof0 = db.getProfessor("lo110");
            assertEquals(prof0.getNetID(), "lo110");
            StringBuilder sb = new StringBuilder();
            for (Professor prof : db.getAllProfessors()) {
                sb.append(prof.getName());
                sb.append(prof.getCourses());
            }
            String expected = "Jaslyn Blanchard[ECE300-001]Cesar Ray[ECE100-001]Kaitlyn Mccarthy[ECE100-004, ECE200-001]Maribel Graves[ECE100-002]Shayla Cardenas[ECE100-003]Miley Thompson[MATH100-001]Larry Oconnell[MATH502-001]Jack Henson[MATH500-001]Kamari Costa[ECE200-002]";
            assertEquals(expected.length(), sb.toString().length());
            assertFalse(db.getAllStudents().isEmpty());

            assertFalse(db.getActiveEnrollment("ECE100-001").isEmpty());
            db.cleanTables();

        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL exception occurred: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }

    }

    @Test
    public void test_checkStudentEnrolledInClass() throws Exception {
        Database db = new MySQLTestDatabase();
        assertTrue(db.checkStudentEnrolledInClass("MATH502-001", "jd102"));
        assertFalse(db.checkStudentEnrolledInClass("MATH502-001", "yz777"));

        db.createNewClass("STA000-111","lo110");
        assertThrows(SQLException.class,()->db.createNewClass("STA020-111","aa000"));
        assertThrows(SQLException.class,()->db.createNewClass("STA020-111","jd102"));
        assertThrows(SQLException.class,()->db.createNewClass("STA000-111","lo110"));
        db.dropStudent("MATH502-001","jd102");
    }
}




