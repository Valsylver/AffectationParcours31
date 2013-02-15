package fr.affectation.service.exclusion;

import java.util.List;

public interface ExclusionService {
	
	public void save(String login);
	
	public void remove(String login);
	
	public boolean isExcluded(String login);

	public void removeAll();

	public List<String> findStudentToExcludeLogins();

}
