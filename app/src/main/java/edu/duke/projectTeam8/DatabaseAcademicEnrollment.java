package edu.duke.projectTeam8;
import java.util.Set;   
  

public class DatabaseAcademicEnrollment implements AcademicEnrollment{
    Database database; 

    public DatabaseAcademicEnrollment(Database database){
        this.database = database;
    }   

    public Student getStudentByNetID(String netID) throws Exception{
        return database.getStudent(netID);

    }

    public Set<Student> getStudents() throws Exception{
        return database.getAllStudents();
    }

    public Professor findProfessorInDirectory(String professorNetID) throws Exception{
        return database.getProfessor(professorNetID);
    }
    
    public Boolean isStudent(User user){
        return "s".equals(user.getType());
    };

    public Database getDatabase(){
        return database;
    }

    
}
