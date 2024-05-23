package edu.duke.projectTeam8;

public class XMLSerializerLeaf extends AbstractSerialObject {
  protected final Object content;

  public XMLSerializerLeaf(String header, Object content) {
    super(header);
    this.content = content;
  }
  
  @Override
  public String getHeader() {
    return header;
  }

  @Override
  public String contentToString() {
    if (content != null) {
      return content.toString();
    }
    else {
      return "null";
    }
  }
  
  @Override
  public String toString() {
    StringBuilder string = new StringBuilder("<");
    string.append(header);
    string.append(">");
    string.append(content);
    string.append("</");
    string.append(header);
    string.append(">");
    return string.toString();
  }

}
