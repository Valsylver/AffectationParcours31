package fr.affectation.domain.superuser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;


@Entity
@Table(name = "responsible")
public class Responsible {
	
	@Id
	private String login;
	
	@Column
	private String specializationAbb;
	
}
