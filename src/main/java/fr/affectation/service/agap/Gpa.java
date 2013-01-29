package fr.affectation.service.agap;

public class Gpa {
	
	private String login;
	
	private Float mean;
	
	public Gpa(String login, Float mean){
		this.login = login;
		this.mean = mean;
	}

	public String getLogin() {
		return login;
	}

	public void setName(String login) {
		this.login = login;
	}

	public Float getMean() {
		return mean;
	}

	public void setMean(Float mean) {
		this.mean = mean;
	}

}

