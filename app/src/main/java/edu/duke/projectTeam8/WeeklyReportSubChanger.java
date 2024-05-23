package edu.duke.projectTeam8;
import java.sql.SQLException;

public class WeeklyReportSubChanger {

    private Student student;
    private Course course;
    private Boolean isSubscribed;

    public WeeklyReportSubChanger( Student student, Course course) throws Exception{
        this.course = course;
        this.student = student;
        this.isSubscribed = student.isSubscribedToWeekyReport(course);
    }   

    public void subscribe() throws Exception {
        student.subscribeToWeeklyReport(course);
        isSubscribed = true;
    }

    public void unsubscribe() throws Exception{
        student.unsubscribeToWeeklyReport(course);
        isSubscribed = false;
    }   


    public Boolean isSubscribed() {
        return isSubscribed;

    }

    
}
