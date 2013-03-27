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
		String otherSuperIc = improvementCourse.getSuperIc();
		if ((otherSuperIc != null) && (superIc != null)){
			int compareIc = superIc.compareTo(improvementCourse.getSuperIc());
			if (compareIc == 0){
				if ((name != null) && (improvementCourse.getName() != null)){
					int compareName = this.name.compareTo(improvementCourse.getName());
					if (compareName != 0){
						return compareName;
					}
					else{
						if ((abbreviation != null) && (improvementCourse.getAbbreviation() != null)){
							int compareAbbreviation = this.abbreviation.compareTo(improvementCourse.getAbbreviation());
							if (compareAbbreviation != 0){
								return compareAbbreviation;
							}
						}
					}
				}
			}
			else{
				return compareIc;
			}
		}
		return 0;
	}

}
