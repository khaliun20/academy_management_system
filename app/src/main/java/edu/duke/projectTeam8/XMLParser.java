package edu.duke.projectTeam8;

public class XMLParser {

  public XMLParser() {
  }

  public String parse3(String data) {
    Integer headerLocation = data.indexOf("<");
    Integer headerLocationEnd = data.indexOf(">");
    String className = data.substring(headerLocation + 1, headerLocationEnd);
    return className;
  }

  public String parse2(String data) {
    Integer headerLocation = data.indexOf("<");
    Integer headerLocationEnd = data.indexOf(">");
    String className = data.substring(headerLocation + 1, headerLocationEnd);
    StringBuilder tagBuilder1 = new StringBuilder("<");
    tagBuilder1.append(className);
    tagBuilder1.append(">");
    String start = tagBuilder1.toString();
    StringBuilder tagBuilder2 = new StringBuilder("</");
    tagBuilder2.append(className);
    tagBuilder2.append(">");
    String end = tagBuilder2.toString();
    return data.substring(data.indexOf(start) + start.length(), data.indexOf("</" + className + ">"));
  }

  public ParsedClass parse(String data){
    Integer headerLocation = data.indexOf("<");
    Integer headerLocationEnd = data.indexOf(">");
    String className = data.substring(headerLocation + 1, headerLocationEnd);
    StringBuilder tagBuilder1 = new StringBuilder("<");
    tagBuilder1.append(className);
    tagBuilder1.append(">");
    String start = tagBuilder1.toString();
    StringBuilder tagBuilder2 = new StringBuilder("</");
    tagBuilder2.append(className);
    tagBuilder2.append(">");
    String end = tagBuilder2.toString();
    String subData = data.substring(data.indexOf(start) + start.length(), data.indexOf(end));
    ParsedClass parsedClass = new ParsedClass(className, subData);
    parseHelper(subData, parsedClass);
    return parsedClass;
  }

  private void parseHelper(String data, ParsedClass parsedClass) {
    Integer headerLocation = data.indexOf("<");
    if (headerLocation >= 0) {
      Integer headerLocationEnd = data.indexOf(">");
      String className = data.substring(headerLocation + 1, headerLocationEnd);
      StringBuilder tagBuilder1 = new StringBuilder("<");
      tagBuilder1.append(className);
      tagBuilder1.append(">");
      String start = tagBuilder1.toString();
      StringBuilder tagBuilder2 = new StringBuilder("</");
      tagBuilder2.append(className);
      tagBuilder2.append(">");
      String end = tagBuilder2.toString();
      String subData = data.substring(data.indexOf(start) + start.length(), data.indexOf(end));
      ParsedClass subClass = new ParsedClass(className, subData);
      parsedClass.addParameter(className, subClass);
      parseHelper(subData, subClass);
      parseHelper(data.substring(data.indexOf(end) + end.length()), parsedClass);
    }
  }
}
