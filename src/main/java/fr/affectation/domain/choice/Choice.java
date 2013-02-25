package fr.affectation.domain.choice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Choice {
	
	@Id
	protected String login;
	
	@Column
	protected String choice1;
	
	@Column
	protected String choice2;
	
	@Column
	protected String choice3;
	
	@Column
	protected String choice4;
	
	@Column
	protected String choice5;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getChoice1() {
		return choice1;
	}

	public void setChoice1(String choice1) {
		this.choice1 = choice1;
	}

	public String getChoice2() {
		return choice2;
	}

	public void setChoice2(String choice2) {
		this.choice2 = choice2;
	}

	public String getChoice3() {
		return choice3;
	}

	public void setChoice3(String choice3) {
		this.choice3 = choice3;
	}

	public String getChoice4() {
		return choice4;
	}

	public void setChoice4(String choice4) {
		this.choice4 = choice4;
	}

	public String getChoice5() {
		return choice5;
	}

	public void setChoice5(String choice5) {
		this.choice5 = choice5;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((choice1 == null) ? 0 : choice1.hashCode());
		result = prime * result + ((choice2 == null) ? 0 : choice2.hashCode());
		result = prime * result + ((choice3 == null) ? 0 : choice3.hashCode());
		result = prime * result + ((choice4 == null) ? 0 : choice4.hashCode());
		result = prime * result + ((choice5 == null) ? 0 : choice5.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
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
		Choice other = (Choice) obj;
		if (choice1 == null) {
			if (other.choice1 != null)
				return false;
		} else if (!choice1.equals(other.choice1))
			return false;
		if (choice2 == null) {
			if (other.choice2 != null)
				return false;
		} else if (!choice2.equals(other.choice2))
			return false;
		if (choice3 == null) {
			if (other.choice3 != null)
				return false;
		} else if (!choice3.equals(other.choice3))
			return false;
		if (choice4 == null) {
			if (other.choice4 != null)
				return false;
		} else if (!choice4.equals(other.choice4))
			return false;
		if (choice5 == null) {
			if (other.choice5 != null)
				return false;
		} else if (!choice5.equals(other.choice5))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Choice [login=" + login + ", choice1=" + choice1 + ", choice2=" + choice2 + ", choice3=" + choice3 + ", choice4=" + choice4 + ", choice5="
				+ choice5 + "]";
	}

}

