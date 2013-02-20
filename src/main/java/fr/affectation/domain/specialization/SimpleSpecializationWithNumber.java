package fr.affectation.domain.specialization;

public class SimpleSpecializationWithNumber extends SimpleSpecialization {
	
	private int number;
	
	public SimpleSpecializationWithNumber(String abbreviation, String name, int number){
		super(abbreviation, name);
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	

}
