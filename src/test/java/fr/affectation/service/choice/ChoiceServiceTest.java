package fr.affectation.service.choice;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Test;

import junit.framework.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.affectation.domain.choice.Choice;
import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.Specialization;
import fr.affectation.service.choice.ChoiceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ChoiceServiceTest {
	
	@Inject
	private ChoiceService choiceService;
	
	@Inject
	private SessionFactory sessionFactory;
	
	@After
	public void cleanDb() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from JobSectorChoice").executeUpdate();
		session.createQuery("delete from ImprovementCourseChoice").executeUpdate();
		transaction.commit();
		session.close();
	}
	
	@Test
	public void saveEmptyJobSectorChoices(){
		Choice jobSectorChoices = new JobSectorChoice();
		jobSectorChoices.setLogin("login");
		choiceService.save(jobSectorChoices);
	}
	
	@Test
	public void saveEmptyImprovementCourseChoices(){
		Choice improvementCourseChoices = new ImprovementCourseChoice();
		improvementCourseChoices.setLogin("login");
		choiceService.save(improvementCourseChoices);
	}
	
	@Test
	public void saveFullJobSectorChoices(){
		Choice jobSectorChoices = createFullChoices("JobSector");
		choiceService.save(jobSectorChoices);
	}
	
	@Test
	public void saveFullImprovementCourseChoices(){
		Choice jobSectorChoices = createFullChoices("ImprovementCourse");
		choiceService.save(jobSectorChoices);
	}
	
	public Choice createFullChoices(String type){
		Choice choices;
		if (type == "ImprovementCourse"){
			choices = new ImprovementCourseChoice();
		}
		else{
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
	public void saveJobSectorChoices(){
		Choice jobSectorChoices = new JobSectorChoice();
		jobSectorChoices.setChoice1("AAA");
		jobSectorChoices.setChoice2("BBB");
		jobSectorChoices.setLogin("login");
		choiceService.save(jobSectorChoices);
	}
	
	@Test
	public void saveImprovementCourseChoices(){
		Choice improvementCourseChoices = new ImprovementCourseChoice();
		improvementCourseChoices.setChoice1("AAA");
		improvementCourseChoices.setChoice2("BBB");
		improvementCourseChoices.setLogin("login");
		choiceService.save(improvementCourseChoices);
	}
	
	@Test
	public void choicesRetrievment(){
		saveFullJobSectorChoices();
		Choice jobSectorChoices = choiceService.getJobSectorChoicesByLogin("login");
		Assert.assertTrue(createFullChoices("JobSector").equals(jobSectorChoices));
	}
	
	@Test
	public void distinctionJobSectorImprovementCourse(){
		saveFullJobSectorChoices();
		Choice jobSectorChoices = choiceService.getJobSectorChoicesByLogin("login");
		Assert.assertFalse(createFullChoices("ImprovementCourse").equals(jobSectorChoices));
	}
	
	@Test
	public void numberJobSector(){
		for (int i=0; i<10; i ++){
			Choice choices = createFullChoices("JobSector", "login" + i);
			choiceService.save(choices);
		}
		List<JobSectorChoice> allChoices = choiceService.findAllJobSectorChoices();
		Assert.assertEquals(10, allChoices.size());
	}
	
	@Test
	public void numberImprovementCourse(){
		for (int i=0; i<10; i ++){
			Choice choices = createFullChoices("ImprovementCourse", "login" + i);
			choiceService.save(choices);
		}
		List<ImprovementCourseChoice> allChoices = choiceService.findAllImprovementCourseChoices();
		Assert.assertEquals(10, allChoices.size());
	}
	
	@Test
	public void update(){
		Choice choices = createFullChoices("ImprovementCourse", "login");
		choiceService.save(choices);
		choices.setChoice1("BBB");
		choiceService.save(choices);
		Assert.assertEquals(1, choiceService.findAllImprovementCourseChoices().size());
	}
	
	@Test
	public void getStudentsByChoiceAndSpecialization(){
		saveFullJobSectorChoices();
		Specialization specialization = new JobSector();
		specialization.setAbbreviation("AAA");
		Assert.assertTrue(choiceService.getLoginsByOrderChoiceAndSpecialization(1, specialization).contains("login"));
		saveFullImprovementCourseChoices();
		specialization = new ImprovementCourse();
		specialization.setAbbreviation("AAA");
		Assert.assertTrue(choiceService.getLoginsByOrderChoiceAndSpecialization(1, specialization).contains("login"));
	}
	
	@Test
	public void count(){
		Choice choice = new Choice();
		for (int i = 0; i<10; i++){
			choice = createFullChoices("ImprovementCourse", "login" + i);
			choiceService.save(choice);
			choice = createFullChoices("Job Sector", "login" + i);
			choiceService.save(choice);
		}
		Assert.assertEquals(20, choiceService.findAllImprovementCourseChoices().size() + choiceService.findAllJobSectorChoices().size());
	}
	
	public Choice createFullChoices(String type, String login){
		Choice choices;
		if (type == "ImprovementCourse"){
			choices = new ImprovementCourseChoice();
		}
		else{
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
	public void deleteJsChoices(){
		Choice choice;
		for (int i=0; i<10; i++){
			choice = createFullChoices("JobSector", "" + i);
			choiceService.save(choice);
			choiceService.delete(choice);
		}
		Assert.assertTrue(choiceService.findAllJobSectorChoices().size() == 0);
	}
	
	@Test
	public void deleteIcChoices(){
		Choice choice = createFullChoices("ImprovementCourse", "haha");
		choiceService.save(choice);
		choiceService.delete(choice);
		Assert.assertTrue(choiceService.findAllImprovementCourseChoices().size() == 0);
	}
	
	@Test
	public void improvementCourseChoiceRetrievement(){
		Choice choices = createFullChoices("ImprovementCourse", "login");
		choiceService.save(choices);
		Assert.assertTrue(choiceService.getImprovementCourseChoicesByLogin("login") != null);
		Assert.assertTrue(choiceService.getImprovementCourseChoicesByLogin("login").getChoice1().equals("AAA"));
	}
	
	@Test
	public void jobSectorChoiceRetrievement(){
		Choice choices = createFullChoices("JobSector", "login");
		choiceService.save(choices);
		Assert.assertTrue(choiceService.getJobSectorChoicesByLogin("login") != null);
		Assert.assertTrue(choiceService.getJobSectorChoicesByLogin("login").getChoice1().equals("AAA"));
	}
	
}
