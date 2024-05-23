package admin;

import edu.duke.projectTeam8.Database;

public class EnrollmentImporter extends DataImporter {

  public EnrollmentImporter(Database database) {
    super(database);
  }

  public EnrollmentImporter(Database database, String filePath) {
    super(database, filePath);
  }

  public void importData() throws Exception {
    database.importEnrollment(filePath);
  }
}
