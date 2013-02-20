package fr.affectation.service.admin;

import java.util.List;

public interface AdminService {
	
	public void save(String login);
	
	public void delete(String login);
	
	public List<String> findAdminLogins();
	
	public boolean isAdmin(String login);

}
