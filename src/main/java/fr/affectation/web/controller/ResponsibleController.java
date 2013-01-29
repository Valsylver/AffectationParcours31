package fr.affectation.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.student.Student;
import fr.affectation.service.agap.AgapService;
import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.responsible.ResponsibleService;
import fr.affectation.service.specialization.SpecializationService;

@Controller
@RequestMapping("/responsable")
public class ResponsibleController {
	
	@Inject
	private SpecializationService specializationService;
	
	@Inject
	private ResponsibleService responsibleService;
	
	@Inject
	private AgapService agapService;
	
	@Inject
	private ChoiceService choiceService;
	
	@RequestMapping("/")
	public String mainPage(Model model){
		return "redirect:/responsable/1";
	}
	
	@RequestMapping("/{order}")
	public String showResultForChoice(@PathVariable int order, Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String login = auth.getName();
		
		String abbreviation = responsibleService.forWhichSpecialization(login);
		
		agapService.generateRanking();
		agapService.generateUeCode();
		
		List<Student> allStudents;
		Specialization specialization;
		
		if (responsibleService.forWhichSpecializationType(login).equals("ic")){
			specialization = specializationService.getImprovementCourseByAbbreviation(abbreviation);
			allStudents = choiceService.getStudentsByOrderChoiceAndSpecialization(order, specialization);
		}
		else{
			specialization = specializationService.getJobSectorByAbbreviation(abbreviation);
			allStudents = choiceService.getStudentsByOrderChoiceAndSpecialization(order, specialization);
		}
		
		model.addAttribute("order", order);
		model.addAttribute("specialization", specialization);
		model.addAttribute("allStudents", allStudents);
		model.addAttribute("rankingTotal", agapService.getRanking().size());
		
		return "responsable/choix";
	}

}