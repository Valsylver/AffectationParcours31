package fr.affectation.service.exclusion;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.StudentToExclude;

@Service
public class ExclusionServiceImpl implements ExclusionService{
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void save(StudentToExclude student) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(student);
	}

	@Override
	@Transactional
	public void deleteAllStudentsToExclude() {
		Session session = sessionFactory.getCurrentSession();
		for (StudentToExclude student : findStudentsToExclude()) {
			session.delete(student);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<StudentToExclude> findStudentsToExclude() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from StudentToExclude");
		List<StudentToExclude> allStudent = (List<StudentToExclude>) query.list();
		return allStudent;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isExcluded(SimpleStudent student) {
		return findByLogin(student.getLogin()) != null;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isExcluded(String login) {
		return findByLogin(login) != null;
	}

	@Override
	@Transactional(readOnly = true)
	public StudentToExclude findByLogin(String login) {
		String queryStudent = "from StudentToExclude where login=:login";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(queryStudent);
		query.setString("login", login);
		List<StudentToExclude> allStudents = query.list();
		if (allStudents.size() == 1) {
			return allStudents.get(0);
		} else {
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findStudentToExcludeLogins() {
		List<String> studentsToExcludeLogin = new ArrayList<String>();
		for (StudentToExclude student : findStudentsToExclude()) {
			studentsToExcludeLogin.add(student.getLogin());
		}
		return studentsToExcludeLogin;
	}

	@Override
	@Transactional
	public void removeStudentByLogin(String login) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("delete StudentToExclude where login=:login");
		query.setString("login", login);
		query.executeUpdate();
	}
}
