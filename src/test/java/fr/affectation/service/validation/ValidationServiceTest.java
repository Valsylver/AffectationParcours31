package fr.affectation.service.validation;

import javax.inject.Inject;

import junit.framework.Assert;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.affectation.domain.student.StudentValidation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ValidationServiceTest {
	
	@Inject
	private ValidationService validationService;
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Test
	public void saveValidationAccepted(){
		validationService.saveStudentValidation("login", true, true);
		Assert.assertTrue(validationService.isValidatedIc("login"));
		Assert.assertTrue(validationService.isValidatedJs("login"));
	}
	
	@Test
	public void saveValidationRefused(){
		validationService.saveStudentValidation("login", false, false);
		Assert.assertTrue(!validationService.isValidatedIc("login"));
		Assert.assertTrue(!validationService.isValidatedJs("login"));
	}
	
	@Test
	public void saveValidationCrossed(){
		validationService.saveStudentValidation("login", true, false);
		Assert.assertTrue(validationService.isValidatedIc("login"));
		Assert.assertTrue(!validationService.isValidatedJs("login"));
		
		validationService.saveStudentValidation("login2", false, true);
		Assert.assertTrue(!validationService.isValidatedIc("login2"));
		Assert.assertTrue(validationService.isValidatedJs("login2"));
	}
	
	@Test
	public void saveStudentNumber(){
		for (int i=0; i<10; i++){
			validationService.saveStudentValidation(new StudentValidation("login"+i, true, true));
		}
		Assert.assertTrue(validationService.findStudentsValidatedIc().size() == 10);
	}
	
	@Test
	public void deleteAllStudents(){
		for (int i=0; i<10; i++){
			validationService.saveStudentValidation(new StudentValidation("login"+i, true, true));
		}
		validationService.deleteAllStudents();
		Assert.assertTrue(validationService.findStudentsValidatedIc().size() == 0);
	}
	
	@Test
	public void deletStudentByLogin(){
		validationService.saveStudentValidation(new StudentValidation("login", true, true));
		validationService.deleteStudentByLogin("login");
		Assert.assertFalse(validationService.isInValidationProcess("login"));
		Assert.assertFalse(validationService.isValidatedIc("login"));
		Assert.assertFalse(validationService.isValidatedIc("login"));
	}
	
	@Test
	public void updateValidationIc(){
		validationService.saveStudentValidation(new StudentValidation("login", true, true));
		validationService.updateIcValidation("login", false);
		Assert.assertFalse(validationService.isValidatedIc("login"));
		Assert.assertTrue(validationService.isValidatedJs("login"));
		Assert.assertTrue(validationService.isInValidationProcess("login"));
	}
	
	@Test
	public void updateValidationJs(){
		validationService.saveStudentValidation(new StudentValidation("login", true, true));
		validationService.updateJsValidation("login", false);
		Assert.assertFalse(validationService.isValidatedJs("login"));
		Assert.assertTrue(validationService.isValidatedIc("login"));
		Assert.assertTrue(validationService.isInValidationProcess("login"));
	}
	
	@After
	public void cleanDb() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from StudentValidation").executeUpdate();
		transaction.commit();
		session.close();
	}
}
