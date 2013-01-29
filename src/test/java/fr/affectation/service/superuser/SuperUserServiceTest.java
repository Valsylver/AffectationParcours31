package fr.affectation.service.superuser;

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

import fr.affectation.domain.superuser.Admin;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SuperUserServiceTest {
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Inject 
	private SuperUserService superUserService;

	@After
	public void cleanDb() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from Admin").executeUpdate();
		transaction.commit();
		session.close();
	}
	
	@Test
	public void saveAdmin(){
		Admin admin = new Admin();
		admin.setLogin("admin");
		superUserService.saveAdmin(admin);
	}
	
	@Test
	public void saveMultipleSameAdmin(){
		Admin admin = new Admin();
		admin.setLogin("admin");
		superUserService.saveAdmin(admin);
		superUserService.saveAdmin(admin);
		Assert.assertEquals(1, superUserService.findAllAdmin().size());
	}
	
	@Test
	public void saveMultipleAdmin(){
		Admin admin = new Admin();
		admin.setLogin("admin");
		superUserService.saveAdmin(admin);
		admin.setLogin("admin2");
		superUserService.saveAdmin(admin);
		Assert.assertEquals(2, superUserService.findAllAdmin().size());
	}
	
	@Test
	public void deleteAdmin(){
		Admin admin = new Admin();
		admin.setLogin("admin");
		superUserService.saveAdmin(admin);
		superUserService.deleteAdmin("admin");
		Assert.assertEquals(0, superUserService.findAllAdmin().size());
	}
	

}

