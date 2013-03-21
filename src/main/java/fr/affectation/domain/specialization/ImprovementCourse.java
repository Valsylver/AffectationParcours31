package fr.affectation.domain.specialization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;


@Entity
@Table
public class ImprovementCourse extends Specialization implements Comparable<ImprovementCourse>{
	
	@Length(min=1)
	@Column
	private String superIc;
	
	public String getSuperIc() {
		return superIc;
	}

	public void setSuperIc(String superIc) {
		this.superIc = superIc;
	}

	private int type = Specialization.IMPROVEMENT_COURSE;
	
	public int getType(){
		return type;
	}
	
	@Override
	public int compareTo(ImprovementCourse improvementCourse) {
		int compareIc = superIc.compareTo(improvementCourse.getSuperIc());
		if (compareIc != 0){
			if ((name != null) && (improvementCourse.getName() != null)){
				return this.name.compareTo(improvementCourse.getName());
			}
			else{
				if ((abbreviation != null) && (improvementCourse.getAbbreviation() != null)){
					return this.abbreviation.compareTo(improvementCourse.getAbbreviation());
				}
				else{
					return 0;
				}
			}
		}
		else{
			return compareIc;
		}
	}

}
