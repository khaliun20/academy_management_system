package edu.duke.projectTeam8;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class StudentView {

    private Student student;
    private StudentIOHandler ioHandler;
    boolean connectionActive;
    private Map<Integer, Course> orderedCourses;


    public static final int MAIN_MENU_OPTIONS = 3;
    public static final int PROFILE_MENU_OPTIONS = 2;
    public static final int COURSE_MENU_OPTIONS = 3;
    public static final int SUBSCRIPTION_OPTIONS = 2;


    public StudentView(Student student, StudentIOHandler ioHandler) throws Exception {
        this.student = student;
        this.ioHandler = ioHandler;
        this.connectionActive = true;
        addStudentCourses();
    }

    private void addStudentCourses() throws Exception {
        if (student.getClasses().size() == 0) {
            this.orderedCourses = null;
        }
        Set<Course> courses = student.getClasses();
        Map<Integer, Course> orderedCourses = new HashMap<>();
        int id = 1;
        for (Course course : courses) {
            orderedCourses.put(id++, course);
        }
        this.orderedCourses = orderedCourses;
    }


    public void showMainMenu() throws Exception {
        while (connectionActive) {
            int maxOptions = MAIN_MENU_OPTIONS;
            ioHandler.displayMainMenu(student.getNetID());
            String invalidMessage = "Invalid Selection. Please choose again\n";
            Integer selectedOption = ioHandler.readAndValidateInput(maxOptions, invalidMessage);
            switch (selectedOption) {
                case 1:
                    updateProfile();
                    break;
                case 2:
                    showCourses();
                    break;
                case 3:
                    stopProgram();
                    break;
            }
        }

    }

    public void updateProfile() throws Exception {
        while (true) {
            ioHandler.displayProfileOptions();
            String invalidMessage = "Invalid Selection. Please choose again\n";
            Integer selectedOption = ioHandler.readAndValidateInput(PROFILE_MENU_OPTIONS, invalidMessage);
            if (selectedOption.equals(0)) {

                break;
            }
            ProfileChanger profileChanger = new ProfileChanger(student);
            processProfileTask(selectedOption, profileChanger);

        }
    }

    private void processProfileTask(int selectedOption, ProfileChanger profileChanger) throws Exception {

        switch (selectedOption) {
            case 1:
                ioHandler.displayPasswordChangePrompt();
                String invalidPassword = "You didn't provide at least 8 character long password. Please choose again\n";
                String newPassword = ioHandler.readString(".{8,}", invalidPassword);
                profileChanger.changePassword(newPassword);
                if (newPassword.equals("0")) {
                    break;
                }

                break;
            case 2:

                String curPreferredName = student.toString();
                ioHandler.displayNameChangePrompt(curPreferredName);

                String invalidName = "Please enter valid name. Name must consist of letters only\n";
                String preferredName = ioHandler.readString("^[a-zA-Z]+$", invalidName);

                if (preferredName.equals("0")) {
                    break;
                }

                profileChanger.changePreferredName(preferredName);
                break;
            case 0:
                break;
        }
    }


    public void showCourses() throws Exception {
        boolean backToMainMenu = true;
        while (backToMainMenu) {
            if (orderedCourses == null) {
                ioHandler.noClassToShow();
                return;
            }
            ioHandler.displayAvailableCourses(student.toString(), orderedCourses); // passing orderedCourses so student can use that method too.
            int maxOptions = student.getClasses().size();
            String invalidMessage = "Invalid Selection. Please choose again\n";
            Integer selectedOption = ioHandler.readAndValidateInput(maxOptions, invalidMessage);
//            System.out.println(selectedOption);
            if (selectedOption.equals(0)) {
                break;
            }
            processCourseTask(selectedOption);

        }
    }

    private void processCourseTask(int selectedOption) throws Exception {
        boolean backToCourseMenu = true;
        while (backToCourseMenu) {

            Course selectedCourse = orderedCourses.get(selectedOption);
            String courseName = selectedCourse.toString();
            ioHandler.displayStudnetCourseTaskOptions(courseName);
            String invalidMessage = "Invalid Selection. Please choose again\n";
            int courseOption = ioHandler.readAndValidateInput(COURSE_MENU_OPTIONS, invalidMessage);
            switch (courseOption) {
                case 1:
                    viewCourseAttendanceRecord(selectedCourse);
                    break;
                case 2:
                    manageSubcriptionsToCourse(selectedCourse);
                    break;
                case 3:
                    receiveAttendanceEmails(selectedCourse);
                    break;

                case 0:
                    backToCourseMenu = false;
                    break;
            }

        }
    }

    private void viewCourseAttendanceRecord(Course course) throws Exception {
        boolean backToCourseMenu = true;
        while (backToCourseMenu) {
            AttendanceRecord attendanceRecord = student.getAttendanceRecord(course);
            String attendanceRecordString = attendanceRecord.getAllAttendanceRecordsAsString();
            String title = "Attendance Record for " + course.toString() + "\n";
            WeeklyTextSummary summarizer = new WeeklyTextSummary();
            String overallGrade = "\n Overall Grade: " + summarizer.computerCumulativeAttendanceScore(student, course) + "\n";
            ioHandler.printAttendanceReportForStudent(title, attendanceRecordString, overallGrade);
            ioHandler.readString("0", "Please type 0 to go back to the main menu\n");
            backToCourseMenu = false;
        }

    }

    private void receiveAttendanceEmails(Course course) throws Exception {
        boolean backToCourseMenu = false;

        while (!backToCourseMenu) {

            ioHandler.displayEmailPrompt();
            String invalidMessage = "Invalid Selection. Please choose again\n";
            Integer selectedOption = ioHandler.readAndValidateInput(2, invalidMessage);

            switch (selectedOption) {
                case 1:
                    sendEmail(course);
                    break;
                case 0:
                    backToCourseMenu = true;
                    break;
            }


        }

    }

    private void sendEmail(Course course) throws Exception {
        Database db = new MySQLDatabase();
        WeeklyTextSummary summarizer = new WeeklyTextSummary(db);
        String body = summarizer.summarizeRecordInDateRangeForSingleCourse(course, student, summarizer.strToDate("1/2/2000 12:00:00 EDT"), null, true);
        EmailSender emailSender = new EmailSender();
        emailSender.sendEmail(student.getEmail(), "Historical Attendance Report", body);
    }

    private void manageSubcriptionsToCourse(Course course) throws Exception {
        boolean backToCourseMenu = true;
        while (backToCourseMenu) {
            WeeklyReportSubChanger sub = new WeeklyReportSubChanger(student, course);
            ioHandler.displaySubscriptionStatus(sub.isSubscribed());
            int maxOptions = SUBSCRIPTION_OPTIONS;
            String invalidMessage = "Invalid Selection. Please choose again\n";
            Integer selectedOption = ioHandler.readAndValidateInput(maxOptions, invalidMessage);
            switch (selectedOption) {
                case 1:
                    sub.subscribe();
                    break;
                case 2:
                    sub.unsubscribe();
                    break;
                case 0:
                    backToCourseMenu = false;
                    break;
            }
        }

    }


    private void stopProgram() throws Exception {
        boolean isActive = ioHandler.stayConnected();
        connectionActive = isActive;
        if (connectionActive) {
            showMainMenu();
        }
    }


}
