package fr.affectation.domain.util;

import java.util.ArrayList;
import java.util.List;

public class StudentsExclusion {
	
	private List<String> currentPromotion;
	
	private List<String> excluded;
	
	public StudentsExclusion(int size){
		currentPromotion = new ArrayList<String>();
		excluded = new ArrayList<String>();
		for (int i=0; i<size; i++){
			currentPromotion.add("");
			excluded.add("");
		}
	}
	
	public StudentsExclusion(){
		currentPromotion = new ArrayList<String>();
		excluded = new ArrayList<String>();
	}

	public List<String> getCurrentPromotion() {
		return currentPromotion;
	}

	public void setCurrentPromotion(List<String> currentPromotion) {
		this.currentPromotion = currentPromotion;
	}

	public List<String> getExcluded() {
		return excluded;
	}

	public void setExcluded(List<String> excluded) {
		this.excluded = excluded;
	}

}
