package fr.affectation.web.logging;

import java.util.ArrayList;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import fr.affectation.service.authentication.AuthenticationService;

@Component("userService")
public class CustomUserDetailsService implements UserDetailsService{
	
	@Inject
	private AuthenticationService authenticationService;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {		
		ArrayList<GrantedAuthority> authority = new ArrayList<GrantedAuthority>();
		if (isStudent(userName)){
			authority.add(new SimpleGrantedAuthority("ROLE_ELEVE"));
		}
		else if (isResponsible(userName)){
			authority.add(new SimpleGrantedAuthority("ROLE_RESPONSABLE"));
		}
		else if (isAdmin(userName)){
			authority.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		User user = new User(userName, userName, true, true, true, true, authority);
		return user;
	}
	
	public boolean isStudent(String userName){
		return authenticationService.isStudent(userName);
	}
	
	public boolean isResponsible(String userName){
		return authenticationService.isResponsible(userName);
	}
	
	public boolean isAdmin(String userName){
		return authenticationService.isAdmin(userName);
	}

}
