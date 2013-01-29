package fr.affectation.domain.student;

public class StudentDetails {
	
	private String login;
	
	private String fullName;
	
	private String civility;
	
	private String bacMention;
	
	private String bacCode;
	
	private String entreeFil;
	
	private String entreePrep;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCivility() {
		return civility;
	}

	public void setCivility(String civility) {
		this.civility = civility;
	}

	public String getBacMention() {
		return bacMention.toLowerCase();
	}

	public void setBacMention(String bacMention) {
		this.bacMention = bacMention;
	}

	public String getBacCode() {
		return bacCode;
	}

	public void setBacCode(String bacCode) {
		this.bacCode = bacCode;
	}

	public String getEntreeFil() {
		return entreeFil;
	}

	public void setEntreeFil(String entreeFil) {
		this.entreeFil = entreeFil;
	}

	public String getEntreePrep() {
		return entreePrep;
	}

	public void setEntreePrep(String entreePrep) {
		this.entreePrep = entreePrep;
	}

}
