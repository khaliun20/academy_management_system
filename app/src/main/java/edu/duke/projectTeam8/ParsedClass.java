package edu.duke.projectTeam8;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class ParsedClass {
  private final String className;
  private final Map<String, ParsedClass> parameters;
  private final String data;
  private Integer count; // A way to prevent duplicates in the HashMap (i.e. multiple Students)

  public ParsedClass(String className, String data) {
    this.className = className;
    this.data = data;
    this.parameters = new HashMap<>();
    this.count = 1; 
  }

  public void addParameter(String parameterName, ParsedClass parameter) {
    if (!parameters.keySet().contains(parameterName)) {
      parameters.put(parameterName, parameter);
    }
    else {
      count++;
      parameters.put(parameterName + count.toString(), parameter);
    }
  }

  public String getName() {
    return className;
  }

  public Set<String> getParameters() {
    return parameters.keySet();
  }

  public String getData() {
    return data;
  }

  public String getParameterData(String parameterName) {
    return parameters.get(parameterName).getData();
  }

  public ParsedClass getParameter(String parameterName) {
    return parameters.get(parameterName);
  }
  
}
