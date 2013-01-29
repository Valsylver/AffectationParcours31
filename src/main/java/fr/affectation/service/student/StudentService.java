package fr.affectation.service.student;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import fr.affectation.domain.student.SimpleStudent;
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

}
