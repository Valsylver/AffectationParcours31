package fr.affectation.domain.specialization;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "Improvement_Course")
public class ImprovementCourse extends Specialization{
	
	private String type = "ImprovementCourse";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
