package fr.affectation.domain.comparator;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import fr.affectation.domain.student.SimpleStudentWithValidation;

public class ComparatorSimpleStudentWithValidation implements Comparator<SimpleStudentWithValidation>{
	
	private String retrieveName(String fullName){
		String[] fullNameList = StringUtils.split(fullName, " ");
		if (fullNameList.length > 1){
			return fullNameList[1];
		}
		else{
			return fullName;
		}
	}
	
	private String retrieveFirstName(String fullName){
		String[] fullNameList = StringUtils.split(fullName, " ");
		if (fullNameList.length > 1){
			return fullNameList[0];
		}
		else{
			return fullName;
		}
	}

	@Override
	public int compare(SimpleStudentWithValidation student0, SimpleStudentWithValidation student1) {
		String name0 = retrieveName(student0.getName());
		String name1 = retrieveName(student1.getName());
		if (name0.compareTo(name1) == 0){
			String firstName0 = retrieveFirstName(student0.getName());
			String firstName1 = retrieveFirstName(student1.getName());
			return firstName0.compareTo(firstName1);
		}
		return name0.compareTo(name1);
	}

}
