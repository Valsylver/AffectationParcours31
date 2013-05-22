package fr.affectation.service.student;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.SimpleSpecializationWithList;
import fr.affectation.domain.specialization.SimpleSpecializationWithNumber;
import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.SimpleStudentWithValidation;
import fr.affectation.domain.student.Student;
import fr.affectation.domain.util.SimpleMail;

public interface StudentService {

	public List<SimpleStudent> findCurrentPromotionStudentsConcerned();
	
	public List<SimpleStudent> findCesureStudentsConcerned();
	
	public List<SimpleStudent> findAllStudentsConcerned();

	public boolean isStudentConcerned(String login);

	public List<String> findAllStudentsConcernedLogin();

	public void populateValidation();

	public List<SimpleStudent> findSimpleStudentsByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization);
	
	public List<String> findLoginsByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization);

	public List<SimpleStudentWithValidation> findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization);

	public List<List<SimpleStudent>> findSimpleStudentsForAllIcByOrder(int order);

	public List<List<SimpleStudentWithValidation>> findSimpleStudentsWithValidationForAllIcByOrder(int order);

	public List<List<SimpleStudent>> findSimpleStudentsForAllJsByOrder(int order);

	public List<List<SimpleStudentWithValidation>> findSimpleStudentsWithValidationForAllJsByOrder(int order);

	public List<SimpleStudent> findStudentsForCategorySynthese(String category, String path);

	public Map<String, Integer> findSizeOfCategories(String path);
	
	public Map<String, Integer> getSizeOfCategories(String path);

	public Student retrieveStudentByLogin(String login, String realPath);

	public List<SimpleStudent> findAllStudentsToExclude();

	public List<String> findStudentsToExcludeName();

	public boolean populateStudentToExcludeFromFile(MultipartFile file);
	
	public void sendSimpleMail(SimpleMail mail, String path);
	
	public List<SimpleSpecializationWithList> findChoiceRepartitionKnowingOne(int knownChoice, int wantedChoice, Specialization specialization);
	
	public List<SimpleSpecializationWithList> findInverseRepartition(Specialization specialization);

	public Map<String, List<String>> findChoiceRepartitionForTheOtherType(String abbreviation, int specializationType);

	public List<Specialization> findIcChoicesFullSpecByLogin(String login);

	public List<Specialization> findJsChoicesFullSpecByLogin(String login);

	public List<List<ImprovementCourse>> findIcAvailableAsListWithSuperIc();

	public List<SimpleSpecializationWithNumber> findSimpleIcStats(int choice);

	public List<SimpleSpecializationWithNumber> findSimpleJsStats(int choice);

}
