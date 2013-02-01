package fr.affectation.web.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.domain.comparator.ComparatorName;
import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.student.Student;
import fr.affectation.domain.student.StudentToExclude;
import fr.affectation.domain.util.StudentsExclusion;
import fr.affectation.service.agap.AgapService;
import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.configuration.ConfigurationService;
import fr.affectation.service.configuration.When;
import fr.affectation.service.documents.DocumentService;
import fr.affectation.service.fake.FakeDataService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.statistics.StatisticsService;
import fr.affectation.service.student.StudentService;
import fr.affectation.service.superuser.SuperUserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Inject
	private ChoiceService choiceService;

	@Inject
	private SpecializationService specializationService;

	@Inject
	private StudentService studentService;

	@Inject
	private DocumentService documentService;

	@Inject
	private AgapService agapService;

	@Inject
	private SuperUserService superUserService;

	@Inject
	private StatisticsService statisticsService;

	@Inject
	private ConfigurationService configurationService;

	@Inject
	private FakeDataService fakeData;

	@RequestMapping({ "/", "" })
	public String redirect(Model mode) {
		return configurationService.isRunning() ? "redirect:/admin/statistics/synthese"
				: "redirect:/admin/config";
	}

	@InitBinder
	public void initBinder(DataBinder binder) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormatter, true));
	}

	@RequestMapping("/administration/specialization")
	public String manageSpecialization(Model model) {
		return "admin/administration/specialization";
	}

	@RequestMapping("/administration/process")
	public String manageProcess(Model model) {
		return "admin/administration/process";
	}

	@RequestMapping("/administration/students")
	public String manageStudents(Model model) {
		model.addAttribute("studentsConcerned",
				studentService.findAllStudentsConcerned());
		model.addAttribute("studentsToExclude",
				studentService.findAllStudentsToExclude());
		model.addAttribute("promo",
				Calendar.getInstance().get(Calendar.YEAR) + 1);
		model.addAttribute("studentExclusion", new StudentsExclusion(
				studentService.findNecessarySizeForStudentExclusion()));
		return "admin/administration/students";
	}

	@RequestMapping(value = "/run/edit-exclusion", method = RequestMethod.POST)
	public String editExclusion(StudentsExclusion studentExclusion) {
		List<String> newExcluded = new ArrayList<String>();
		List<String> oldExcluded = studentService.findAllStudentToExcludeLogin();
		for (String login : studentExclusion.getExcluded()){
			if (!login.equals("")){
				newExcluded.add(login);
			}
		}
		for (String login : newExcluded){
			if (!oldExcluded.contains(login)){
				studentService.saveStudentToExclude(new StudentToExclude(login));
			}
		}
		for (String login : oldExcluded){
			if (!newExcluded.contains(login)){
				studentService.removeStudentByLogin(login);
			}
		}
		return "redirect:/admin/administration/students";
	}

	@RequestMapping("/exclude")
	public String excludeStudent(Model model) {
		List<String> studentsName = findAllStudentsToExcludeName();
		model.addAttribute("studentsToExclude", studentsName);
		return "admin/exclude-students";
	}

	public List<String> findAllStudentsToExcludeName() {
		List<String> studentsLogin = studentService
				.findAllStudentToExcludeLogin();
		List<String> studentsName = new ArrayList<String>();
		for (String login : studentsLogin) {
			studentsName.add(agapService.getNameFromLogin(login));
		}
		Collections.sort(studentsName, new ComparatorName());
		return studentsName;
	}

	@RequestMapping(value = "/excludeProcess", method = RequestMethod.POST)
	public String processExclusion(
			@RequestParam(value = "exclusion", required = true) MultipartFile exclusion,
			RedirectAttributes redirectAttributes) {
		if (exclusion.isEmpty()) {
			redirectAttributes.addFlashAttribute("alertMessage",
					"Le fichier est vide.");
		} else {
			boolean condition = true;
			condition = exclusion.getContentType().equals(
					"application/vnd.ms-excel") || exclusion.getContentType().equals(
							"application/msexcel");
			if (!condition) {
				redirectAttributes.addFlashAttribute("alertMessage",
						"Seuls les fichiers (*.xls) sont acceptés.");
				System.out.println("ptdr " + exclusion.getContentType());
			} else {
				if (studentService.populateStudentToExcludeFromFile(exclusion)) {
					redirectAttributes.addFlashAttribute("successMessage",
							"Le fichier a bien été ajouté.");
				} else {
					redirectAttributes
							.addFlashAttribute("alertMessage",
									"Une erreur est survenue lors de la lecture du fichier.");
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
	public String displayStudent(@PathVariable String login, Model model,
			HttpServletRequest request) {
		agapService.generateRanking();
		agapService.generateUeCode();
		Student student = new Student();
		student.setDetails(agapService.getStudentDetailsFromLogin(login));
		ImprovementCourseChoice icChoice = (ImprovementCourseChoice) choiceService
				.getImprovementCourseChoicesByLogin(login);
		student.setImprovementCourseChoice(icChoice);
		JobSectorChoice jsChoice = (JobSectorChoice) choiceService
				.getJobSectorChoicesByLogin(login);
		student.setJobSectorChoice(jsChoice);
		student.setResults(agapService.getResultsFromLogin(login));
		model.addAttribute("student", student);

		String path = request.getSession().getServletContext().getRealPath("/");
		model.addAttribute("hasFilledResume",
				documentService.hasFilledResume(path, login));
		model.addAttribute("hasFilledLetterIc",
				documentService.hasFilledLetterIc(path, login));
		model.addAttribute("hasFilledLetterJs",
				documentService.hasFilledLetterJs(path, login));

		model.addAttribute("allIc",
				specializationService.findAllImprovementCourse());
		model.addAttribute("allJs", specializationService.findAllJobSector());
		return "admin/student";
	}

	@RequestMapping("/config")
	public String configurationIndex(Model model) {
		model.addAttribute("when", new When());
		model.addAttribute("paAvailable",
				specializationService.findAllImprovementCourse());
		model.addAttribute("fmAvailable",
				specializationService.findAllJobSector());
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		model.addAttribute("now", dateFormat.format(date));
		return "admin/configure-index";
	}

	@RequestMapping("/config/modif/parcours/{abbreviation}")
	public String modifyIc(@PathVariable String abbreviation, Model model) {
		model.addAttribute("specialization", specializationService
				.getImprovementCourseByAbbreviation(abbreviation));
		model.addAttribute("alreadyExists", true);
		model.addAttribute("state", configurationService.isRunning() ? "run"
				: "config");
		return "admin/edit-specialization";
	}

	@RequestMapping("/config/modif/filieres/{abbreviation}")
	public String modifyJs(@PathVariable String abbreviation, Model model) {
		model.addAttribute("specialization",
				specializationService.getJobSectorByAbbreviation(abbreviation));
		model.addAttribute("alreadyExists", true);
		model.addAttribute("state", configurationService.isRunning() ? "run"
				: "config");
		return "admin/edit-specialization";
	}

	@RequestMapping(value = "/config/modif/new/filieres", method = RequestMethod.GET)
	public String addNewJobSector(Model model) {
		model.addAttribute("specialization", new JobSector());
		model.addAttribute("alreadyExists", false);
		model.addAttribute("state", "config");
		return "admin/edit-specialization";
	}

	@RequestMapping(value = "/config/modif/new/parcours", method = RequestMethod.GET)
	public String addNewImprovementCourse(Model model) {
		model.addAttribute("specialization", new ImprovementCourse());
		model.addAttribute("alreadyExists", false);
		model.addAttribute("state", "config");
		return "admin/edit-specialization";
	}

	@RequestMapping(value = "/config/edit/ic", method = RequestMethod.POST)
	public String saveImprovementCourse(
			@ModelAttribute("specialization") @Valid ImprovementCourse specialization,
			BindingResult result, Model model) {
		List<String> validUeCode = agapService.findAllValidForSpecUeCode();
		if (!validUeCode.contains(specialization.getCodeUe1())) {
			FieldError fieldError = new FieldError("specialization", "codeUe1",
					"lol");
			result.addError(fieldError);
		}
		if (!validUeCode.contains(specialization.getCodeUe2())) {
			FieldError fieldError = new FieldError("specialization", "codeUe2",
					"lol");
			result.addError(fieldError);
		}
		if (!validUeCode.contains(specialization.getCodeUe3())) {
			FieldError fieldError = new FieldError("specialization", "codeUe3",
					"lol");
			result.addError(fieldError);
		}
		if (!validUeCode.contains(specialization.getCodeUe4())) {
			FieldError fieldError = new FieldError("specialization", "codeUe4",
					"lol");
			result.addError(fieldError);
		}
		if (!validUeCode.contains(specialization.getCodeUe5())) {
			FieldError fieldError = new FieldError("specialization", "codeUe5",
					"lol");
			result.addError(fieldError);
		}

		if (result.hasErrors()) {
			model.addAttribute("state", configurationService.isRunning() ? "run"
					: "config");
			return "admin/edit-specialization";
		}
		specializationService.save(specialization);
		return configurationService.isRunning() ? "redirect:/admin/config/ic"
				: "redirect:/admin/config";
	}

	@RequestMapping(value = "/config/edit/js", method = RequestMethod.POST)
	public String saveJobSector(
			@ModelAttribute("specialization") @Valid JobSector specialization,
			BindingResult result, Model model) {
		List<String> validUeCode = agapService.findAllValidForSpecUeCode();
		if (!validUeCode.contains(specialization.getCodeUe1())) {
			FieldError fieldError = new FieldError("specialization", "codeUe1",
					"lol");
			result.addError(fieldError);
		}
		if (!validUeCode.contains(specialization.getCodeUe2())) {
			FieldError fieldError = new FieldError("specialization", "codeUe2",
					"lol");
			result.addError(fieldError);
		}
		if (!validUeCode.contains(specialization.getCodeUe3())) {
			FieldError fieldError = new FieldError("specialization", "codeUe3",
					"lol");
			result.addError(fieldError);
		}
		if (!validUeCode.contains(specialization.getCodeUe4())) {
			FieldError fieldError = new FieldError("specialization", "codeUe4",
					"lol");
			result.addError(fieldError);
		}
		if (!validUeCode.contains(specialization.getCodeUe5())) {
			FieldError fieldError = new FieldError("specialization", "codeUe5",
					"lol");
			result.addError(fieldError);
		}
		if (result.hasErrors()) {
			model.addAttribute("state", configurationService.isRunning() ? "run"
					: "config");
			return "admin/edit-specialization";
		}
		specializationService.save(specialization);
		return configurationService.isRunning() ? "redirect:/admin/config/js"
				: "redirect:/admin/config";
	}

	@RequestMapping(value = "/config/ic")
	public String configurePa(Model model) {
		model.addAttribute("paAvailable",
				specializationService.findAllImprovementCourse());
		if (configurationService.isRunning()) {
			model.addAttribute("state", "run");
		} else {
			model.addAttribute("state", "config");
		}
		return "admin/configure-pa";
	}

	@RequestMapping(value = "/config/js")
	public String configureFm(Model model) {
		model.addAttribute("fmAvailable",
				specializationService.findAllJobSector());
		if (configurationService.isRunning()) {
			model.addAttribute("state", "run");
		} else {
			model.addAttribute("state", "config");
		}
		return "admin/configure-fm";
	}

	@RequestMapping("/config/delete/filieres/{abbreviation}")
	public String deleteJobSector(@PathVariable String abbreviation) {
		specializationService.delete(specializationService
				.getJobSectorByAbbreviation(abbreviation));
		return "redirect:/admin/config";
	}

	@RequestMapping("/config/delete/parcours/{abbreviation}")
	public String deleteImprovementCourse(@PathVariable String abbreviation) {
		specializationService.delete(specializationService
				.getImprovementCourseByAbbreviation(abbreviation));
		return "redirect:/admin/config";
	}

	@RequestMapping(value = "/save-config", method = RequestMethod.POST)
	public String post(@ModelAttribute When when, BindingResult result,
			Model model) {
		if (when.getFirstEmail() == null) {
			FieldError fieldError = new FieldError("when", "firstEmail", "lol");
			result.addError(fieldError);
		}
		if (when.getSecondEmail() == null) {
			FieldError fieldError = new FieldError("when", "secondEmail", "lol");
			result.addError(fieldError);
		}
		if (when.getEndSubmission() == null) {
			FieldError fieldError = new FieldError("when", "endSubmission",
					"lol");
			result.addError(fieldError);
		}
		if (when.getEndValidation() == null) {
			FieldError fieldError = new FieldError("when", "endValidation",
					"lol");
			result.addError(fieldError);
		}
		if (result.hasErrors()) {
			model.addAttribute("paAvailable",
					specializationService.findAllImprovementCourse());
			model.addAttribute("fmAvailable",
					specializationService.findAllJobSector());
			return "admin/configure-index";
		}
		else{
			List<Date> allDates = new ArrayList<Date>();
			allDates.add(when.getFirstEmail());
			allDates.add(when.getSecondEmail());
			allDates.add(when.getEndSubmission());
			allDates.add(when.getEndValidation());
			boolean areDatesSuccessive = true;
			for (int i=0; i<allDates.size()-1; i++){
				Date date1 = allDates.get(i);
				Date date2 = allDates.get(i+1);
				if (!date1.before(date2)){
					areDatesSuccessive = false;
				}
			}
			if (!areDatesSuccessive){
				model.addAttribute("paAvailable",
						specializationService.findAllImprovementCourse());
				model.addAttribute("fmAvailable",
						specializationService.findAllJobSector());
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

	@RequestMapping("/config/stopProcess")
	public String stopProcess(Model model) {
		try {
			configurationService.stopProcess();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/admin";
	}

	@RequestMapping("/parcours/statistics")
	public String statisticsIc(Model model, HttpServletRequest request) {
		statisticsService.generatePieChartIc(request.getSession()
				.getServletContext().getRealPath("/"));
		model.addAttribute("type", 1);
		model.addAttribute("allIc",
				specializationService.findAllImprovementCourse());
		model.addAttribute("allJs", specializationService.findAllJobSector());
		return "admin/statistics";
	}

	@RequestMapping("/filieres/statistics")
	public String statisticsSynthese(Model model, HttpServletRequest request) {
		statisticsService.generatePieChartJs(request.getSession()
				.getServletContext().getRealPath("/"));
		model.addAttribute("type", 2);
		model.addAttribute("allIc",
				specializationService.findAllImprovementCourse());
		model.addAttribute("allJs", specializationService.findAllJobSector());
		return "admin/statistics";
	}

	@RequestMapping("/statistics/synthese")
	public String statisticsJs(Model model, HttpServletRequest request) {
		statisticsService.generatePieChartJs(request.getSession()
				.getServletContext().getRealPath("/"));
		statisticsService.generatePieChartIc(request.getSession()
				.getServletContext().getRealPath("/"));
		model.addAttribute("allIc",
				specializationService.findAllImprovementCourse());
		model.addAttribute("allJs", specializationService.findAllJobSector());
		return "admin/statistics-synthese";
	}

	//OK
	@RequestMapping("/parcours/synthese/choix{order}")
	public String allResultsIc(@PathVariable int order, Model model) {
		model.addAttribute("order", order);
		model.addAttribute("allIc", specializationService.findAllImprovementCourse());
		model.addAttribute("allJs", specializationService.findAllJobSector());
		model.addAttribute("running", !configurationService.isValidationForAdminAvailable());
		model.addAttribute("allStudents", configurationService.isValidationForAdminAvailable() ? studentService.findSimpleStudentsWithValidationForAllIcByOrder(order) :
				studentService.findSimpleStudentsForAllIcByOrder(order));
		return "admin/choix/parcours/synthese";
	}
	
	//OK
	@RequestMapping("/filieres/synthese/choix{order}")
	public String allResultsJs(@PathVariable int order, Model model) {
		model.addAttribute("order", order);
		model.addAttribute("allIc", specializationService.findAllImprovementCourse());
		model.addAttribute("allJs", specializationService.findAllJobSector());
		model.addAttribute("running", !configurationService.isValidationForAdminAvailable());
		model.addAttribute("allStudents", configurationService.isValidationForAdminAvailable() ? studentService.findSimpleStudentsWithValidationForAllJsByOrder(order) :
			studentService.findSimpleStudentsForAllJsByOrder(order));
		return "admin/choix/filieres/synthese";
	}

	//OK
	@RequestMapping("/parcours/details/{abbreviation}/choix{order}")
	public String resultsIcDetails(@PathVariable String abbreviation,
			@PathVariable int order, Model model) {
		ImprovementCourse improvementCourse = specializationService
				.getImprovementCourseByAbbreviation(abbreviation);
		model.addAttribute("order", order);
		model.addAttribute("allIc", specializationService.findAllImprovementCourse());
		model.addAttribute("allJs", specializationService.findAllJobSector());
		model.addAttribute("abbreviation", abbreviation);
		model.addAttribute("specialization", improvementCourse);
		model.addAttribute("allStudents", configurationService.isValidationForAdminAvailable() ? studentService.findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(order, improvementCourse) :
			studentService.findSimpleStudentsByOrderChoiceAndSpecialization(order, improvementCourse));
		model.addAttribute("running", !configurationService.isValidationForAdminAvailable());
		return "admin/choix/parcours/details";
	}

	//OK
	@RequestMapping("/filieres/details/{abbreviation}/choix{order}")
	public String resultsJsDetails(@PathVariable String abbreviation,
			@PathVariable int order, Model model) {
		JobSector jobSector = specializationService
				.getJobSectorByAbbreviation(abbreviation);
		model.addAttribute("order", order);
		model.addAttribute("allIc", specializationService.findAllImprovementCourse());
		model.addAttribute("allJs", specializationService.findAllJobSector());
		model.addAttribute("abbreviation", abbreviation);
		model.addAttribute("specialization", jobSector);
		model.addAttribute("allStudents", configurationService.isValidationForAdminAvailable() ? studentService.findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(order, jobSector) :
			studentService.findSimpleStudentsByOrderChoiceAndSpecialization(order, jobSector));
		model.addAttribute("running", !configurationService.isValidationForAdminAvailable());
		return "admin/choix/filieres/details";
	}

	@RequestMapping("/statistics/eleves/{category}")
	public String studentSynthese(@PathVariable String category,
			HttpServletRequest request, Model model) {
		List<String> logins = agapService.getAllStudentConcernedLogin();
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

		String path = request.getSession().getServletContext().getRealPath("/");

		Map<String, Object> map;
		boolean filledDoc = false;
		boolean filledChoices = false;
		int nbreAll = 0;
		int nbrePartial = 0;
		int nbreNo = 0;

		for (String login : logins) {
			filledDoc = documentService.hasFilledLetterIc(path, login)
					&& documentService.hasFilledLetterJs(path, login)
					&& documentService.hasFilledResume(path, login);
			filledChoices = (choiceService
					.getElementNotFilledImprovementCourse(login).size() == 0)
					&& (choiceService.getElementNotFilledJobSector(login)
							.size() == 0);

			if ((filledDoc) && (filledChoices)) {
				if (category.equals("all")) {
					map = new HashMap<String, Object>();
					map.put("name", agapService.getNameFromLogin(login));
					map.put("login", login);
					results.add(map);
				}
				nbreAll += 1;
			} else {
				if ((!filledDoc) && (!filledChoices)) {
					if (category.equals("no")) {
						map = new HashMap<String, Object>();
						map.put("name", agapService.getNameFromLogin(login));
						map.put("login", login);
						results.add(map);
					}
					nbreNo += 1;
				} else {
					if (category.equals("partial")) {
						map = new HashMap<String, Object>();
						map.put("name", agapService.getNameFromLogin(login));
						map.put("login", login);
						results.add(map);
					}
					nbrePartial += 1;
				}
			}

		}

		model.addAttribute("nbreAll", nbreAll);
		model.addAttribute("nbrePartial", nbrePartial);
		model.addAttribute("nbreNo", nbreNo);
		model.addAttribute("category", category);
		model.addAttribute("results", results);
		model.addAttribute("allIc",
				specializationService.findAllImprovementCourse());
		model.addAttribute("allJs", specializationService.findAllJobSector());

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

	@PreDestroy
	public void deleteFake() {
		fakeData.deleteAll();
	}

}
