package fr.affectation.service.validation;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.affectation.domain.student.StudentValidation;

@Service
public class ValidationServiceImpl implements ValidationService {
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void save(String login, boolean validatedIc, boolean validatedJs) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(new StudentValidation(login, validatedIc, validatedJs));
	}
	
	@Override
	@Transactional
	public void updateIcValidation(String login, boolean validation) {
		Session session = sessionFactory.getCurrentSession();
		StudentValidation studentValidation = (StudentValidation) session.get(StudentValidation.class, login);
		studentValidation.setValidatedIc(validation);
	}

	@Override
	@Transactional
	public void updateJsValidation(String login, boolean validation) {
		Session session = sessionFactory.getCurrentSession();
		StudentValidation studentValidation = (StudentValidation) session.get(StudentValidation.class, login);
		studentValidation.setValidatedJs(validation);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isValidatedIc(String login) {
		Session session = sessionFactory.getCurrentSession();
		StudentValidation studentValidation = (StudentValidation) session.get(StudentValidation.class, login);
		if (!(studentValidation == null)){
			return studentValidation.isValidatedIc();
		}
		return false;
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isValidatedJs(String login) {
		Session session = sessionFactory.getCurrentSession();
		StudentValidation studentValidation = (StudentValidation) session.get(StudentValidation.class, login);
		if (!(studentValidation == null)){
			return studentValidation.isValidatedJs();
		}
		return false;
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isInValidationProcess(String login) {
		Session session = sessionFactory.getCurrentSession();
		StudentValidation studentValidation = (StudentValidation) session.get(StudentValidation.class, login);
		return studentValidation != null;
	}

	@Override
	@Transactional
	public void remove(String login) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("delete StudentValidation where login=:login");
		query.setString("login", login);
		query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<String> findStudentValidatedIcLogins() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(StudentValidation.class);
		criteria.setProjection(Property.forName("login"));
		criteria.add(Restrictions.eq("validatedIc", true));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<String> findStudentValidatedJsLogins() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(StudentValidation.class);
		criteria.setProjection(Property.forName("login"));
		criteria.add(Restrictions.eq("validatedJs", true));
		return criteria.list();
	}

	@Override
	@Transactional
	public void removeAll() {
		Session session = sessionFactory.getCurrentSession();
		String deleteQuery = "delete from StudentValidation";
		Query query = session.createQuery(deleteQuery);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<String> findAllStudentsInValidationProcessLogin() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(StudentValidation.class);
		criteria.setProjection(Property.forName("login"));
		return criteria.list();
	}

}
