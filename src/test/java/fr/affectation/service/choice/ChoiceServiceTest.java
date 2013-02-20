package fr.affectation.service.choice;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import junit.framework.Assert;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.affectation.domain.choice.Choice;
import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.Specialization;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ChoiceServiceTest {

	@Inject
	private ChoiceService choiceService;

	@Inject
	private SessionFactory sessionFactory;

	@Before
	public void cleanDbBefore() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from JobSectorChoice").executeUpdate();
		session.createQuery("delete from ImprovementCourseChoice").executeUpdate();
		transaction.commit();
		session.close();
	}

	@After
	public void cleanDbAfter() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from JobSectorChoice").executeUpdate();
		session.createQuery("delete from ImprovementCourseChoice").executeUpdate();
		transaction.commit();
		session.close();
	}

	@Test
	public void findJsChoicesByLogin() {
		Choice jobSectorChoices = new JobSectorChoice();
		jobSectorChoices.setLogin("login");
		jobSectorChoices.setChoice1("AAA");
		jobSectorChoices.setChoice5("BBB");
		choiceService.save(jobSectorChoices);
		JobSectorChoice jsChoice = choiceService.findJsChoicesByLogin("login");
		Assert.assertTrue(jsChoice.getChoice1().equals("AAA"));
		Assert.assertTrue(jsChoice.getChoice5().equals("BBB"));
		Assert.assertTrue(jsChoice.getChoice2() == null);
		Assert.assertTrue(jsChoice.getChoice3() == null);
		Assert.assertTrue(jsChoice.getChoice4() == null);
	}

	@Test
	public void findIcChoicesByLogin() {
		Choice improvementCourseChoices = new ImprovementCourseChoice();
		improvementCourseChoices.setLogin("login");
		improvementCourseChoices.setChoice1("AAA");
		improvementCourseChoices.setChoice5("BBB");
		choiceService.save(improvementCourseChoices);
		ImprovementCourseChoice icChoice = choiceService.findIcChoicesByLogin("login");
		Assert.assertTrue(icChoice.getChoice1().equals("AAA"));
		Assert.assertTrue(icChoice.getChoice5().equals("BBB"));
		Assert.assertTrue(icChoice.getChoice2() == null);
		Assert.assertTrue(icChoice.getChoice3() == null);
		Assert.assertTrue(icChoice.getChoice4() == null);
	}

	@Test
	public void findIcChoicesByLoginWhenNotInDb() {
		ImprovementCourseChoice icChoice = choiceService.findIcChoicesByLogin("login");
		Assert.assertTrue(icChoice == null);
	}

	@Test
	public void findJsChoicesByLoginWhenNotInDb() {
		JobSectorChoice jsChoice = choiceService.findJsChoicesByLogin("login");
		Assert.assertTrue(jsChoice == null);
	}

	@Test
	public void saveEmptyJobSectorChoices() {
		Choice jobSectorChoices = new JobSectorChoice();
		jobSectorChoices.setLogin("login");
		choiceService.save(jobSectorChoices);
	}

	@Test
	public void saveEmptyImprovementCourseChoices() {
		Choice improvementCourseChoices = new ImprovementCourseChoice();
		improvementCourseChoices.setLogin("login");
		choiceService.save(improvementCourseChoices);
	}

	@Test
	public void saveFullJobSectorChoices() {
		Choice jobSectorChoices = createFullChoices("JobSector");
		choiceService.save(jobSectorChoices);
	}

	@Test
	public void saveFullImprovementCourseChoices() {
		Choice jobSectorChoices = createFullChoices("ImprovementCourse");
		choiceService.save(jobSectorChoices);
	}

	public Choice createFullChoices(String type) {
		Choice choices;
		if (type.equals("ImprovementCourse")) {
			choices = new ImprovementCourseChoice();
		} else {
			choices = new JobSectorChoice();
		}
		choices.setChoice1("AAA");
		choices.setChoice2("AAA");
		choices.setChoice3("AAA");
		choices.setChoice4("AAA");
		choices.setChoice5("AAA");
		choices.setLogin("login");
		return choices;
	}

	@Test
	public void saveJobSectorChoices() {
		Choice jobSectorChoices = new JobSectorChoice();
		jobSectorChoices.setChoice1("AAA");
		jobSectorChoices.setChoice2("BBB");
		jobSectorChoices.setLogin("login");
		choiceService.save(jobSectorChoices);
	}

	@Test
	public void saveImprovementCourseChoices() {
		Choice improvementCourseChoices = new ImprovementCourseChoice();
		improvementCourseChoices.setChoice1("AAA");
		improvementCourseChoices.setChoice2("BBB");
		improvementCourseChoices.setLogin("login");
		choiceService.save(improvementCourseChoices);
	}

	@Test
	public void choicesRetrievment() {
		saveFullJobSectorChoices();
		Choice jobSectorChoices = choiceService.findJobSectorChoiceByLogin("login");
		Assert.assertTrue(createFullChoices("JobSector").equals(jobSectorChoices));
	}

	@Test
	public void distinctionJobSectorImprovementCourse() {
		saveFullJobSectorChoices();
		Choice jobSectorChoices = choiceService.findJobSectorChoiceByLogin("login");
		Assert.assertFalse(createFullChoices("ImprovementCourse").equals(jobSectorChoices));
	}

	@Test
	public void numberJobSector() {
		for (int i = 0; i < 10; i++) {
			Choice choices = createFullChoices("JobSector", "login" + i);
			choiceService.save(choices);
		}
		List<JobSectorChoice> allChoices = choiceService.findJobSectorChoices();
		Assert.assertEquals(10, allChoices.size());
	}

	@Test
	public void numberImprovementCourse() {
		for (int i = 0; i < 10; i++) {
			Choice choices = createFullChoices("ImprovementCourse", "login" + i);
			choiceService.save(choices);
		}
		List<ImprovementCourseChoice> allChoices = choiceService.findImprovementCourseChoices();
		Assert.assertEquals(10, allChoices.size());
	}

	@Test
	public void update() {
		Choice choices = createFullChoices("ImprovementCourse", "login");
		choiceService.save(choices);
		choices.setChoice1("BBB");
		choiceService.save(choices);
		Assert.assertEquals(1, choiceService.findImprovementCourseChoices().size());
	}

	@Test
	public void getStudentsByChoiceAndSpecialization() {
		saveFullJobSectorChoices();
		Specialization specialization = new JobSector();
		specialization.setAbbreviation("AAA");
		Assert.assertTrue(choiceService.findLoginsByOrderChoiceAndSpecialization(1, specialization).contains("login"));
		saveFullImprovementCourseChoices();
		specialization = new ImprovementCourse();
		specialization.setAbbreviation("AAA");
		Assert.assertTrue(choiceService.findLoginsByOrderChoiceAndSpecialization(1, specialization).contains("login"));
	}

	@Test
	public void count() {
		Choice choice = new Choice();
		for (int i = 0; i < 10; i++) {
			choice = createFullChoices("ImprovementCourse", "login" + i);
			choiceService.save(choice);
			choice = createFullChoices("Job Sector", "login" + i);
			choiceService.save(choice);
		}
		Assert.assertEquals(20, choiceService.findImprovementCourseChoices().size() + choiceService.findJobSectorChoices().size());
	}

	@Test
	public void otherChoicesRepartitionIcOne() {
		Choice choice = new ImprovementCourseChoice();
		choice.setLogin("login");
		choice.setChoice1("AAA");
		choice.setChoice3("DDD");
		choiceService.save(choice);
		Map<String, List<String>> otherChoiceRepartition = choiceService.findChoiceRepartitionKnowingOne(1, 3, "AAA", Specialization.IMPROVEMENT_COURSE);
		List<String> logins = otherChoiceRepartition.get("DDD");
		Assert.assertTrue(otherChoiceRepartition.keySet().size() == 1);
		Assert.assertTrue(otherChoiceRepartition.size() == 1);
		Assert.assertTrue(otherChoiceRepartition.containsKey("DDD"));
		Assert.assertTrue(logins.contains("login"));
	}

	@Test
	public void otherChoicesRepartitionIcMultiple() {
		Choice choice;
		int testNumber = 10;
		for (int i = 0; i < testNumber; i++) {
			choice = new ImprovementCourseChoice();
			choice.setLogin("login" + i);
			choice.setChoice1("AAA");
			choice.setChoice4("DDD");
			choiceService.save(choice);
		}
		Map<String, List<String>> otherChoiceRepartition = choiceService.findChoiceRepartitionKnowingOne(1, 4, "AAA", Specialization.IMPROVEMENT_COURSE);
		Assert.assertTrue(otherChoiceRepartition.containsKey("DDD"));
		List<String> logins = otherChoiceRepartition.get("DDD");
		Assert.assertTrue(otherChoiceRepartition.keySet().size() == 1);
		Assert.assertTrue(logins.size() == testNumber);
		for (int i = 0; i < testNumber; i++) {
			Assert.assertTrue(logins.contains("login" + i));
		}
	}

	@Test
	public void otherChoicesRepartitionIcMultipleDifferent() {
		Choice choice;
		choice = new ImprovementCourseChoice();
		choice.setLogin("login1");
		choice.setChoice4("AAA");
		choice.setChoice3("DDD");
		choiceService.save(choice);
		choice = new ImprovementCourseChoice();
		choice.setLogin("login2");
		choice.setChoice4("AAA");
		choice.setChoice3("EEE");
		choiceService.save(choice);
		Map<String, List<String>> otherChoiceRepartition = choiceService.findChoiceRepartitionKnowingOne(4, 3, "AAA", Specialization.IMPROVEMENT_COURSE);
		Assert.assertTrue(otherChoiceRepartition.keySet().size() == 2);
		Assert.assertTrue(otherChoiceRepartition.containsKey("DDD"));
		Assert.assertTrue(otherChoiceRepartition.containsKey("EEE"));
		Assert.assertTrue(otherChoiceRepartition.get("DDD").size() == 1);
		Assert.assertTrue(otherChoiceRepartition.get("EEE").size() == 1);
		Assert.assertTrue(otherChoiceRepartition.get("DDD").contains("login1"));
		Assert.assertTrue(otherChoiceRepartition.get("EEE").contains("login2"));
	}

	@Test
	public void otherChoicesRepartitionJsOne() {
		Choice choice = new JobSectorChoice();
		choice.setLogin("login");
		choice.setChoice1("AAA");
		choice.setChoice3("DDD");
		choiceService.save(choice);
		Map<String, List<String>> otherChoiceRepartition = choiceService.findChoiceRepartitionKnowingOne(1, 3, "AAA", Specialization.JOB_SECTOR);
		List<String> logins = otherChoiceRepartition.get("DDD");
		Assert.assertTrue(otherChoiceRepartition.keySet().size() == 1);
		Assert.assertTrue(otherChoiceRepartition.size() == 1);
		Assert.assertTrue(otherChoiceRepartition.containsKey("DDD"));
		Assert.assertTrue(logins.contains("login"));
	}

	@Test
	public void otherChoicesRepartitionJsMultiple() {
		Choice choice;
		int testNumber = 10;
		for (int i = 0; i < testNumber; i++) {
			choice = new JobSectorChoice();
			choice.setLogin("login" + i);
			choice.setChoice1("AAA");
			choice.setChoice2("DDD");
			choiceService.save(choice);
		}
		Map<String, List<String>> otherChoiceRepartition = choiceService.findChoiceRepartitionKnowingOne(1, 2, "AAA", Specialization.JOB_SECTOR);
		Assert.assertTrue(otherChoiceRepartition.containsKey("DDD"));
		List<String> logins = otherChoiceRepartition.get("DDD");
		Assert.assertTrue(otherChoiceRepartition.keySet().size() == 1);
		Assert.assertTrue(logins.size() == testNumber);
		for (int i = 0; i < testNumber; i++) {
			Assert.assertTrue(logins.contains("login" + i));
		}
	}
	
	@Test
	public void otherChoicesRepartitionJsMultipleDifferent() {
		Choice choice;
		choice = new JobSectorChoice();
		choice.setLogin("login1");
		choice.setChoice4("AAA");
		choice.setChoice3("DDD");
		choiceService.save(choice);
		choice = new JobSectorChoice();
		choice.setLogin("login2");
		choice.setChoice4("AAA");
		choice.setChoice3("EEE");
		choiceService.save(choice);
		Map<String, List<String>> otherChoiceRepartition = choiceService.findChoiceRepartitionKnowingOne(4, 3, "AAA", Specialization.JOB_SECTOR);
		Assert.assertTrue(otherChoiceRepartition.keySet().size() == 2);
		Assert.assertTrue(otherChoiceRepartition.containsKey("DDD"));
		Assert.assertTrue(otherChoiceRepartition.containsKey("EEE"));
		Assert.assertTrue(otherChoiceRepartition.get("DDD").size() == 1);
		Assert.assertTrue(otherChoiceRepartition.get("EEE").size() == 1);
		Assert.assertTrue(otherChoiceRepartition.get("DDD").contains("login1"));
		Assert.assertTrue(otherChoiceRepartition.get("EEE").contains("login2"));
	}

	public Choice createFullChoices(String type, String login) {
		Choice choices;
		if (type == "ImprovementCourse") {
			choices = new ImprovementCourseChoice();
		} else {
			choices = new JobSectorChoice();
		}
		choices.setChoice1("AAA");
		choices.setChoice2("AAA");
		choices.setChoice3("AAA");
		choices.setChoice4("AAA");
		choices.setChoice5("AAA");
		choices.setLogin(login);
		return choices;
	}

	@Test
	public void deleteJsChoices() {
		Choice choice;
		for (int i = 0; i < 10; i++) {
			choice = createFullChoices("JobSector", "" + i);
			choiceService.save(choice);
			choiceService.delete(choice);
		}
		Assert.assertTrue(choiceService.findJobSectorChoices().size() == 0);
	}

	@Test
	public void deleteIcChoices() {
		Choice choice = createFullChoices("ImprovementCourse", "haha");
		choiceService.save(choice);
		choiceService.delete(choice);
		Assert.assertTrue(choiceService.findImprovementCourseChoices().size() == 0);
	}

	@Test
	public void elementsNotFilledJs() {
		Choice choice = new JobSectorChoice();
		choice.setLogin("login");
		choice.setChoice1("AAA");
		choice.setChoice4("BBB");
		choiceService.save(choice);
		List<Integer> notFilled = choiceService.findElementNotFilledJobSector("login");
		Assert.assertTrue(notFilled.size() == 3);
		Assert.assertTrue(notFilled.contains(2));
		Assert.assertTrue(notFilled.contains(3));
		Assert.assertTrue(notFilled.contains(5));
		Assert.assertFalse(notFilled.contains(1));
		Assert.assertFalse(notFilled.contains(4));
	}

	@Test
	public void elementsNotFilledIc() {
		Choice choice = new ImprovementCourseChoice();
		choice.setLogin("login");
		choice.setChoice1("AAA");
		choice.setChoice4("BBB");
		choiceService.save(choice);
		List<Integer> notFilled = choiceService.findElementNotFilledImprovementCourse("login");
		Assert.assertTrue(notFilled.size() == 3);
		Assert.assertTrue(notFilled.contains(2));
		Assert.assertTrue(notFilled.contains(3));
		Assert.assertTrue(notFilled.contains(5));
		Assert.assertFalse(notFilled.contains(1));
		Assert.assertFalse(notFilled.contains(4));
	}

	@Test
	public void improvementCourseChoiceRetrievement() {
		Choice choices = createFullChoices("ImprovementCourse", "login");
		choiceService.save(choices);
		Assert.assertTrue(choiceService.findImprovementCourseChoiceByLogin("login") != null);
		Assert.assertTrue(choiceService.findImprovementCourseChoiceByLogin("login").getChoice1().equals("AAA"));
	}

	@Test
	public void jobSectorChoiceRetrievement() {
		Choice choices = createFullChoices("JobSector", "login");
		choiceService.save(choices);
		Assert.assertTrue(choiceService.findJobSectorChoiceByLogin("login") != null);
		Assert.assertTrue(choiceService.findJobSectorChoiceByLogin("login").getChoice1().equals("AAA"));
	}

	@Test
	public void deleteAll() {
		Choice choice = new Choice();
		for (int i = 0; i < 10; i++) {
			choice = createFullChoices("ImprovementCourse", "login" + i);
			choiceService.save(choice);
			choice = createFullChoices("Job Sector", "login" + i);
			choiceService.save(choice);
		}
		choiceService.deleteAll();
		Assert.assertTrue(choiceService.findJobSectorChoices().size() == 0);
		Assert.assertTrue(choiceService.findImprovementCourseChoices().size() == 0);
	}
}
