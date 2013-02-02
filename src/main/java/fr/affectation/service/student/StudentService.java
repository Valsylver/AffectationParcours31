package fr.affectation.service.student;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.SimpleStudentWithValidation;
import fr.affectation.domain.student.Student;
import fr.affectation.domain.student.StudentToExclude;

public interface StudentService {

	public void saveStudentToExclude(StudentToExclude student);

	public void deleteAllStudentToExclude();

	public List<StudentToExclude> findAllStudentToExclude();

	public List<String> findAllStudentToExcludeLogin();
	
	public List<String> findStudentsToExcludeName();

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

	public void populateValidation();

	public List<SimpleStudent> findSimpleStudentsByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization);

	public List<SimpleStudentWithValidation> findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization);

	public List<List<SimpleStudent>> findSimpleStudentsForAllIcByOrder(int order);

	public List<List<SimpleStudentWithValidation>> findSimpleStudentsWithValidationForAllIcByOrder(int order);

	public List<List<SimpleStudent>> findSimpleStudentsForAllJsByOrder(int order);

	public List<List<SimpleStudentWithValidation>> findSimpleStudentsWithValidationForAllJsByOrder(int order);

	public void updateValidation(List<String> students, List<Boolean> validated_, String type);

	public List<Map<String, Object>> findStudentsForCategorySynthese(String category, String path);

	public List<Integer> findSizeOfCategories(String path);

	public Student retrieveStudentByLogin(String login, String realPath);

}
