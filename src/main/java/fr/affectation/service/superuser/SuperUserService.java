package fr.affectation.service.superuser;

import java.util.List;

import fr.affectation.domain.superuser.Admin;

public interface SuperUserService {

	public void saveAdmin(Admin admin);
	
	public void deleteAdmin(String login);
	
	public List<Admin> findAllAdmin();
	
	public List<String> findAllAdminLogin();

}
