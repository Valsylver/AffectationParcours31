package fr.affectation.domain.comparator;

import java.util.Comparator;

import fr.affectation.domain.specialization.SimpleSpecialization;

public class ComparatorSimpleSpecialization implements Comparator<SimpleSpecialization>{

	@Override
	public int compare(SimpleSpecialization spec1, SimpleSpecialization spec2) {
		return spec1.getName().compareTo(spec2.getName());
	}

}
