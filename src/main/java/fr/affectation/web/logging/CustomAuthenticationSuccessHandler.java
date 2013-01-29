package fr.affectation.web.logging;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.User;

import fr.affectation.service.responsible.ResponsibleService;

@Component("authenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private ResponsibleService responsibleService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
		if (isRolePresent(authorities, "ROLE_ELEVE")){
			response.sendRedirect("eleve/add");
		}
		if (isRolePresent(authorities, "ROLE_ADMIN")){
			response.sendRedirect("admin/");
		}
		if (isRolePresent(authorities, "ROLE_RESPONSABLE")){
			User currentUser = (User) authentication.getPrincipal();
			String login = currentUser.getUsername();
			String specialisation = responsibleService.forWhichSpecialization(login).toLowerCase();
			response.sendRedirect("responsable/");
		}
	}
	
	private boolean isRolePresent(Collection<GrantedAuthority> authorities, String role){
		boolean isRolePresent = false;
		for (GrantedAuthority grantedAuthority : authorities){
			isRolePresent = grantedAuthority.getAuthority().equals(role);
			if (isRolePresent) break;
		}
		return isRolePresent;	
	}

}
