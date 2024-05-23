package edu.duke.projectTeam8;

import java.util.Objects;
import java.util.Optional;

/**
 * This class represents a course ID, which is a unique identifier for a course
 */
public class CourseID {
    private final String subject;
    private final Integer code;
    private final Optional<String> section;

    /**
     * Creates a CourseID object with a subject and course code
     */
    public CourseID(String subject, Integer code) {// expected format: ECE651-001 where 001 is optional
        this.subject = subject;
        this.code = code;
        this.section = Optional.empty(); // No section by default
    }

    /**
     * Creates a CourseID object with a subject, course code, and section
     */
    public CourseID(String subject, Integer code, String section) {
        this.subject = subject;
        this.code = code;
        this.section = Optional.ofNullable(section);
    }

    /**
     * Returns the subject of the course
     * 
     * @return the subject of the course
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Returns the course code
     * 
     * @return the course code
     */
    public Integer getNumber() {
        return code;
    }

    /**
     * Returns the section of the course
     * 
     * @return the section of the course
     */
    public Optional<String> getSection() {
        return section;
    }

    /**
     * Returns the string of the full course ID
     * 
     * @return the full course ID
     */
    @Override
    public String toString() {
        return subject + code + section.map(s -> "-" + s).orElse("");
    }

    /**
     * Compares two CourseID objects
     * 
     * @param other is the object to compare
     * @return true if the CourseID objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        CourseID courseID = (CourseID) other;
        return Objects.equals(code, courseID.code) &&
                Objects.equals(subject, courseID.subject) &&
                Objects.equals(section, courseID.section);
    }

    /**
     * Returns a hash code value for the CourseID
     * 
     * @return a hash code value for the CourseID
     */
    @Override
    public int hashCode() {
        return Objects.hash(subject, code, section);
    }
}
