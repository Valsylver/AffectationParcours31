package fr.affectation.service.agap;

import java.util.List;
import java.util.Map;

import fr.affectation.domain.student.Contentious;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.UeResult;

public interface AgapService{
	
	public List<SimpleStudent> findStudentsConcerned();

	public List<String> findStudentConcernedLogins();

	public List<String> findCurrentPromotionStudentLogins();
	
	public List<String> findCesureStudentLogins();
	
	public List<SimpleStudent> findCurrentPromotionSimpleStudents();
	
	public List<SimpleStudent> findCesureSimpleStudents();

	public String findNameFromLogin(String login);
	
	public String findOriginFromLogin(String login);

	public List<Contentious> findContentious(String login);

	public List<Float> findGpaMeans(String login);

	public List<UeResult> findUeResults(String login);

	public Map<String, String> findNamesForAListOfLogins(List<String> allLogins);
	
	public boolean isStudent(String login);

}
