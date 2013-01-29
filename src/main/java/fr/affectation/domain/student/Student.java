package fr.affectation.domain.student;

import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;

public class Student {

	private StudentDetails details;
	private Result results;
	private JobSectorChoice jobSectorChoice;
	private ImprovementCourseChoice improvementCourseChoice;
	
	public Result getResults() {
		return results;
	}
	public void setResults(Result results) {
		this.results = results;
	}
	public JobSectorChoice getJobSectorChoice() {
		return jobSectorChoice;
	}
	public void setJobSectorChoice(JobSectorChoice jobSectorChoice) {
		this.jobSectorChoice = jobSectorChoice;
	}
	public ImprovementCourseChoice getImprovementCourseChoice() {
		return improvementCourseChoice;
	}
	public void setImprovementCourseChoice(
			ImprovementCourseChoice improvementCourseChoice) {
		this.improvementCourseChoice = improvementCourseChoice;
	}
	public StudentDetails getDetails() {
		return details;
	}
	public void setDetails(StudentDetails details) {
		this.details = details;
	}

}

