        package edu.duke.projectTeam8;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.Date;

public class WeeklyTextSummaryTest {
    WeeklyTextSummary summarizer = new WeeklyTextSummary();
    OldStudent john = new OldStudent(new Name("John", "Smith"), "jd101", "jd101@duke.edu");
    OldCourse c = new OldCourse(new CourseID("ECE", 110));
    private boolean initialized = false;

    private void initialization() throws Exception {
        if (initialized)
            return;
        Date date = summarizer.strToDate("10/22/2023 10:22:00 EDT");
        Date date1 = summarizer.strToDate("10/22/2023 12:34:56 EDT");
        c.enrollStudent(john);
        OldLecture l = new OldLecture(c, date);
        c.addLecture(l);
        OldLecture l1 = new OldLecture(c, date1);
        c.addLecture(l1);
        AttendanceTaker at = new AttendanceTaker(john, l, 3);
        at.initalUpdateToStudentAttendanceRecord();

        AttendanceTaker at1 = new AttendanceTaker(john, l1, 2);
        at1.initalUpdateToStudentAttendanceRecord();
        initialized = true;
    }

    @Test
    public void test_name() throws Exception {
        initialization();
        Date startDate = summarizer.strToDate("10/20/2023 00:00:00 EDT");
        Date endDate = summarizer.strToDate("10/25/2023 00:00:00 EDT");


        String out = summarizer.summarizeRecordInDateRange(john,
                startDate, endDate, false);
        String expected = "Attendance summary for John Smith\n" +
                "\n" +
                "From 10/20/2023 00:00:00 EDT to 10/25/2023 00:00:00 EDT\n" +
                "*******BEGIN OF SUMMARY*********\n" +
                "----------\n" +
                "Course: ECE110\n" +
                "10/22/2023 10:22:00 EDT: Absent\n" +
                "10/22/2023 12:34:56 EDT: Tardy\n" +
                "----------\n" +
                "******END OF SUMMARY******\n";

        assertEquals(expected, out);

        out = summarizer.summarizeRecordInDateRange(john,
                startDate, summarizer.strToDate("10/22/2023 11:00:00 EDT"), false);

        expected = "Attendance summary for John Smith\n" +
                "\n" +
                "From 10/20/2023 00:00:00 EDT to 10/22/2023 11:00:00 EDT\n" +
                "*******BEGIN OF SUMMARY*********\n" +
                "----------\n" +
                "Course: ECE110\n" +
                "10/22/2023 10:22:00 EDT: Absent\n" +
                "----------\n" +
                "******END OF SUMMARY******\n";
        assertEquals(expected, out);

        out = summarizer.summarizeRecordInDateRange(john,
                null, summarizer.strToDate("10/22/2023 11:00:00 EDT"), false);

        expected = "Attendance summary for John Smith\n" +
                "\n" +
                "From ---start of semester--- to 10/22/2023 11:00:00 EDT\n" +
                "*******BEGIN OF SUMMARY*********\n" +
                "----------\n" +
                "Course: ECE110\n" +
                "10/22/2023 10:22:00 EDT: Absent\n" +
                "----------\n" +
                "******END OF SUMMARY******\n";
        assertEquals(expected, out);


        expected = "Attendance summary for John Smith\n" +
                "\n" +
                "From ---start of semester--- to 10/25/2023 00:00:00 EDT\n" +
                "*******BEGIN OF SUMMARY*********\n" +
                "----------\n" +
                "Course: ECE110\n" +
                "10/22/2023 10:22:00 EDT: Absent\n" +
                "10/22/2023 12:34:56 EDT: Tardy\n" +
                "----------\n" +
                "******END OF SUMMARY******\n";

        out = summarizer.summarizeRecordInDateRange(john,
                null, null, false);
        int id1 = expected.indexOf("to ") + 3;
        int id2 = expected.indexOf("EDT") + 3;

        assertEquals(out.substring(0, id1), expected.substring(0, id1));
        assertEquals(out.substring(id2), expected.substring(id2));
    }

    @Test
    public void test_score() throws Exception {
        initialization();

        Date startDate = summarizer.strToDate("10/20/2023 00:00:00 EDT");
        Date endDate = summarizer.strToDate("10/25/2023 00:00:00 EDT");

        String out = summarizer.summarizeRecordInDateRange(john, startDate, endDate);
        String expected = "Attendance summary for John Smith\n" +
                "\n" +
                "From 10/20/2023 00:00:00 EDT to 10/25/2023 00:00:00 EDT\n" +
                "*******BEGIN OF SUMMARY*********\n" +
                "----------\n" +
                "Course: ECE110\n" +
                "10/22/2023 10:22:00 EDT: Absent\n" +
                "10/22/2023 12:34:56 EDT: Tardy\n" +
                "Cumulative attendance score: 40.00\n" +
                "----------\n" +
                "******END OF SUMMARY******\n";

        assertEquals(expected, out);

        out = summarizer.summarizeRecordInDateRange(john, endDate, endDate);
        expected = "Attendance summary for John Smith\n" +
                "\n" +
                "From 10/25/2023 00:00:00 EDT to 10/25/2023 00:00:00 EDT\n" +
                "*******BEGIN OF SUMMARY*********\n" +
                "----------\n" +
                "Course: ECE110\n" +
                "Cumulative attendance score: 40.00\n" +
                "----------\n" +
                "******END OF SUMMARY******\n";

        assertEquals(expected, out);


    }
}
