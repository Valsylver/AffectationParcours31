package fr.affectation.service.exclusion;

import java.util.List;

import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.StudentToExclude;

public interface ExclusionService {

	public void save(StudentToExclude student);

	public void deleteAllStudentsToExclude();

	public List<StudentToExclude> findStudentsToExclude();

	public List<String> findStudentToExcludeLogins();
	
	public boolean isExcluded(String login);

	public StudentToExclude findByLogin(String login);

	public boolean isExcluded(SimpleStudent student);

	public void removeStudentByLogin(String login);

}
