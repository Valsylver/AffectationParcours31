package fr.affectation.domain.agap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import fr.affectation.domain.student.SimpleStudent;

@Entity
@Table(name = "z720_choix3A_eleves")
public class SimpleStudentAgap extends SimpleStudent{
	
	@Id
	@Column(name = "uid")
	@Length(max = 50)
	private String login;
	
	@Column(name = "nom")
	private String name;
	
	@Column(name = "entree_fil")
	@Length(max = 30)
	private String origin;
	
	public SimpleStudentAgap(){
	}
	
	public SimpleStudentAgap(String login, String name, String origin){
		super(login, name);
		this.origin = origin;
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

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
}
