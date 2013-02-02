package fr.affectation.service.choice;

import java.util.List;

import fr.affectation.domain.choice.Choice;
import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.student.Student;

public interface ChoiceService {
	
	public void save(Choice choice);
	
	public void delete(Choice choice);
	
	public Choice getJobSectorChoicesByLogin(String login);
	
	public Choice getImprovementCourseChoicesByLogin(String login);
	
	public List<JobSectorChoice> findAllJobSectorChoices();
	
	public List<ImprovementCourseChoice> findAllImprovementCourseChoices();
	
	public List<Student> getSimpleStudentsByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization);
	
	public List<Student> getStudentsByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization);
	
	public List<String> getLoginsByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization);
	
	public List<Integer> getElementNotFilledImprovementCourse(String login);
	
	public List<Integer> getElementNotFilledJobSector(String login);
	
	public ImprovementCourseChoice findIcChoicesByLogin(String login);
	
	public JobSectorChoice findJsChoicesByLogin(String login);
	
	public boolean hasFilledAll(String login);

}
