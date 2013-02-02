package fr.affectation.domain.specialization;

import java.util.Comparator;

public class ComparatorAlphabetic implements Comparator<Specialization>{

	public int compare(Specialization spec1, Specialization spec2) {
		if (spec1.getName() != null  && spec2.getName() != null){
			return spec1.getName().compareTo(spec2.getName());
		}
		else{
			return spec1.getAbbreviation().compareTo(spec2.getAbbreviation());
		}
	}

}