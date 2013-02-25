package fr.affectation.service.specialization;

import java.util.List;

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.Specialization;

public interface SpecializationService {
	
	public void save(Specialization specialization);
	
	public void delete(Specialization specialization);
	
	public void deleteAll();
	
	public JobSector getJobSectorByAbbreviation(String abbreviation);
	
	public ImprovementCourse getImprovementCourseByAbbreviation(String abbreviation);

	public List<String> findJobSectorAbbreviations();

	public List<String> findImprovementCourseAbbreviations();

	public List<JobSector> findJobSectors();

	public List<ImprovementCourse> findImprovementCourses();
	
	public List<String> findJobSectorStringsForForm();
	
	public List<String> findImprovementCourseStringsForForm();

	public String getAbbreviationFromStringForForm(String forForm);
	
	public String findNameFromIcAbbreviation(String abbreviation);
	
	public String findNameFromJsAbbreviation(String abbreviation);

}

