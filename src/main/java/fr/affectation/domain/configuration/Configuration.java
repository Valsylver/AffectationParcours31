package fr.affectation.domain.configuration;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Configuration {

	@Id
	private int id = 42;

	@Column
	private Date firstMail;

	@Column
	private Date secondMail;

	@Column
	private Date endSubmission;

	@Column
	private Date endValidation;

	public Configuration() {
	}

	public Configuration(Date firstMail, Date secondMail, Date endSubmission, Date endValidation) {
		this.firstMail = firstMail;
		this.secondMail = secondMail;
		this.endSubmission = endSubmission;
		this.endValidation = endValidation;
	}

	public Date getFirstMail() {
		return firstMail;
	}

	public void setFirstMail(Date firstMail) {
		this.firstMail = firstMail;
	}

	public Date getSecondMail() {
		return secondMail;
	}

	public void setSecondMail(Date secondMail) {
		this.secondMail = secondMail;
	}

	public Date getEndSubmission() {
		return endSubmission;
	}

	public void setEndSubmission(Date endSubmission) {
		this.endSubmission = endSubmission;
	}

	public Date getEndValidation() {
		return endValidation;
	}

	public void setEndValidation(Date endValidation) {
		this.endValidation = endValidation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endSubmission == null) ? 0 : endSubmission.hashCode());
		result = prime * result + ((endValidation == null) ? 0 : endValidation.hashCode());
		result = prime * result + ((firstMail == null) ? 0 : firstMail.hashCode());
		result = prime * result + id;
		result = prime * result + ((secondMail == null) ? 0 : secondMail.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Configuration other = (Configuration) obj;
		if (endSubmission == null) {
			if (other.endSubmission != null)
				return false;
		} else if (!endSubmission.equals(other.endSubmission))
			return false;
		if (endValidation == null) {
			if (other.endValidation != null)
				return false;
		} else if (!endValidation.equals(other.endValidation))
			return false;
		if (firstMail == null) {
			if (other.firstMail != null)
				return false;
		} else if (!firstMail.equals(other.firstMail))
			return false;
		if (secondMail == null) {
			if (other.secondMail != null)
				return false;
		} else if (!secondMail.equals(other.secondMail))
			return false;
		return true;
	}

}
