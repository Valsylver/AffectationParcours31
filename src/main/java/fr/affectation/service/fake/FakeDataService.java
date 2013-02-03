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
import fr.affectation.domain.superuser.Admin;
import fr.affectation.service.agap.AgapService;
import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.exclusion.ExclusionService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.student.StudentService;
import fr.affectation.service.superuser.SuperUserService;
import fr.affectation.service.validation.ValidationService;

@Service
public class FakeDataService {
	
	@Inject
	private ChoiceService choiceService;
	
	@Inject
	private AgapService agapService;
	
	@Inject
	private SuperUserService superUserService;
	
	@Inject
	private StudentService studentService;
	
	@Inject
	private SpecializationService specializationService;
	
	@Inject
	private ValidationService validationService;
	
	@Inject
	private ExclusionService exclusionService;
	
	public void createFakeChoices(){
		List<String> liste = new ArrayList<String>();
		liste.add("AISE");
		liste.add("M3S");
		liste.add("FETES");
		liste.add("GM");
		liste.add("CMV");
		liste.add("PM");
		liste.add("MSA");
		liste.add("OP");
		liste.add("CSA");
		liste.add("I2T");
		liste.add("MAF");
		liste.add("S2I");

		List<String> liste2 = new ArrayList<String>();
		liste2.add("R&D");
		liste2.add("PRL");
		liste2.add("MEE");
		liste2.add("AC");
		liste2.add("CBE");

		int i = 0;
		List<String> liste3 = new ArrayList<String>();
		List<String> liste4 = new ArrayList<String>();

		for (String login : agapService.getAllStudentConcernedLogin()) {
			int willSubmit = (int) (Math.random() * 10);
			if (willSubmit < 9){
				liste3.clear();
				for (String s : liste) {
					liste3.add(s);
				}
				Choice choice = new ImprovementCourseChoice();
				choice.setLogin(login);
				for (int iterationPa = 1; iterationPa < 6; iterationPa++) {
					i = (int) (Math.random() * (12 - iterationPa + 1));
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
					f = Math.random() * (5 - iterationFm + 1);
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
		Admin admin = new Admin();
		admin.setLogin("admin");
		superUserService.saveAdmin(admin);
	}
	
	public String getRandomUeCode() {
		List<String> liste = new ArrayList<String>();
		liste.add("ALG-1");
		liste.add("DPP-1");
		liste.add("ELE-1");
		liste.add("INP-1");
		liste.add("LCI-1");
		liste.add("MAT-1");
		liste.add("MGP-1");
		liste.add("PHO-1");
		liste.add("PHQ-1");
		liste.add("SPM-1");
		liste.add("DPP-2");
		liste.add("EAO-2");
		liste.add("ECO-2");
		liste.add("LCI-2");
		liste.add("MAT-2");
		liste.add("MCO-2");
		liste.add("MGP-2");
		liste.add("PHS-2");
		liste.add("PJT-2");
		liste.add("RMS-2");
		liste.add("STG-2");
		liste.add("THS-2");
		liste.add("ASL-3");
		liste.add("DPP-3");
		liste.add("LCI-3");
		liste.add("MAT-3");
		liste.add("MNG-3");
		liste.add("PJT-3");
		liste.add("STD-3");

		int i = (int) (Math.random() * liste.size());
		return liste.get(i);
	}
	
	public void fakeValidation(){
		for (String login :validationService.findLoginsValidatedIc()){
			int willSwitch = (int) (Math.random() * 10);
			if (willSwitch>8){
				validationService.updateIcValidation(login, false);
			}
		}
		for (String login :validationService.findLoginsValidatedJs()){
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
				.findAllImprovementCourseChoices()) {
			choiceService.delete(icc);
		}
		for (JobSector js : specializationService.findJobSectors()) {
			specializationService.delete(js);
		}
		for (JobSectorChoice jsc : choiceService.findAllJobSectorChoices()) {
			choiceService.delete(jsc);
		}
		exclusionService.deleteAllStudentsToExclude();
		validationService.deleteAllStudents();
	}
	

}
