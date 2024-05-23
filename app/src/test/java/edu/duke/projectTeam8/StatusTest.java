package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class StatusTest {
  @Test
  public void test_createStatus() {
    Status enrolled = new Status(true);
    assertTrue(enrolled.getStatus());
    Status notEnrolled = new Status(false);
    assertFalse(notEnrolled.getStatus());
    assertEquals("Enrolled", enrolled.toString());
    assertEquals("Not Enrolled", notEnrolled.toString());
    enrolled.modifyStatus(false);
    assertFalse(enrolled.getStatus());
    assertTrue(notEnrolled.equals(enrolled));
    enrolled.modifyStatus(true);
    assertTrue(enrolled.getStatus());
    assertFalse(notEnrolled.equals(enrolled));
    assertFalse(notEnrolled.equals(5));
    assertEquals(1, enrolled.hashCode());
    assertEquals(0, notEnrolled.hashCode());
  }

}
