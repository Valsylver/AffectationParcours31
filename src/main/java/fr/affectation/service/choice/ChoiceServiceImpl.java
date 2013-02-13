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

@Service
public class ChoiceServiceImpl implements ChoiceService {

	@Inject
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void save(Choice choice) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(choice);
	}

	@Override
	@Transactional(readOnly = true)
	public JobSectorChoice findJobSectorChoiceByLogin(String login) {
		Session session = sessionFactory.getCurrentSession();
		JobSectorChoice choices = (JobSectorChoice) session.get(JobSectorChoice.class, login);
		if (choices == null) {
			return new JobSectorChoice();
		} else {
			return choices;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ImprovementCourseChoice findImprovementCourseChoiceByLogin(String login) {
		Session session = sessionFactory.getCurrentSession();
		ImprovementCourseChoice choices = (ImprovementCourseChoice) session.get(ImprovementCourseChoice.class, login);
		if (choices == null) {
			return new ImprovementCourseChoice();
		} else {
			return choices;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<JobSectorChoice> findJobSectorChoices() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from JobSectorChoice");
		@SuppressWarnings("unchecked")
		List<JobSectorChoice> allChoices = query.list();
		return allChoices;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ImprovementCourseChoice> findImprovementCourseChoices() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from ImprovementCourseChoice");
		List<ImprovementCourseChoice> allChoices = query.list();
		return allChoices;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<String> findLoginsByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization) {
		String querySpecialization = "from ";
		querySpecialization += specialization.getType().equals("JobSector") ? "JobSectorChoice" : "ImprovementCourseChoice";
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
	@Transactional
	public void delete(Choice choice) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(choice);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Integer> findElementNotFilledImprovementCourse(String login) {
		Choice choice = findIcChoicesByLogin(login);
		List<Integer> notFilled = new ArrayList<Integer>();
		if (choice == null){
			for (int i=1; i<6; i++){
				notFilled.add(i);
			}
		}
		else{
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
		}
		return notFilled;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Integer> findElementNotFilledJobSector(String login) {
		Choice choice = findJsChoicesByLogin(login);
		List<Integer> notFilled = new ArrayList<Integer>();
		if (choice == null){
			for (int i=1; i<6; i++){
				notFilled.add(i);
			}
		}
		else{
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
		}
		return notFilled;
	}

	@Override
	@Transactional(readOnly = true)
	public ImprovementCourseChoice findIcChoicesByLogin(String login) {
		Session session = sessionFactory.getCurrentSession();
		ImprovementCourseChoice choice = (ImprovementCourseChoice) session.get(ImprovementCourseChoice.class, login);
		return choice;
	}

	@Override
	@Transactional(readOnly = true)
	public JobSectorChoice findJsChoicesByLogin(String login) {
		Session session = sessionFactory.getCurrentSession();
		JobSectorChoice choice = (JobSectorChoice) session.get(JobSectorChoice.class, login);
		return choice;
	}

	@Override
	@Transactional
	public void deleteAllChoices() {
		Session session = sessionFactory.getCurrentSession();
		session.createQuery("delete from JobSectorChoice").executeUpdate();
		session.createQuery("delete from ImprovementCourseChoice").executeUpdate();
	}

}
