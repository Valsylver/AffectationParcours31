package fr.affectation.domain.specialization;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table
public class ImprovementCourse extends Specialization{
	
	private int type = Specialization.IMPROVEMENT_COURSE;
	
	public int getType(){
		return type;
	}

}
