package fr.affectation.domain.configuration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("unused")
@Entity
@Table
public class AlreadyLaunched {
	
	@Id
	private int ID=42;
	
	@Column
	private boolean state=true;

}
