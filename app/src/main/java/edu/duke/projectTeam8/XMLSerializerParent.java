package edu.duke.projectTeam8;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.function.BiFunction;

public class XMLSerializerParent<T, U> extends AbstractSerialObject {
  protected final Collection<XMLSerializer> children;

  public XMLSerializerParent(String header, XMLSerializer... children) {
    super(header);
    this.children = new ArrayList<>();
    for (XMLSerializer child: children) {
      this.children.add(child);
    }
  }

  public XMLSerializerParent(String header, Iterable<XMLSerializer> children) {
    super(header);
    this.children = new ArrayList<>();
    for (XMLSerializer child: children) {
      this.children.add(child);
    }
  }

  public XMLSerializerParent(String header, T object, Map<String, Function<T, Object>> dataMap) {
    super(header);
    this.children = new ArrayList<>();
    for (String string: dataMap.keySet()) {
      this.children.add(new XMLSerializer(string, dataMap.get(string).apply(object)));
    }
  }

  @Override
  public String getHeader() {
    return header;
  }
  
  @Override
  public String toString() {
    StringBuilder string = new StringBuilder("<");
    string.append(header);
    string.append(">");
    for (Object child: children) {
      for (String line: child.toString().split("\n")) {          
        string.append("\n");
        string.append("\t");
        string.append(line);
      }
    }
    string.append("\n");
    string.append("</");
    string.append(header);
    string.append(">");
    return string.toString();
  }

  @Override
  public String contentToString() {
    StringBuilder string = new StringBuilder();
    for (Object child: children) {
      for (String line: child.toString().split("\n")) {          
        string.append("\n");
        string.append("\t");
        string.append(line);
      }
    }
    string.append("\n");
    return string.toString();
  }

}
