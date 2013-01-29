package fr.affectation.service.authentication;


import javax.inject.Inject;

import org.springframework.stereotype.Service;

import fr.affectation.service.agap.AgapService;
import fr.affectation.service.responsible.ResponsibleService;
import fr.affectation.service.superuser.SuperUserService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
	
	@Inject
	private AgapService agapService;
	
	@Inject
	private ResponsibleService responsibleService;
	
	@Inject
	private SuperUserService superUserService;

	@Override
	public boolean isStudent(String login) {
		return agapService.isStudentConcerned(login);
	}

	@Override
	public boolean isResponsible(String login) {
		return responsibleService.getAllResponsible().contains(login);
	}

	@Override
	public boolean isAdmin(String login) {
		return superUserService.findAllAdminLogin().contains(login);
	}

}
