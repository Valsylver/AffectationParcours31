package fr.affectation.service.validation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
	@Transactional(readOnly = true)
	public boolean isValidated(String login) {
		Session session = sessionFactory.getCurrentSession();
		StudentValidation studentValidation = (StudentValidation) session.get(StudentValidation.class, login);
		if (!(studentValidation == null)){
			return studentValidation.isValidated();
		}
		return false;
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
	public List<String> getAllStudentsValidatedLogin() {
		List<String> allLogins = new ArrayList<String>();
		for (StudentValidation studentValidation : getAllStudentsValidated()){
			allLogins.add(studentValidation.getLogin());
		}
		return allLogins;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<StudentValidation> getAllStudentsValidated() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from StudentValidation");
		List<StudentValidation> allStudentValidation = (List<StudentValidation>) query
				.list();
		return allStudentValidation;
	}

	@Override
	@Transactional
	public void deleteAllStudents() {
		Session session = sessionFactory.getCurrentSession();
		for (StudentValidation student : getAllStudentsValidated()) {
			session.delete(student);
		}
	}

}
