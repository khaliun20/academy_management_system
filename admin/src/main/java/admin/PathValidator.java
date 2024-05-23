package admin;

import java.io.File;

public class PathValidator {
  protected String filePath;

  public PathValidator(String filePath) {
    this.filePath = filePath;
  }

  public Boolean confirmValidPath() {
    File file = new File(filePath);
    System.out.println(file.getAbsolutePath());
    if (file.exists() && file.isFile()) {
      return true;
    }
    else {
      return false;
    }
  }

}
