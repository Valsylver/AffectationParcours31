package fr.affectation.service.choice;

import java.util.List;
import java.util.Map;

import fr.affectation.domain.choice.Choice;
import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.domain.specialization.Specialization;

public interface ChoiceService {
	
	public void save(Choice choice);
	
	public void delete(Choice choice);
	
	public Choice findJobSectorChoiceByLogin(String login);
	
	public Choice findImprovementCourseChoiceByLogin(String login);
	
	public List<JobSectorChoice> findJobSectorChoices();
	
	public List<ImprovementCourseChoice> findImprovementCourseChoices();
	
	public List<String> findLoginsByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization);
	
	public List<Integer> findElementNotFilledImprovementCourse(String login);
	
	public List<Integer> findElementNotFilledJobSector(String login);
	
	public Map<String, List<String>> findChoiceRepartitionKnowingOne(int knownChoice, int wantedChoice, String abbreviation, int specializationType);
	
	public ImprovementCourseChoice findIcChoicesByLogin(String login);
	
	public JobSectorChoice findJsChoicesByLogin(String login);
	
	public void deleteAll();

	public Map<String, List<String>> findInverseRepartitionForAListOfLogin(List<String> loginsConcerned, int specializationType);

	public List<String> findLoginsByOrderChoiceAndSpecialization(int orderChoice, String abbreviation, int specializationType);

}
