package fr.affectation.service.admin;

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
public class AdminServiceTest {
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Inject 
	private AdminService adminService;
	
	@Before
	public void cleanDbBefore() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from Admin").executeUpdate();
		transaction.commit();
		session.close();
	}

	@After
	public void cleanDbAfter() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from Admin").executeUpdate();
		transaction.commit();
		session.close();
	}
	
	@Test
	public void save(){
		adminService.save("admin");
		Assert.assertEquals(1, adminService.findAdminLogins().size());
	}
	
	@Test
	public void saveByLogin(){
		adminService.save("admin");
		Assert.assertEquals(1, adminService.findAdminLogins().size());
	}
	
	@Test
	public void saveMultipleSameAdmin(){
		adminService.save("admin");
		adminService.save("admin");
		Assert.assertEquals(1, adminService.findAdminLogins().size());
	}
	
	@Test
	public void saveMultipleAdmin(){
		adminService.save("admin");
		adminService.save("admin2");
		Assert.assertEquals(2, adminService.findAdminLogins().size());
	}
	
	@Test
	public void deleteAdmin(){
		adminService.save("admin");
		adminService.delete("admin");
		Assert.assertEquals(0, adminService.findAdminLogins().size());
	}
	
	@Test 
	public void isAdmin(){
		
		adminService.save("admin");
		Assert.assertTrue(adminService.isAdmin("admin"));
	}
	

}

