package admin;

import edu.duke.projectTeam8.Database;

public class UserImporter extends DataImporter {

  public UserImporter(Database database) {
    super(database);
  }

  public UserImporter(Database database, String filePath) {
    super(database, filePath);
  }

  public void importData() throws Exception {
    database.importUsers(filePath);
  }
}
