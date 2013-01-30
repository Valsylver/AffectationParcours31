package fr.affectation.service.student;

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
public class StudentServiceTest {
	
	@Inject
	private StudentService studentService;
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Test
	public void saveStudentToExclude(){
		StudentToExclude student = new StudentToExclude("login");
		studentService.saveStudentToExclude(student);
	}
	
	@Test
	public void isStudentToExcludeSaved(){
		StudentToExclude student = new StudentToExclude("login");
		studentService.saveStudentToExclude(student);
		Assert.assertTrue(studentService.findAllStudentToExcludeLogin().contains("login"));
	}
	
	@Test
	public void deleteAllStudentToExclude(){
		studentService.deleteAllStudentToExclude();
	}
	
	@Test
	public void deleteStudent(){
		StudentToExclude student = new StudentToExclude("login");
		studentService.saveStudentToExclude(student);
		studentService.removeStudentByLogin("login");
		Assert.assertTrue(!studentService.findAllStudentToExcludeLogin().contains("login"));
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
