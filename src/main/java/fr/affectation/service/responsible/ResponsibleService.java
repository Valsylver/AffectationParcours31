package fr.affectation.service.responsible;

import java.util.List;

public interface ResponsibleService {
	
	public List<String> getAllResponsible();
	
	public String forWhichSpecialization(String login);

	public String forWhichSpecializationType(String login);

}
