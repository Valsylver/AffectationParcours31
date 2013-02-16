package fr.affectation.domain.student;

public class SimpleStudentWithValidation extends SimpleStudent{
	
	private boolean validated;
	
	public SimpleStudentWithValidation(String login, String name, boolean validated){
		super(login, name);
		this.validated = validated;
	}
	
	public SimpleStudentWithValidation(){
		super();
	}
	
	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

}