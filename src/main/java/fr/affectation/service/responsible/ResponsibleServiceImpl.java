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
import fr.affectation.domain.specialization.Specialization;

@Service
public class ResponsibleServiceImpl implements ResponsibleService {
	
	@Inject
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<String> findResponsibles() {
		String queryIc = "select improvementCourse.responsibleLogin from ImprovementCourse improvementCourse";
		String queryJs = "select jobSector.responsibleLogin from JobSector jobSector";
		Session session = sessionFactory.getCurrentSession();
		List<String> icResponsibles = (List<String>) session.createQuery(queryIc).list();
		List<String> jsResponsibles = (List<String>) session.createQuery(queryJs).list();
		List<String> allResponsible = new ArrayList<String>();
		for (String login : icResponsibles){
			allResponsible.add(login);
		}
		for (String login : jsResponsibles){
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
	public int forWhichSpecializationType(String login) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ImprovementCourse.class);
		criteria.add(Restrictions.eq("responsibleLogin", login));
		ImprovementCourse improvementCourse = (ImprovementCourse) criteria.uniqueResult();
		if (improvementCourse != null){
			return Specialization.IMPROVEMENT_COURSE;
		}
		else {
			return Specialization.JOB_SECTOR;
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isResponsible(String login){
		return findResponsibles().contains(login);
	}


}
