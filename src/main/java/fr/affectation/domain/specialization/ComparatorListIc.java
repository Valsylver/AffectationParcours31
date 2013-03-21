package fr.affectation.domain.specialization;

import java.util.Comparator;
import java.util.List;

public class ComparatorListIc implements Comparator<List<ImprovementCourse>>{

	@Override
	public int compare(List<ImprovementCourse> first, List<ImprovementCourse> second) {
		if ((first.size() == 0) && (second.size() == 0)){
			return 0;
		}
		else{
			if ((first.size() == 0)){
				return -1;
			}
			else if (second.size() == 0){
				return 1;
			}
			else{
				return first.get(0).getSuperIc().compareTo(second.get(0).getSuperIc());
			}
		}
	}


}
