package fr.affectation.service.agap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.affectation.domain.student.Contentious;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.SimpleStudentAgap;
import fr.affectation.domain.student.UeResult;

@SuppressWarnings("unchecked")
@Service
public class AgapCacheServiceImpl implements AgapCacheService {

	@Inject
	private AgapService agapService;

	@Inject
	private SessionFactory sessionFactory;

	public List<Contentious> findContentious(String login) {
		return agapService.findContentious(login);
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findCurrentPromotionStudentLogins() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SimpleStudentAgap.class)
				.setProjection(
						Projections.projectionList().add(
								Projections.property("login")));
		criteria.add(Restrictions.eq("origin",
				SimpleStudentAgap.CURRENT_PROMOTION));
		return (List<String>) criteria.list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<SimpleStudent> findCurrentPromotionSimpleStudents() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SimpleStudentAgap.class);
		criteria.add(Restrictions.eq("origin",
				SimpleStudentAgap.CURRENT_PROMOTION));
		return (List<SimpleStudent>) criteria.list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findCesureStudentLogins() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SimpleStudentAgap.class)
				.setProjection(
						Projections.projectionList().add(
								Projections.property("login")));
		criteria.add(Restrictions.eq("origin", SimpleStudentAgap.CESURE));
		return (List<String>) criteria.list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<SimpleStudent> findCesureSimpleStudents() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SimpleStudentAgap.class);
		criteria.add(Restrictions.eq("origin", SimpleStudentAgap.CESURE));
		return (List<SimpleStudent>) criteria.list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<SimpleStudent> findStudentsConcerned() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from SimpleStudentAgap");
		return (List<SimpleStudent>) query.list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findStudentConcernedLogins() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createQuery("select student.login from SimpleStudentAgap student");
		return (List<String>) query.list();
	}

	@Override
	@Transactional(readOnly = true)
	public String findNameFromLogin(String login) {
		Session session = sessionFactory.getCurrentSession();
		SimpleStudent student = (SimpleStudent) session.get(SimpleStudentAgap.class, login);
		return student != null ? student.getName() : login; 
	}

	@Override
	public List<Float> findGpaMeans(String login) {
		return agapService.findGpaMeans(login);
	}

	@Override
	public List<UeResult> findUeResults(String login) {
		return agapService.findUeResults(login);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, String> findNamesForAListOfLogins(List<String> allLogins) {
		Map<String, String> nameLoginMap = new HashMap<String, String>();
		if (allLogins.size() > 0) {
			Session session = sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(SimpleStudentAgap.class);
			criteria.add(Restrictions.in("login", allLogins));
			List<SimpleStudentAgap> students = (List<SimpleStudentAgap>) criteria
					.list();
			for (SimpleStudentAgap student : students) {
				nameLoginMap.put(student.getLogin(), student.getName());
			}
		}
		return nameLoginMap;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isStudent(String login) {
		Session session = sessionFactory.getCurrentSession();
		return session.get(SimpleStudentAgap.class, login) != null;
	}

}
