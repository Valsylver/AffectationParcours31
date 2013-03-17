package fr.affectation.service.validation;

import java.util.List;

public interface ValidationService {
	
	public void save(String login, boolean validatedIc, boolean validatedJs);
	
	public void updateIcValidation(String login, boolean validation);
	
	public void updateJsValidation(String login, boolean validation);
	
	public boolean isValidatedIc(String login);
	
	public boolean isValidatedJs(String login);
	
	public boolean isInValidationProcess(String login);
	
	public void remove(String login);
	
	public List<String> findAllStudentsInValidationProcessLogin();
	
	public List<String> findStudentValidatedIcLogins();
	
	public List<String> findStudentValidatedJsLogins();
	
	public void removeAll();


}
