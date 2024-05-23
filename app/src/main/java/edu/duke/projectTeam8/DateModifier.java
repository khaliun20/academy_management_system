package edu.duke.projectTeam8;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateModifier {


    public DateModifier(){

    }


    public String changeDate (Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int minutes = calendar.get(Calendar.MINUTE);
        int roundedMinutes = Math.round((float) minutes / 15) * 15;
        if (roundedMinutes == 60) { // If the minutes are 60, then add an hour
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.MINUTE, 0);
        } else {
            calendar.set(Calendar.MINUTE, roundedMinutes);
        }

        calendar.set(Calendar.SECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        return dateFormat.format(calendar.getTime());
    }


    
}
