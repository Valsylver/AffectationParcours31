package fr.affectation.service.student;

import java.util.Arrays;
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
import fr.affectation.domain.specialization.Specialization;
import fr.affectation.service.choice.ChoiceService;
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
	public void validateStudentFromListIc(){
		List<String> logins = Arrays.asList("login1", "login2", "login3");
		for (String login : logins){
			validationService.save(login, true, true);
		}
		List<Boolean> validation = Arrays.asList(false, true, false);
		studentService.updateValidationFromList(logins, validation, Specialization.IMPROVEMENT_COURSE);
		Assert.assertFalse(validationService.isValidatedIc("login1"));
		Assert.assertTrue(validationService.isValidatedIc("login2"));
		Assert.assertFalse(validationService.isValidatedIc("login3"));
	}
	
	@Test
	public void validateStudentFromListJs(){
		List<String> logins = Arrays.asList("login1", "login2", "login3");
		for (String login : logins){
			validationService.save(login, true, true);
		}
		List<Boolean> validation = Arrays.asList(false, true, false);
		studentService.updateValidationFromList(logins, validation, Specialization.JOB_SECTOR);
		Assert.assertFalse(validationService.isValidatedJs("login1"));
		Assert.assertTrue(validationService.isValidatedJs("login2"));
		Assert.assertFalse(validationService.isValidatedJs("login3"));
	}
	
	@Test
	public void validateStudentFromListDistinctionSpec(){
		List<String> logins = Arrays.asList("login");
		validationService.save("login", true, true);
		List<Boolean> validation = Arrays.asList(false);
		studentService.updateValidationFromList(logins, validation, Specialization.IMPROVEMENT_COURSE);
		Assert.assertFalse(validationService.isValidatedIc("login"));
		Assert.assertTrue(validationService.isValidatedJs("login"));
	}
	
	@Before
	public void deleteBefore(){
		choiceService.deleteAll();
		validationService.removeAll();
	}
	
	@After
	public void deleteAfter(){
		choiceService.deleteAll();
		validationService.removeAll();
	}

}
