package fr.affectation.service.validation;

import java.util.List;

import fr.affectation.domain.student.StudentValidation;

public interface ValidationService {
	
	public void saveStudentValidation(StudentValidation studentValidation);
	
	public void saveStudentValidation(String login, boolean validatedIc, boolean validatedJs);
	
	public void updateIcValidation(String login, boolean validation);
	
	public void updateJsValidation(String login, boolean validation);
	
	public boolean isValidatedIc(String login);
	
	public boolean isValidatedJs(String login);
	
	public boolean isInValidationProcess(String login);
	
	public void deleteStudentByLogin(String login);
	
	public List<String> findLoginsValidatedIc();
	
	public List<String> findLoginsValidatedJs();
	
	public List<StudentValidation> findStudentsValidatedIc();
	
	public List<StudentValidation> findStudentsValidatedJs();
	
	public void deleteAllStudents();


}
