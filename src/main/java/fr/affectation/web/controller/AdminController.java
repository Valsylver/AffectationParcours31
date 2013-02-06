package fr.affectation.web.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.quartz.SchedulerException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.SimpleSpecialization;
import fr.affectation.domain.student.StudentToExclude;
import fr.affectation.domain.util.StudentsExclusion;
import fr.affectation.service.configuration.ConfigurationService;
import fr.affectation.service.configuration.When;
import fr.affectation.service.exclusion.ExclusionService;
import fr.affectation.service.export.ExportService;
import fr.affectation.service.fake.FakeDataService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.statistics.StatisticsService;
import fr.affectation.service.student.StudentService;
import fr.affectation.service.superuser.SuperUserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Inject
	private SpecializationService specializationService;

	@Inject
	private StudentService studentService;
	
	@Inject
	private ExclusionService exclusionService;

	@Inject
	private SuperUserService superUserService;

	@Inject
	private StatisticsService statisticsService;

	@Inject
	private ConfigurationService configurationService;
	
	@Inject 
	private ExportService exportService;

	@Inject
	private FakeDataService fakeData;

	@RequestMapping({ "/", "" })
	public String redirect(Model mode) {
		return configurationService.isRunning() ? "redirect:/admin/statistics/synthese" : "redirect:/admin/config";
	}

	@InitBinder
	public void initBinder(DataBinder binder) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormatter, true));
	}

	@RequestMapping("/administration/specialization")
	public String manageSpecialization(Model model) {
		model.addAttribute("paAvailable", specializationService.findImprovementCourses());
		model.addAttribute("fmAvailable", specializationService.findJobSectors());
		return "admin/administration/specialization";
	}

	@RequestMapping("/administration/process")
	public String manageProcess(Model model) {
		return "admin/administration/process";
	}

	@RequestMapping("/administration/students")
	public String manageStudents(Model model) {
		model.addAttribute("studentsConcerned", studentService.findAllStudentsConcerned());
		model.addAttribute("studentsToExclude", studentService.findAllStudentsToExclude());
		model.addAttribute("promo", Calendar.getInstance().get(Calendar.YEAR) + 1);
		model.addAttribute("studentExclusion", new StudentsExclusion(studentService.findNecessarySizeForStudentExclusion()));
		return "admin/administration/students";
	}
	
	@RequestMapping("/administration/export")
	public String exportResults(Model model, HttpServletRequest request) {
		exportService.generatePdfResults(request.getSession().getServletContext().getRealPath("/"));
		return "admin/administration/export";
	}

	@RequestMapping(value = "/run/edit-exclusion", method = RequestMethod.POST)
	public String editExclusion(StudentsExclusion studentExclusion) {
		List<String> newExcluded = new ArrayList<String>();
		List<String> oldExcluded = exclusionService.findStudentToExcludeLogins();
		for (String login : studentExclusion.getExcluded()) {
			if (!login.equals("")) {
				newExcluded.add(login);
			}
		}
		for (String login : newExcluded) {
			if (!oldExcluded.contains(login)) {
				exclusionService.save(new StudentToExclude(login));
			}
		}
		for (String login : oldExcluded) {
			if (!newExcluded.contains(login)) {
				exclusionService.removeStudentByLogin(login);
			}
		}
		return "redirect:/admin/administration/students";
	}

	@RequestMapping("/exclude")
	public String excludeStudent(Model model) {
		model.addAttribute("studentsToExclude", studentService.findStudentsToExcludeName());
		return "admin/exclude-students";
	}

	@RequestMapping(value = "/excludeProcess", method = RequestMethod.POST)
	public String processExclusion(@RequestParam(value = "exclusion", required = true) MultipartFile exclusion, RedirectAttributes redirectAttributes) {
		if (exclusion.isEmpty()) {
			redirectAttributes.addFlashAttribute("alertMessage", "Le fichier est vide.");
		} else {
			boolean condition = true;
			condition = exclusion.getContentType().equals("application/vnd.ms-excel") || exclusion.getContentType().equals("application/msexcel");
			if (!condition) {
				redirectAttributes.addFlashAttribute("alertMessage", "Seuls les fichiers (*.xls) sont acceptés.");
			} else {
				if (studentService.populateStudentToExcludeFromFile(exclusion)) {
					redirectAttributes.addFlashAttribute("successMessage", "Le fichier a bien été ajouté.");
				} else {
					redirectAttributes.addFlashAttribute("alertMessage", "Une erreur est survenue lors de la lecture du fichier.");
				}
			}
		}
		return "redirect:/admin/exclude";
	}

	@RequestMapping("/stop-process")
	public String stopProcess() {
		try {
			configurationService.stopProcess();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return "redirect:/admin";
	}

	@RequestMapping("/student/{login}")
	public String displayStudent(@PathVariable String login, Model model, HttpServletRequest request) {
		model.addAttribute("student", studentService.retrieveStudentByLogin(login, request.getSession().getServletContext().getRealPath("/")));
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		return "admin/student";
	}

	@RequestMapping("/config")
	public String configurationIndex(Model model) {
		model.addAttribute("when", new When());
		model.addAttribute("paAvailable", specializationService.findImprovementCourses());
		model.addAttribute("fmAvailable", specializationService.findJobSectors());
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		model.addAttribute("now", dateFormat.format(date));
		return "admin/configuration/index";
	}

	@RequestMapping("/config/modif/parcours/{abbreviation}")
	public String modifyIc(@PathVariable String abbreviation, Model model) {
		model.addAttribute("specialization", specializationService.getImprovementCourseByAbbreviation(abbreviation));
		model.addAttribute("alreadyExists", true);
		model.addAttribute("state", configurationService.isRunning() ? "run" : "config");
		return "admin/configuration/edit-specialization";
	}

	@RequestMapping("/config/modif/filieres/{abbreviation}")
	public String modifyJs(@PathVariable String abbreviation, Model model) {
		model.addAttribute("specialization", specializationService.getJobSectorByAbbreviation(abbreviation));
		model.addAttribute("alreadyExists", true);
		model.addAttribute("state", configurationService.isRunning() ? "run" : "config");
		return "admin/configuration/edit-specialization";
	}

	@RequestMapping(value = "/config/modif/new/filieres", method = RequestMethod.GET)
	public String addNewJobSector(Model model) {
		model.addAttribute("specialization", new JobSector());
		model.addAttribute("alreadyExists", false);
		model.addAttribute("state", "config");
		return "admin/configuration/edit-specialization";
	}

	@RequestMapping(value = "/config/modif/new/parcours", method = RequestMethod.GET)
	public String addNewImprovementCourse(Model model) {
		model.addAttribute("specialization", new ImprovementCourse());
		model.addAttribute("alreadyExists", false);
		model.addAttribute("state", "config");
		return "admin/configuration/edit-specialization";
	}

	@RequestMapping(value = "/config/edit/ic", method = RequestMethod.POST)
	public String saveImprovementCourse(@ModelAttribute("specialization") @Valid ImprovementCourse specialization, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("state", configurationService.isRunning() ? "run" : "config");
			return "admin/configuration/edit-specialization";
		}
		specializationService.save(specialization);
		return configurationService.isRunning() ? "redirect:/admin/administration/specialization" : "redirect:/admin/config";
	}

	@RequestMapping(value = "/config/edit/js", method = RequestMethod.POST)
	public String saveJobSector(@ModelAttribute("specialization") @Valid JobSector specialization, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("state", configurationService.isRunning() ? "run" : "config");
			return "admin/configuration/edit-specialization";
		}
		specializationService.save(specialization);
		return configurationService.isRunning() ? "redirect:/admin/administration/specialization" : "redirect:/admin/config";
	}

	@RequestMapping("/config/delete/filieres/{abbreviation}")
	public String deleteJobSector(@PathVariable String abbreviation) {
		specializationService.delete(specializationService.getJobSectorByAbbreviation(abbreviation));
		return "redirect:/admin/config";
	}

	@RequestMapping("/config/delete/parcours/{abbreviation}")
	public String deleteImprovementCourse(@PathVariable String abbreviation) {
		specializationService.delete(specializationService.getImprovementCourseByAbbreviation(abbreviation));
		return "redirect:/admin/config";
	}

	@RequestMapping(value = "/save-config", method = RequestMethod.POST)
	public String post(@ModelAttribute When when, BindingResult result, Model model) {
		if (when.getFirstEmail() == null) {
			FieldError fieldError = new FieldError("when", "firstEmail", "lol");
			result.addError(fieldError);
		}
		if (when.getSecondEmail() == null) {
			FieldError fieldError = new FieldError("when", "secondEmail", "lol");
			result.addError(fieldError);
		}
		if (when.getEndSubmission() == null) {
			FieldError fieldError = new FieldError("when", "endSubmission", "lol");
			result.addError(fieldError);
		}
		if (when.getEndValidation() == null) {
			FieldError fieldError = new FieldError("when", "endValidation", "lol");
			result.addError(fieldError);
		}
		if (result.hasErrors()) {
			model.addAttribute("paAvailable", specializationService.findImprovementCourses());
			model.addAttribute("fmAvailable", specializationService.findJobSectors());
			return "admin/configure-index";
		} else {
			List<Date> allDates = new ArrayList<Date>();
			allDates.add(when.getFirstEmail());
			allDates.add(when.getSecondEmail());
			allDates.add(when.getEndSubmission());
			allDates.add(when.getEndValidation());
			boolean areDatesSuccessive = true;
			for (int i = 0; i < allDates.size() - 1; i++) {
				Date date1 = allDates.get(i);
				Date date2 = allDates.get(i + 1);
				if (!date1.before(date2)) {
					areDatesSuccessive = false;
				}
			}
			if (!areDatesSuccessive) {
				model.addAttribute("paAvailable", specializationService.findImprovementCourses());
				model.addAttribute("fmAvailable", specializationService.findJobSectors());
				model.addAttribute("alertMessage", "Les dates doivent êtres successives.");
				return "admin/configure-index";
			}
		}
		try {
			configurationService.setWhen(when);
			configurationService.initialize();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

		return "redirect:/admin";
	}

	@RequestMapping("/parcours/statistics")
	public String statisticsIc(Model model, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		statisticsService.generatePieChartIc(path);
		statisticsService.generateBarChartIc(path);
		model.addAttribute("type", 1);
		model.addAttribute("specForStats", statisticsService.findSimpleIcStats());
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		return "admin/statistics";
	}

	@RequestMapping("/filieres/statistics")
	public String statisticsSynthese(Model model, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		statisticsService.generatePieChartJs(path);
		statisticsService.generateBarChartJs(path);
		model.addAttribute("type", 2);
		model.addAttribute("specForStats", statisticsService.findSimpleJsStats());
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		return "admin/statistics";
	}

	@RequestMapping("/statistics/synthese")
	public String statisticsJs(Model model, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		statisticsService.generatePieChartJs(path);
		statisticsService.generatePieChartIc(path);
		statisticsService.generateBarChartIc(path);
		statisticsService.generateBarChartJs(path);
		model.addAttribute("theList", statisticsService.findSimpleIcStats());
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		return "admin/statistics-synthese";
	}

	@RequestMapping("/parcours/synthese/choix{order}")
	public String allResultsIc(@PathVariable int order, Model model) {
		model.addAttribute("order", order);
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		model.addAttribute("running", !configurationService.isValidationForAdminAvailable());
		model.addAttribute(
				"allStudents",
				configurationService.isValidationForAdminAvailable() ? studentService.findSimpleStudentsWithValidationForAllIcByOrder(order) : studentService
						.findSimpleStudentsForAllIcByOrder(order));
		return "admin/choix/parcours/synthese";
	}

	@RequestMapping("/filieres/synthese/choix{order}")
	public String allResultsJs(@PathVariable int order, Model model) {
		model.addAttribute("order", order);
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		model.addAttribute("running", !configurationService.isValidationForAdminAvailable());
		model.addAttribute(
				"allStudents",
				configurationService.isValidationForAdminAvailable() ? studentService.findSimpleStudentsWithValidationForAllJsByOrder(order) : studentService
						.findSimpleStudentsForAllJsByOrder(order));
		return "admin/choix/filieres/synthese";
	}

	@RequestMapping("/parcours/details/{abbreviation}/choix{order}")
	public String resultsIcDetails(@PathVariable String abbreviation, @PathVariable int order, Model model) {
		ImprovementCourse improvementCourse = specializationService.getImprovementCourseByAbbreviation(abbreviation);
		model.addAttribute("order", order);
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		model.addAttribute("abbreviation", abbreviation);
		model.addAttribute("specialization", improvementCourse);
		model.addAttribute(
				"allStudents",
				configurationService.isValidationForAdminAvailable() ? studentService.findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(order,
						improvementCourse) : studentService.findSimpleStudentsByOrderChoiceAndSpecialization(order, improvementCourse));
		model.addAttribute("running", !configurationService.isValidationForAdminAvailable());
		return "admin/choix/parcours/details";
	}

	@RequestMapping("/filieres/details/{abbreviation}/choix{order}")
	public String resultsJsDetails(@PathVariable String abbreviation, @PathVariable int order, Model model) {
		JobSector jobSector = specializationService.getJobSectorByAbbreviation(abbreviation);
		model.addAttribute("order", order);
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		model.addAttribute("abbreviation", abbreviation);
		model.addAttribute("specialization", jobSector);
		model.addAttribute(
				"allStudents",
				configurationService.isValidationForAdminAvailable() ? studentService.findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(order,
						jobSector) : studentService.findSimpleStudentsByOrderChoiceAndSpecialization(order, jobSector));
		model.addAttribute("running", !configurationService.isValidationForAdminAvailable());
		return "admin/choix/filieres/details";
	}

	@RequestMapping("/statistics/eleves/{category}")
	public String studentSynthese(@PathVariable String category, HttpServletRequest request, Model model) {
		String path = request.getSession().getServletContext().getRealPath("/");
		List<Map<String, Object>> results = studentService.findStudentsForCategorySynthese(category, path);
		List<Integer> numbersForCategories = studentService.findSizeOfCategories(path);
		model.addAttribute("nbreAll", numbersForCategories.get(0));
		model.addAttribute("nbrePartial", numbersForCategories.get(1));
		model.addAttribute("nbreNo", numbersForCategories.get(2));
		model.addAttribute("category", category);
		model.addAttribute("results", results);
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());

		return "admin/eleves-synthese";
	}

	@PostConstruct
	public void createFake() {
		fakeData.createFakeSpecialization();
		fakeData.createFakeAdmin();
	}

	@RequestMapping("/fake")
	public String populateResults() {
		fakeData.createFakeChoices();
		return "redirect:/admin/statistics/synthese";
	}
	
	@RequestMapping("/fake2")
	public String fakeValidation() {
		fakeData.fakeValidation();
		return "redirect:/admin/statistics/synthese";
	}

	@PreDestroy
	public void deleteFake() {
		fakeData.deleteAll();
	}

}
