package fr.affectation.domain.student;

public class SimpleStudentWithOrigin extends SimpleStudent {
	
	private String origin;

	public SimpleStudentWithOrigin(String login, String name, String origin) {
		super(login, name);
		this.origin = origin;
	}

}
