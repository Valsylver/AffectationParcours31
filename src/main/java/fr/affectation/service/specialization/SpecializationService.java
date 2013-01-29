package fr.affectation.service.specialization;

import java.util.List;

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.Specialization;

public interface SpecializationService {
	
	public void save(Specialization specialization);
	
	public void delete(Specialization specialization);
	
	public JobSector getJobSectorByAbbreviation(String abbreviation);
	
	public ImprovementCourse getImprovementCourseByAbbreviation(String abbreviation);

	public List<String> findAllJobSectorAbbreviation();

	public List<String> findAllImprovementCourseAbbreviation();

	public List<JobSector> findAllJobSector();

	public List<ImprovementCourse> findAllImprovementCourse();
	
	public List<String> findAllJobSectorForForm();
	
	public List<String> findAllImprovementCourseForForm();

	public String getAbbreviationFromStringForForm(String forForm);

}

