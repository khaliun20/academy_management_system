package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class JSONSerializerLeafTest {
  @Test
  public void test_Leaf() {
    String first_name = "Mike";
    JSONSerializerLeaf serializedName = new JSONSerializerLeaf("first name", first_name);
    assertEquals("first name", serializedName.getHeader());
    assertEquals("\"Mike\"", serializedName.contentToString());
    Boolean isEmpty = false;
    JSONSerializerLeaf serializedBool = new JSONSerializerLeaf("empty", isEmpty);
    assertEquals("false", serializedBool.contentToString());
    String thing = null;
    JSONSerializerLeaf serializedNull = new JSONSerializerLeaf("thing", thing);
    assertEquals("null", serializedNull.contentToString());
  }

}
