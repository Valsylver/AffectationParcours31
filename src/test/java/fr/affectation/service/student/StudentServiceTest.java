package fr.affectation.service.student;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.affectation.domain.choice.Choice;
import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.Specialization;
import fr.affectation.service.agap.AgapService;
import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.validation.ValidationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class StudentServiceTest {
	
	@Inject
	private StudentService studentService;
	
	@Inject
	private ChoiceService choiceService;
	
	@Inject
	private ValidationService validationService;
	
	@Inject
	private SpecializationService specializationService;
	
	@Inject
	private AgapService agapService;
	
	@Test
	public void repartitionOtherTypeJs(){
		Choice choice;
		choice = new JobSectorChoice();
		choice.setLogin("login");
		choice.setChoice1("JS1");
		choiceService.save(choice);
		choice = new ImprovementCourseChoice();
		choice.setLogin("login");
		choice.setChoice1("IC1");
		choiceService.save(choice);
		Map<String, List<String>> otherTypeRepartition = studentService.findChoiceRepartitionForTheOtherType("IC1", Specialization.IMPROVEMENT_COURSE);
		Assert.assertTrue(otherTypeRepartition.keySet().size() == 1);
		Assert.assertTrue(otherTypeRepartition.containsKey("JS1"));
		Assert.assertTrue(otherTypeRepartition.get("JS1").size() == 1);
		Assert.assertTrue(otherTypeRepartition.get("JS1").contains("login"));
	}
	
	@Test
	public void repartitionOtherTypeIc(){
		Choice choice;
		choice = new JobSectorChoice();
		choice.setLogin("login");
		choice.setChoice1("JS1");
		choiceService.save(choice);
		choice = new ImprovementCourseChoice();
		choice.setLogin("login");
		choice.setChoice1("IC1");
		choiceService.save(choice);
		Map<String, List<String>> otherTypeRepartition = studentService.findChoiceRepartitionForTheOtherType("JS1", Specialization.JOB_SECTOR);
		Assert.assertTrue(otherTypeRepartition.keySet().size() == 1);
		Assert.assertTrue(otherTypeRepartition.containsKey("IC1"));
		Assert.assertTrue(otherTypeRepartition.get("IC1").size() == 1);
		Assert.assertTrue(otherTypeRepartition.get("IC1").contains("login"));
	}
	
	@Test
	public void inverseRepartitionWhenNoChoices(){
		JobSector js = new JobSector();
		js.setAbbreviation("JS1");
		js.setName("Job sector 1");
		specializationService.save(js);
		studentService.findInverseRepartition(js);
	}
	
	@Test
	public void testRetrieveStudentDataFromAgap(){
		String login = findRandomExistingLogin();
		if (!login.equals("")){
			studentService.retrieveStudentByLogin(login, "");
		}
	}
	
	public String findRandomExistingLogin() {
		List<String> existingLogins = agapService.findStudentConcernedLogins();
		if (existingLogins.size() != 0) {
			return existingLogins.get((int) (Math.random() * existingLogins
					.size()));
		}
		return "";
	}
	
	@Before
	public void deleteBefore(){
		choiceService.deleteAll();
		validationService.removeAll();
		specializationService.deleteAll();
	}
	
	@After
	public void deleteAfter(){
		choiceService.deleteAll();
		validationService.removeAll();
		specializationService.deleteAll();
	}

}
