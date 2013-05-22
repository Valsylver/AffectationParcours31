package fr.affectation.service.student;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.domain.specialization.ComparatorListIc;
import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.SimpleSpecializationWithList;
import fr.affectation.domain.specialization.SimpleSpecializationWithNumber;
import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.SimpleStudentWithOrigin;
import fr.affectation.domain.student.SimpleStudentWithValidation;
import fr.affectation.domain.student.Student;
import fr.affectation.domain.util.SimpleMail;
import fr.affectation.service.agap.AgapService;
import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.documents.DocumentService;
import fr.affectation.service.exclusion.ExclusionService;
import fr.affectation.service.mail.MailService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.validation.ValidationService;

@Service
public class StudentServiceImpl implements StudentService {

	@Inject
	private AgapService agapCacheService;

	@Inject
	private ValidationService validationService;

	@Inject
	private ChoiceService choiceService;

	@Inject
	private SpecializationService specializationService;

	@Inject
	private DocumentService documentService;

	@Inject
	private MailService mailService;

	@Inject
	private ExclusionService exclusionService;

	private Map<String, Integer> sizeOfCategories;

	@Override
	@SuppressWarnings("unchecked")
	public boolean populateStudentToExcludeFromFile(MultipartFile file) {
		try {
			InputStream inputStream = file.getInputStream();

			POIFSFileSystem fileSystem = null;

			fileSystem = new POIFSFileSystem(inputStream);

			HSSFWorkbook workBook = new HSSFWorkbook(fileSystem);
			HSSFSheet sheet = workBook.getSheetAt(0);
			Iterator<HSSFRow> rows = sheet.rowIterator();

			List<String> excludableStudentLogins = agapCacheService.findStudentConcernedLogins();

			while (rows.hasNext()) {
				HSSFRow row = rows.next();
				HSSFCell cell = row.getCell(0);
				if (cell != null) {
					if (!cell.toString().equals("")) {
						String login = cell.toString();
						if ((!login.equals("")) && (excludableStudentLogins.contains(login))) {
							exclusionService.save(login);
						}
					}
				}
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public List<SimpleStudent> findAllStudentsConcerned() {
		List<SimpleStudent> allStudents = agapCacheService.findStudentsConcerned();
		List<String> excludedStudents = exclusionService.findStudentToExcludeLogins();
		List<SimpleStudent> allStudentsConcerned = new ArrayList<SimpleStudent>();
		for (SimpleStudent student : allStudents) {
			if (!excludedStudents.contains(student.getLogin())) {
				allStudentsConcerned.add(student);
			}
		}
		return allStudentsConcerned;
	}

	@Override
	public List<String> findAllStudentsConcernedLogin() {
		List<String> studentConcernedLogins = agapCacheService.findStudentConcernedLogins();
		studentConcernedLogins.removeAll(exclusionService.findStudentToExcludeLogins());
		return studentConcernedLogins;
	}

	@Override
	public boolean isStudentConcerned(String login) {
		return agapCacheService.isStudent(login) && (!exclusionService.isExcluded(login));
	}

	@Override
	public List<SimpleStudentWithValidation> findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization) {
		List<String> allLogins = choiceService.findLoginsByOrderChoiceAndSpecialization(orderChoice, specialization);
		List<SimpleStudentWithValidation> allSimpleStudents = new ArrayList<SimpleStudentWithValidation>();
		List<String> studentsToExcludeLogins = exclusionService.findStudentToExcludeLogins();
		Map<String, String> nameLoginMap = agapCacheService.findNamesForAListOfLogins(allLogins);
		for (String login : allLogins) {
			if (!studentsToExcludeLogins.contains(login)) {
				boolean isValidated = specialization instanceof JobSector ? validationService.isValidatedJs(login) : validationService.isValidatedIc(login);
				allSimpleStudents.add(new SimpleStudentWithValidation(login, nameLoginMap.get(login), isValidated));
			}
		}
		Collections.sort(allSimpleStudents);
		return allSimpleStudents;
	}

	@Override
	public List<List<SimpleStudentWithValidation>> findSimpleStudentsWithValidationForAllIcByOrder(int order) {
		List<ImprovementCourse> allIc = specializationService.findImprovementCourses();
		List<List<SimpleStudentWithValidation>> allStudentsForAllIc = new ArrayList<List<SimpleStudentWithValidation>>();
		for (ImprovementCourse improvementCourse : allIc) {
			allStudentsForAllIc.add(findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(order, improvementCourse));
		}
		return allStudentsForAllIc;
	}

	@Override
	public List<SimpleStudent> findSimpleStudentsByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization) {
		List<String> allLogins = choiceService.findLoginsByOrderChoiceAndSpecialization(orderChoice, specialization);
		List<SimpleStudent> allSimpleStudents = new ArrayList<SimpleStudent>();
		List<String> studentsToExcludeLogins = exclusionService.findStudentToExcludeLogins();
		Map<String, String> nameLoginMap = agapCacheService.findNamesForAListOfLogins(allLogins);
		for (String login : allLogins) {
			if (!studentsToExcludeLogins.contains(login)) {
				allSimpleStudents.add(new SimpleStudent(login, nameLoginMap.get(login)));
			}
		}
		Collections.sort(allSimpleStudents);
		return allSimpleStudents;
	}
	
	@Override
	public List<String> findLoginsByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization) {
		List<String> allLogins = choiceService.findLoginsByOrderChoiceAndSpecialization(orderChoice, specialization);
		Collections.sort(allLogins);
		return allLogins;
	}
	
	@Override
	public List<SimpleSpecializationWithNumber> findSimpleIcStats(int choice) {
		List<String> excludedLogins = exclusionService.findStudentToExcludeLogins();
 		List<ImprovementCourse> allIc = specializationService.findImprovementCourses();
		List<SimpleSpecializationWithNumber> specializations = new ArrayList<SimpleSpecializationWithNumber>();
		int total = 0;
		for (ImprovementCourse improvementCourse : allIc){
			List<String> logins = findLoginsByOrderChoiceAndSpecialization(choice, improvementCourse);
			logins.removeAll(excludedLogins);
			int numberOfStudents = logins.size();
			total += numberOfStudents;
			String abbreviation = improvementCourse.getAbbreviation();
			SimpleSpecializationWithNumber specialization = new SimpleSpecializationWithNumber(abbreviation, improvementCourse.getName(), numberOfStudents);
			specializations.add(specialization);
		}
		int diff = findAllStudentsConcernedLogin().size() - total;
		if (diff != 0){
			specializations.add(new SimpleSpecializationWithNumber(" Pas de choix", " Pas de choix", diff));
		}
		Collections.sort(specializations); 
		return specializations;
	}
	
	@Override
	public List<SimpleSpecializationWithNumber> findSimpleJsStats(int choice) {
		List<String> excludedLogins = exclusionService.findStudentToExcludeLogins();
 		List<JobSector> allJs = specializationService.findJobSectors();
		List<SimpleSpecializationWithNumber> specializations = new ArrayList<SimpleSpecializationWithNumber>();
		int total = 0;
		for (JobSector jobSector : allJs){
			List<String> logins = findLoginsByOrderChoiceAndSpecialization(choice, jobSector);
			logins.removeAll(excludedLogins);
			int numberOfStudents = logins.size();
			total += numberOfStudents;
			String abbreviation = jobSector.getAbbreviation();
			SimpleSpecializationWithNumber specialization = new SimpleSpecializationWithNumber(abbreviation, jobSector.getName(), numberOfStudents);
			specializations.add(specialization);
		}
		int diff = findAllStudentsConcernedLogin().size() - total;
		if (diff != 0){
			specializations.add(new SimpleSpecializationWithNumber(" Pas de choix", " Pas de choix", diff));
		}
		Collections.sort(specializations); 
		return specializations;
	}

	@Override
	public List<List<SimpleStudent>> findSimpleStudentsForAllIcByOrder(int order) {
		List<ImprovementCourse> allIc = specializationService.findImprovementCourses();
		List<List<SimpleStudent>> allStudentsForAllIc = new ArrayList<List<SimpleStudent>>();
		for (ImprovementCourse improvementCourse : allIc) {
			allStudentsForAllIc.add(findSimpleStudentsByOrderChoiceAndSpecialization(order, improvementCourse));
		}
		return allStudentsForAllIc;
	}

	@Override
	public List<List<SimpleStudent>> findSimpleStudentsForAllJsByOrder(int order) {
		List<JobSector> allJs = specializationService.findJobSectors();
		List<List<SimpleStudent>> allStudentsForAllJs = new ArrayList<List<SimpleStudent>>();
		for (JobSector jobSector : allJs) {
			allStudentsForAllJs.add(findSimpleStudentsByOrderChoiceAndSpecialization(order, jobSector));
		}
		return allStudentsForAllJs;
	}

	@Override
	public List<List<SimpleStudentWithValidation>> findSimpleStudentsWithValidationForAllJsByOrder(int order) {
		List<JobSector> allJs = specializationService.findJobSectors();
		List<List<SimpleStudentWithValidation>> allStudentsWithValidationForAllJs = new ArrayList<List<SimpleStudentWithValidation>>();
		for (JobSector jobSector : allJs) {
			allStudentsWithValidationForAllJs.add(findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(order, jobSector));
		}
		return allStudentsWithValidationForAllJs;
	}

	@Override
	public void populateValidation() {
		validationService.removeAll();
		for (String login : agapCacheService.findStudentConcernedLogins()) {
			validationService.save(login, true, true);
		}
	}

	@Override
	public List<SimpleStudent> findStudentsForCategorySynthese(String category, String path) {
		List<SimpleStudent> students = findAllStudentsConcerned();
		List<SimpleStudent> results = new ArrayList<SimpleStudent>();
		boolean filledDoc = false;
		boolean filledChoices = false;
		int nbreAll = 0;
		int nbrePartial = 0;
		int nbreNo = 0;

		for (SimpleStudent student : students) {
			String login = student.getLogin();
				filledDoc = documentService.hasFilledLetterIc(path, login) && documentService.hasFilledLetterJs(path, login)
						&& documentService.hasFilledResume(path, login);
				List<Integer> l1 = choiceService.findElementNotFilledImprovementCourse(login);
				List<Integer> l2 = choiceService.findElementNotFilledJobSector(login);
				filledChoices = (l1.size() == 0) && (l2.size() == 0);
	
				if ((filledDoc) && (filledChoices)) {
					if (category.equals("all")) {
						results.add(student);
					}
					nbreAll += 1;
				} else {
					if ((!filledDoc) && (!filledChoices)) {
						if (category.equals("no")) {
							results.add(student);
						}
						nbreNo += 1;
					} else {
						if (category.equals("partial")) {
							results.add(student);
						}
						nbrePartial += 1;
					}
				}
		}
		Collections.sort(results);

		sizeOfCategories = new HashMap<String, Integer>();
		sizeOfCategories.put("total", nbreAll);
		sizeOfCategories.put("partial", nbrePartial);
		sizeOfCategories.put("empty", nbreNo);
		return results;
	}

	@Override
	public Map<String, Integer> findSizeOfCategories(String path) {
		List<String> logins = findAllStudentsConcernedLogin();
		Map<String, Integer> results = new HashMap<String, Integer>();
		boolean filledDoc = false;
		boolean filledChoices = false;
		int nbreAll = 0;
		int nbrePartial = 0;
		int nbreNo = 0;

		for (String login : logins) {
			filledDoc = documentService.hasFilledLetterIc(path, login) && documentService.hasFilledLetterJs(path, login)
					&& documentService.hasFilledResume(path, login);
			filledChoices = (choiceService.findElementNotFilledImprovementCourse(login).size() == 0)
					&& (choiceService.findElementNotFilledJobSector(login).size() == 0);

			if ((filledDoc) && (filledChoices)) {
				nbreAll += 1;
			} else {
				if ((!filledDoc) && (!filledChoices)) {
					nbreNo += 1;
				} else {
					nbrePartial += 1;
				}
			}
		}
		results.put("total", nbreAll);
		results.put("partial", nbrePartial);
		results.put("empty", nbreNo);
		return results;
	}

	@Override
	public List<String> findStudentsToExcludeName() {
		List<String> studentsLogin = exclusionService.findStudentToExcludeLogins();
		Map<String, String> nameLoginMap = agapCacheService.findNamesForAListOfLogins(studentsLogin);
		List<String> studentsName = new ArrayList<String>();
		for (String login : studentsLogin) {
			studentsName.add(nameLoginMap.get(login));
		}
		Collections.sort(studentsName);
		return studentsName;
	}

	@Override
	public Student retrieveStudentByLogin(String login, String path) {
		if (isStudentConcerned(login)) {
			Student student = new Student();
			student.setLogin(login);
			student.setOrigin(agapCacheService.findOriginFromLogin(login));
			student.setName(agapCacheService.findNameFromLogin(login));
			student.setContentious(agapCacheService.findContentious(login));
			student.setGpaMeans(agapCacheService.findGpaMeans(login));
			student.setResults(agapCacheService.findUeResults(login));
			student.setHasFilledResume(documentService.hasFilledResume(path, login));
			student.setHasFilledLetterIc(documentService.hasFilledLetterIc(path, login));
			student.setHasFilledLetterJs(documentService.hasFilledLetterJs(path, login));
			ImprovementCourseChoice icc = choiceService.findIcChoicesByLogin(login);
			JobSectorChoice jsc = choiceService.findJsChoicesByLogin(login);
			student.setIcChoices(icc == null ? new ImprovementCourseChoice() : icc);
			student.setJsChoices(jsc == null ? new JobSectorChoice() : jsc);
			return student;
		} else {
			return null;
		}
	}

	@Override
	public List<SimpleStudent> findAllStudentsToExclude() {
		List<SimpleStudent> studentsToExclude = new ArrayList<SimpleStudent>();
		List<String> studentsLogin = exclusionService.findStudentToExcludeLogins();
		List<String> currentPromotion = agapCacheService.findCurrentPromotionStudentLogins();
		Map<String, String> nameForLogin = agapCacheService.findNamesForAListOfLogins(studentsLogin);
		for (String login : studentsLogin) {
			String origin;
			if (currentPromotion.contains(login)) {
				origin = "promo";
			} else {
				origin = "cesure";
			}
			SimpleStudent student = new SimpleStudentWithOrigin(login, nameForLogin.get(login), origin);
			studentsToExclude.add(student);
		}
		Collections.sort(studentsToExclude);
		return studentsToExclude;
	}

	@Override
	public Map<String, Integer> getSizeOfCategories(String path) {
		return sizeOfCategories;
	}

	@Override
	public void sendSimpleMail(SimpleMail mail, String path) {
		List<String> addressees = new ArrayList<String>();
		if (mail.getAddressee().charAt(0) == 'E') {
			List<SimpleStudent> partial = findStudentsForCategorySynthese("partial", path);
			List<SimpleStudent> no = findStudentsForCategorySynthese("no", path);
			for (SimpleStudent student : partial) {
				addressees.add(student.getLogin());
			}
			for (SimpleStudent student : no) {
				addressees.add(student.getLogin());
			}
		} else {
			addressees = findAllStudentsConcernedLogin();
		}
		mailService.sendSimpleMail(mail, addressees);
	}

	@Override
	public List<SimpleStudent> findCurrentPromotionStudentsConcerned() {
		List<SimpleStudent> studentsAgap = agapCacheService.findCurrentPromotionSimpleStudents();
		List<SimpleStudent> students = new ArrayList<SimpleStudent>();
		List<String> studentExcludedLogins = exclusionService.findStudentToExcludeLogins();
		for (SimpleStudent student : studentsAgap) {
			if (!studentExcludedLogins.contains(student.getLogin())) {
				students.add(student);
			}
		}
		Collections.sort(students);
		return students;
	}

	@Override
	public List<SimpleStudent> findCesureStudentsConcerned() {
		List<SimpleStudent> studentsAgap = agapCacheService.findCesureSimpleStudents();
		List<SimpleStudent> students = new ArrayList<SimpleStudent>();
		List<String> studentExcludedLogins = exclusionService.findStudentToExcludeLogins();
		for (SimpleStudent student : studentsAgap) {
			if (!studentExcludedLogins.contains(student.getLogin())) {
				students.add(student);
			}
		}
		Collections.sort(students);
		return students;
	}

	@Override
	public List<SimpleSpecializationWithList> findChoiceRepartitionKnowingOne(int knownChoice, int wantedChoice, Specialization specialization) {
		String abbreviationToLookFor = specialization.getAbbreviation();
		boolean isAnIc = specialization instanceof ImprovementCourse;
		Map<String, List<String>> choicesResults = choiceService.findChoiceRepartitionKnowingOne(knownChoice, wantedChoice, abbreviationToLookFor,
				isAnIc ? Specialization.IMPROVEMENT_COURSE : Specialization.JOB_SECTOR);
		List<SimpleSpecializationWithList> results = new ArrayList<SimpleSpecializationWithList>();

		List<String> allLogins = new ArrayList<String>();
		for (String abbreviation : choicesResults.keySet()) {
			allLogins.addAll(choicesResults.get(abbreviation));
		}
		Map<String, String> nameLoginMap = agapCacheService.findNamesForAListOfLogins(allLogins);

		for (String abbreviation : choicesResults.keySet()) {
			List<String> logins = choicesResults.get(abbreviation);
			List<String> names = new ArrayList<String>();
			for (String login : logins) {
				names.add(nameLoginMap.get(login));
			}
			Collections.sort(names);
			String specializationName = isAnIc ? specializationService.findNameFromIcAbbreviation(abbreviation) : specializationService
					.findNameFromJsAbbreviation(abbreviation);
			results.add(new SimpleSpecializationWithList(abbreviation, specializationName, names));
		}
		Collections.sort(results);
		return results;
	}

	@Override
	public Map<String, List<String>> findChoiceRepartitionForTheOtherType(String abbreviation, int specializationType) {
		return choiceService.findInverseRepartitionForAListOfLogin(choiceService.findLoginsByOrderChoiceAndSpecialization(1, abbreviation, specializationType),
				specializationType);
	}

	@Override
	public List<SimpleSpecializationWithList> findInverseRepartition(Specialization specialization) {
		int type = specialization instanceof JobSector ? Specialization.JOB_SECTOR : Specialization.IMPROVEMENT_COURSE;
		Map<String, List<String>> inverseRepartition = findChoiceRepartitionForTheOtherType(specialization.getAbbreviation(), type);
		List<SimpleSpecializationWithList> results = new ArrayList<SimpleSpecializationWithList>();
		List<String> studentsConcernedLogins = findAllStudentsConcernedLogin();

		List<String> allLogins = new ArrayList<String>();
		for (String abbreviation : inverseRepartition.keySet()) {
			allLogins.addAll(inverseRepartition.get(abbreviation));
		}
		Map<String, String> nameLoginMap = agapCacheService.findNamesForAListOfLogins(allLogins);
		for (String abbreviation : inverseRepartition.keySet()) {
			List<String> logins = inverseRepartition.get(abbreviation);
			List<String> names = new ArrayList<String>();
			for (String login : logins) {
				if (studentsConcernedLogins.contains(login)) {
					names.add(nameLoginMap.get(login));
				}
			}
			Collections.sort(names);
			String specializationName = type == Specialization.IMPROVEMENT_COURSE ? specializationService.findNameFromJsAbbreviation(abbreviation)
					: specializationService.findNameFromIcAbbreviation(abbreviation);
			results.add(new SimpleSpecializationWithList(abbreviation, specializationName, names));
		}
		Collections.sort(results);
		return results;
	}

	@Override
	public List<Specialization> findIcChoicesFullSpecByLogin(String login) {
		ImprovementCourseChoice icc = choiceService.findIcChoicesByLogin(login);
		List<Specialization> specs = new ArrayList<Specialization>();
		specs.add(icc.getChoice1() != null ? specializationService.getImprovementCourseByAbbreviation(icc.getChoice1()) : null);
		specs.add(icc.getChoice2() != null ? specializationService.getImprovementCourseByAbbreviation(icc.getChoice2()) : null);
		specs.add(icc.getChoice3() != null ? specializationService.getImprovementCourseByAbbreviation(icc.getChoice3()) : null);
		specs.add(icc.getChoice4() != null ? specializationService.getImprovementCourseByAbbreviation(icc.getChoice4()) : null);
		specs.add(icc.getChoice5() != null ? specializationService.getImprovementCourseByAbbreviation(icc.getChoice5()) : null);
		return specs;
	}

	@Override
	public List<Specialization> findJsChoicesFullSpecByLogin(String login) {
		JobSectorChoice jsc = choiceService.findJsChoicesByLogin(login);
		List<Specialization> specs = new ArrayList<Specialization>();
		specs.add(jsc.getChoice1() != null ? specializationService.getJobSectorByAbbreviation(jsc.getChoice1()) : null);
		specs.add(jsc.getChoice2() != null ? specializationService.getJobSectorByAbbreviation(jsc.getChoice2()) : null);
		specs.add(jsc.getChoice3() != null ? specializationService.getJobSectorByAbbreviation(jsc.getChoice3()) : null);
		specs.add(jsc.getChoice4() != null ? specializationService.getJobSectorByAbbreviation(jsc.getChoice4()) : null);
		specs.add(jsc.getChoice5() != null ? specializationService.getJobSectorByAbbreviation(jsc.getChoice5()) : null);
		return specs;
	}

	@Override
	public List<List<ImprovementCourse>> findIcAvailableAsListWithSuperIc(){
		List<ImprovementCourse> paAvailable = specializationService.findImprovementCourses();
		Map<String, List<ImprovementCourse>> paAvailableMap = new HashMap<String, List<ImprovementCourse>>();
		for (ImprovementCourse ic : paAvailable){
			String superIc = ic.getSuperIc();
			if (!paAvailableMap.containsKey(superIc)){
				paAvailableMap.put(superIc, new ArrayList<ImprovementCourse>());
			}
			paAvailableMap.get(superIc).add(ic);
		}
		List<List<ImprovementCourse>> paAvailableList = new ArrayList<List<ImprovementCourse>>();
		for (String key : paAvailableMap.keySet()){
			Collections.sort(paAvailableMap.get(key));
			paAvailableList.add(paAvailableMap.get(key));
		}
		Collections.sort(paAvailableList, new ComparatorListIc());
		return paAvailableList;
	}
}
