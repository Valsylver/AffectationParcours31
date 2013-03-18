package fr.affectation.domain.agap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "720_choix3A_cesure")
public class StudentCesure {
	
	@Id
	@Column(name = "nom")
	private String name;
	
	@Column(name = "cesure")
	@Length(max = 15)
	private String cesure;
	
	public StudentCesure(){}
	
	public StudentCesure(String name, String cesure){
		this.name = name;
		this.cesure = cesure;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCesure() {
		return cesure;
	}

	public void setCesure(String cesure) {
		this.cesure = cesure;
	}
}
