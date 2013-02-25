package fr.affectation.domain.util;

import javax.validation.constraints.Size;

public class SimpleMail {

	private String addressee;
	
	@Size(min = 1, max = 255)
	private String object;
	
	@Size(min = 1, max = 1500)
	private String message;
	
	private boolean receiveACopy;

	public String getAddressee() {
		return addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
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

	public boolean isReceiveACopy() {
		return receiveACopy;
	}

	public void setReceiveACopy(boolean receiveACopy) {
		this.receiveACopy = receiveACopy;
	}
}
