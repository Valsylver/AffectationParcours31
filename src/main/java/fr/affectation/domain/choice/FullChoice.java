package fr.affectation.domain.choice;

public class FullChoice {

	private ImprovementCourseChoice improvementCourseChoice = new ImprovementCourseChoice();
	
	private JobSectorChoice jobSectorChoice = new JobSectorChoice();

	public ImprovementCourseChoice getImprovementCourseChoice() {
		return improvementCourseChoice;
	}

	public void setImprovementCourseChoice(
			ImprovementCourseChoice improvementCourseChoice) {
		this.improvementCourseChoice = improvementCourseChoice;
	}

	public JobSectorChoice getJobSectorChoice() {
		return jobSectorChoice;
	}

	public void setJobSectorChoice(JobSectorChoice jobSectorChoice) {
		this.jobSectorChoice = jobSectorChoice;
	}
}
