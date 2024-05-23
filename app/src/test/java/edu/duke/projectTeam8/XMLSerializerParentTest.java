package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class XMLSerializerParentTest {
  @Test
  public void test_toString() {
    String first_name = "John";
    String last_name = "Smith";
    Name name = new Name(first_name, last_name);
    XMLSerializer first = new XMLSerializer("first name", first_name);
    XMLSerializer last = new XMLSerializer("last name", last_name);
    XMLSerializerParent serializedName = new XMLSerializerParent("name", first, last);
    assertEquals("<name>\n\t<first name>John</first name>\n\t<last name>Smith</last name>\n</name>", serializedName.toString());
  }

}
