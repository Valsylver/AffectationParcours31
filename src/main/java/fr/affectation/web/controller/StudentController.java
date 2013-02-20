package fr.affectation.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import fr.affectation.domain.choice.Choice;
import fr.affectation.domain.choice.FullChoice;
import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.configuration.ConfigurationService;
import fr.affectation.service.documents.DocumentService;
import fr.affectation.service.specialization.SpecializationService;

@Controller
@RequestMapping("/student")
public class StudentController {
	
	@Inject 
	private ChoiceService choiceService;
	
	@Inject
	private SpecializationService specializationService;
	
	@Inject
	private DocumentService documentService;
	
	@Inject
	private ConfigurationService configurationService;
	
	@RequestMapping({"/", ""})
	public String index(){
		if (configurationService.isSubmissionAvailable()){
			return "redirect:/student/add";
		}
		else{
			return "student/noSubmission";
		}
	}
	
	private void validatePdf(MultipartFile file) throws FileUploadException {
		if (!file.getContentType().equals("application/pdf")){
			throw new FileUploadException("Seuls les fichiers pdf sont acceptés");
		}
	}
	
	@RequestMapping(value = "/processForm", method = RequestMethod.POST)
	public String processForm(FullChoice fullChoice, Model model,
			BindingResult bindingResult,
			@RequestParam(value="resume", required=false) MultipartFile resume,
			@RequestParam(value="letterIc", required=false) MultipartFile letterIc,
			@RequestParam(value="letterJs", required=false) MultipartFile letterJs,
			HttpServletRequest request
			){
		if (configurationService.isSubmissionAvailable()){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String login = auth.getName();
			
			String path = request.getSession().getServletContext().getRealPath("/");
			
			try{
				if (!resume.isEmpty()){
					validatePdf(resume);
					documentService.saveResume(path, login, resume);
				}
				if (!letterIc.isEmpty()){
					validatePdf(letterIc);
					documentService.saveLetterIc(path, login, letterIc);
				}
				if (!letterJs.isEmpty()){
					validatePdf(letterJs);
					documentService.saveLetterJs(path, login, letterJs);
				}
			}
			catch (FileUploadException e) {
				return "redirect:/student/add";
			}
			
			ImprovementCourseChoice improvementCourseChoice = fullChoice.getImprovementCourseChoice();
			JobSectorChoice jobSectorChoice = fullChoice.getJobSectorChoice();
			
			improvementCourseChoice.setLogin(login);
			jobSectorChoice.setLogin(login);
			
			if (improvementCourseChoice.getChoice1().equals("------------------------- Pas de choix -------------------------")){
				improvementCourseChoice.setChoice1("");
			}
			if (improvementCourseChoice.getChoice2().equals("------------------------- Pas de choix -------------------------")){
				improvementCourseChoice.setChoice2("");
			}
			if (improvementCourseChoice.getChoice3().equals("------------------------- Pas de choix -------------------------")){
				improvementCourseChoice.setChoice3("");
			}
			if (improvementCourseChoice.getChoice4().equals("------------------------- Pas de choix -------------------------")){
				improvementCourseChoice.setChoice4("");
			}
			if (improvementCourseChoice.getChoice5().equals("------------------------- Pas de choix -------------------------")){
				improvementCourseChoice.setChoice5("");
			}
			
			if (jobSectorChoice.getChoice1().equals("------------------------- Pas de choix -------------------------")){
				jobSectorChoice.setChoice1("");
			}
			if (jobSectorChoice.getChoice2().equals("------------------------- Pas de choix -------------------------")){
				jobSectorChoice.setChoice2("");
			}
			if (jobSectorChoice.getChoice3().equals("------------------------- Pas de choix -------------------------")){
				jobSectorChoice.setChoice3("");
			}
			if (jobSectorChoice.getChoice4().equals("------------------------- Pas de choix -------------------------")){
				jobSectorChoice.setChoice4("");
			}
			if (jobSectorChoice.getChoice5().equals("------------------------- Pas de choix -------------------------")){
				jobSectorChoice.setChoice5("");
			}
			
	
			improvementCourseChoice.setChoice1(specializationService.getAbbreviationFromStringForForm(improvementCourseChoice.getChoice1()));
			improvementCourseChoice.setChoice2(specializationService.getAbbreviationFromStringForForm(improvementCourseChoice.getChoice2()));
			improvementCourseChoice.setChoice3(specializationService.getAbbreviationFromStringForForm(improvementCourseChoice.getChoice3()));
			improvementCourseChoice.setChoice4(specializationService.getAbbreviationFromStringForForm(improvementCourseChoice.getChoice4()));
			improvementCourseChoice.setChoice5(specializationService.getAbbreviationFromStringForForm(improvementCourseChoice.getChoice5()));
			
			jobSectorChoice.setChoice1(specializationService.getAbbreviationFromStringForForm(jobSectorChoice.getChoice1()));
			jobSectorChoice.setChoice2(specializationService.getAbbreviationFromStringForForm(jobSectorChoice.getChoice2()));
			jobSectorChoice.setChoice3(specializationService.getAbbreviationFromStringForForm(jobSectorChoice.getChoice3()));
			jobSectorChoice.setChoice4(specializationService.getAbbreviationFromStringForForm(jobSectorChoice.getChoice4()));
			jobSectorChoice.setChoice5(specializationService.getAbbreviationFromStringForForm(jobSectorChoice.getChoice5()));
	
			choiceService.save(improvementCourseChoice);
			choiceService.save(jobSectorChoice);
			
			List<Integer> notFilledJs = choiceService.findElementNotFilledJobSector(login);
			List<Integer> notFilledIc = choiceService.findElementNotFilledImprovementCourse(login);
			
			String nfJs = "Vous n'avez pas fait de choix ";
			int index = 0;
			for (Integer i : notFilledJs){
				if (index == 0){
					nfJs += i;
				}
				else{
					nfJs += ", " + i;
				}
				index += 1;
			}
			nfJs += ".";
			
			String nfIc = "Vous n'avez pas fait de choix ";
			index = 0;
			for (Integer i : notFilledIc){
				if (index == 0){
					nfIc += i;
				}
				else{
					nfIc += ", " + i;
				}
				index += 1;
			}
			nfIc += ".";
			
			model.addAttribute("notFilledJs", nfJs);
			model.addAttribute("notFilledJsNumber", notFilledJs);
			model.addAttribute("notFilledIc", nfIc);
			model.addAttribute("notFilledIcNumber", notFilledIc);
			
			model.addAttribute("hasFilledLetterIc", documentService.hasFilledLetterIc(path, login));
			model.addAttribute("hasFilledLetterJs", documentService.hasFilledLetterJs(path, login));
			model.addAttribute("hasFilledResume", documentService.hasFilledResume(path, login));
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy à HH:mm");
			String dateEnd = dateFormat.format(configurationService.getWhen().getEndSubmission());
			model.addAttribute("dateEnd", dateEnd);
		
			return "student/success";
		}
		else{
			return "student/noSubmission";
		}
	}
	
	@RequestMapping("/add")
	public String add(Model model, HttpServletRequest request) {
		if (configurationService.isSubmissionAvailable()){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String login = auth.getName();
			String path = request.getSession().getServletContext().getRealPath("/");
			Choice choiceIc = choiceService.findImprovementCourseChoiceByLogin(login);
			Choice choiceJs = choiceService.findJobSectorChoiceByLogin(login);
			model.addAttribute("hasFilledLetterIc", documentService.hasFilledLetterIc(path, login));
			model.addAttribute("hasFilledLetterJs", documentService.hasFilledLetterJs(path, login));
			model.addAttribute("hasFilledResume", documentService.hasFilledResume(path, login));
			model.addAttribute("choiceIc", choiceIc);
			model.addAttribute("choiceJs", choiceJs);
		    model.addAttribute("fullChoice", new FullChoice());
		    model.addAttribute("paAvailable", specializationService.findImprovementCourses());
		    model.addAttribute("fmAvailable", specializationService.findJobSectors());
		    return "student/form";
		}
		else{
			return "student/noSubmission";
		}
	}

}
