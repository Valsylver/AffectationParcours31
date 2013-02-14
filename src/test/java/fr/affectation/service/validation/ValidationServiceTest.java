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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ValidationServiceTest {
	
	@Inject
	private ValidationService validationService;
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Test
	public void saveValidationAccepted(){
		validationService.save("login", true, true);
		Assert.assertTrue(validationService.isValidatedIc("login"));
		Assert.assertTrue(validationService.isValidatedJs("login"));
	}
	
	@Test
	public void saveValidationRefused(){
		validationService.save("login", false, false);
		Assert.assertTrue(!validationService.isValidatedIc("login"));
		Assert.assertTrue(!validationService.isValidatedJs("login"));
	}
	
	@Test
	public void saveValidationCrossed(){
		validationService.save("login", true, false);
		Assert.assertTrue(validationService.isValidatedIc("login"));
		Assert.assertTrue(!validationService.isValidatedJs("login"));
		
		validationService.save("login2", false, true);
		Assert.assertTrue(!validationService.isValidatedIc("login2"));
		Assert.assertTrue(validationService.isValidatedJs("login2"));
	}
	
	@Test
	public void saveStudentNumber(){
		for (int i=0; i<10; i++){
			validationService.save("login"+i, true, true);
		}
		Assert.assertTrue(validationService.findStudentValidatedIcLogins().size() == 10);
	}
	
	@Test
	public void deleteAllStudents(){
		for (int i=0; i<10; i++){
			validationService.save("login"+i, true, true);
		}
		validationService.removeAll();
		Assert.assertTrue(validationService.findStudentValidatedIcLogins().size() == 0);
	}
	
	@Test
	public void deletStudentByLogin(){
		validationService.save("login", true, true);
		validationService.remove("login");
		Assert.assertFalse(validationService.isInValidationProcess("login"));
		Assert.assertFalse(validationService.isValidatedIc("login"));
		Assert.assertFalse(validationService.isValidatedIc("login"));
	}
	
	@Test
	public void updateValidationIc(){
		validationService.save("login", true, true);
		validationService.updateIcValidation("login", false);
		Assert.assertFalse(validationService.isValidatedIc("login"));
		Assert.assertTrue(validationService.isValidatedJs("login"));
		Assert.assertTrue(validationService.isInValidationProcess("login"));
	}
	
	@Test
	public void updateValidationJs(){
		validationService.save("login", true, true);
		validationService.updateJsValidation("login", false);
		Assert.assertFalse(validationService.isValidatedJs("login"));
		Assert.assertTrue(validationService.isValidatedIc("login"));
		Assert.assertTrue(validationService.isInValidationProcess("login"));
	}
	
	@Test
	public void findStudentValidatedIcTrue(){
		validationService.save("login", true, true);
		Assert.assertTrue(validationService.findStudentValidatedIcLogins().contains("login"));
	}
	
	@Test
	public void findStudentValidatedIcFalse(){
		validationService.save("login", true, true);
		validationService.updateIcValidation("login", false);
		Assert.assertFalse(validationService.findStudentValidatedIcLogins().contains("login"));
	}
	
	@Test
	public void findStudentValidatedJsTrue(){
		validationService.save("login", true, true);
		Assert.assertTrue(validationService.findStudentValidatedJsLogins().contains("login"));
	}
	
	@Test
	public void findStudentValidatedJsFalse(){
		validationService.save("login", true, true);
		validationService.updateJsValidation("login", false);
		Assert.assertFalse(validationService.findStudentValidatedJsLogins().contains("login"));
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
