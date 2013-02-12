package fr.affectation.service.authentication;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import fr.affectation.service.admin.AdminService;
import fr.affectation.service.responsible.ResponsibleService;
import fr.affectation.service.student.StudentService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
	
	@Inject
	private StudentService studentService;
	
	@Inject
	private ResponsibleService responsibleService;
	
	@Inject
	private AdminService adminService;

	@Override
	public boolean isStudent(String login) {
		return studentService.isStudentConcerned(login);
	}

	@Override
	public boolean isResponsible(String login) {
		return responsibleService.isResponsible(login);
	}

	@Override
	public boolean isAdmin(String login) {
		return adminService.isAdmin(login);
	}

}
