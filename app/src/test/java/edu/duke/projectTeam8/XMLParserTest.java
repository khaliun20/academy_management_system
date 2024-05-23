package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.io.IOException;

public class XMLParserTest {
  @Test
  public void test_XMLNameParser() {
    String testName = "<name>\n\t<first_name>John</first_name>\n\t<last_name>Smith</last_name>\n</name>";
    XMLParser parser = new XMLParser();
    String className = parser.parse3(testName);
    assertEquals("name", className);
    assertEquals("\n\t<first_name>John</first_name>\n\t<last_name>Smith</last_name>\n", parser.parse2(testName));
    ParsedClass name = parser.parse(testName);
    assertEquals("name", name.getName());
    assertEquals("Smith", name.getParameterData("last_name"));
    assertEquals("John", name.getParameterData("first_name"));
  }

  @Test
  public void test_XMLEnrollmentParser() throws IOException {
    String testEnrollment = "<enrollment>\n\t<student>\n\t\t<netID>js101</netID>\n\t\t<last_name>Smith</last_name>\n\t\t<first_name>John</first_name>\n\t\t<email>john@aol.com</email>\n\t</student>\n\t<student>\n\t\t<netID>fs101</netID>\n\t\t<last_name>Smith</last_name>\n\t\t<first_name>Fred</first_name>\n\t\t<email>fred@aol.com</email>\n\t</student>\n\t<student>\n\t\t<netID>ms101</netID>\n\t\t<last_name>Smith</last_name>\n\t\t<first_name>Mike</first_name>\n\t\t<email>mike@aol.com</email>\n\t</student>\n</enrollment>";
    XMLParser parser = new XMLParser();
    ParsedClass enrollment = parser.parse(testEnrollment);
    assertEquals("enrollment", enrollment.getName());
    assertEquals("Smith", enrollment.getParameter("student").getParameterData("last_name"));
    assertEquals("John", enrollment.getParameter("student").getParameterData("first_name"));
    assertEquals("js101", enrollment.getParameter("student").getParameterData("netID"));
    assertEquals("john@aol.com", enrollment.getParameter("student").getParameterData("email"));
assertEquals("Smith", enrollment.getParameter("student2").getParameterData("last_name"));
    assertEquals("Fred", enrollment.getParameter("student2").getParameterData("first_name"));
    assertEquals("fs101", enrollment.getParameter("student2").getParameterData("netID"));
    assertEquals("fred@aol.com", enrollment.getParameter("student2").getParameterData("email"));
    assertEquals("Smith", enrollment.getParameter("student3").getParameterData("last_name"));
    assertEquals("Mike", enrollment.getParameter("student3").getParameterData("first_name"));
    assertEquals("ms101", enrollment.getParameter("student3").getParameterData("netID"));
    assertEquals("mike@aol.com", enrollment.getParameter("student3").getParameterData("email"));
 }
    

}
