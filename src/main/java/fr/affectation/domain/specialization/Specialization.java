package fr.affectation.domain.specialization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Size;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Specialization{
	
	public static final int IMPROVEMENT_COURSE = 1;
	
	public static final int JOB_SECTOR = 2;
	
	@Id
	@Size(min=1)
	protected String abbreviation;
	
	@Column
	protected String responsibleLogin;
	
	@Column
	@Size(min=1)
	protected String name;

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
	
	public int getJOB_SECTOR(){
		return JOB_SECTOR;
	}
	
	public int getIMPROVEMENT_COURSE(){
		return IMPROVEMENT_COURSE;
	}

	public static int getImprovementCourse() {
		return IMPROVEMENT_COURSE;
	}

	public static int getJobSector() {
		return JOB_SECTOR;
	}
}
