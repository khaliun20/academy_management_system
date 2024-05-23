package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;


import static org.junit.jupiter.api.Assertions.*;

class PeriodicExecutorTest {
    @Test
    void test_recurrentSend()
            throws Exception {
        PeriodicExecutor pe = new PeriodicExecutor(500, 2);
        AcademicEnrollmentV1 academicEnrollment = new AcademicEnrollmentV1("src/main/resources/CryptoTest.txt");
        academicEnrollment.loadAcademicEnrollment();
        String recipientAddress = "fake@aaa.edu";
        WeeklyTextSummary summary = new WeeklyTextSummary();
        Date startDate = summary.strToDate("01/01/2000 00:00:00 EDT");
        String outcome = pe.recurrentEmailSend(academicEnrollment, summary, startDate, recipientAddress);
        assertEquals("Program terminated after 2 iterations.", outcome);

        Thread UserThread = new Thread(() -> {
            try {
                // Execute the method
                String outcomeCut = pe.recurrentEmailSend(academicEnrollment, summary, new Date(), null);

                assertEquals(outcomeCut, "Program terminated due to user interrupt.");
            } catch (Exception e){
                System.out.println("Irrelevant exception");
            }

        });
        UserThread.start();
        try {
            Thread.sleep(50); // Wait for 0.01 seconds
            UserThread.interrupt(); // Interrupt the thread

        } catch (InterruptedException e) {
            System.out.println();
        }
        assertTrue(UserThread.isInterrupted());
    }

    @Test
    void test_main() {
        String[] args = new String[]{"src/main/resources/CryptoTest.txt",
                "01/01/1999", "EST", "1000", "2"};
        try {
            PeriodicExecutor.main(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void test_main_error()
            throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        PrintStream outStream = new PrintStream(outputStream);
        PrintStream errStream = new PrintStream(errorStream);
        String[] args;

        System.setOut(outStream);
        System.setErr(errStream);

        args = new String[]{"src/main/resources/CryptoTest.txt",
                "01/01/1999", "EST"};

        PeriodicExecutor.main(args);


        args = new String[]{"src/main/resources/CryptoTest.txt",
                "01-01-1999", "EST", "1000", "1"};
        PeriodicExecutor.main(args);
        args = new String[]{"src/main/resources/CryptoTest.txt",
                "01/01/1999", "AA2", "1000", "1"};
        PeriodicExecutor.main(args);

        String expectedOutput = "Usage: java PeriodicExecutor dataSource MM/dd/yyyy z interval(ms) repeat\n" +
                "E.g. java PeriodicExecutor dataFile.txt 06/20/2013 EDT 360000 2\n" +
                "E.g. java PeriodicExecutor DATABASE 06/20/2013 EDT 360000 2\n";

        String expectedErr =
                "Invalid date format or time zone code\n" +
                        "Invalid date format or time zone code\n";
        assertEquals(expectedOutput, outputStream.toString());

        args = new String[]{"src/main/resources/CryptoTest.txt",
                "01/01/1999", "UTC", "-1", "1"};
        PeriodicExecutor.main(args);
        args = new String[]{"src/main/resources/CryptoTest.txt",
                "01/01/1999", "UTC", "1000", "-1"};
        PeriodicExecutor.main(args);
        expectedErr += "Value of 'interval' must be positive!\n";
        expectedErr += "Value of 'repetition' must be positive!\n";

        assertEquals(expectedErr, errorStream.toString());

    }
}
