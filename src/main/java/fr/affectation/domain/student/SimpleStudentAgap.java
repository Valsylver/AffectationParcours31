package fr.affectation.domain.student;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table
public class SimpleStudentAgap extends SimpleStudent{

	public static final int CURRENT_PROMOTION = 1;
	
	public static final int CESURE = 2;	

	@Column
	private int origin;
	
	public SimpleStudentAgap(){}
	
	public SimpleStudentAgap(String login, String name, int origin){
		super(login, name);
		this.origin = origin;
	}

	public int getOrigin() {
		return origin;
	}

	public void setOrigin(int origin) {
		this.origin = origin;
	}
	

}
