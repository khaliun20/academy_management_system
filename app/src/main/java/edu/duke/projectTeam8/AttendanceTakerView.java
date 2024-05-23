package edu.duke.projectTeam8;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;


public class AttendanceTakerView {

    private ProfessorIOHandler ioHandler;
    public static final int COURSE_MAIN_TAB_OPTIONS = 2;
    public static final int ATTENDANCE_STATUS_OPTIONS = 3;

    public AttendanceTakerView(ProfessorIOHandler ioHandler) {
        this.ioHandler = ioHandler;
    }


    public void displayTodaysAttendance(Course course) throws Exception {
        Lecture newLecture = new DatabaseLecture(course, new Date(), new MySQLDatabase());
        takeAttendanceAtStartOfLecture(course, newLecture);

    }

    /**
     * Takes attendance for enrolled students at the start of a lecture.
     *
     * @param course  represents the course the teacher is taking attendance for
     * @param lecture represents single class meeting in which student's attendance
     *                record is being changed
     * @throws Exception if the attendance status is invalid
     */
    public void takeAttendanceAtStartOfLecture(Course course, Lecture lecture) throws Exception {
        Set<Student> enrolledStudents = course.getActiveEnrollment();
        int maxOption = ATTENDANCE_STATUS_OPTIONS;

        for (Student student : enrolledStudents) {

            ioHandler.printStudentAttendancePrompt(student);
            String invalidMessage = "Invalid attendance type. Please take attendance again for " + student.toString() + "\n"
                    + "1. Attended\n" + "2. Tardy\n" + "3. Absent\n";
            int selectedOption = ioHandler.readAndValidateInputWithNoBack(maxOption, invalidMessage);

            AttendanceTaker attendanceTaker = new AttendanceTaker(student, lecture, selectedOption);
            attendanceTaker.initalUpdateToStudentAttendanceRecord();

        }
    }


    public void displayAnotherDayAttendance(Course course) throws Exception {

        ioHandler.printDatePrompt();


        String date = ioHandler.readString(".*", "Invalid date. Please enter a valid date format");
        if (date.equals("0")) {
            return;
        }

        Date newDate = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy").parse(date);
        Lecture newLecture = new DatabaseLecture(course, newDate, new MySQLDatabase());
        takeAttendanceAtStartOfLecture(course, newLecture);

    }

    public void displayModifyAttendance(Course course) throws Exception {

        if (course.getLecturesList().isEmpty()) {
            ioHandler.displayNoAttendanceSheetsToModify();
            String invalidMessage = "Invalid Selection. Please choose again";
            ioHandler.readAndValidateInput(1, invalidMessage);

        } else {
            while (true) {
                ioHandler.printOldAttendanceSheetsPrompt(course);
                int maxOptions = course.getLecturesList().size();
                String invalidMessage = "Invalid Selection. Please choose again";
                int selected = ioHandler.readAndValidateInput(maxOptions, invalidMessage);
                if (selected == 0) {
                    break;
                }
                Lecture oldLecture = course.getLecturesList().get(selected - 1);
                takeAttendanceAfterLecture(course, oldLecture);
            }
        }

    }


    /**
     * This method lets the teacher change the attendance status for a student by
     * searching by their netID
     *
     * @param course  represents the course the teacher is taking attendance for
     * @param lecture represents single class meeting in which student's attendance
     *                record is being changed
     * @throws Exception if the attendance status is invalid
     */
    public void takeAttendanceAfterLecture(Course course, Lecture lecture) throws Exception {
        ioHandler.printStudentToSearchPrompt(course);

        Student student = ioHandler.readAndValidateStudent(course, true);
        if (student == null) {
            return;
        }
        String oldStatus = lecture.getAttendanceOfStudent(student.getNetID()).toString();
        ioHandler.printOldStatusAndAttendancePrompt(lecture, student);
        String invalidMessage = "Invalid attendance type. Please take attendance again for " + student.toString() + "\n"
                + "1. Attended\n" + "2. Tardy\n" + "3. Absent\n";
        int selectedOption = ioHandler.readAndValidateInputWithNoBack(ATTENDANCE_STATUS_OPTIONS, invalidMessage);
        AttendanceTaker attendanceTaker = new AttendanceTaker(student, lecture, selectedOption);
        attendanceTaker.modifyStudentAttendanceRecord();

        // add email notifier to the student
        String message = "For course: " + course.getCourseID() + "," + "your attendance status for lecture on "
                + changeDate(lecture.getDate()) + " has been changed to "
                + lecture.getAttendanceOfStudent(student.getNetID()).toString() + "from" + oldStatus + "\n";
        Notification notification = new AttendanceStatusChangeNotification(message, student);
        try {
            notification.notifyObservers();
        } catch (Exception e) {
            ioHandler.printEmailSenderror();
        }
    }


    private String changeDate(Date date) {
        return ioHandler.changeDate(date);
    }

}
