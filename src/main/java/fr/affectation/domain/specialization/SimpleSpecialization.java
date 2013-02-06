package fr.affectation.domain.specialization;

public class SimpleSpecialization {

	private String abbreviation;

	private String name;

	private int number;
	
	public SimpleSpecialization(String abbreviation, String name, int number){
		this.abbreviation = abbreviation;
		this.name = name;
		this.number = number;
	}

	public SimpleSpecialization(){}
	
	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
