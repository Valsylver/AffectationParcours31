package fr.affectation.domain.specialization;

public class SimpleSpecialization implements Comparable<SimpleSpecialization> {

	private String abbreviation;

	private String name;

	public SimpleSpecialization(String abbreviation, String name) {
		this.abbreviation = abbreviation;
		this.name = name;
	}

	public SimpleSpecialization() {
	}

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

	@Override
	public int compareTo(SimpleSpecialization specialization) {
		if ((name != null) && (specialization.getName() != null)){
			return this.name.compareTo(specialization.getName());
		}
		else{
			if ((abbreviation != null) && (specialization.getAbbreviation() != null)){
				return this.abbreviation.compareTo(specialization.getAbbreviation());
			}
			else{
				return 0;
			}
		}
	}
}
