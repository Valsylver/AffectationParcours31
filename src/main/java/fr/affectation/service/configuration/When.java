package fr.affectation.service.configuration;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class When {
	
	@NotNull
	private Date firstEmail;
	
	@NotNull
	private Date secondEmail;
	
	@NotNull
	private Date endSubmission;

	public Date getFirstEmail() {
		return firstEmail;
	}

	public void setFirstEmail(Date firstEmail) {
		this.firstEmail = firstEmail;
	}

	public Date getSecondEmail() {
		return secondEmail;
	}

	public void setSecondEmail(Date secondEmail) {
		this.secondEmail = secondEmail;
	}

	public void setEndSubmission(Date endSubmission) {
		this.endSubmission = endSubmission;
	}

	public Date getEndSubmission() {
		return endSubmission;
	}

}
