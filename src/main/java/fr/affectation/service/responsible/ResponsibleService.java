package fr.affectation.service.responsible;

import java.util.List;

public interface ResponsibleService {
	
	public List<String> findResponsibles();
	
	public String forWhichSpecialization(String login);

	public int forWhichSpecializationType(String login);
	
	public boolean isResponsible(String login);

}
