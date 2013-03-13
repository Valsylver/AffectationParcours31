package fr.affectation.domain.specialization;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class JobSector extends Specialization {

	private int type = Specialization.JOB_SECTOR;

	public int getType() {
		return type;
	}
}
