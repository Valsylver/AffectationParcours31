package fr.affectation.service.admin;

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
public class AdminServiceImpl implements AdminService {
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void save(String login) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(new Admin(login));
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Admin> findAdmins() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Admin");
		List<Admin> allAdmin = (List<Admin>) query.list();
		return allAdmin;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findAdminLogins() {
		List<Admin> allAdmin = findAdmins();
		List<String> allAdminLogin = new ArrayList<String>();
		for (Admin admin : allAdmin){
			allAdminLogin.add(admin.getLogin());
		}
		return allAdminLogin;
	}

	@Override
	@Transactional
	public void delete(String login) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("delete from Admin where login= :login");
		query.setParameter("login", login);
		query.executeUpdate();
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isAdmin(String login){
		return findAdminLogins().contains(login);
	}

}
