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

import fr.affectation.domain.superuser.Admin;


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
	public void saveAdmin(){
		Admin admin = new Admin();
		admin.setLogin("admin");
		adminService.saveAdmin(admin);
		Assert.assertEquals(1, adminService.findAdmins().size());
	}
	
	@Test
	public void saveAdminByLogin(){
		adminService.saveAdmin("admin");
		Assert.assertEquals(1, adminService.findAdmins().size());
	}
	
	@Test
	public void saveMultipleSameAdmin(){
		Admin admin = new Admin();
		admin.setLogin("admin");
		adminService.saveAdmin(admin);
		adminService.saveAdmin(admin);
		Assert.assertEquals(1, adminService.findAdmins().size());
	}
	
	@Test
	public void saveMultipleAdmin(){
		Admin admin = new Admin();
		admin.setLogin("admin");
		adminService.saveAdmin(admin);
		admin.setLogin("admin2");
		adminService.saveAdmin(admin);
		Assert.assertEquals(2, adminService.findAdmins().size());
	}
	
	@Test
	public void deleteAdmin(){
		Admin admin = new Admin();
		admin.setLogin("admin");
		adminService.saveAdmin(admin);
		adminService.deleteAdmin("admin");
		Assert.assertEquals(0, adminService.findAdmins().size());
	}
	
	@Test 
	public void isAdmin(){
		Admin admin = new Admin();
		admin.setLogin("admin");
		adminService.saveAdmin(admin);
		Assert.assertTrue(adminService.isAdmin("admin"));
	}
	

}

