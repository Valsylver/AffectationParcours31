package fr.affectation.service.validation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
	public void saveStudentValidation(StudentValidation studentValidation) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(studentValidation);
	}
	
	@Override
	@Transactional
	public void saveStudentValidation(String login, boolean validatedIc, boolean validatedJs) {
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
	public void deleteStudentByLogin(String login) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("delete StudentValidation where login=:login");
		query.setString("login", login);
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findLoginsValidatedIc() {
		List<String> allLogins = new ArrayList<String>();
		for (StudentValidation studentValidation : findStudentsValidatedIc()){
			allLogins.add(studentValidation.getLogin());
		}
		return allLogins;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> findLoginsValidatedJs() {
		List<String> allLogins = new ArrayList<String>();
		for (StudentValidation studentValidation : findStudentsValidatedJs()){
			allLogins.add(studentValidation.getLogin());
		}
		return allLogins;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<StudentValidation> findStudentsValidatedIc() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(StudentValidation.class);
		criteria.add(Restrictions.eq("validatedIc", true));
		List<StudentValidation> allStudentValidation = criteria.list();
		return allStudentValidation;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<StudentValidation> findStudentsValidatedJs() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(StudentValidation.class);
		criteria.add(Restrictions.eq("validatedJs", true));
		List<StudentValidation> allStudentValidation = criteria.list();
		return allStudentValidation;
	}

	@Override
	@Transactional
	public void deleteAllStudents() {
		Session session = sessionFactory.getCurrentSession();
		String deleteQuery = "delete from StudentValidation";
		Query query = session.createQuery(deleteQuery);
		query.executeUpdate();
	}

}
