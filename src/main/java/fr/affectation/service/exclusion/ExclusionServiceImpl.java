package fr.affectation.service.exclusion;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.affectation.domain.student.StudentToExclude;

@Service
public class ExclusionServiceImpl implements ExclusionService {

	@Inject
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void save(String login) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(new StudentToExclude(login));
	}

	@Override
	@Transactional
	public void removeAll() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("delete from StudentToExclude");
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<String> findStudentToExcludeLogins() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("select student.login from StudentToExclude student");
		return query.list();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isExcluded(String login) {
		String queryStudent = "from StudentToExclude where login=:login";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(queryStudent);
		query.setString("login", login);
		return query.list().size() != 0;

	}

	@Override
	@Transactional
	public void remove(String login) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("delete StudentToExclude where login=:login");
		query.setString("login", login);
		query.executeUpdate();
	}
}
