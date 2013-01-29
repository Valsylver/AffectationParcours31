package fr.affectation.service.authentication;

import java.util.List;

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

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.superuser.Admin;
import fr.affectation.service.agap.AgapService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.superuser.SuperUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AuthenticationServiceTest {
	
	@Inject
	private AuthenticationService authenticationService;
	
	@Inject
	private SuperUserService superUserService;
	
	@Inject
	private SpecializationService specializationService;
	
	@Inject 
	private SessionFactory sessionFactory;
	
	@Inject
	private AgapService agapService;
	
	@After
	public void cleanDb() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from Admin").executeUpdate();
		session.createQuery("delete from ImprovementCourse").executeUpdate();
		session.createQuery("delete from JobSector").executeUpdate();
		transaction.commit();
		session.close();
	}
	
	@Test
	public void studentTrue(){
		if (agapService.getAllStudentConcernedLogin().size() > 0){
			String login = chooseRandomLogin();
			Assert.assertTrue(authenticationService.isStudent(login));
		}
	}
	
	@Test
	public void studentFalse(){
		Assert.assertTrue(! authenticationService.isStudent("afakestudentlogin"));
	}
	
	@Test
	public void responsibleJS(){
		Specialization specialization = new JobSector();
		specialization.setAbbreviation("ABB");
		specialization.setResponsibleLogin("login");
		specializationService.save(specialization);
		Assert.assertTrue(authenticationService.isResponsible("login"));
	}
	
	@Test
	public void responsibleIC(){
		Specialization specialization = new ImprovementCourse();
		specialization.setAbbreviation("ABB");
		specialization.setResponsibleLogin("login");
		specializationService.save(specialization);
		Assert.assertTrue(authenticationService.isResponsible("login"));
	}
	
	@Test
	public void responsibleFalse(){
		Assert.assertTrue(! authenticationService.isResponsible("afakeresponsibleLogin"));
	}
	
	@Test
	public void admin(){
		Admin admin = new Admin();
		admin.setLogin("admin");
		superUserService.saveAdmin(admin);
		Assert.assertTrue(authenticationService.isAdmin("admin"));
	}
	
	public String chooseRandomLogin(){
		List<String> allLogin = agapService.getAllStudentConcernedLogin();
		return allLogin.get((int) (Math.random() * allLogin.size()));   
	}

}
