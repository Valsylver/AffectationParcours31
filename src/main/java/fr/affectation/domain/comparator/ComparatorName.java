package fr.affectation.domain.comparator;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;



public class ComparatorName implements Comparator<String>{

	@Override
	public int compare(String fullName0, String fullName1) {
		String name0 = retrieveName(fullName0);
		String name1 = retrieveName(fullName1);
		return name0.compareTo(name1);
	}
	
	private String retrieveName(String fullName){
		String[] fullNameList = StringUtils.split(fullName, " ");
		if (fullNameList.length > 1){
			return fullNameList[1];
		}
		else{
			return fullName;
		}
	}

}