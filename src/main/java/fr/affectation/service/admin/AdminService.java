package fr.affectation.service.admin;

import java.util.List;

import fr.affectation.domain.superuser.Admin;

public interface AdminService {
	
	public void saveAdmin(String login);

	public void saveAdmin(Admin admin);
	
	public void deleteAdmin(String login);
	
	public List<Admin> findAdmins();
	
	public List<String> findAdminLogins();
	
	public boolean isAdmin(String login);

}
