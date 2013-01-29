package fr.affectation.domain.student;

public class SimpleStudent {
	
	private String login;
	
	private String name;
	
	public SimpleStudent(String login, String name){
		this.name = name;
		this.login = login;
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
	
	

}
