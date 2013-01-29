package fr.affectation.domain.specialization;

import java.util.Comparator;

public class ComparatorAlphabetic implements Comparator<Specialization>{

	public int compare(Specialization spec1, Specialization spec2) {
		return spec1.getName().compareTo(spec2.getName());
	}

}