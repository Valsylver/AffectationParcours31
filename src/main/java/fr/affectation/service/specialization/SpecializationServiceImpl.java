package fr.affectation.service.specialization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.affectation.domain.specialization.ComparatorAlphabetic;
import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.Specialization;

@Service
public class SpecializationServiceImpl implements SpecializationService {

	@Inject
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void save(Specialization specialization) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(specialization);
	}

	@Override
	@Transactional(readOnly = true)
	public JobSector getJobSectorByAbbreviation(String abbreviation) {
		Session session = sessionFactory.getCurrentSession();
		return (JobSector) session.get(JobSector.class, abbreviation);
	}

	@Override
	@Transactional(readOnly = true)
	public ImprovementCourse getImprovementCourseByAbbreviation(String abbreviation) {
		Session session = sessionFactory.getCurrentSession();
		return (ImprovementCourse) session.get(ImprovementCourse.class, abbreviation);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<JobSector> findAllJobSector() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from JobSector");
		List<JobSector> allJs = (List<JobSector>) query.list();
		Collections.sort(allJs, new ComparatorAlphabetic());
		return allJs;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ImprovementCourse> findAllImprovementCourse() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from ImprovementCourse");
		List<ImprovementCourse> allIc = (List<ImprovementCourse>) query.list();
		Collections.sort(allIc, new ComparatorAlphabetic());
		return allIc;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findAllJobSectorAbbreviation() {
		List<String> allJSAbb = new ArrayList<String>();
		for (JobSector jobSector : findAllJobSector()){
			allJSAbb.add(jobSector.getAbbreviation());
		}
		return allJSAbb;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findAllImprovementCourseAbbreviation() {
		List<String> allICAbb = new ArrayList<String>();
		for (ImprovementCourse improvementCourse : findAllImprovementCourse()){
			allICAbb.add(improvementCourse.getAbbreviation());
		}
		return allICAbb;
	}

	@Override
	@Transactional
	public void delete(Specialization specialization) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(specialization);
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findAllImprovementCourseForForm() {
		List<ImprovementCourse> allIc = findAllImprovementCourse();
		List<String> allIcForForm = new ArrayList<String>();
		String icForForm;
		for (ImprovementCourse ic : allIc){
			icForForm = ic.getName() + " (" + ic.getAbbreviation() + ")";
			allIcForForm.add(icForForm);
		}
		Collections.sort(allIcForForm);
		return allIcForForm;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findAllJobSectorForForm() {
		List<JobSector> allJs = findAllJobSector();
		List<String> allJsForForm = new ArrayList<String>();
		String jsForForm;
		for (JobSector js : allJs){
			jsForForm = js.getName() + " (" + js.getAbbreviation() + ")";
			allJsForForm.add(jsForForm);
		}
		Collections.sort(allJsForForm);
		return allJsForForm;
	}
	
	@Override
	public String getAbbreviationFromStringForForm(String forForm){
		if (! forForm.equals("")){
			String[] nameAndAbb = forForm.split(" ");
			String abbWithParentheses = nameAndAbb[nameAndAbb.length - 1];
			String abbWithoutParentheses = abbWithParentheses.substring(1, abbWithParentheses.length() - 1);
			return abbWithoutParentheses;
		}
		else{
			return null;
		}
	}

}
