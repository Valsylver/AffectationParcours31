package fr.affectation.domain.specialization;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class JobSector extends Specialization{
	
	private String type = "JobSector";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

