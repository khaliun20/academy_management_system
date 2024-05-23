package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NameTest {
  @Test
  public void test_name() {
    Name john = new Name("Jonathan", "Smith", "John");
    assertEquals(john.toString(), "John Smith");
    assertEquals(john.serialize(), "<Name><firstName>Jonathan</firstName><lastName>Smith</lastName>"+
                 "<preferredName>John</preferredName></Name>");
    
    Name john2 = new Name("Jonathan", "Smith");
    assertEquals(john2.toString(), "Jonathan Smith");
    assertEquals(john2.serialize(), "<Name><firstName>Jonathan</firstName><lastName>Smith</lastName>"+
                 "<preferredName>Jonathan</preferredName></Name>");
    
    john2.modifyPreferredName("Jane");
    assertEquals(john2.toString(), "Jane Smith");
    assertEquals(john2.serialize(), "<Name><firstName>Jonathan</firstName><lastName>Smith</lastName>"+
                 "<preferredName>Jane</preferredName></Name>");
    
    
  }

}
