package fr.affectation.service.exclusion;

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

import fr.affectation.domain.student.StudentToExclude;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ExclusionServiceTest {
	
	@Inject
	private ExclusionService exclusionService;
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Test
	public void saveStudentToExclude(){
		StudentToExclude student = new StudentToExclude("login");
		exclusionService.save(student);
	}
	
	@Test
	public void isStudentToExcludeSaved(){
		StudentToExclude student = new StudentToExclude("login");
		exclusionService.save(student);
		Assert.assertTrue(exclusionService.findStudentToExcludeLogins().contains("login"));
	}
	
	@Test
	public void deleteAllStudentToExclude(){
		exclusionService.deleteAllStudentsToExclude();
	}
	
	@Test
	public void deleteStudent(){
		StudentToExclude student = new StudentToExclude("login");
		exclusionService.save(student);
		exclusionService.removeStudentByLogin("login");
		Assert.assertTrue(!exclusionService.findStudentToExcludeLogins().contains("login"));
	}
	
	@Test
	public void findByLogin(){
		StudentToExclude student = new StudentToExclude("login");
		exclusionService.save(student);
		Assert.assertTrue(exclusionService.findByLogin("login") != null);
	}
	
	@After
	public void cleanDb() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from StudentToExclude").executeUpdate();
		transaction.commit();
		session.close();
	}


}
