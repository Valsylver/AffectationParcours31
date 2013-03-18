package fr.affectation.domain.agap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "720_choix3A_notes_details")
public class UeResult {
	
	@Column(name = "cycle")
	//@Length(max = 25)
	private String cycle;
	
	@Column(name = "nom")
	private String name;
	
	@Column(name = "code_ue")
	//@Length(max = 20)
	private String ueCode;
	
	@Column(name = "session")
	private Integer session;
	
	@Column(name = "sem")
	//@Length(max = 15)
	private String semester;
	
	@Column(name = "credits_ects")
	private double ectsCredits;
	
	@Column(name = "grade_gpa")
	//@Length(max = 5)
	private String gpaGrade;
	
	@Column(name = "moy_ue")
	private double ueMean;
	
	@Column(name = "grade_ects")
	//@Length(max = 5)
	private String ectsGrade;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUeCode() {
		return ueCode;
	}

	public void setUeCode(String ueCode) {
		this.ueCode = ueCode;
	}

	public String getGpaGrade() {
		return gpaGrade;
	}

	public void setGpaGrade(String gpaGrade) {
		this.gpaGrade = gpaGrade;
	}

	public Integer getSession() {
		return session;
	}

	public void setSession(Integer session) {
		this.session = session;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getEctsGrade() {
		return ectsGrade;
	}

	public void setEctsGrade(String ectsGrade) {
		this.ectsGrade = ectsGrade;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public double getUeMean() {
		return ueMean;
	}

	public void setUeMean(double ueMean) {
		this.ueMean = ueMean;
	}

	public double getEctsCredits() {
		return ectsCredits;
	}

	public void setEctsCredits(double ectsCredits) {
		this.ectsCredits = ectsCredits;
	}

}
