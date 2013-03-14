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
import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.SimpleSpecializationWithList;
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
	private AgapService agapService;

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

			while (rows.hasNext()) {
				HSSFRow row = rows.next();
				HSSFCell cell = row.getCell(0);
				if (cell != null) {
					if (!cell.toString().equals("")) {
						String login = cell.toString();
						if ((!login.equals("")) && (agapService.isAnExcludableStudent(login))) {
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
		List<SimpleStudent> allStudents = agapService.findStudentsConcerned();
		List<SimpleStudent> allStudentsConcerned = new ArrayList<SimpleStudent>();
		List<String> allStudentsExcludedLogin = exclusionService.findStudentToExcludeLogins();
		for (SimpleStudent student : allStudents) {
			if (!allStudentsExcludedLogin.contains(student.getLogin())) {
				allStudentsConcerned.add(student);
			}
		}
		Collections.sort(allStudentsConcerned);
		return allStudentsConcerned;
	}

	@Override
	public List<String> findAllStudentsConcernedLogin() {
		List<String> studentsConcernedLoginsAgap = agapService.findStudentConcernedLogins();
		List<String> studentsExcludedLogins = exclusionService.findStudentToExcludeLogins();
		List<String> studentsConcernedLogins = new ArrayList<String>();
		for (String login : studentsConcernedLoginsAgap){
			if (!studentsExcludedLogins.contains(login)){
				studentsConcernedLogins.add(login);
			}
		} 
		return studentsConcernedLogins;
	}

	@Override
	public boolean isStudentConcerned(String login) {
		return findAllStudentsConcernedLogin().contains(login);
	}

	@Override
	public List<SimpleStudentWithValidation> findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization) {
		List<String> allLogins = choiceService.findLoginsByOrderChoiceAndSpecialization(orderChoice, specialization);
		List<SimpleStudentWithValidation> allSimpleStudents = new ArrayList<SimpleStudentWithValidation>();
		List<String> studentsToExcludeLogins = exclusionService.findStudentToExcludeLogins();
		for (String login : allLogins) {
			if (!studentsToExcludeLogins.contains(login)) {
				boolean isValidated = specialization instanceof JobSector ? validationService.isValidatedJs(login) : validationService.isValidatedIc(login);
				allSimpleStudents.add(new SimpleStudentWithValidation(login, agapService.findNameFromLogin(login), isValidated));
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
		for (String login : allLogins) {
			if (!studentsToExcludeLogins.contains(login)) {
				allSimpleStudents.add(new SimpleStudent(login, agapService.findNameFromLogin(login)));
			}
		}
		Collections.sort(allSimpleStudents);
		return allSimpleStudents;
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
		for (String login : findAllStudentsConcernedLogin()) {
			validationService.save(login, true, true);
		}
	}

	@Override
	public void updateValidationFromList(List<String> students, List<Boolean> validated_, int specializationType) {
		for (int i = 0; i < students.size(); i++) {
			String login = students.get(i);
			Boolean validated;
			if (i < validated_.size()) {
				validated = validated_.get(i);
			} else {
				validated = null;
			}
			if (validated == null) {
				validated = false;
			}
			if (specializationType == Specialization.JOB_SECTOR) {
				validationService.updateJsValidation(login, validated);
			} else {
				validationService.updateIcValidation(login, validated);
			}
		}
	}

	@Override
	public List<Map<String, Object>> findStudentsForCategorySynthese(String category, String path) {
		List<String> logins = findAllStudentsConcernedLogin();
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		boolean filledDoc = false;
		boolean filledChoices = false;
		int nbreAll = 0;
		int nbrePartial = 0;
		int nbreNo = 0;

		for (String login : logins) {
			filledDoc = documentService.hasFilledLetterIc(path, login) && documentService.hasFilledLetterJs(path, login)
					&& documentService.hasFilledResume(path, login);
			List<Integer> l1 = choiceService.findElementNotFilledImprovementCourse(login);
			List<Integer> l2 = choiceService.findElementNotFilledJobSector(login);
			filledChoices = (l1.size() == 0) && (l2.size() == 0);

			if ((filledDoc) && (filledChoices)) {
				if (category.equals("all")) {
					map = new HashMap<String, Object>();
					map.put("name", agapService.findNameFromLogin(login));
					map.put("login", login);
					results.add(map);
				}
				nbreAll += 1;
			} else {
				if ((!filledDoc) && (!filledChoices)) {
					if (category.equals("no")) {
						map = new HashMap<String, Object>();
						map.put("name", agapService.findNameFromLogin(login));
						map.put("login", login);
						results.add(map);
					}
					nbreNo += 1;
				} else {
					if (category.equals("partial")) {
						map = new HashMap<String, Object>();
						map.put("name", agapService.findNameFromLogin(login));
						map.put("login", login);
						results.add(map);
					}
					nbrePartial += 1;
				}
			}

		}

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
		List<String> studentsName = new ArrayList<String>();
		for (String login : studentsLogin) {
			studentsName.add(agapService.findNameFromLogin(login));
		}
		Collections.sort(studentsName);
		return studentsName;
	}

	@Override
	public Student retrieveStudentByLogin(String login, String path) {
		if (isStudentConcerned(login)) {
			Student student = new Student();
			student.setLogin(login);
			student.setName(agapService.findNameFromLogin(login));
			student.setContentious(agapService.findContentious(login));
			student.setGpaMeans(agapService.findGpaMeans(login));
			student.setResults(agapService.findUeResults(login));
			student.setHasFilledResume(documentService.hasFilledResume(path, login));
			student.setHasFilledLetterIc(documentService.hasFilledLetterIc(path, login));
			student.setHasFilledLetterJs(documentService.hasFilledLetterJs(path, login));
			student.setIcChoices(choiceService.findIcChoicesByLogin(login) == null ? new ImprovementCourseChoice() : choiceService.findIcChoicesByLogin(login));
			student.setJsChoices(choiceService.findJsChoicesByLogin(login) == null ? new JobSectorChoice() : choiceService.findJsChoicesByLogin(login));
			return student;
		} else {
			return null;
		}
	}

	@Override
	public List<SimpleStudent> findAllStudentsToExclude() {
		List<SimpleStudent> studentsToExclude = new ArrayList<SimpleStudent>();
		List<String> studentsLogin = exclusionService.findStudentToExcludeLogins();
		for (String login : studentsLogin) {
			String origin;
			if (agapService.findCurrentPromotionStudentLogins().contains(login)) {
				origin = "promo";
			} else {
				origin = "cesure";
			}
			SimpleStudent student = new SimpleStudentWithOrigin(login, agapService.findNameFromLogin(login), origin);
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
			List<Map<String, Object>> partialMap = findStudentsForCategorySynthese("partial", path);
			List<Map<String, Object>> noMap = findStudentsForCategorySynthese("no", path);
			for (Map<String, Object> map : partialMap) {
				addressees.add((String) map.get("login"));
			}
			for (Map<String, Object> map : noMap) {
				addressees.add((String) map.get("login"));
			}
		} else {
			addressees = findAllStudentsConcernedLogin();
		}
		mailService.sendSimpleMail(mail, addressees);
	}

	@Override
	public List<SimpleStudent> findCurrentPromotionStudentsConcerned() {
		List<String> logins = agapService.findCurrentPromotionStudentLogins();
		List<SimpleStudent> currentPromotion = new ArrayList<SimpleStudent>();
		for (String login : logins) {
			if (!exclusionService.isExcluded(login)) {
				currentPromotion.add(new SimpleStudent(login, agapService.findNameFromLogin(login)));
			}
		}
		Collections.sort(currentPromotion);
		return currentPromotion;
	}

	@Override
	public List<SimpleStudent> findCesureStudentsConcerned() {
		List<String> logins = agapService.findCesureStudentLogins();
		List<SimpleStudent> cesure = new ArrayList<SimpleStudent>();
		for (String login : logins) {
			if (!exclusionService.isExcluded(login)) {
				cesure.add(new SimpleStudent(login, agapService.findNameFromLogin(login)));
			}
		}
		Collections.sort(cesure);
		return cesure;
	}

	@Override
	public List<SimpleSpecializationWithList> findChoiceRepartitionKnowingOne(int knownChoice, int wantedChoice, Specialization specialization) {
		String abbreviationToLookFor = specialization.getAbbreviation();
		boolean isAnIc = specialization instanceof ImprovementCourse;
		Map<String, List<String>> choicesResults = choiceService.findChoiceRepartitionKnowingOne(knownChoice, wantedChoice, abbreviationToLookFor,
				isAnIc ? Specialization.IMPROVEMENT_COURSE : Specialization.JOB_SECTOR);
		List<SimpleSpecializationWithList> results = new ArrayList<SimpleSpecializationWithList>();
		for (String abbreviation : choicesResults.keySet()) {
			List<String> logins = choicesResults.get(abbreviation);
			List<String> names = new ArrayList<String>();
			for (String login : logins) {
				names.add(agapService.findNameFromLogin(login));
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
		for (String abbreviation : inverseRepartition.keySet()) {
			List<String> logins = inverseRepartition.get(abbreviation);
			List<String> names = new ArrayList<String>();
			for (String login : logins) {
				if (studentsConcernedLogins.contains(login)) {
					names.add(agapService.findNameFromLogin(login));
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
}
