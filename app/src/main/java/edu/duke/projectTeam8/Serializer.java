package edu.duke.projectTeam8;

public class Serializer {
  public static final String classSepToken = "<#CLASS#>";
  public static final String objectSepToken= "<#OBJ#>";

  public static String  wrapString(String header, String content){
    return "<"+header+">"+content+"</"+header+">";
  }
  
  public static void SerializeCourse(Course course){
    
  }


}
