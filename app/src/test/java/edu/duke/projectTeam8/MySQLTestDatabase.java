package edu.duke.projectTeam8;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class MySQLTestDatabase extends MySQLDatabase{
    public MySQLTestDatabase() throws SQLException, IOException, NoSuchAlgorithmException {
        super();
        super.cleanTables();
        super.importUsers("src/test/resources/schoolList.csv");
        super.importCourses("src/test/resources/courseList.csv");
        super.importEnrollment("src/test/resources/enrollList.csv");

    }

    public void reset() throws Exception {
        super.cleanTables();
        super.importUsers("src/test/resources/schoolList.csv");
        super.importCourses("src/test/resources/courseList.csv");
        super.importEnrollment("src/test/resources/enrollList.csv");
    }
}
