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
	public void deleteAll(){
		for (int i=0; i<10; i++){
			validationService.saveStudentValidation(new StudentValidation("login"+i, true));
		}
		validationService.deleteAllStudents();
		Assert.assertTrue(validationService.getAllStudentsValidatedLogin().size() == 0);
		Assert.assertTrue(validationService.getAllStudentsValidated().size() == 0);
	}
	
	@Test
	public void addStudent(){
		validationService.saveStudentValidation(new StudentValidation("login", true));
		Assert.assertTrue(validationService.getAllStudentsValidatedLogin().contains("login"));
	}
	
	@Test
	public void isValidate(){
		validationService.saveStudentValidation(new StudentValidation("login", true));
		Assert.assertTrue(validationService.isValidated("login"));
	}
	
	@Test
	public void isNotValidate(){
		validationService.saveStudentValidation(new StudentValidation("login", false));
		Assert.assertFalse(validationService.isValidated("login"));
	}
	
	@Test
	public void isNotValidateWhenNotInDb(){
		Assert.assertFalse(validationService.isValidated("login"));
	}
	
	@Test
	public void deleteStudent(){
		validationService.saveStudentValidation(new StudentValidation("login", true));
		validationService.deleteStudentByLogin("login");
		Assert.assertFalse(validationService.isValidated("login"));
	}
	
	@Test
	public void getAllStudentsValidated(){
		for (int i=0; i<10; i++){
			validationService.saveStudentValidation(new StudentValidation("login"+i, true));
		}
		Assert.assertTrue(validationService.getAllStudentsValidatedLogin().size() == 10);
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
