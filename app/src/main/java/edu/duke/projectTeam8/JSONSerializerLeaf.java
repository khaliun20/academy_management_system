package edu.duke.projectTeam8;

public class JSONSerializerLeaf extends AbstractSerialObject {
  protected final Object content;

  public JSONSerializerLeaf(String header, Object content) {
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
      if (content.toString() == "true" || content.toString() == "false") {
        return content.toString();
      }
      else {
        return "\"" + content.toString() + "\"";
      }
    }
    else {
      return "null";
    }
  }

}
