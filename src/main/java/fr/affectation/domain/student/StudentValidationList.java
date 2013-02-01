package fr.affectation.domain.student;

import java.util.ArrayList;
import java.util.List;

public class StudentValidationList {

	private List<String> students;
	
	private List<Boolean> validated;
	
	public StudentValidationList(List<String> logins, List<Boolean> validated){
		this.students = new ArrayList<String>();
		this.validated = new ArrayList<Boolean>();
		for (int i=0; i<validated.size(); i++){
			this.students.add(logins.get(i));
			this.validated.add(validated.get(i));
		}
	}
	
	public StudentValidationList(){
		students = new ArrayList<String>();
		validated = new ArrayList<Boolean>();
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}

	public List<Boolean> getValidated() {
		return validated;
	}

	public void setValidated(List<Boolean> validated) {
		this.validated = validated;
	}

}
