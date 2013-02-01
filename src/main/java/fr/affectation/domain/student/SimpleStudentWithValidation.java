package fr.affectation.domain.student;

public class SimpleStudentWithValidation {
	
	private String login;
	
	private String name;
	
	private boolean validated;
	
	public SimpleStudentWithValidation(String login, String name, boolean validated){
		this.name = name;
		this.login = login;
		this.validated = validated;
	}
	
	public SimpleStudentWithValidation(){
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

}