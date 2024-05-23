package edu.duke.projectTeam8;

/**
 * Creates a Status object which defines if a student is currently enrolled
 */
public class Status {
  private Boolean enrolled;

  /**
   * Creates a Status object from a Boolean defining whether or not the student is currently enrolled
   *
   * @param enrolled is a Boolean defining if a student is currently enrolled
   */
  public Status(Boolean enrolled) {
    this.enrolled = enrolled;
  }

  /**
   * Returns a Boolean defining the student's current enrollment status
   *
   * @return a Boolean which is True if the student is currently enrolled and false otherwise
   */
  public Boolean getStatus() {
    return enrolled;
  }

  /**
   * Modifies a Student's enrollment status
   *
   * @param newStatus is a Boolean defining the new enrollment status of the student
   */
  public void modifyStatus(Boolean newStatus) {
    this.enrolled = newStatus;
  }

  @Override
  public String toString() {
    if (enrolled) {
      return "Enrolled";
    }
    else {
      return "Not Enrolled";
    }
  }

  @Override
  public int hashCode() {
    if (enrolled) {
      return 1;
    }
    else {
      return 0;
    }
  }

  @Override
  public boolean equals(Object other) {
    if (other != null && other.getClass().equals(getClass())) {
      Status otherStatus = (Status) other;
      return enrolled == otherStatus.enrolled;
    }
    return false;
  }

}
