package fr.affectation.service.student;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.SimpleStudentWithValidation;
import fr.affectation.domain.student.StudentToExclude;

public interface StudentService {

	public void saveStudentToExclude(StudentToExclude student);

	public void deleteAllStudentToExclude();

	public List<StudentToExclude> findAllStudentToExclude();
	
	public List<String> findAllStudentToExcludeLogin();
	
	public boolean populateStudentToExcludeFromFile(MultipartFile file);

	public List<SimpleStudent> findAllStudentsToExclude();

	public StudentToExclude findByLogin(String login);

	public boolean isExcluded(SimpleStudent student);

	public List<SimpleStudent> findAllStudentsConcerned();

	public int findNecessarySizeForStudentExclusion();
	
	public void removeStudentByLogin(String login);

	public boolean isExcluded(String login);

	public boolean isStudentConcerned(String login);

	public List<String> findAllStudentsConcernedLogin();
	
	//OK
	public void populateValidation();
	
	//OK
	public List<SimpleStudent> findSimpleStudentsByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization);
	
	//OK
	public List<SimpleStudentWithValidation> findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization);
	
	//OK
	public List<List<SimpleStudent>> findSimpleStudentsForAllIcByOrder(int order);

	//OK
	public List<List<SimpleStudentWithValidation>> findSimpleStudentsWithValidationForAllIcByOrder(int order);
	
	//OK
	public List<List<SimpleStudent>> findSimpleStudentsForAllJsByOrder(int order);
	
	//OK
	public List<List<SimpleStudentWithValidation>> findSimpleStudentsWithValidationForAllJsByOrder(int order);
	

}
