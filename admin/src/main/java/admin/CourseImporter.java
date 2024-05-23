package admin;

import edu.duke.projectTeam8.Database;

public class CourseImporter extends DataImporter {

  public CourseImporter(Database database) {
    super(database);
  }

  public CourseImporter(Database database, String filePath) {
    super(database, filePath);
  }

  public void importData() throws Exception {
    database.importCourses(filePath);
  }
}
