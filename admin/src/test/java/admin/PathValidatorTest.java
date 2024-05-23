package admin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PathValidatorTest {
  @Test
  public void test_PathValidator() {
    PathValidator validator = new PathValidator("src/main/java/admin/App.java");
    assertTrue(validator.confirmValidPath());
    PathValidator fakeFile = new PathValidator("fakeFile.txt.csv");
    assertFalse(fakeFile.confirmValidPath());
  }

}
