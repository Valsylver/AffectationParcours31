package fr.affectation.web.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
		return configurationService.isRunning() ? "redirect:/admin/run/main/statistics/choice1" : "redirect:/admin/config";
	}

	@InitBinder
	public void initBinder(DataBinder binder) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormatter, true));
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

	@RequestMapping("/config")
	public String configurationIndex(Model model) {
		model.addAttribute("when", new When());
		model.addAttribute("paAvailable", specializationService.findImprovementCourses());
		model.addAttribute("fmAvailable", specializationService.findJobSectors());
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		model.addAttribute("now", dateFormat.format(date));
		return "admin/config/index";
	}

	@RequestMapping("/common/edit/ic/{abbreviation}")
	public String editIc(@PathVariable String abbreviation, Model model) {
		model.addAttribute("specialization", specializationService.getImprovementCourseByAbbreviation(abbreviation));
		model.addAttribute("alreadyExists", true);
		model.addAttribute("state", configurationService.isRunning() ? "run" : "config");
		return "admin/common/edit-specialization";
	}
	
	@RequestMapping(value = "/common/process-edition/ic", method = RequestMethod.POST)
	public String saveImprovementCourse(@ModelAttribute("specialization") @Valid ImprovementCourse specialization, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("state", configurationService.isRunning() ? "run" : "config");
			return "admin/common/edit-specialization";
		}
		specializationService.save(specialization);
		return configurationService.isRunning() ? "redirect:/admin/run/settings/specializations" : "redirect:/admin/";
	}

	@RequestMapping("/common/edit/js/{abbreviation}")
	public String editJs(@PathVariable String abbreviation, Model model) {
		model.addAttribute("specialization", specializationService.getJobSectorByAbbreviation(abbreviation));
		model.addAttribute("alreadyExists", true);
		model.addAttribute("state", configurationService.isRunning() ? "run" : "config");
		return "admin/common/edit-specialization";
	}
	
	@RequestMapping(value = "/common/process-edition/js", method = RequestMethod.POST)
	public String saveJobSector(@ModelAttribute("specialization") @Valid JobSector specialization, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("state", configurationService.isRunning() ? "run" : "config");
			return "admin/common/edit-specialization";
		}
		specializationService.save(specialization);
		return configurationService.isRunning() ? "redirect:/admin/run/settings/specializations" : "redirect:/admin/";
	}

	@RequestMapping(value = "/config/new/job-sector", method = RequestMethod.GET)
	public String addNewJobSector(Model model) {
		model.addAttribute("specialization", new JobSector());
		model.addAttribute("alreadyExists", false);
		model.addAttribute("state", "config");
		return "admin/common/edit-specialization";
	}

	@RequestMapping(value = "/config/new/improvement-course", method = RequestMethod.GET)
	public String addNewImprovementCourse(Model model) {
		model.addAttribute("specialization", new ImprovementCourse());
		model.addAttribute("alreadyExists", false);
		model.addAttribute("state", "config");
		return "admin/common/edit-specialization";
	}

	@RequestMapping("/config/delete/job-sector/{abbreviation}")
	public String deleteJobSector(@PathVariable String abbreviation) {
		specializationService.delete(specializationService.getJobSectorByAbbreviation(abbreviation));
		return "redirect:/admin/";
	}

	@RequestMapping("/config/delete/improvement-course/{abbreviation}")
	public String deleteImprovementCourse(@PathVariable String abbreviation) {
		specializationService.delete(specializationService.getImprovementCourseByAbbreviation(abbreviation));
		return "redirect:/admin/";
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
	
	@RequestMapping("/run/main/student/{login}")
	public String displayStudent(@PathVariable String login, Model model, HttpServletRequest request) {
		model.addAttribute("student", studentService.retrieveStudentByLogin(login, request.getSession().getServletContext().getRealPath("/")));
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		return "admin/student";
	}
	
	@RequestMapping("/run/settings/process")
	public String manageProcess(Model model) {
		When whenToModify = configurationService.getWhen();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, 1);
		Calendar calendarFirstEmail = Calendar.getInstance();
		calendarFirstEmail.setTime(whenToModify.getFirstEmail());
		Calendar calendarSecondEmail = Calendar.getInstance();
		calendarSecondEmail.setTime(whenToModify.getSecondEmail());
		Calendar calendarEndSubmission = Calendar.getInstance();
		calendarEndSubmission.setTime(whenToModify.getEndSubmission());
		Calendar calendarEndValidation = Calendar.getInstance();
		calendarEndValidation.setTime(whenToModify.getEndValidation());
		model.addAttribute("modifyFirstEmail", calendar.before(calendarFirstEmail));
		model.addAttribute("modifySecondEmail", calendar.before(calendarSecondEmail));
		model.addAttribute("modifyEndSubmission", calendar.before(calendarEndSubmission));
		model.addAttribute("modifyEndValidation", calendar.before(calendarEndValidation));
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		model.addAttribute("firstEmail", dateFormat.format(whenToModify.getFirstEmail()));
		model.addAttribute("secondEmail", dateFormat.format(whenToModify.getSecondEmail()));
		model.addAttribute("endSubmission", dateFormat.format(whenToModify.getEndSubmission()));
		model.addAttribute("endValidation", dateFormat.format(whenToModify.getEndValidation()));
		model.addAttribute("when", new When());
		return "admin/run/settings/process";
	}
	
	@RequestMapping("/run/settings/specializations")
	public String manageSpecialization(Model model) {
		model.addAttribute("paAvailable", specializationService.findImprovementCourses());
		model.addAttribute("fmAvailable", specializationService.findJobSectors());
		return "admin/run/settings/specializations";
	}

	@RequestMapping("/run/settings/students")
	public String manageStudents(Model model) {
		model.addAttribute("studentsConcerned", studentService.findAllStudentsConcerned());
		model.addAttribute("studentsToExclude", studentService.findAllStudentsToExclude());
		model.addAttribute("promo", Calendar.getInstance().get(Calendar.YEAR) + 1);
		model.addAttribute("studentExclusion", new StudentsExclusion(studentService.findNecessarySizeForStudentExclusion()));
		return "admin/run/settings/students";
	}
	
	@RequestMapping("/run/settings/export")
	public String exportResults(Model model, HttpServletRequest request) {
		exportService.generatePdfResults(request.getSession().getServletContext().getRealPath("/"));
		return "admin/run/settings/export";
	}
	
	@RequestMapping(value = "/run/settings/edit-process", method = RequestMethod.POST)
	public String editProcess(@ModelAttribute When when, BindingResult result, Model model) {
		if ((when.getNumber() == 1) && (when.getEndValidation() == null)){
			FieldError fieldError = new FieldError("when", "endValidation", "lol");
			result.addError(fieldError);
		}
		if (when.getNumber() == 2){
			if (when.getEndSubmission() == null){
				FieldError fieldError = new FieldError("when", "endSubmission", "lol");
				result.addError(fieldError);
			}
			if (when.getEndValidation() == null){
				FieldError fieldError = new FieldError("when", "endValidation", "lol");
				result.addError(fieldError);
			}
		}
		if (when.getNumber() == 3){
			if (when.getSecondEmail() == null){
				FieldError fieldError = new FieldError("when", "secondEmail", "lol");
				result.addError(fieldError);
			}
			if (when.getEndSubmission() == null){
				FieldError fieldError = new FieldError("when", "endSubmission", "lol");
				result.addError(fieldError);
			}
			if (when.getEndValidation() == null){
				FieldError fieldError = new FieldError("when", "endValidation", "lol");
				result.addError(fieldError);
			}
		}
		if (when.getNumber() == 4){
			if (when.getFirstEmail() == null){
				FieldError fieldError = new FieldError("when", "firstEmail", "lol");
				result.addError(fieldError);
			}
			if (when.getSecondEmail() == null){
				FieldError fieldError = new FieldError("when", "secondEmail", "lol");
				result.addError(fieldError);
			}
			if (when.getEndSubmission() == null){
				FieldError fieldError = new FieldError("when", "endSubmission", "lol");
				result.addError(fieldError);
			}
			if (when.getEndValidation() == null){
				FieldError fieldError = new FieldError("when", "endValidation", "lol");
				result.addError(fieldError);
			}
		}
		if (result.hasErrors()) {
			When whenToModify = configurationService.getWhen();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR, 1);
			Calendar calendarFirstEmail = Calendar.getInstance();
			calendarFirstEmail.setTime(whenToModify.getFirstEmail());
			Calendar calendarSecondEmail = Calendar.getInstance();
			calendarSecondEmail.setTime(whenToModify.getSecondEmail());
			Calendar calendarEndSubmission = Calendar.getInstance();
			calendarEndSubmission.setTime(whenToModify.getEndSubmission());
			Calendar calendarEndValidation = Calendar.getInstance();
			calendarEndValidation.setTime(whenToModify.getEndValidation());
			model.addAttribute("modifyFirstEmail", calendar.before(calendarFirstEmail));
			model.addAttribute("modifySecondEmail", calendar.before(calendarSecondEmail));
			model.addAttribute("modifyEndSubmission", calendar.before(calendarEndSubmission));
			model.addAttribute("modifyEndValidation", calendar.before(calendarEndValidation));
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			model.addAttribute("firstEmail", dateFormat.format(whenToModify.getFirstEmail()));
			model.addAttribute("secondEmail", dateFormat.format(whenToModify.getSecondEmail()));
			model.addAttribute("endSubmission", dateFormat.format(whenToModify.getEndSubmission()));
			model.addAttribute("endValidation", dateFormat.format(whenToModify.getEndValidation()));
			model.addAttribute("when", new When());
			return "admin/run/settings/process";
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
				model.addAttribute("alertMessage", "Les dates doivent êtres successives.");
				When whenToModify = configurationService.getWhen();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.HOUR, 1);
				Calendar calendarFirstEmail = Calendar.getInstance();
				calendarFirstEmail.setTime(whenToModify.getFirstEmail());
				Calendar calendarSecondEmail = Calendar.getInstance();
				calendarSecondEmail.setTime(whenToModify.getSecondEmail());
				Calendar calendarEndSubmission = Calendar.getInstance();
				calendarEndSubmission.setTime(whenToModify.getEndSubmission());
				Calendar calendarEndValidation = Calendar.getInstance();
				calendarEndValidation.setTime(whenToModify.getEndValidation());
				model.addAttribute("modifyFirstEmail", calendar.before(calendarFirstEmail));
				model.addAttribute("modifySecondEmail", calendar.before(calendarSecondEmail));
				model.addAttribute("modifyEndSubmission", calendar.before(calendarEndSubmission));
				model.addAttribute("modifyEndValidation", calendar.before(calendarEndValidation));
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				model.addAttribute("firstEmail", dateFormat.format(whenToModify.getFirstEmail()));
				model.addAttribute("secondEmail", dateFormat.format(whenToModify.getSecondEmail()));
				model.addAttribute("endSubmission", dateFormat.format(whenToModify.getEndSubmission()));
				model.addAttribute("endValidation", dateFormat.format(whenToModify.getEndValidation()));
				model.addAttribute("when", new When());
				return "admin/run/settings/process";
			}
		}
		try {
			configurationService.updateWhen(when);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		model.addAttribute("successMessage", "Les modifications ont bien été prises en compte");
		return "redirect:/admin/administration/process";
	}
	
	@RequestMapping("/run/settings/stop-process")
	public String stopProcess() {
		try {
			configurationService.stopProcess();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return "redirect:/admin";
	}

	@RequestMapping("/run/main/choices/improvement-course/synthese/choice{order}")
	public String allResultsIc(@PathVariable int order, Model model) {
		model.addAttribute("order", order);
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		model.addAttribute("running", !configurationService.isValidationForAdminAvailable());
		model.addAttribute(
				"allStudents",
				configurationService.isValidationForAdminAvailable() ? studentService.findSimpleStudentsWithValidationForAllIcByOrder(order) : studentService
						.findSimpleStudentsForAllIcByOrder(order));
		return "admin/run/main/choices/improvement-course/synthese";
	}

	@RequestMapping("/run/main/choices/job-sector/synthese/choice{order}")
	public String allResultsJs(@PathVariable int order, Model model) {
		model.addAttribute("order", order);
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		model.addAttribute("running", !configurationService.isValidationForAdminAvailable());
		model.addAttribute(
				"allStudents",
				configurationService.isValidationForAdminAvailable() ? studentService.findSimpleStudentsWithValidationForAllJsByOrder(order) : studentService
						.findSimpleStudentsForAllJsByOrder(order));
		return "admin/run/main/choices/job-sector/synthese";
	}

	@RequestMapping("/run/main/choices/improvement-course/details/{abbreviation}/choice{order}")
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
		return "admin/run/main/choices/improvement-course/details";
	}

	@RequestMapping("/run/main/choices/job-sector/details/{abbreviation}/choice{order}")
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
		return "admin/run/main/choices/job-sector/details";
	}

	@RequestMapping("/run/main/statistics/choice{choice}")
	public String pieChartsForChoice(@PathVariable int choice, Model model){
		model.addAttribute("choiceNumber", choice);
		model.addAttribute("simpleImprovementCourses", statisticsService.findSimpleIcStats(choice));
		model.addAttribute("simpleJobSectors", statisticsService.findSimpleJsStats(choice));
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		return "admin/run/main/statistics/choice";
	}
	
	@RequestMapping("/run/main/statistics/form/synthese")
	public String pieChartsForForms(HttpServletRequest request, Model model){
		String path = request.getSession().getServletContext().getRealPath("/");
		Map<String, Integer> numbersForCategories = studentService.findSizeOfCategories(path);
		model.addAttribute("nbreAll", numbersForCategories.get("total"));
		model.addAttribute("nbrePartial", numbersForCategories.get("partial"));
		model.addAttribute("nbreNo", numbersForCategories.get("empty"));
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		return "admin/run/main/statistics/form-synthese";
	}
	
	@RequestMapping("/run/main/statistics/form/details/{category}")
	public String detailsForForms(HttpServletRequest request, @PathVariable String category, Model model){
		String path = request.getSession().getServletContext().getRealPath("/");
		List<Map<String, Object>> results = studentService.findStudentsForCategorySynthese(category, path);
		Map<String, Integer> numbersForCategories = studentService.getSizeOfCategories(path);
		model.addAttribute("nbreAll", numbersForCategories.get("total"));
		model.addAttribute("nbrePartial", numbersForCategories.get("partial"));
		model.addAttribute("nbreNo", numbersForCategories.get("empty"));
		model.addAttribute("category", category);
		model.addAttribute("results", results);
		model.addAttribute("allIc", specializationService.findImprovementCourses());
		model.addAttribute("allJs", specializationService.findJobSectors());
		return "admin/run/main/statistics/form-details";
	}

	@PostConstruct
	public void createFake() {
		fakeData.createFakeSpecialization();
		fakeData.createFakeAdmin();
	}

	@RequestMapping("/fake")
	public String populateResults() {
		fakeData.createFakeChoices();
		return "redirect:/admin/";
	}
	
	@RequestMapping("/fake2")
	public String fakeValidation() {
		fakeData.fakeValidation();
		return "redirect:/admin/";
	}

	@PreDestroy
	public void deleteFake() {
		fakeData.deleteAll();
	}

}
