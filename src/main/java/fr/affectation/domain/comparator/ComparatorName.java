package fr.affectation.domain.comparator;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;



public class ComparatorName implements Comparator<String>{

	@Override
	public int compare(String name, String otherName) {
		String lastName = retrieveLastName(name);
		String lastNameOther = retrieveLastName(otherName);
		if (lastName.compareTo(lastNameOther) == 0) {
			String firstName = retrieveFirstName(name);
			String firstNameOther = retrieveFirstName(otherName);
			return firstName.compareTo(firstNameOther);
		}
		return lastName.compareTo(lastNameOther);
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