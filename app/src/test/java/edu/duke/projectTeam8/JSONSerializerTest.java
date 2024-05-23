package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class JSONSerializerTest {
  @Test
  public void test_jsonSerializer() {
    JSONSerializer serializer = new JSONSerializer();
    String name = serializer.serialize("name", "John");
    String expectedName = "{\"name\": \"John\"}";
    assertEquals(expectedName, name);
    String salary = serializer.serialize("salary", "56000");
    String expectedSalary = "{\"salary\": 56000}";
    assertEquals(expectedSalary, salary);
    String employee = serializer.serialize("employee", name, salary);
    String expectedEmployee = "{\n\t\"employee\": {\n\t\t\"name\": \"John\",\n\t\t\"salary\": 56000\n\t}\n}";
    assertEquals(expectedEmployee, employee);
  }

  @Test
  public void test_jsonSerializer2() {
    JSONSerializerLeaf name = new JSONSerializerLeaf("name", "John");
    JSONSerializer nameSerializer = new JSONSerializer(name);
    String expectedName = "{\n\t\"name\": \"John\"\n}";
    assertEquals(expectedName, nameSerializer.toString());
    XMLSerializerLeaf salary = new XMLSerializerLeaf("salary", "56000");
    JSONSerializer salarySerializer = new JSONSerializer(salary);
    String expectedSalary = "{\n\t\"salary\": 56000\n}";
    assertEquals(expectedSalary, salarySerializer.toString());
    JSONSerializer employee = new JSONSerializer(new JSONSerializerClass("employee", nameSerializer, salarySerializer));
    String expectedEmployee = "{\n\t\"employee\": {\n\t\t\"name\": \"John\",\n\t\t\"salary\": 56000\n\t}\n}";
    assertEquals(expectedEmployee, employee.toString());
  }

}
