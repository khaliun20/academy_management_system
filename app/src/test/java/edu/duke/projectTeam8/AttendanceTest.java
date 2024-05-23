package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AttendanceTest {
  @Test
  public void test_createAttendance() {
    Attendance test = new Attendance();
    assertThrows(IllegalStateException.class, () -> test.toString());
    test.markAttended();
    assertEquals("Attended", test.toString());
    test.markTardy();
    assertEquals("Tardy", test.toString());
    test.markAbsent();
    assertEquals("Absent", test.toString());
  }
  
  @Test
  public void test_getMethods() {
    Attendance test = new Attendance();
    assertThrows(IllegalStateException.class, () -> test.wasTardy());
    assertThrows(IllegalStateException.class, () -> test.attended());
    test.markTardy();
    assertTrue(test.wasTardy());
    assertTrue(test.attended());
    test.markAbsent();
    assertFalse(test.wasTardy());
    assertFalse(test.attended());
    test.markAttended();
    assertFalse(test.wasTardy());
    assertTrue(test.attended());
  }
  

}
