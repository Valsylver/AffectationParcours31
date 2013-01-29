package fr.affectation.domain.student;

import java.util.List;
import java.util.Map;

import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.student.Contentious;

public class Result {

	private Float gpaMeanS5;
	private Float gpaMeanS6;
	private Float gpaMeanS7;
	private int ranking;
	private Float rankingIndicator;
	private Float meanForSpecialization;
	private Map<String, Float> ueGrade;
	private List<Contentious> contentious;
	
	public void generateMeanForSpecialization(Specialization specialization){
		Float sum = new Float(0);
		if (ueGrade.containsKey(specialization.getCodeUe1())){
			sum += ueGrade.get(specialization.getCodeUe1());
		}
		if (ueGrade.containsKey(specialization.getCodeUe2())){
			sum += ueGrade.get(specialization.getCodeUe2());
		}
		if (ueGrade.containsKey(specialization.getCodeUe3())){
			sum += ueGrade.get(specialization.getCodeUe3());
		}
		if (ueGrade.containsKey(specialization.getCodeUe4())){
			sum += ueGrade.get(specialization.getCodeUe4());
		}
		if (ueGrade.containsKey(specialization.getCodeUe5())){
			sum += ueGrade.get(specialization.getCodeUe5());
		}
		meanForSpecialization = (sum / 5) * (5/4);
	}
	
	public Float getAppreciation(){
		Float appreciation = new Float(0);
		if (contentious.size() == 0 ){
			appreciation += 5;
		}
		else{
			if (contentious.size() < 3){
				appreciation += 2;
			}
		}
		appreciation += meanForSpecialization;
		appreciation += rankingIndicator;
		return (float) Math.round(appreciation * 100) / 100;
	}
	
	public int getRanking() {
		return ranking;
	}
	
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public Map<String, Float> getUeGrade() {
		return ueGrade;
	}
	public void setUeGrade(Map<String, Float> ueGrade) {
		this.ueGrade = ueGrade;
	}
	public List<Contentious> getContentious() {
		return contentious;
	}
	public void setContentious(List<Contentious> contentious) {
		this.contentious = contentious;
	}
	public Float getGpaMeanS5() {
		return gpaMeanS5;
	}
	public void setGpaMeanS5(Float gpaMeanS5) {
		this.gpaMeanS5 = gpaMeanS5;
	}
	public Float getGpaMeanS6() {
		return gpaMeanS6;
	}
	public void setGpaMeanS6(Float gpaMeanS6) {
		this.gpaMeanS6 = gpaMeanS6;
	}
	public Float getGpaMeanS7() {
		return gpaMeanS7;
	}
	public void setGpaMeanS7(Float gpaMeanS7) {
		this.gpaMeanS7 = gpaMeanS7;
	}
	public Float getRankingIndicator() {
		return rankingIndicator;
	}
	public void setRankingIndicator(Float rankingIndicator) {
		this.rankingIndicator = rankingIndicator;
	}
}
