package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class XMLSerializerLeafTest {
  @Test
  public void test_Leaf() {
    String first_name = "Mike";
    XMLSerializerLeaf serializedName = new XMLSerializerLeaf("first name", first_name);
    assertEquals("first name", serializedName.getHeader());
    assertEquals("Mike", serializedName.contentToString());
    assertEquals("<first name>Mike</first name>", serializedName.toString());
  }

  @Test
  public void test_null() {
    String first_name = null;
    XMLSerializerLeaf serializedName = new XMLSerializerLeaf("first name", first_name);
    assertEquals("first name", serializedName.getHeader());
    assertEquals("null", serializedName.contentToString());
    assertEquals("<first name>null</first name>", serializedName.toString());
  }

}
