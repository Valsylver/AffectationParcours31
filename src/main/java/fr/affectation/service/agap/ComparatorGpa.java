package fr.affectation.service.agap;

import java.util.Comparator;

public class ComparatorGpa implements Comparator<Gpa>{

	public int compare(Gpa gpa1, Gpa gpa2) {
		if (gpa1.getMean() > gpa2.getMean()){
			return -1;
		}
		else{
			if (gpa1.getMean() < gpa2.getMean()){
				return 1;
			}
		}
		return 0;
	}

}
