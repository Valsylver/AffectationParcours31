package fr.affectation.service.choice;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.affectation.domain.choice.Choice;
import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.student.Student;

@Service
public class ChoiceServiceImpl implements ChoiceService {

	@Inject
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void save(Choice choices) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(choices);
	}

	@Override
	@Transactional(readOnly = true)
	public JobSectorChoice getJobSectorChoicesByLogin(String login) {
		Session session = sessionFactory.getCurrentSession();
		JobSectorChoice choices = (JobSectorChoice) session.get(
				JobSectorChoice.class, login);
		if (choices == null) {
			return new JobSectorChoice();
		} else {
			return choices;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ImprovementCourseChoice getImprovementCourseChoicesByLogin(
			String login) {
		Session session = sessionFactory.getCurrentSession();
		ImprovementCourseChoice choices = (ImprovementCourseChoice) session
				.get(ImprovementCourseChoice.class, login);
		if (choices == null) {
			return new ImprovementCourseChoice();
		} else {
			return choices;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<JobSectorChoice> findAllJobSectorChoices() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from JobSectorChoice");
		List<JobSectorChoice> allChoices = query.list();
		return allChoices;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ImprovementCourseChoice> findAllImprovementCourseChoices() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from ImprovementCourseChoice");
		List<ImprovementCourseChoice> allChoices = query.list();
		return allChoices;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getLoginsByOrderChoiceAndSpecialization(
			int orderChoice, Specialization specialization) {
		String querySpecialization = "from ";
		querySpecialization += specialization.getType().equals("JobSector") ? "JobSectorChoice"
				: "ImprovementCourseChoice";
		querySpecialization += " where choice" + orderChoice + "=:abbreviation";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(querySpecialization);
		query.setString("abbreviation", specialization.getAbbreviation());
		List<Choice> allChoices = query.list();
		List<String> allLogins = new ArrayList<String>();
		for (Choice choice : allChoices) {
			allLogins.add(choice.getLogin());
		}
		return allLogins;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Student> getStudentsByOrderChoiceAndSpecialization(
			int orderChoice, Specialization specialization) {
		List<String> allLogin = getLoginsByOrderChoiceAndSpecialization(
				orderChoice, specialization);
		List<Student> allStudent = new ArrayList<Student>();
		for (String login : allLogin) {
			Student student = new Student();
			student.setImprovementCourseChoice(getImprovementCourseChoicesByLogin(login));
			allStudent.add(student);
		}
		return allStudent;
	}

	@Override
	@Transactional
	public void delete(Choice choice) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(choice);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Integer> getElementNotFilledImprovementCourse(String login) {
		Choice choice = getImprovementCourseChoicesByLogin(login);
		List<Integer> notFilled = new ArrayList<Integer>();
		if (choice.getChoice1() == null) {
			notFilled.add(1);
		}
		if (choice.getChoice2() == null) {
			notFilled.add(2);
		}
		if (choice.getChoice3() == null) {
			notFilled.add(3);
		}
		if (choice.getChoice4() == null) {
			notFilled.add(4);
		}
		if (choice.getChoice5() == null) {
			notFilled.add(5);
		}
		return notFilled;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Integer> getElementNotFilledJobSector(String login) {
		Choice choice = getJobSectorChoicesByLogin(login);
		List<Integer> notFilled = new ArrayList<Integer>();
		if (choice.getChoice1() == null) {
			notFilled.add(1);
		}
		if (choice.getChoice2() == null) {
			notFilled.add(2);
		}
		if (choice.getChoice3() == null) {
			notFilled.add(3);
		}
		if (choice.getChoice4() == null) {
			notFilled.add(4);
		}
		if (choice.getChoice5() == null) {
			notFilled.add(5);
		}
		return notFilled;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasFilledAll(String login) {
		return (getElementNotFilledImprovementCourse(login).size() == 0)
				&& (getElementNotFilledJobSector(login).size() == 0);
	}

	@Override
	public List<Student> getSimpleStudentsByOrderChoiceAndSpecialization(
			int orderChoice, Specialization specialization) {
		// TODO Auto-generated method stub
		return null;
	}

}
