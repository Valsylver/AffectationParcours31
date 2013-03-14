package fr.affectation.web.logging;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component("authenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Resource(name="path")
	private String path;

	@SuppressWarnings("unchecked")
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
		if (isRolePresent(authorities, "ROLE_ELEVE")){
			response.sendRedirect(path + "/student/");
		}
		else{
			if (isRolePresent(authorities, "ROLE_ADMIN")){
				response.sendRedirect(path + "/admin/");
			}
			else{
				if (isRolePresent(authorities, "ROLE_RESPONSABLE")){
					response.sendRedirect(path + "/responsable/");
				}
				else{
					response.sendRedirect(path + "/noauthorities");
				}
			}
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
