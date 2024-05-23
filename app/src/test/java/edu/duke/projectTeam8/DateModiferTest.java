package edu.duke.projectTeam8;

import org.junit.jupiter.api.Test;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateModiferTest {
    @Test
    public void testChangeDate() throws Exception {
        DateModifier dateModifier = new DateModifier();

        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:15:30");
        String expected1 = "Sun Apr 10 12:15:00 EDT 2022";
        assertEquals(expected1, dateModifier.changeDate(date1));

        Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:45:30");
        String expected2 = "Sun Apr 10 12:45:00 EDT 2022";
        assertEquals(expected2, dateModifier.changeDate(date2));

        Date date3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:00:00");
        String expected3 = "Sun Apr 10 12:00:00 EDT 2022";
        assertEquals(expected3, dateModifier.changeDate(date3));

        Date date4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:08:00");
        String expected4 = "Sun Apr 10 12:15:00 EDT 2022";
        assertEquals(expected4, dateModifier.changeDate(date4));

        Date date5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:16:00");
        String expected5 = "Sun Apr 10 12:15:00 EDT 2022";
        assertEquals(expected5, dateModifier.changeDate(date5));

        Date date6 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:29:59");
        String expected6 = "Sun Apr 10 12:30:00 EDT 2022";
        assertEquals(expected6, dateModifier.changeDate(date6));

        Date date7 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:59:59");
        String expected7 = "Sun Apr 10 13:00:00 EDT 2022";
        assertEquals(expected7, dateModifier.changeDate(date7));

        Date date8 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:45:59");
        String expected8 = "Sun Apr 10 12:45:00 EDT 2022";
        assertEquals(expected8, dateModifier.changeDate(date8));

        Date date9 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:52:30");
        String expected9 = "Sun Apr 10 12:45:00 EDT 2022";
        assertEquals(expected9, dateModifier.changeDate(date9));

        Date date10 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:53:00");
        String expected10 = "Sun Apr 10 13:00:00 EDT 2022";
        assertEquals(expected10, dateModifier.changeDate(date10));

        Date date11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-10 12:52:29");
        String expected11 = "Sun Apr 10 12:45:00 EDT 2022";
        assertEquals(expected11, dateModifier.changeDate(date11));
    }
}
