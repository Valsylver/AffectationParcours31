package fr.affectation.service.fake;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import fr.affectation.domain.choice.Choice;
import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.service.admin.AdminService;
import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.exclusion.ExclusionService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.student.StudentService;
import fr.affectation.service.validation.ValidationService;

@Service
public class FakeDataService {
	
	@Inject
	private ChoiceService choiceService;
	
//	@Inject
//	private AgapService agapService;
	
	@Inject
	private AdminService superUserService;
	
	@Inject
	private StudentService studentService;
	
	@Inject
	private SpecializationService specializationService;
	
	@Inject
	private ValidationService validationService;
	
	@Inject
	private ExclusionService exclusionService;
	
	public void createFakeChoices(){
		List<String> liste = specializationService.findImprovementCourseAbbreviations();

		List<String> liste2 = specializationService.findJobSectorAbbreviations();

		int i = 0;
		List<String> liste3 = new ArrayList<String>();
		List<String> liste4 = new ArrayList<String>();

		for (String login : studentService.findAllStudentsConcernedLogin()) {
			int willSubmit = (int) (Math.random() * 10);
			if (willSubmit < 9){
				liste3.clear();
				for (String s : liste) {
					liste3.add(s);
				}
				Choice choice = new ImprovementCourseChoice();
				choice.setLogin(login);
				for (int iterationPa = 1; iterationPa < 6; iterationPa++) {
					i = (int) (Math.random() * (liste.size() - iterationPa + 1));
					switch (iterationPa) {
					case 1:
						choice.setChoice1(liste3.get(i));
					case 2:
						choice.setChoice2(liste3.get(i));
					case 3:
						choice.setChoice3(liste3.get(i));
					case 4:
						choice.setChoice4(liste3.get(i));
					case 5:
						choice.setChoice5(liste3.get(i));
					}
					liste3.remove(i);
				}
				choiceService.save(choice);
	
				liste4.clear();
				for (String s : liste2) {
					liste4.add(s);
				}
				Choice choice2 = new JobSectorChoice();
				choice2.setLogin(login);
				double f;
				for (int iterationFm = 1; iterationFm < 6; iterationFm++) {
					f = Math.random() * (liste2.size() - iterationFm + 1);
					i = (int) (f);
					switch (iterationFm) {
					case 1:
						choice2.setChoice1(liste4.get(i));
					case 2:
						choice2.setChoice2(liste4.get(i));
					case 3:
						choice2.setChoice3(liste4.get(i));
					case 4:
						choice2.setChoice4(liste4.get(i));
					case 5:
						choice2.setChoice5(liste4.get(i));
					}
					liste4.remove(i);
				}
				choiceService.save(choice2);
			}
		}
	}
	
	public void createFakeSpecialization(){
		createAndSaveFakeIc("AISE",
				"Acoustique Industrielle, Sons et Environnement");
		createAndSaveFakeIc("M3S",
				"Modélisation Mécanique des Matériaux et des Structures");
		createAndSaveFakeIc("FETES",
				"Fluides : Energie, Transport, Environnement, Santé");
		createAndSaveFakeIc("GM", "Génie Mer");
		createAndSaveFakeIc("CMV", "Chimie : Molécules et Vivant");
		createAndSaveFakeIc("PM", "Procédés et Molécules");
		createAndSaveFakeIc("MSA", "Micro Systèmes Avancés");
		createAndSaveFakeIc("OP", "Optique et Photonique");
		createAndSaveFakeIc("CSA", "Conception des Systèmes Automatisés");
		createAndSaveFakeIc("I2T",
				"Ingénierie des Images et Télécommunications");
		createAndSaveFakeIc("MAF", "Mathématiques Appliquées Finance");
		createAndSaveFakeIc("S2I", "Systèmes d'Information et Informatique");

		createAndSaveFakeJs("R&D", "Recherche et développement");
		createAndSaveFakeJs("CBE", "Conception, Bureau d'Etudes");
		createAndSaveFakeJs("PRL", "Production, Logistique");
		createAndSaveFakeJs("AC", "Audit et Conseil");
		createAndSaveFakeJs("MEE", "Management d'Entreprise, Entreprenariat");
	}
	
	public void createAndSaveFakeIc(String abbreviation, String name) {
		ImprovementCourse improvementCourse = new ImprovementCourse();
		improvementCourse.setAbbreviation(abbreviation);
		improvementCourse.setName(name);
		improvementCourse.setResponsibleLogin("respo_" + abbreviation);
		specializationService.save(improvementCourse);
	}

	public void createAndSaveFakeJs(String abbreviation, String name) {
		JobSector jobSector = new JobSector();
		jobSector.setAbbreviation(abbreviation);
		jobSector.setName(name);
		jobSector.setResponsibleLogin("respo_" + abbreviation);
		specializationService.save(jobSector);
	}
	
	public void createFakeAdmin(){
		superUserService.save("admin");
	}
	
	public void fakeValidation(){
		for (String login : validationService.findStudentValidatedIcLogins()){
			int willSwitch = (int) (Math.random() * 10);
			if (willSwitch>8){
				validationService.updateIcValidation(login, false);
			}
		}
		for (String login : validationService.findStudentValidatedJsLogins()){
			int willSwitch = (int) (Math.random() * 10);
			if (willSwitch>8){
				validationService.updateJsValidation(login, false);
			}
		}
	}
	
	public void deleteAll(){
		for (ImprovementCourse ic : specializationService
				.findImprovementCourses()) {
			specializationService.delete(ic);
		}
		for (ImprovementCourseChoice icc : choiceService
				.findImprovementCourseChoices()) {
			choiceService.delete(icc);
		}
		for (JobSector js : specializationService.findJobSectors()) {
			specializationService.delete(js);
		}
		for (JobSectorChoice jsc : choiceService.findJobSectorChoices()) {
			choiceService.delete(jsc);
		}
		exclusionService.removeAll();
		//validationService.deleteAllStudents();
	}
	

}
