package fr.affectation.service.exclusion;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ExclusionServiceTest {
	
	@Inject
	private ExclusionService exclusionService;
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Test
	public void isStudentToExcludeSaved(){
		exclusionService.save("login");
		Assert.assertTrue(exclusionService.findStudentToExcludeLogins().contains("login"));
	}
	
	@Test
	public void findStudentToExcludeLogins(){
		for (int i=0; i<10; i++){
			exclusionService.save("login" + i);
		}
		Assert.assertTrue(exclusionService.findStudentToExcludeLogins().size() == 10);
	}
	
	@Test
	public void deleteAllStudentToExclude(){
		for (int i=0; i<10; i++){
			exclusionService.save("login" + i);
		}
		exclusionService.removeAll();
		Assert.assertTrue(exclusionService.findStudentToExcludeLogins().size() == 0);
	}
	
	@Test
	public void removeStudent(){
		exclusionService.save("login");
		exclusionService.remove("login");
		Assert.assertTrue(!exclusionService.findStudentToExcludeLogins().contains("login"));
	}
	
	@Test
	public void isExcluded(){
		exclusionService.save("login");
		Assert.assertTrue(exclusionService.isExcluded("login"));
	}
	
	@Test
	public void duplication(){
		exclusionService.save("login");
		exclusionService.save("login");
		Assert.assertTrue(exclusionService.findStudentToExcludeLogins().size() == 1);
	}
	
	@After
	public void cleanDbAfter(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from StudentToExclude").executeUpdate();
		transaction.commit();
		session.close();
	}
	
	@Before
	public void cleanDbBefore(){
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from StudentToExclude").executeUpdate();
		transaction.commit();
		session.close();
	}


}
