package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseAttendanceRecordTest {
    MySQLDatabase db = new MySQLTestDatabase();
    WeeklyTextSummary summarizer = new WeeklyTextSummary();
    Professor lo = db.getProfessor("lo110");
    Student john = db.getStudent("jd102");
    Course course = new DatabaseCourse("ECE100-001", lo, db);

    DatabaseAttendanceRecordTest() throws Exception {
    }

    @Test
    public void test_add_record() throws Exception {

        Date date = summarizer.strToDate("10/22/2023 10:22:00 EDT");
        Date date1 = summarizer.strToDate("10/22/2023 12:34:56 EDT");
        DatabaseAttendanceRecord record = new DatabaseAttendanceRecord(john, course, db);
        Attendance attendance = new Attendance();
        attendance.markAttended();
        Attendance attendance1 = new Attendance();
        attendance1.markTardy();
        record.addRecord(date, attendance);
        assertEquals(record.getAttendanceForDate(date).toString(),"Attended");
        record.modifyRecord(date,Attendance::markAbsent);

        assertEquals(record.getAttendanceForDate(date).toString(),"Absent");
        record.addRecord(date1, attendance1);

        assertEquals(record.getAttendanceForDate(date1).toString(),"Tardy");
        HashSet<Date> expected = new HashSet<>();
        expected.add(date);
        expected.add(date1);
        assertIterableEquals(record.getValidDates(),expected);

    }
}