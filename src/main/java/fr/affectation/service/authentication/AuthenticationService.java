package fr.affectation.service.authentication;

public interface AuthenticationService {
	
	public boolean isStudent(String login);
	
	public boolean isResponsible(String login);
	
	public boolean isAdmin(String login);

}

