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
import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.superuser.Admin;
import fr.affectation.domain.util.Mail;
import fr.affectation.domain.util.SimpleMail;
import fr.affectation.domain.util.StudentsExclusion;
import fr.affectation.service.admin.AdminService;
import fr.affectation.service.agap.AgapService;
import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.configuration.ConfigurationService;
import fr.affectation.service.configuration.When;
import fr.affectation.service.documents.DocumentService;
import fr.affectation.service.exclusion.ExclusionService;
import fr.affectation.service.export.ExportService;
import fr.affectation.service.fake.FakeDataService;
import fr.affectation.service.mail.MailService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.statistics.StatisticsService;
import fr.affectation.service.student.StudentService;
import fr.affectation.service.validation.ValidationService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Inject
	private SpecializationService specializationService;

	@Inject
	private StudentService studentService;

	@Inject
	private AgapService agapService;

	@Inject
	private ValidationService validationService;

	@Inject
	private ChoiceService choiceService;

	@Inject
	private ExclusionService exclusionService;

	@Inject
	private StatisticsService statisticsService;

	@Inject
	private ConfigurationService configurationService;

	@Inject
	private ExportService exportService;

	@Inject
	private FakeDataService fakeData;

	@Inject
	private MailService mailService;

	@Inject
	private AdminService adminService;

	@Inject
	private DocumentService documentService;

	@RequestMapping({ "/", "" })
	public String redirect(Model mode) {
		return configurationService.isRunning() ? "redirect:/admin/run/main/statistics/choice1" : "redirect:/admin/config";
	}

	@InitBinder
	public void initBinder(DataBinder binder) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormatter, true));
	}

	@RequestMapping(value = "/common/process-students-exclusion", method = RequestMethod.POST)
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
				exclusionService.save(login);
			}
		}
		for (String login : oldExcluded) {
			if (!newExcluded.contains(login)) {
				exclusionService.remove(login);
			}
		}
		return configurationService.isRunning() ? "redirect:/admin/run/settings/students" : "redirect:/admin/config/exclude";
	}

	@RequestMapping("/config/exclude")
	public String excludeStudent(Model model) {
		if (!configurationService.isRunning()) {
			model.addAttribute("run", false);
			model.addAttribute("studentsConcerned", studentService.findCurrentPromotionStudentsConcerned());
			model.addAttribute("studentsToExclude", studentService.findAllStudentsToExclude());
			model.addAttribute("studentsCesure", studentService.findCesureStudentsConcerned());
			model.addAttribute("promo", Calendar.getInstance().get(Calendar.YEAR) + 1);
			model.addAttribute("studentExclusion", new StudentsExclusion(studentService.findNecessarySizeForStudentExclusion()));
			return "admin/common/exclude-students";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/config/exclude-from-file")
	public String excludeStudentFromFile(Model model) {
		if (!configurationService.isRunning()) {
			model.addAttribute("studentsToExclude", studentService.findStudentsToExcludeName());
			model.addAttribute("run", false);
			return "admin/config/exclude-students-from-file";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping(value = "/config/process-students-exclusion", method = RequestMethod.POST)
	public String processExclusion(@RequestParam(value = "exclusion", required = true) MultipartFile exclusion, RedirectAttributes redirectAttributes) {
		if (!configurationService.isRunning()) {
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
			return "redirect:/admin/config/exclude";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/config")
	public String configurationIndex(Model model) {
		if (!configurationService.isRunning()) {
			model.addAttribute("when", new When());
			model.addAttribute("paAvailable", specializationService.findImprovementCourses());
			model.addAttribute("fmAvailable", specializationService.findJobSectors());
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			model.addAttribute("now", dateFormat.format(date));
			return "admin/config/index";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping(value = "/config/process-config-saving", method = RequestMethod.POST)
	public String post(@ModelAttribute When when, BindingResult result, Model model) {
		if (!configurationService.isRunning()) {
			if (when.getFirstEmail() == null) {
				FieldError fieldError = new FieldError("when", "firstEmail", "");
				result.addError(fieldError);
			}
			if (when.getSecondEmail() == null) {
				FieldError fieldError = new FieldError("when", "secondEmail", "");
				result.addError(fieldError);
			}
			if (when.getEndSubmission() == null) {
				FieldError fieldError = new FieldError("when", "endSubmission", "");
				result.addError(fieldError);
			}
			if (when.getEndValidation() == null) {
				FieldError fieldError = new FieldError("when", "endValidation", "");
				result.addError(fieldError);
			}
			if (result.hasErrors()) {
				model.addAttribute("paAvailable", specializationService.findImprovementCourses());
				model.addAttribute("fmAvailable", specializationService.findJobSectors());
				return "admin/config/index";
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
					return "admin/config/index";
				} else {
					if (!((specializationService.findImprovementCourses().size() >= 5) && (specializationService.findJobSectors().size() >= 5))) {
						model.addAttribute("paAvailable", specializationService.findImprovementCourses());
						model.addAttribute("fmAvailable", specializationService.findJobSectors());
						model.addAttribute("alertMessage",
								"Pour pouvoir lancer le processus, il est nécessaire qu'il y ait au moins 5 parcours d'approfondissement et 5 filières métier.");
						return "admin/config/index";
					} else {
						configurationService.setWhen(when);
						if (!configurationService.initialize()) {
							model.addAttribute("paAvailable", specializationService.findImprovementCourses());
							model.addAttribute("fmAvailable", specializationService.findJobSectors());
							model.addAttribute("alertMessage", "Impossible de sauvegarder la configuration.");
							return "admin/config/index";
						}
					}
				}
			}
			return "redirect:/admin";
		} else {
			return "redirect:/admin";
		}
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
		if (!configurationService.isRunning()) {
			model.addAttribute("specialization", new JobSector());
			model.addAttribute("alreadyExists", false);
			model.addAttribute("state", "config");
			return "admin/common/edit-specialization";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping(value = "/config/new/improvement-course", method = RequestMethod.GET)
	public String addNewImprovementCourse(Model model) {
		if (!configurationService.isRunning()) {
			model.addAttribute("specialization", new ImprovementCourse());
			model.addAttribute("alreadyExists", false);
			model.addAttribute("state", "config");
			return "admin/common/edit-specialization";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/config/delete/job-sector/{abbreviation}")
	public String deleteJobSector(@PathVariable String abbreviation) {
		if (!configurationService.isRunning()) {
			specializationService.delete(specializationService.getJobSectorByAbbreviation(abbreviation));
			return "redirect:/admin/";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/config/delete/improvement-course/{abbreviation}")
	public String deleteImprovementCourse(@PathVariable String abbreviation) {
		if (!configurationService.isRunning()) {
			specializationService.delete(specializationService.getImprovementCourseByAbbreviation(abbreviation));
			return "redirect:/admin/";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/main/student/{login}")
	public String displayStudent(@PathVariable String login, Model model, HttpServletRequest request) {
		if (configurationService.isRunning()) {
			model.addAttribute("student", studentService.retrieveStudentByLogin(login, request.getSession().getServletContext().getRealPath("/")));
			model.addAttribute("allIc", specializationService.findImprovementCourses());
			model.addAttribute("allJs", specializationService.findJobSectors());
			return "admin/run/main/student/student";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/settings/admins")
	public String manageAdmins(Model model) {
		if (configurationService.isRunning()) {
			model.addAttribute("mail1Activated", configurationService.isFirstMailActivated());
			model.addAttribute("mail2Activated", configurationService.isSecondMailActivated());
			model.addAttribute("currentAdmins", adminService.findAdminLogins());
			model.addAttribute("newAdmin", new Admin());
			return "admin/run/settings/admins";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/settings/admins/delete/{login}")
	public String deleteAdmins(Model model, @PathVariable String login, RedirectAttributes redirectAttributes) {
		if (configurationService.isRunning()) {
			int numberOfAdmins = adminService.findAdminLogins().size();
			if (numberOfAdmins == 1) {
				redirectAttributes.addFlashAttribute("errorMessage",
						"Impossible de supprimer cet administrateur. Il doit toujours rester au moins un administrateur.");
			} else {
				adminService.delete(login);
				redirectAttributes.addFlashAttribute("successMessage", "L'administrateur dont le login était " + login + " a bien été supprimé.");
			}
			return "redirect:/admin/run/settings/admins";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping(value = "/run/settings/admins/new", method = RequestMethod.POST)
	public String addNewAdmin(Model model, @ModelAttribute Admin admin, RedirectAttributes redirectAttributes) {
		if (configurationService.isRunning()) {
			if (admin.getLogin().equals("") || admin.getLogin() == null) {
				redirectAttributes.addFlashAttribute("alertMessage", "Impossible de sauvegarder un administrateur dont le login est vide.");
			} else {
				adminService.save(admin.getLogin());
				redirectAttributes
						.addFlashAttribute("successMessage", "Un nouvel administrateur dont le login est " + admin.getLogin() + " a bien été ajouté.");
			}
			return "redirect:/admin/run/settings/admins";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/settings/process")
	public String manageProcess(Model model) {
		if (configurationService.isRunning()) {
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
			model.addAttribute("mail1Activated", configurationService.isFirstMailActivated());
			model.addAttribute("mail2Activated", configurationService.isSecondMailActivated());
			model.addAttribute("firstEmail", dateFormat.format(whenToModify.getFirstEmail()));
			model.addAttribute("secondEmail", dateFormat.format(whenToModify.getSecondEmail()));
			model.addAttribute("endSubmission", dateFormat.format(whenToModify.getEndSubmission()));
			model.addAttribute("endValidation", dateFormat.format(whenToModify.getEndValidation()));
			model.addAttribute("when", new When());
			return "admin/run/settings/process";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/settings/specializations")
	public String manageSpecialization(Model model) {
		if (configurationService.isRunning()) {
			model.addAttribute("mail1Activated", configurationService.isFirstMailActivated());
			model.addAttribute("mail2Activated", configurationService.isSecondMailActivated());
			model.addAttribute("paAvailable", specializationService.findImprovementCourses());
			model.addAttribute("fmAvailable", specializationService.findJobSectors());
			return "admin/run/settings/specializations";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/settings/students")
	public String manageStudents(Model model) {
		if (configurationService.isRunning()) {
			model.addAttribute("run", true);
			model.addAttribute("mail1Activated", configurationService.isFirstMailActivated());
			model.addAttribute("mail2Activated", configurationService.isSecondMailActivated());
			model.addAttribute("studentsConcerned", studentService.findCurrentPromotionStudentsConcerned());
			model.addAttribute("studentsToExclude", studentService.findAllStudentsToExclude());
			model.addAttribute("studentsCesure", studentService.findCesureStudentsConcerned());
			model.addAttribute("promo", Calendar.getInstance().get(Calendar.YEAR) + 1);
			model.addAttribute("studentExclusion", new StudentsExclusion(studentService.findNecessarySizeForStudentExclusion()));
			return "admin/common/exclude-students";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/settings/export")
	public String exportResults(Model model) {
		if (configurationService.isRunning()) {
			model.addAttribute("mail1Activated", configurationService.isFirstMailActivated());
			model.addAttribute("mail2Activated", configurationService.isSecondMailActivated());
			return "admin/run/settings/export";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/settings/mail{number}")
	public String editMails(Model model, HttpServletRequest request, @PathVariable int number) {
		if (configurationService.isRunning()) {
			model.addAttribute("mail1Activated", configurationService.isFirstMailActivated());
			model.addAttribute("mail2Activated", configurationService.isSecondMailActivated());
			model.addAttribute("mail", number == 1 ? mailService.getFirstMail() : mailService.getSecondMail());
			model.addAttribute("number", number);
			model.addAttribute("activated", number == 1 ? configurationService.isFirstMailActivated() : configurationService.isSecondMailActivated());
			return "admin/run/settings/mail";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/settings/inverse-activation{number}")
	public String inverseMailActivation(Model model, HttpServletRequest request, @PathVariable int number) {
		if (configurationService.isRunning() && ((number == 1) || (number == 2))) {
			if (number == 1) {
				configurationService.setFirstMailActivated(!configurationService.isFirstMailActivated());
			} else if (number == 2) {
				configurationService.setSecondMailActivated(!configurationService.isSecondMailActivated());
			}
			return "redirect:/admin/run/settings/mail" + number;
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/settings/spontaneous-mail")
	public String spontaneousMail(Model model) {
		if (configurationService.isRunning()) {
			model.addAttribute("mail1Activated", configurationService.isFirstMailActivated());
			model.addAttribute("mail2Activated", configurationService.isSecondMailActivated());
			model.addAttribute("simpleMail", new SimpleMail());
			return "admin/run/settings/spontaneous-mail";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping(value = "/run/settings/process-spontaneous-mail", method = RequestMethod.POST)
	public String processSpontaneousMail(@ModelAttribute @Valid SimpleMail simpleMail, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (configurationService.isRunning()) {
			if (result.hasErrors()) {
				return "admin/run/settings/spontaneous-mail";
			}
			String path = request.getSession().getServletContext().getRealPath("/");
			studentService.sendSimpleMail(simpleMail, path);
			redirectAttributes.addFlashAttribute("flashMessage", "Le mail a bien été envoyé.");
			return "redirect:/admin/run/settings/spontaneous-mail";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping(value = "/run/settings/process-mail-edition", method = RequestMethod.POST)
	public String saveFirstMail(@ModelAttribute @Valid Mail mail, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (configurationService.isRunning()) {
			if (result.hasErrors()) {
				model.addAttribute("number", (int) mail.getId());
				return "admin/run/settings/mail";
			}
			mailService.save(mail);
			redirectAttributes.addFlashAttribute("flashMessage", "Le mail a bien été modifié.");
			return "redirect:/admin/run/settings/mail" + mail.getId();
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping(value = "/run/settings/edit-process", method = RequestMethod.POST)
	public String editProcess(@ModelAttribute When when, BindingResult result, Model model) {
		if (configurationService.isRunning()) {
			if ((when.getNumber() == 1) && (when.getEndValidation() == null)) {
				FieldError fieldError = new FieldError("when", "endValidation", "");
				result.addError(fieldError);
			}
			if (when.getNumber() == 2) {
				if (when.getEndSubmission() == null) {
					FieldError fieldError = new FieldError("when", "endSubmission", "");
					result.addError(fieldError);
				}
				if (when.getEndValidation() == null) {
					FieldError fieldError = new FieldError("when", "endValidation", "");
					result.addError(fieldError);
				}
			}
			if (when.getNumber() == 3) {
				if (when.getSecondEmail() == null) {
					FieldError fieldError = new FieldError("when", "secondEmail", "");
					result.addError(fieldError);
				}
				if (when.getEndSubmission() == null) {
					FieldError fieldError = new FieldError("when", "endSubmission", "");
					result.addError(fieldError);
				}
				if (when.getEndValidation() == null) {
					FieldError fieldError = new FieldError("when", "endValidation", "");
					result.addError(fieldError);
				}
			}
			if (when.getNumber() == 4) {
				if (when.getFirstEmail() == null) {
					FieldError fieldError = new FieldError("when", "firstEmail", "");
					result.addError(fieldError);
				}
				if (when.getSecondEmail() == null) {
					FieldError fieldError = new FieldError("when", "secondEmail", "");
					result.addError(fieldError);
				}
				if (when.getEndSubmission() == null) {
					FieldError fieldError = new FieldError("when", "endSubmission", "");
					result.addError(fieldError);
				}
				if (when.getEndValidation() == null) {
					FieldError fieldError = new FieldError("when", "endValidation", "");
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
				model.addAttribute("mail1Activated", configurationService.isFirstMailActivated());
				model.addAttribute("mail2Activated", configurationService.isSecondMailActivated());
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				model.addAttribute("firstEmail", dateFormat.format(whenToModify.getFirstEmail()));
				model.addAttribute("secondEmail", dateFormat.format(whenToModify.getSecondEmail()));
				model.addAttribute("endSubmission", dateFormat.format(whenToModify.getEndSubmission()));
				model.addAttribute("endValidation", dateFormat.format(whenToModify.getEndValidation()));
				model.addAttribute("when", new When());
				return "admin/run/settings/process";
			} else {
				List<Date> allDates = new ArrayList<Date>();
				switch (when.getNumber()) {
				case 1:
					allDates.add(when.getEndValidation());
					break;
				case 2:
					allDates.add(when.getEndSubmission());
					allDates.add(when.getEndValidation());
					break;
				case 3:
					allDates.add(when.getSecondEmail());
					allDates.add(when.getEndSubmission());
					allDates.add(when.getEndValidation());
					break;
				case 4:
					allDates.add(when.getFirstEmail());
					allDates.add(when.getSecondEmail());
					allDates.add(when.getEndSubmission());
					allDates.add(when.getEndValidation());
				}
				boolean areDatesSuccessive = true;
				for (int i = 0; i < allDates.size() - 1; i++) {
					Date date1 = allDates.get(i);
					Date date2 = allDates.get(i + 1);
					if (!date1.before(date2)) {
						areDatesSuccessive = false;
					}
				}
				if (!areDatesSuccessive) {
					model.addAttribute("alertMessage", "Impossible de sauvegarder cette configuration, les dates doivent êtres successives.");
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
					model.addAttribute("mail1Activated", configurationService.isFirstMailActivated());
					model.addAttribute("mail2Activated", configurationService.isSecondMailActivated());
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
			return "redirect:/admin/run/settings/process";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/settings/stop-process")
	public String stopProcess(HttpServletRequest request) {
		if (configurationService.isRunning()) {
			try {
				configurationService.stopProcess();
				choiceService.deleteAll();
				validationService.removeAll();
				exclusionService.removeAll();
				documentService.deleteAll(request.getSession().getServletContext().getRealPath("/"));
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
			return "redirect:/admin";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/main/choices/improvement-course/synthese/choice{order}")
	public String allResultsIc(@PathVariable int order, Model model) {
		if (configurationService.isRunning()) {
			model.addAttribute("order", order);
			model.addAttribute("allIc", specializationService.findImprovementCourses());
			model.addAttribute("allJs", specializationService.findJobSectors());
			model.addAttribute("running", !configurationService.isValidationForAdminAvailable());
			model.addAttribute("allStudents",
					configurationService.isValidationForAdminAvailable() ? studentService.findSimpleStudentsWithValidationForAllIcByOrder(order)
							: studentService.findSimpleStudentsForAllIcByOrder(order));
			return "admin/run/main/choices/improvement-course/synthese";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/main/choices/job-sector/synthese/choice{order}")
	public String allResultsJs(@PathVariable int order, Model model) {
		if (configurationService.isRunning()) {
			model.addAttribute("order", order);
			model.addAttribute("allIc", specializationService.findImprovementCourses());
			model.addAttribute("allJs", specializationService.findJobSectors());
			model.addAttribute("running", !configurationService.isValidationForAdminAvailable());
			model.addAttribute("allStudents",
					configurationService.isValidationForAdminAvailable() ? studentService.findSimpleStudentsWithValidationForAllJsByOrder(order)
							: studentService.findSimpleStudentsForAllJsByOrder(order));
			return "admin/run/main/choices/job-sector/synthese";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/main/choices/improvement-course/details/{abbreviation}/choice{order}")
	public String resultsIcDetails(@PathVariable String abbreviation, @PathVariable int order, Model model) {
		if (configurationService.isRunning()) {
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
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/main/choices/job-sector/details/{abbreviation}/choice{order}")
	public String resultsJsDetails(@PathVariable String abbreviation, @PathVariable int order, Model model) {
		if (configurationService.isRunning()) {
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
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/main/statistics/choice{choice}")
	public String pieChartsForChoice(@PathVariable int choice, Model model) {
		if (configurationService.isRunning()) {
			model.addAttribute("choiceNumber", choice);
			model.addAttribute("simpleImprovementCourses", statisticsService.findSimpleIcStats(choice));
			model.addAttribute("simpleJobSectors", statisticsService.findSimpleJsStats(choice));
			model.addAttribute("allIc", specializationService.findImprovementCourses());
			model.addAttribute("allJs", specializationService.findJobSectors());
			return "admin/run/main/statistics/choice";
		} else {
			return "redirect:/admin";
		}
	}
	
	@RequestMapping("/run/main/statistics/repartition-other-choice{choice}/{type}/{abbreviation}")
	public String pieChartsForOtherChoice(@PathVariable int choice, @PathVariable String abbreviation, @PathVariable int type, Model model) {
		if (configurationService.isRunning()) {
			Specialization specialization = type == Specialization.IMPROVEMENT_COURSE ? specializationService
					.getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
			model.addAttribute("specialization", specialization);
			model.addAttribute("choiceNumber", choice);
			model.addAttribute("type", type);
			model.addAttribute("specializations", studentService.findChoiceRepartitionKnowingOne(1, choice, specialization));
			model.addAttribute("allIc", specializationService.findImprovementCourses());
			model.addAttribute("allJs", specializationService.findJobSectors());
			return "admin/run/main/statistics/repartition-other-choice";
		} else {
			return "redirect:/admin/";
		}
	}

	@RequestMapping("/run/main/statistics/inverse-repartition/ic/{abbreviation}")
	public String inverseRepartitionIc(@PathVariable String abbreviation, Model model) {
		if (configurationService.isRunning()) {
			Specialization specialization = specializationService.getImprovementCourseByAbbreviation(abbreviation);
			model.addAttribute("specialization", specialization);
			model.addAttribute("inverseSpecializations", studentService.findInverseRepartition(specialization));
			model.addAttribute("allIc", specializationService.findImprovementCourses());
			model.addAttribute("allJs", specializationService.findJobSectors());
			return "admin/run/main/statistics/inverse-repartition";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/main/statistics/inverse-repartition/js/{abbreviation}")
	public String inverseRepartitionJs(@PathVariable String abbreviation, Model model) {
		if (configurationService.isRunning()) {
			Specialization specialization = specializationService.getJobSectorByAbbreviation(abbreviation);
			model.addAttribute("specialization", specialization);
			model.addAttribute("inverseSpecializations", studentService.findInverseRepartition(specialization));
			model.addAttribute("allIc", specializationService.findImprovementCourses());
			model.addAttribute("allJs", specializationService.findJobSectors());
			return "admin/run/main/statistics/inverse-repartition";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/main/statistics/form/synthese")
	public String pieChartsForForms(HttpServletRequest request, Model model) {
		if (configurationService.isRunning()) {
			String path = request.getSession().getServletContext().getRealPath("/");
			Map<String, Integer> numbersForCategories = studentService.findSizeOfCategories(path);
			model.addAttribute("nbreAll", numbersForCategories.get("total"));
			model.addAttribute("nbrePartial", numbersForCategories.get("partial"));
			model.addAttribute("nbreNo", numbersForCategories.get("empty"));
			model.addAttribute("allIc", specializationService.findImprovementCourses());
			model.addAttribute("allJs", specializationService.findJobSectors());
			return "admin/run/main/statistics/form-synthese";
		} else {
			return "redirect:/admin";
		}
	}

	@RequestMapping("/run/main/statistics/form/details/{category}")
	public String detailsForForms(HttpServletRequest request, @PathVariable String category, Model model) {
		if (configurationService.isRunning()) {
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
		} else {
			return "redirect:/admin";
		}
	}

	@PostConstruct
	public void initialize() {
		fakeData.createFakeSpecialization();
		adminService.save("admin");
		// Mail first = new Mail((long) 1, "Voeux Parcours/Filières 3A",
		// "Bonjour, vous n'avez pas ...");
		// Mail second = new Mail((long) 2, "Voeux Parcours/Filières 3A",
		// "Bonjour, vous n'avez pas ...");
		// mailService.save(first);
		// mailService.save(second);
		configurationService.initializeFromDataBase();
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

}
