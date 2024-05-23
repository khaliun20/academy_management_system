package edu.duke.projectTeam8;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;


public class PeriodicExecutor {
    private final EmailSender emailSender;
    private final int max_recurrence;
    private final long interval; //interval in milliseconds

    public PeriodicExecutor(long interval, int max_recurrence) {
        this.emailSender = new EmailSender();
        this.interval = interval;
        this.max_recurrence = max_recurrence;
    }

    @NonNull
    public String recurrentEmailSend(String sourceFile, WeeklyTextSummary summary,
                                     Date day0, String defaultAddress) throws Exception {
        AcademicEnrollment academicEnrollment = null;
        if ("DATABASE".equalsIgnoreCase(sourceFile)) {
            academicEnrollment = new DatabaseAcademicEnrollment(new MySQLDatabase());

        } else if (sourceFile.endsWith(".txt")) {

            academicEnrollment = new AcademicEnrollmentV1(sourceFile);

        } else {
            throw new Exception("Unsupported dataSource.");
        }

        return recurrentEmailSend(academicEnrollment, summary, day0, defaultAddress);
    }

    public String recurrentEmailSend(AcademicEnrollment academicEnrollment, WeeklyTextSummary summary,
                                     Date day0, String defaultAddress)
            throws RuntimeException {

        int loopCount = 0;
        Date startDate = day0;

        while (true) {
            try {
                Date current = new Date();

                for (Student s : academicEnrollment.getStudents()) {
                    String studentName = s.toString();
                    String summaryText = summary.summarizeRecordInDateRange(s, startDate, current, true);
                    sendEmail(defaultAddress == null ? s.getEmail() : defaultAddress,
                            studentName + " Attendance Report", summaryText);
                }
                loopCount++;
                if (loopCount >= max_recurrence) {
                    break;
                }
                startDate = current;
                Thread.sleep(this.interval);

            } catch (InterruptedException e) {
                // Handle interrupted exception
                return "Program terminated due to user interrupt.";
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return "Program terminated after " + loopCount + " iterations.";
    }

    public void sendEmail(String recipientAddress, String title, String content)
            throws IOException {
        emailSender.sendEmail(recipientAddress, title, content);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 5) {
            System.out.println("Usage: java PeriodicExecutor dataSource MM/dd/yyyy z interval(ms) repeat");
            System.out.println("E.g. java PeriodicExecutor dataFile.txt 06/20/2013 EDT 360000 2");
            System.out.println("E.g. java PeriodicExecutor DATABASE 06/20/2013 EDT 360000 2");

            return;
        }
        int interval = -1, repetition = -1;

        try {
            interval = Integer.parseInt(args[3]);
            repetition = Integer.parseInt(args[4]);

            if (interval <= 0)
                throw new IllegalArgumentException("Value of 'interval' must be positive!");

            if (repetition <= 0)
                throw new IllegalArgumentException("Value of 'repetition' must be positive!");

        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        PeriodicExecutor pe = new PeriodicExecutor(interval, repetition);
        WeeklyTextSummary summary = new WeeklyTextSummary(
                new SimpleDateFormat("MM/dd/yyyy hh:mm:ss z"), args[2]);
        try {
            Date startTime = summary.strToDate(args[1] + " 00:00:00 " + args[2]);
            String outcome = pe.recurrentEmailSend(args[0], summary, startTime, "yz696@duke.edu");
            System.out.println(outcome);
        } catch (ParseException e) {
            System.err.println("Invalid date format or time zone code");
        }

    }

}

