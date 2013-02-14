package fr.affectation.domain.student;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class StudentToExclude {

	@Id
	private String login;
	
	public StudentToExclude(String login){
		this.login = login;
	}
	
	public StudentToExclude(){
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
}
