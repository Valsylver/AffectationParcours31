package fr.affectation.domain.specialization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Size;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Specialization implements Comparable<Specialization>{
	
	public static final int IMPROVEMENT_COURSE = 1;
	
	public static final int JOB_SECTOR = 2;
	
	@Id
	@Size(min=1, max=6)
	private String abbreviation;
	
	@Column
	private String responsibleLogin;
	
	@Column
	@Size(min=1)
	private String name;

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
	
	@Override
	public int compareTo(Specialization specialization) {
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
