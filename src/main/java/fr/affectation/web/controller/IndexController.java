package fr.affectation.web.controller;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.affectation.service.authentication.AuthenticationService;

@Controller
public class IndexController {
	
	@Inject
	private AuthenticationService authenticationService;

	@RequestMapping({ "", "/" })
	public String login() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			String login = auth.getName();
			if (authenticationService.isStudent(login)){
				return "redirect:/student/";
			}
			else{
				if (authenticationService.isResponsible(login)){
					return "redirect:/responsable/";
				}
				else if (authenticationService.isAdmin(login)){
					return "redirect:/admin/";
				}
				else{
					return "redirect:/noauthorities";
				}
			}
		}
		return "";
	}
	
	@RequestMapping("/login")
	public String log(){
		return "login";
	}

}