package fr.affectation.domain.specialization;

import java.util.List;

public class SimpleSpecializationWithList extends SimpleSpecialization{

	private List<String> students;
	
	public SimpleSpecializationWithList(String abbreviation, String name, List<String> students){
		super(abbreviation, name);
		this.students = students;
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}
}
