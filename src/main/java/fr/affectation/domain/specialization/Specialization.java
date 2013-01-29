package fr.affectation.domain.specialization;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Size;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Specialization {
	
	@Id
	@Size(min=1, max=6)
	private String abbreviation;
	
	@Column
	private String responsibleLogin;
	
	@Column
	@Size(min=1)
	private String name;
	
	@Column
	private String codeUe1;
	
	@Column
	private String codeUe2;
	
	@Column
	private String codeUe3;
	
	@Column
	private String codeUe4;
	
	@Column
	private String codeUe5;
	
	abstract public String getType();

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getResponsibleLogin() {
		return responsibleLogin;
	}

	public void setResponsibleLogin(String responsibleLogin) {
		this.responsibleLogin = responsibleLogin;
	}
	
	public String getStringForForm(){
		return name + " (" + abbreviation + ")"; 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCodeUe1() {
		return codeUe1;
	}

	public void setCodeUe1(String codeUe1) {
		this.codeUe1 = codeUe1;
	}

	public String getCodeUe2() {
		return codeUe2;
	}

	public void setCodeUe2(String codeUe2) {
		this.codeUe2 = codeUe2;
	}

	public String getCodeUe3() {
		return codeUe3;
	}

	public void setCodeUe3(String codeUe3) {
		this.codeUe3 = codeUe3;
	}

	public String getCodeUe4() {
		return codeUe4;
	}

	public void setCodeUe4(String codeUe4) {
		this.codeUe4 = codeUe4;
	}

	public String getCodeUe5() {
		return codeUe5;
	}

	public void setCodeUe5(String codeUe5) {
		this.codeUe5 = codeUe5;
	}
	
	public List<String> getCodeUeList(){
		List<String> codeUe = new ArrayList<String>();
		codeUe.add(codeUe1);
		codeUe.add(codeUe2);
		codeUe.add(codeUe3);
		codeUe.add(codeUe4);
		codeUe.add(codeUe5);
		return codeUe;
	}
}
