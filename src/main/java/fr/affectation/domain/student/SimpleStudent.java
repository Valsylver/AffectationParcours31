package fr.affectation.domain.student;

import org.apache.commons.lang.StringUtils;

public class SimpleStudent implements Comparable<SimpleStudent> {

	private String login;

	private String name;

	public SimpleStudent(String login, String name) {
		this.name = name;
		this.login = login;
	}

	public SimpleStudent() {
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(SimpleStudent other) {
		String name = retrieveLastName(getName());
		String otherName = retrieveLastName(other.getName());
		if (name.compareTo(otherName) == 0) {
			String firstName = retrieveFirstName(getName());
			String firstNameOther = retrieveFirstName(getName());
			return firstName.compareTo(firstNameOther);
		}
		return name.compareTo(otherName);
	}

	private String retrieveLastName(String fullName) {
		String[] fullNameList = StringUtils.split(fullName, " ");
		if (fullNameList.length == 2) {
			return fullNameList[1];
		} else if (fullNameList.length > 2) {
			String name = "";
			for (int i = 1; i < fullNameList.length; i++) {
				name += fullNameList[i];
				if (i != fullNameList.length - 1) {
					name += " ";
				}
			}
			return name;
		} else {
			return fullName;
		}
	}

	private String retrieveFirstName(String fullName) {
		String[] fullNameList = StringUtils.split(fullName, " ");
		if (fullNameList.length > 1) {
			return fullNameList[0];
		} else {
			return fullName;
		}
	}

}
