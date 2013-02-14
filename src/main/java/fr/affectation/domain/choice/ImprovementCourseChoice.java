package fr.affectation.domain.choice;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table
public class ImprovementCourseChoice extends Choice{
	
	public static final String type = "ImprovementCourse";

	public String getType() {
		return type;
	}
	
	
}