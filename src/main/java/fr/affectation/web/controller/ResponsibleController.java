package fr.affectation.web.controller;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.affectation.domain.specialization.Specialization;
import fr.affectation.service.agap.AgapService;
import fr.affectation.service.configuration.ConfigurationService;
import fr.affectation.service.responsible.ResponsibleService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.student.StudentService;

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
	private StudentService studentService;
	
	@Inject
	private ConfigurationService configurationService;
	
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
		
		Specialization specialization = responsibleService.forWhichSpecializationType(login).equals("ic") ?
				specializationService.getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
		
		if (configurationService.isSubmissionAvailable()){
			model.addAttribute("allStudents", studentService.findSimpleStudentsByOrderChoiceAndSpecialization(order, specialization));
		}
		else{
			model.addAttribute("allStudents", studentService.findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(order, specialization));
		}
		
		model.addAttribute("order", order);
		model.addAttribute("specialization", specialization);
		
		if (configurationService.isValidating()){
			return "responsable/choix-validation";
		}
		else{
			model.addAttribute("state", configurationService.isSubmissionAvailable() ? "before" : "after");
			return "responsable/choix";
		}
	}

}