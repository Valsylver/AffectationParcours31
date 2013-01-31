package fr.affectation.domain.student;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class StudentValidation {
	
	@Id
	private String login;
	
	@Column
	private boolean validated = true;
	
	public StudentValidation(String login, boolean validation){
		this.login = login;
		this.validated = validation;
	}
	
	public StudentValidation(String login){
		this.login = login;
	}
	
	public StudentValidation(){
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidation(boolean validation) {
		this.validated = validation;
	}
	
}