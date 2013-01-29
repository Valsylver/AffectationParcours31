package fr.affectation.service.responsible;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;

@Service
public class ResponsibleServiceImpl implements ResponsibleService {
	
	@Inject
	private SessionFactory sessionFactory;

	@Override
	@Transactional(readOnly = true)
	public List<String> getAllResponsible() {
		String queryIC = "SELECT improvementCourse.responsibleLogin from ImprovementCourse improvementCourse";
		String queryJS = "SELECT jobSector.responsibleLogin from JobSector jobSector";
		Session session = sessionFactory.getCurrentSession();
		List<String> allICResponsible = (List<String>) session.createQuery(queryIC).list();
		List<String> allJSResponsible = (List<String>) session.createQuery(queryJS).list();
		List<String> allResponsible = new ArrayList<String>();
		for (String login : allICResponsible){
			allResponsible.add(login);
		}
		for (String login : allJSResponsible){
			allResponsible.add(login);
		}
		return allResponsible;
	}

	@Override
	@Transactional(readOnly = true)
	public String forWhichSpecialization(String login) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ImprovementCourse.class);
		criteria.add(Restrictions.eq("responsibleLogin", login));
		ImprovementCourse improvementCourse = (ImprovementCourse) criteria.uniqueResult();
		if (improvementCourse != null){
			return improvementCourse.getAbbreviation();
		}
		else {
			criteria = session.createCriteria(JobSector.class);
			criteria.add(Restrictions.eq("responsibleLogin", login));
			JobSector jobSector = (JobSector) criteria.uniqueResult();
			return jobSector.getAbbreviation();
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public String forWhichSpecializationType(String login) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ImprovementCourse.class);
		criteria.add(Restrictions.eq("responsibleLogin", login));
		ImprovementCourse improvementCourse = (ImprovementCourse) criteria.uniqueResult();
		if (improvementCourse != null){
			return "ic";
		}
		else {
			return "js";
		}
	}


}
