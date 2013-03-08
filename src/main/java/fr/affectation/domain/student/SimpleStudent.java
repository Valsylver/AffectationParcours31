package fr.affectation.domain.student;


public class SimpleStudent implements Comparable<SimpleStudent> {

	private String login;

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
