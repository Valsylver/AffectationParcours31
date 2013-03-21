package fr.affectation.domain.specialization;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class JobSector extends Specialization implements Comparable<JobSector>{

	private int type = Specialization.JOB_SECTOR;

	public int getType() {
		return type;
	}
	
	@Override
	public int compareTo(JobSector jobSector) {
		if ((name != null) && (jobSector.getName() != null)){
			return this.name.compareTo(jobSector.getName());
		}
		else{
			if ((abbreviation != null) && (jobSector.getAbbreviation() != null)){
				return this.abbreviation.compareTo(jobSector.getAbbreviation());
			}
			else{
				return 0;
			}
		}
	}
}
