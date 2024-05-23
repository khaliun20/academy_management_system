package edu.duke.projectTeam8;

import java.util.Set;


/**
 * This is the master list of all of the enrolled students at the school
 */
public interface AcademicEnrollment {
 

    public Student getStudentByNetID(String netID) throws Exception;

 
    public Set<Student> getStudents() throws Exception;

    public Professor findProfessorInDirectory(String professorNetID) throws Exception   ;
    
    public Boolean isStudent(User user) throws Exception;
    public Database getDatabase();


  
   
  
   

    
}
