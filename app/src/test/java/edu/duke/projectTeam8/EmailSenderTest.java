package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

public class EmailSenderTest {

  @Test
  public void test_() throws IOException {

    EmailSender es = new EmailSender();
    String address = "a616585987@gmail.com";
    

    es.sendEmail(address, "Email Sender Test",
                   "Test email sent from java within VCM image");
  }
}
