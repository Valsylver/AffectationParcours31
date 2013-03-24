package fr.affectation.domain.student;

import java.util.List;

import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;

public class Student {

	private String name;
	
	private String login;
	
	private String origin;

	private boolean hasFilledResume;
	
	private boolean hasFilledLetterIc;
	
	private boolean hasFilledLetterJs;
	
	private ImprovementCourseChoice icChoices;
	
	private JobSectorChoice jsChoices;
	
	private List<Contentious> contentious;
	
	private List<UeResult> results;
	
	private List<Float> gpaMeans;
	
	public ImprovementCourseChoice getIcChoices() {
		return icChoices;
	}

	public void setIcChoices(ImprovementCourseChoice icChoices) {
		this.icChoices = icChoices;
	}

	public JobSectorChoice getJsChoices() {
		return jsChoices;
	}

	public void setJsChoices(JobSectorChoice jsChoices) {
		this.jsChoices = jsChoices;
	}

	public boolean isHasFilledLetterJs() {
		return hasFilledLetterJs;
	}

	public void setHasFilledLetterJs(boolean hasFilledLetterJs) {
		this.hasFilledLetterJs = hasFilledLetterJs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public boolean isHasFilledResume() {
		return hasFilledResume;
	}

	public void setHasFilledResume(boolean hasFilledResume) {
		this.hasFilledResume = hasFilledResume;
	}

	public boolean isHasFilledLetterIc() {
		return hasFilledLetterIc;
	}

	public void setHasFilledLetterIc(boolean hasFilledLetterIc) {
		this.hasFilledLetterIc = hasFilledLetterIc;
	}

	public List<Contentious> getContentious() {
		return contentious;
	}

	public void setContentious(List<Contentious> contenious) {
		this.contentious = contenious;
	}

	public List<UeResult> getResults() {
		return results;
	}

	public void setResults(List<UeResult> results) {
		this.results = results;
	}

	public List<Float> getGpaMeans() {
		return gpaMeans;
	}

	public void setGpaMeans(List<Float> gpaMeans) {
		this.gpaMeans = gpaMeans;
	}
	
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

}

