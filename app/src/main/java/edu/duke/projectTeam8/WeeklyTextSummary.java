package edu.duke.projectTeam8;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeeklyTextSummary {
    private final Database db;
    private final SimpleDateFormat dateFormat;

    public WeeklyTextSummary(SimpleDateFormat dateFormat, String timeZoneCode, Database db) {
        this.dateFormat = dateFormat;
        this.dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneCode));
        this.db = db;
    }

    public WeeklyTextSummary(Database db) {
        this(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z"), "EDT", db);
    }

    public WeeklyTextSummary(SimpleDateFormat dateFormat, String timeZoneCode) {
        this(dateFormat, timeZoneCode, null);
    }

    public WeeklyTextSummary() {
        this(null);
    }

    public Date strToDate(String date) throws ParseException {
        return dateFormat.parse(date);
    }


    private List<Date> sortDates(Iterable<Date> dates) {
        List<Date> temp = new LinkedList<>();
        for (Date date : dates)
            temp.add(date);
        Collections.sort(temp);
        return temp;
    }

    private String getStartHeader(Student student, Date startDate, Date endDate)
        throws SQLException {

        StringBuilder sb = new StringBuilder();
        String startStr = startDate == null ? "---start of semester---" : dateFormat.format(startDate);
        sb.append("Attendance summary for " + student.getName() + "\n\n");
        sb.append("From " + startStr +
                " to " + dateFormat.format(endDate) + "\n");
        sb.append("*******BEGIN OF SUMMARY*********\n");
        return sb.toString();
    }

    private String getAttendanceTextInDateRange(AttendanceRecord record, Course course, Date startDate, Date endDate) throws Exception{
        StringBuilder sb = new StringBuilder();

        sb.append("Course: " + course.toString() + "\n");
        List<Date> sortedDates = sortDates(record.getValidDates());
        for (Date date : sortedDates) {
            if ((startDate != null && date.before(startDate)) ||
                    date.after(endDate))
                continue;
            String status = record.getAttendanceForDate(date).toString();
            sb.append(dateFormat.format(date) + ": " + status + "\n");
        }
        return sb.toString();
    }

    private String getAllCourseRecordInDateRange(Student student, Date startDate,
                                Date endDate, boolean displayScore) throws Exception{
        StringBuilder sb = new StringBuilder();
        for (Course course : student.getClasses()) {
            sb.append(getSingleCourseRecordInDateRange(course,student,startDate,endDate,displayScore));
        }
        return sb.toString();
    }

    private String getSingleCourseRecordInDateRange(Course course, Student student, Date startDate, Date endDate, boolean displayScore) throws Exception{
        StringBuilder sb = new StringBuilder();
        AttendanceRecord attendanceRecord = student.getAttendanceRecord(course);
        String textRecord = attendanceRecord.getAllAttendanceRecordsAsString();
        sb.append("----------\n");
        sb.append(getAttendanceTextInDateRange(attendanceRecord, course, startDate, endDate));
        if (displayScore) {
            sb.append("Cumulative attendance score: ");
            sb.append(String.format("%.2f", computerCumulativeAttendanceScore( student, course)) + "\n");
        }
        sb.append("----------\n");
       
        return sb.toString();
    }

    private String stringCat(String... paragraphs) {
        StringBuilder sb = new StringBuilder();
        for (String str : paragraphs) {
            sb.append(str);
        }
        return sb.toString();
    }

    private String endOfSummary() {
        return "******END OF SUMMARY******\n";
    }

    public double computerCumulativeAttendanceScore(Student student, Course course)
        throws Exception {

        int earnedScore = 0;
        int numRecords = 0;

        AttendanceRecord attendanceRecord = student.getAttendanceRecord(course);
        String rawText = attendanceRecord.getAllAttendanceRecordsAsString();
        if (rawText != null) {
           
            Pattern pattern = Pattern.compile("[A-Z]{3} \\d{4} : (.*?)\\n");
            Matcher matcher = pattern.matcher(rawText);
            while (matcher.find()) {
                numRecords++;
                String match = matcher.group(1);
                earnedScore += Attendance.mapStrToScore(match);
            }
        }
        return (double) earnedScore / (double) numRecords;

    }


    public String summarizeRecordInDateRange(Student student, Date startDate,
                            Date endDate, boolean displayScore) throws Exception {
        if (endDate == null)
            endDate = new Date();

        String header = getStartHeader(student, startDate, endDate);
        String textRecord = getAllCourseRecordInDateRange(student, startDate, endDate, displayScore);

        String eos = endOfSummary();
        return stringCat(header, textRecord, eos);
    }

    public String summarizeRecordInDateRange(Student student, Date startDate,
                                Date endDate) throws Exception{
        return summarizeRecordInDateRange(student,startDate,endDate,true);
    }

    public String summarizeRecordInDateRangeForSingleCourse(Course course, Student student, Date startDate,
    Date endDate, boolean displayScore) throws Exception {
        if (endDate == null)
        endDate = new Date();

        String header = getStartHeader(student, startDate, endDate);
        String textRecord = getSingleCourseRecordInDateRange( course, student, startDate, endDate, displayScore);

        String eos = endOfSummary();
        return stringCat(header, textRecord, eos);
}

    
}
