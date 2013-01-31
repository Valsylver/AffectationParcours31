package fr.affectation.service.validation;

import java.util.List;

import fr.affectation.domain.student.StudentValidation;

public interface ValidationService {
	
	public void saveStudentValidation(StudentValidation studentValidation);
	
	public boolean isValidated(String login);
	
	public void deleteStudentByLogin(String login);
	
	public List<String> getAllStudentsValidatedLogin();
	
	public void deleteAllStudents();

	public List<StudentValidation> getAllStudentsValidated();

}
