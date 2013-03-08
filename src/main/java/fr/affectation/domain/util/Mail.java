package fr.affectation.domain.util;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table
public class Mail {
	
	@Id 
	private long id;
	
	@Column
	@Size(min = 1, max = 255)
	private String object;
	
	@Column
	@Size(min = 1, max = 600)
	private String message;
	
	public Mail(long id, String object, String message){
		this.id = id;
		this.object = object;
		this.message = message;
	}
	
	public Mail(){};

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public SimpleMail toSimpleMail(){
		SimpleMail simpleMail = new SimpleMail();
		simpleMail.setMessage(this.message);
		simpleMail.setObject(this.message);
		simpleMail.setAddressee("Elèves");
		return simpleMail;
	}

}
