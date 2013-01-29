package fr.affectation.service.superuser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.affectation.domain.superuser.Admin;

@Service
public class SuperUserServiceImpl implements SuperUserService {
	
	@Inject
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void saveAdmin(Admin admin) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(admin);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Admin> findAllAdmin() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Admin");
		List<Admin> allAdmin = (List<Admin>) query.list();
		return allAdmin;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findAllAdminLogin() {
		List<Admin> allAdmin = findAllAdmin();
		List<String> allAdminLogin = new ArrayList<String>();
		for (Admin admin : allAdmin){
			allAdminLogin.add(admin.getLogin());
		}
		return allAdminLogin;
	}

	@Override
	@Transactional
	public void deleteAdmin(String login) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("delete from Admin where login= :login");
		query.setParameter("login", login);
		query.executeUpdate();
	}

}
