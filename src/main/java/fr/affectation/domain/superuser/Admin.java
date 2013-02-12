package fr.affectation.domain.superuser;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Admin {
	
	@Id
	private String login;
	
	public Admin(){}
	
	public Admin(String login){
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}
