package fr.affectation.service.agap;

import java.util.List;
import java.util.Map;

import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.student.Result;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.Student;
import fr.affectation.domain.student.StudentDetails;

public interface AgapService {
	
	public Student getStudent(String login);

	public StudentDetails getStudentDetailsFromLogin(String login);
	
	public Result getResultsFromLogin(String login);
	
	public boolean isStudentConcerned(String login);
	
	public List<String> getAllStudentConcernedLogin();
	
	public List<String> getAllStudentConcernedName();
	
	public void generateRanking();
	
	public void generateUeCode();
	
	public List<String> getRanking();
	
	public List<Float> getMeans();
	
	public String getNameFromLogin(String login);
	
	public String getLoginFromName(String name);
	
	public Map<String, Float> getUeGrade(String login);

	public Result getResultsFromLoginAndSpecialization(String login,
			Specialization specialization);

	public List<String> findAllValidForSpecUeCode();
	
	public List<SimpleStudent> findAllStudentsConcerned();
	
	public boolean checkStudent(String login);
}
