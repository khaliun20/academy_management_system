package admin;

import edu.duke.projectTeam8.Database;

public abstract class DataImporter {
  protected Database database;
  protected String filePath;

  public DataImporter(Database database) {
    this.database = database;
  }

  public DataImporter(Database database, String filePath) {
    this.database = database;
    this.filePath = filePath;
  }

  public abstract void importData() throws Exception;

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

}
