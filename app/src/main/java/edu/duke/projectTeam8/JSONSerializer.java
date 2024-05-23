package edu.duke.projectTeam8;

/**
 * Creates an object that converts object data to JSON
 */
public class JSONSerializer extends Serializer {
  private final AbstractSerialObject object;

  /**
   * Constructs a JSONSerializer wrapping null data
   */
  public JSONSerializer() {
    this.object = null;
  }

  /**
   * Constructs a JSONSerializer wrapping any Serial Object that extends AbstractSerialObject
   *
   * @param object is an Object that extends AbstractSerialObject to be wrapped
   */
  public JSONSerializer(AbstractSerialObject object) {
    this.object = object;
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder("{\n\t\"");
    string.append(object.getHeader());
    string.append("\": ");
    string.append(object.contentToString());
    string.append("\n}");
    return string.toString();
  }

  /**
   * Serializes an object from a header and its contents
   *
   * @param header is a String defining the JSON header for the content being serialized
   * @param object is an object with the content to be serialized
   * @return returns a string of the content in JSON format
   */
  public String serialize(String header, Object object) {
    String content = object.toString();
    StringBuilder string = new StringBuilder("{\"");
    string.append(header);
    string.append("\": ");
    if (content.charAt(0) != '{') {
      if (checkTreatAsString(object) == false) {
        string.append(content);
      }
      else {
        string.append("\"");
        string.append(content);
        string.append("\"");
      }
    }
    else {
      string.append("\n\t");
      string.append(content);
      string.append("\n");
    }
    string.append("}");
    return string.toString();
  }

  /**
   * Serializes an object from a header and its contents
   *
   * @param header is a String defining the JSON header for the content being serialized
   * @param objects are objects with the content to be serialized
   * @return returns a string of the content in JSON format
   */
  public String serialize(String header, String... objects) {
    StringBuilder string = new StringBuilder("{\n\t\"");
    string.append(header);
    string.append("\": {");
    Boolean notFirst = false;
    for (String object: objects) {
      if (notFirst) {
        string.append(",");
      }
      string.append("\n\t\t");
      string.append(object.substring(1, object.length() - 1));
      notFirst = true;
    }
    string.append("\n\t}\n}");
    return string.toString();
  }

  private Boolean checkTreatAsString(Object object) {
    String content = object.toString();
    if (content.equals("true") || content.equals(false)) {
      return false;
    }
    for (Integer i = 0; i < content.length(); i++) {
      if (Character.isLetter(content.charAt(i))) {
        return true;
      }
    }
    return false;
  }
}
