package edu.duke.projectTeam8;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Converts a Class Object into JSON by wrapping its member variable data, which must already be converted to a JSONSerializer
 */
public class JSONSerializerClass extends AbstractSerialObject {
  protected final Collection<Object> children;

  /**
   * Constructs a JSON Serializer Class from the header that defines the class and its children
   *
   * @param header is a string defining the Class
   * @param children are the Objects that define the JSON serialized children
   */
  public JSONSerializerClass(String header, Object... children) {
    super(header);
    this.children = new ArrayList<>();
    for (Object child: children) {
      this.children.add(child);
    }
  }

  @Override
  public String getHeader() {
    return header;
  }

  @Override
  public String contentToString() {
    StringBuilder string = new StringBuilder("{");
    Boolean notFirst = false;
    for (Object child: children) {
      if (notFirst) {
        string.append(",");
      }
      String[] lines = child.toString().split("\n");
      for (Integer i = 1; i < lines.length - 1; i++) {
        string.append("\n\t");
        string.append(lines[i]);
      }
      notFirst = true;
    }
    string.append("\n\t}");
    return string.toString();
  }
}
