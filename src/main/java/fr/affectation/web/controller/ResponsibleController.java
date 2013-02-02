package fr.affectation.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.student.SimpleStudentWithValidation;
import fr.affectation.domain.student.StudentValidationList;
import fr.affectation.service.configuration.ConfigurationService;
import fr.affectation.service.responsible.ResponsibleService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.statistics.StatisticsService;
import fr.affectation.service.student.StudentService;

@Controller
@RequestMapping("/responsable")
public class ResponsibleController {

	@Inject
	private SpecializationService specializationService;

	@Inject
	private StatisticsService statisticsService;

	@Inject
	private ResponsibleService responsibleService;

	@Inject
	private StudentService studentService;

	@Inject
	private ConfigurationService configurationService;

	@RequestMapping("/")
	public String mainPage(Model model) {
		return "redirect:/responsable/1";
	}

	@RequestMapping("/{order}")
	public String showResultForChoice(@PathVariable int order, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String login = auth.getName();

		String abbreviation = responsibleService.forWhichSpecialization(login);

		Specialization specialization = responsibleService.forWhichSpecializationType(login).equals("ic") ? specializationService
				.getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
		model.addAttribute("specialization", specialization);

		if (configurationService.isSubmissionAvailable()) {
			model.addAttribute("allStudents", studentService.findSimpleStudentsByOrderChoiceAndSpecialization(order, specialization));
			model.addAttribute("state", "before");
			return "responsable/choix";
		} else {
			if ((configurationService.isValidating()) && (order == 1)) {
				List<SimpleStudentWithValidation> studentsWithValidation = studentService.findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(order,
						specialization);
				List<String> logins = new ArrayList<String>();
				List<Boolean> validated = new ArrayList<Boolean>();
				for (SimpleStudentWithValidation student : studentsWithValidation) {
					logins.add(student.getLogin());
					validated.add(student.isValidated());
				}
				model.addAttribute("studentsValidation", new StudentValidationList(logins, validated));
				model.addAttribute("allStudents", studentsWithValidation);
				System.out.println("BEFORE");
				System.out.println(logins);
				System.out.println(validated);
				return "responsable/choix-validation";
			} else {
				model.addAttribute("allStudents", studentService.findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(order, specialization));
				model.addAttribute("state", "after");
				return "responsable/choix";
			}
		}
	}

	@RequestMapping(value = "/edit-validation", method = RequestMethod.POST)
	public String editExclusion(StudentValidationList studentsValidation) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String login = auth.getName();

		String type = responsibleService.forWhichSpecializationType(login).equals("ic") ? "ImprovementCourse" : "JobSector";
		studentService.updateValidation(studentsValidation.getStudents(), studentsValidation.getValidated(), type);
		return "redirect:/responsable/1";
	}

	@RequestMapping("/student/{login}")
	public String displayStudent(@PathVariable String login, Model model, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginRespo = auth.getName();
		String abbreviation = responsibleService.forWhichSpecialization(loginRespo);

		Specialization specialization = responsibleService.forWhichSpecializationType(loginRespo).equals("ic") ? specializationService
				.getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
		model.addAttribute("specialization", specialization);
		model.addAttribute("student", studentService.retrieveStudentByLogin(login, request.getSession().getServletContext().getRealPath("/")));
		return "responsable/student";
	}

	@RequestMapping("/statistics")
	public String statistics(Model model, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginRespo = auth.getName();
		String abbreviation = responsibleService.forWhichSpecialization(loginRespo);

		Specialization specialization = responsibleService.forWhichSpecializationType(loginRespo).equals("ic") ? specializationService
				.getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
		model.addAttribute("specialization", specialization);
		String path = request.getSession().getServletContext().getRealPath("/");
		model.addAttribute("specForStats",
				specialization.getType().equals("ImprovementCourse") ? statisticsService.findSimpleIcStats() : statisticsService.findSimpleJsStats());
		model.addAttribute("type", specialization.getType().equals("ImprovementCourse") ? 1 : 2);
		statisticsService.generateBarChartIc(path);
		statisticsService.generatePieChartIc(path);
		return "responsable/statistics";
	}
}