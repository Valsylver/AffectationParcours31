package fr.affectation.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/noauthorities")
public class AnonymousController {
	
	@RequestMapping({ "/", "" })
	public String redirect(Model mode) {
		return "noauthorities/index";
	}

}
