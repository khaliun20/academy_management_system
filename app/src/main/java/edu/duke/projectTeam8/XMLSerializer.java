package edu.duke.projectTeam8;

import java.util.Map;
import java.util.function.Function;

/**
 * Serializes an object into XML
 */
public class XMLSerializer<T> extends Serializer {
  protected final AbstractSerialObject object;

  /**
   * Constructs an XML Serializer
   *
   * @param object is an AbstractSerialObject that will be wrapped
   */
  public XMLSerializer(AbstractSerialObject object) {
    this.object = object;
  }

  public XMLSerializer() {
    this.object = null;
  }

  public XMLSerializer(String header, Object object) {
    this(new XMLSerializerLeaf(header, object));
  }

  public XMLSerializer(String header, XMLSerializer... children) {
    this(new XMLSerializerParent(header, children));
  }

  public XMLSerializer(String header, Iterable<XMLSerializer> children) {
    this(new XMLSerializerParent(header, children));
  }


  public XMLSerializer(String header, T object, Map<String, Function<T, Object>> dataMap) {
    this(new XMLSerializerParent(header, object, dataMap));
  }

  
  @Override
  public String toString() {
    StringBuilder string = new StringBuilder("<");
    String header = object.getHeader();
    string.append(header);
    string.append(">");
    string.append(object.contentToString());
    string.append("</");
    string.append(header);
    string.append(">");
    return string.toString();
  }
  

  /**
   * Serializes an object from a header and its contents
   *
   * @param header is a String defining the XML header for the content being serialized
   * @param object is an object with the content to be serialized
   * @return returns a string of the content in XML format
   */
  public String serialize(String header, Object object) {
    String content = object.toString();
    StringBuilder string = new StringBuilder("<");
    string.append(header);
    string.append(">");
    if (content.charAt(0) != '<') {
      string.append(content);
    }
    else {
      string.append("\n\t");
      string.append(content);
      string.append("\n");
    }
    string.append("</");
    string.append(header);
    string.append(">");
    return string.toString();
  }

  /**
   * Serializes an object from a header and its contents
   *
   * @param header is a String defining the XML header for the content being serialized
   * @param objects are objects with the content to be serialized
   * @return returns a string of the content in XML format
   */
  public String serialize(String header, Object... objects) {
    StringBuilder string = new StringBuilder("<");
    string.append(header);
    string.append(">");
    for (Object object: objects) {
      string.append("\n");
      string.append("\t");
      string.append(object.toString());
    }  
    string.append("\n</");
    string.append(header);
    string.append(">");
    return string.toString();
  }
}
