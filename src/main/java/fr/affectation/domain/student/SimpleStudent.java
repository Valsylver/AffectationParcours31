package fr.affectation.domain.student;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class SimpleStudent implements Comparable<SimpleStudent> {

	@Id
	private String login;

	@Column
	private String name;

	public SimpleStudent(String login, String name) {
		this.name = name;
		this.login = login;
	}

	public SimpleStudent() {
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

	@Override
	public int compareTo(SimpleStudent other) {
		return ((this.name != null) && (other.getName() != null)) ? this.name.compareTo(other.getName()) : 0;
	}

}
