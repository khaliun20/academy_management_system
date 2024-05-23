package edu.duke.projectTeam8;

import java.util.Map;
import java.util.HashMap;

/**
 * This object defines Attendance by using a Set of acceptable statuses for
 * Attendance
 */
public class Attendance {
    private Boolean attended;
    private Boolean tardy;
    private static final Map<Integer, String> valueToString = new HashMap<>();
    private static final Map<String, Integer> StringToScore = new HashMap<>();

    /**
     * This is a static block that initializes the valueToString map
     * with the acceptable statuses for Attendance
     * 0: Absent
     * 1: Attended
     * 2: Tardy
     */
    static {
        valueToString.put(0, "Absent");
        valueToString.put(1, "Attended");
        valueToString.put(2, "Tardy");
        StringToScore.put("Absent", 0);
        StringToScore.put("Attended", 100);
        StringToScore.put("Tardy", 80);

    }

    public static int mapStrToScore(String status) {
        return StringToScore.get(status);
    }

    /**
     * Creates an Attendance with null values
     */
    public Attendance() {
        attended = null;
        tardy = null;
    }

    /**
     * Returns a Boolean defining the attendance status
     *
     * @return a Boolean defining the attendance status
     */
    public Boolean attended() {
        if (attended != null && tardy != null) {
            return attended;
        } else {
            throw new IllegalStateException("Attendance was not initialized");
        }
    }

    /**
     * Returns a Boolean defining the attendance status
     *
     * @return a Boolean defining the attendance status
     */
    public Boolean wasTardy() {
        if (tardy != null && attended != null) {
            return tardy;
        } else {
            throw new IllegalStateException("Attendance was not initialized");
        }
    }

    /**
     * Returns the sum of the booleans
     *
     * @return the sum of the booleans
     */
    private Integer sumBooleans() {
        if (attended == null || tardy == null) {
            throw new IllegalStateException("Attendance was not initialized");
        }
        if (attended && tardy) {
            return 2;
        } else if (attended || tardy) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns a String representation of the Attendance
     *
     * @return a String representation of the Attendance
     */
    @Override
    public String toString() {
        return valueToString.get(sumBooleans());
    }

    /**
     * Marks the attendance to Absent
     */
    public void markAbsent() {
        attended = false;
        tardy = false;
    }

    /**
     * Marks the attendance to Tardy
     */
    public void markTardy() {
        attended = true;
        tardy = true;
    }

    /**
     * Marks the attendance to In Attendance
     */
    public void markAttended() {
        attended = true;
        tardy = false;
    }

}
