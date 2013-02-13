package fr.affectation.service.agap;

import java.util.List;

import fr.affectation.domain.student.Contentious;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.UeResult;

public interface AgapService {
	
	public List<SimpleStudent> findStudentsConcerned();
	
	public boolean checkStudent(String login);
	
	public String findNameFromLogin(String login);
	
	public List<Contentious> findContentious(String login);
	
	public List<Float> findGpaMeans(String login);
	
	public List<UeResult> findUeResults(String login);

	public List<String> findCurrentPromotionStudentLogins();
	
	public List<String> findCesureStudentLogins();
}
