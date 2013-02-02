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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.domain.comparator.ComparatorName;
import fr.affectation.domain.comparator.ComparatorSimpleStudent;
import fr.affectation.domain.comparator.ComparatorSimpleStudentWithValidation;
import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.SimpleStudentWithValidation;
import fr.affectation.domain.student.Student;
import fr.affectation.domain.student.StudentToExclude;
import fr.affectation.domain.student.StudentValidation;
import fr.affectation.service.agap.AgapService;
import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.documents.DocumentService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.validation.ValidationService;

@Service
public class StudentServiceImpl implements StudentService {

	@Inject
	private SessionFactory sessionFactory;

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

	@Override
	@Transactional
	public void saveStudentToExclude(StudentToExclude student) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(student);
	}

	@Override
	@Transactional
	public void deleteAllStudentToExclude() {
		Session session = sessionFactory.getCurrentSession();
		for (StudentToExclude student : findAllStudentToExclude()) {
			session.delete(student);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<StudentToExclude> findAllStudentToExclude() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from StudentToExclude");
		List<StudentToExclude> allStudent = (List<StudentToExclude>) query.list();
		return allStudent;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isExcluded(SimpleStudent student) {
		return findByLogin(student.getLogin()) != null;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isExcluded(String login) {
		return findByLogin(login) != null;
	}

	@Override
	@Transactional(readOnly = true)
	public StudentToExclude findByLogin(String login) {
		String queryStudent = "from StudentToExclude where login=:login";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(queryStudent);
		query.setString("login", login);
		List<StudentToExclude> allStudents = query.list();
		if (allStudents.size() == 1) {
			return allStudents.get(0);
		} else {
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findAllStudentToExcludeLogin() {
		List<String> studentsToExcludeLogin = new ArrayList<String>();
		for (StudentToExclude student : findAllStudentToExclude()) {
			studentsToExcludeLogin.add(student.getLogin());
		}
		return studentsToExcludeLogin;
	}

	@Override
	@Transactional
	public boolean populateStudentToExcludeFromFile(MultipartFile file) {
		try {
			InputStream inputStream = file.getInputStream();

			POIFSFileSystem fileSystem = null;

			fileSystem = new POIFSFileSystem(inputStream);

			HSSFWorkbook workBook = new HSSFWorkbook(fileSystem);
			HSSFSheet sheet = workBook.getSheetAt(0);
			@SuppressWarnings("unchecked")
			Iterator<HSSFRow> rows = sheet.rowIterator();

			while (rows.hasNext()) {
				HSSFRow row = rows.next();
				HSSFCell cell = row.getCell(0);
				if (cell != null) {
					if (!cell.toString().equals("")) {
						String login = cell.toString();
						if ((!login.equals("")) && (agapService.checkStudent(login))) {
							StudentToExclude student = new StudentToExclude(login);
							saveStudentToExclude(student);
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
	@Transactional(readOnly = true)
	public List<SimpleStudent> findAllStudentsConcerned() {
		List<SimpleStudent> allStudents = agapService.findAllStudentsConcerned();
		List<SimpleStudent> allStudentsConcerned = new ArrayList<SimpleStudent>();
		List<String> allStudentsExcludedLogin = findAllStudentToExcludeLogin();
		for (SimpleStudent student : allStudents) {
			if (!allStudentsExcludedLogin.contains(student.getLogin())) {
				allStudentsConcerned.add(student);
			}
		}
		Collections.sort(allStudentsConcerned, new ComparatorSimpleStudent());
		return allStudentsConcerned;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findAllStudentsConcernedLogin() {
		List<SimpleStudent> allStudentsConcerned = findAllStudentsConcerned();
		List<String> allStudentsConcernedLogin = new ArrayList<String>();
		for (SimpleStudent student : allStudentsConcerned) {
			allStudentsConcernedLogin.add(student.getLogin());
		}
		return allStudentsConcernedLogin;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isStudentConcerned(String login) {
		return findAllStudentsConcernedLogin().contains(login);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SimpleStudent> findAllStudentsToExclude() {
		List<SimpleStudent> studentsToExclude = new ArrayList<SimpleStudent>();
		List<String> studentsLogin = findAllStudentToExcludeLogin();
		for (String login : studentsLogin) {
			SimpleStudent student = new SimpleStudent(login, agapService.findNameFromLogin(login));
			studentsToExclude.add(student);
		}
		Collections.sort(studentsToExclude, new ComparatorSimpleStudent());
		return studentsToExclude;
	}

	@Override
	@Transactional(readOnly = true)
	public int findNecessarySizeForStudentExclusion() {
		return findAllStudentsConcerned().size() + findAllStudentsToExclude().size();
	}

	@Override
	@Transactional
	public void removeStudentByLogin(String login) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("delete StudentToExclude where login=:login");
		query.setString("login", login);
		query.executeUpdate();
	}

	@Override
	@Transactional(readOnly = true)
	public List<SimpleStudentWithValidation> findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization) {
		List<String> allLogins = choiceService.getLoginsByOrderChoiceAndSpecialization(orderChoice, specialization);
		List<SimpleStudentWithValidation> allSimpleStudents = new ArrayList<SimpleStudentWithValidation>();
		List<String> studentsToExcludeLogins = findAllStudentToExcludeLogin();
		for (String login : allLogins) {
			if (!studentsToExcludeLogins.contains(login)) {
				boolean isValidated = specialization.getType().equals("JobSector") ? validationService.isValidatedJs(login) : validationService
						.isValidatedIc(login);
				allSimpleStudents.add(new SimpleStudentWithValidation(login, agapService.findNameFromLogin(login), isValidated));
			}
		}
		Collections.sort(allSimpleStudents, new ComparatorSimpleStudentWithValidation());
		return allSimpleStudents;
	}

	@Override
	@Transactional(readOnly = true)
	public List<List<SimpleStudentWithValidation>> findSimpleStudentsWithValidationForAllIcByOrder(int order) {
		List<ImprovementCourse> allIc = specializationService.findAllImprovementCourse();
		List<List<SimpleStudentWithValidation>> allStudentsForAllIc = new ArrayList<List<SimpleStudentWithValidation>>();
		for (ImprovementCourse improvementCourse : allIc) {
			allStudentsForAllIc.add(findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(order, improvementCourse));
		}
		return allStudentsForAllIc;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SimpleStudent> findSimpleStudentsByOrderChoiceAndSpecialization(int orderChoice, Specialization specialization) {
		List<String> allLogins = choiceService.getLoginsByOrderChoiceAndSpecialization(orderChoice, specialization);
		List<SimpleStudent> allSimpleStudents = new ArrayList<SimpleStudent>();
		List<String> studentsToExcludeLogins = findAllStudentToExcludeLogin();
		for (String login : allLogins) {
			if (!studentsToExcludeLogins.contains(login)) {
				allSimpleStudents.add(new SimpleStudent(login, agapService.findNameFromLogin(login)));
			}
		}
		Collections.sort(allSimpleStudents, new ComparatorSimpleStudent());
		return allSimpleStudents;
	}

	@Override
	@Transactional(readOnly = true)
	public List<List<SimpleStudent>> findSimpleStudentsForAllIcByOrder(int order) {
		List<ImprovementCourse> allIc = specializationService.findAllImprovementCourse();
		List<List<SimpleStudent>> allStudentsForAllIc = new ArrayList<List<SimpleStudent>>();
		for (ImprovementCourse improvementCourse : allIc) {
			allStudentsForAllIc.add(findSimpleStudentsByOrderChoiceAndSpecialization(order, improvementCourse));
		}
		return allStudentsForAllIc;
	}

	@Override
	@Transactional(readOnly = true)
	public List<List<SimpleStudent>> findSimpleStudentsForAllJsByOrder(int order) {
		List<JobSector> allJs = specializationService.findAllJobSector();
		List<List<SimpleStudent>> allStudentsForAllJs = new ArrayList<List<SimpleStudent>>();
		for (JobSector jobSector : allJs) {
			allStudentsForAllJs.add(findSimpleStudentsByOrderChoiceAndSpecialization(order, jobSector));
		}
		return allStudentsForAllJs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<List<SimpleStudentWithValidation>> findSimpleStudentsWithValidationForAllJsByOrder(int order) {
		List<JobSector> allJs = specializationService.findAllJobSector();
		List<List<SimpleStudentWithValidation>> allStudentsWithValidationForAllJs = new ArrayList<List<SimpleStudentWithValidation>>();
		for (JobSector jobSector : allJs) {
			allStudentsWithValidationForAllJs.add(findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(order, jobSector));
		}
		return allStudentsWithValidationForAllJs;
	}

	@Override
	@Transactional
	public void populateValidation() {
		validationService.deleteAllStudents();
		List<String> allLoginsConcerned = findAllStudentsConcernedLogin();
		for (String login : allLoginsConcerned) {
			validationService.saveStudentValidation(new StudentValidation(login, true, true));
		}
	}

	@Override
	public void updateValidation(List<String> students, List<Boolean> validated_, String type) {
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
			if (type.equals("JobSector")) {
				validationService.updateJsValidation(login, validated);
			} else {
				validationService.updateIcValidation(login, validated);
			}
		}
	}

	@Override
	// TODO with SimpleStudent instead of map
	public List<Map<String, Object>> findStudentsForCategorySynthese(String category, String path) {
		List<String> logins = findAllStudentsConcernedLogin();
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		boolean filledDoc = false;
		boolean filledChoices = false;

		for (String login : logins) {
			filledDoc = documentService.hasFilledLetterIc(path, login) && documentService.hasFilledLetterJs(path, login)
					&& documentService.hasFilledResume(path, login);
			filledChoices = (choiceService.getElementNotFilledImprovementCourse(login).size() == 0)
					&& (choiceService.getElementNotFilledJobSector(login).size() == 0);

			if ((filledDoc) && (filledChoices)) {
				if (category.equals("all")) {
					map = new HashMap<String, Object>();
					map.put("name", agapService.findNameFromLogin(login));
					map.put("login", login);
					results.add(map);
				}
			} else {
				if ((!filledDoc) && (!filledChoices)) {
					if (category.equals("no")) {
						map = new HashMap<String, Object>();
						map.put("name", agapService.findNameFromLogin(login));
						map.put("login", login);
						results.add(map);
					}
				} else {
					if (category.equals("partial")) {
						map = new HashMap<String, Object>();
						map.put("name", agapService.findNameFromLogin(login));
						map.put("login", login);
						results.add(map);
					}
				}
			}

		}
		return results;
	}

	@Override
	public List<Integer> findSizeOfCategories(String path) {
		List<String> logins = findAllStudentsConcernedLogin();
		List<Integer> results = new ArrayList<Integer>();
		boolean filledDoc = false;
		boolean filledChoices = false;
		int nbreAll = 0;
		int nbrePartial = 0;
		int nbreNo = 0;

		for (String login : logins) {
			filledDoc = documentService.hasFilledLetterIc(path, login) && documentService.hasFilledLetterJs(path, login)
					&& documentService.hasFilledResume(path, login);
			filledChoices = (choiceService.getElementNotFilledImprovementCourse(login).size() == 0)
					&& (choiceService.getElementNotFilledJobSector(login).size() == 0);

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
		results.add(nbreAll);
		results.add(nbrePartial);
		results.add(nbreNo);
		return results;
	}

	@Override
	public List<String> findStudentsToExcludeName() {
		List<String> studentsLogin = findAllStudentToExcludeLogin();
		List<String> studentsName = new ArrayList<String>();
		for (String login : studentsLogin) {
			studentsName.add(agapService.findNameFromLogin(login));
		}
		Collections.sort(studentsName, new ComparatorName());
		return studentsName;
	}

	@Override
	public Student retrieveStudentByLogin(String login, String path) {
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
	}

}
