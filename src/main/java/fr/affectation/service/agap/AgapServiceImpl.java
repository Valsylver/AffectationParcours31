package fr.affectation.service.agap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import fr.affectation.domain.comparator.ComparatorName;
import fr.affectation.domain.comparator.ComparatorSimpleStudent;
import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.student.Contentious;
import fr.affectation.domain.student.Result;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.Student;
import fr.affectation.domain.student.StudentDetails;

@Service
public class AgapServiceImpl implements AgapService {

	@Inject
	private JdbcTemplate jdbcTemplate;

	@Inject
	private NamedParameterJdbcTemplate namedParameterjdbcTemplate;

	private List<String> ranking;

	private List<Float> means;

	private List<String> allUeCode;

	@Override
	public StudentDetails getStudentDetailsFromLogin(String login) {
		String queryStudents = "SELECT * FROM eleves WHERE personne_id=:login";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("login", login);
		List<Map<String, Object>> allContentiousMap = namedParameterjdbcTemplate
				.queryForList(queryStudents, namedParameter);
		List<StudentDetails> allStudentDetails = new ArrayList<StudentDetails>();
		for (Map<String, Object> map : allContentiousMap) {
			StudentDetails studentDetails = new StudentDetails();
			studentDetails.setBacCode((String) map.get("bac_code"));
			studentDetails.setBacMention((String) map.get("bac_mention"));
			studentDetails.setCivility(convertCivilityToString((Integer) map
					.get("civil")));
			studentDetails.setEntreeFil((String) map.get("entree_fil"));
			studentDetails.setEntreePrep((String) map.get("entree_prep"));
			studentDetails.setFullName((String) map.get("nom"));
			studentDetails.setLogin(login);
			allStudentDetails.add(studentDetails);
		}
		return allStudentDetails.get(0);
	}

	public String convertCivilityToString(Integer civility) {
		if (civility == 1) {
			return "M.";
		} else {
			if (civility == 3) {
				return "Mlle";
			}
		}
		return null;
	}

	public void generateRanking() {
		List<String> allLogin = getAllStudentConcernedLogin();
		List<Gpa> allGpa = new ArrayList<Gpa>();
		for (String login : allLogin) {
			List<Float> gpaAllSemester = findGpa(login);
			Float mean = (gpaAllSemester.get(0) + gpaAllSemester.get(1) + gpaAllSemester
					.get(2)) / 3;
			allGpa.add(new Gpa(login, mean));
		}
		Collections.sort(allGpa, new ComparatorGpa());
		List<String> ranking_ = new ArrayList<String>();
		List<Float> means_ = new ArrayList<Float>();
		for (Gpa gpa : allGpa) {
			ranking_.add(gpa.getLogin());
			means_.add(gpa.getMean());
		}
		ranking = ranking_;
		means = means_;
	}

	public Float computeRankingIndicator(String login) {
		Float minMean = means.get(means.size() - 1);
		Float maxMean = means.get(0);
		Float mean = means.get(ranking.indexOf(login));
		Float indicator = 10 * ((mean - minMean) / (maxMean - minMean));
		return indicator;
	}

	public String getCurrentCycle() {
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		int lastYear = currentYear - 1;
		String cycle = "" + lastYear + '-' + currentYear;
		return cycle;
	}

	public String getLastCycle() {
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		int lastYear = currentYear - 1;
		int yearBeforeLastYear = lastYear - 1;
		String cycle = "" + yearBeforeLastYear + '-' + lastYear;
		return cycle;
	}

	@Override
	public Result getResultsFromLogin(String login) {
		Result result = new Result();
		List<Float> gpaMeanList = findGpa(login);
		result.setGpaMeanS5(gpaMeanList.get(0));
		result.setGpaMeanS6(gpaMeanList.get(1));
		result.setGpaMeanS7(gpaMeanList.get(2));
		result.setRanking(ranking.indexOf(login) + 1);
		result.setRankingIndicator(computeRankingIndicator(login));
		result.setContentious(findContentious(login));
		return result;
	}

	@Override
	public Result getResultsFromLoginAndSpecialization(String login,
			Specialization specialization) {
		Result result = new Result();
		List<Float> gpaMeanList = findGpa(login);
		result.setGpaMeanS5(gpaMeanList.get(0));
		result.setGpaMeanS6(gpaMeanList.get(1));
		result.setGpaMeanS7(gpaMeanList.get(2));
		result.setRanking(ranking.indexOf(login) + 1);
		result.setRankingIndicator(computeRankingIndicator(login));
		result.setUeGrade(getUeGrade(login));
		result.setContentious(findContentious(login));
		result.generateMeanForSpecialization(specialization);
		return result;
	}

	public List<Float> findGpa(String login) {
		String requeteEleves = "SELECT * FROM gpa WHERE nom IN (SELECT nom FROM eleves WHERE personne_id=:login) and sem IN ('SEM-5', 'SEM-6', 'SEM-7')";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("login", login);
		List<Map<String, Object>> gpaMap = namedParameterjdbcTemplate
				.queryForList(requeteEleves, namedParameter);
		Map<String, List<Float>> gpaValues = new HashMap<String, List<Float>>();
		for (Map<String, Object> map : gpaMap) {
			String semester = (String) map.get("sem");
			Float gpa = (Float) map.get("gpa");
			if (!gpaValues.containsKey(semester)) {
				List<Float> gpaList = new ArrayList<Float>();
				gpaList.add(gpa);
				gpaValues.put(semester, gpaList);
			} else {
				List<Float> gpaList = gpaValues.get(semester);
				gpaList.add(gpa);
				gpaValues.put(semester, gpaList);
			}
		}
		List<Float> gpaMeanList = new ArrayList<Float>();
		for (int indexSemester = 5; indexSemester < 8; indexSemester++) {
			String key = "SEM-" + indexSemester;
			Float gpaMean = (float) 0.0;
			if (gpaValues.containsKey(key)) {
				Float mean = (float) 0.0;
				for (Float record : gpaValues.get(key)) {
					mean += record;
				}
				gpaMean = mean / gpaValues.get(key).size();
			}
			gpaMeanList.add(gpaMean);
		}
		return gpaMeanList;
	}

	public List<Contentious> findContentious(String login) {
		String queryContentious = "SELECT * FROM contentieux WHERE nom IN (SELECT nom FROM eleves WHERE personne_id=:login)";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("login", login);
		List<Map<String, Object>> allContentiousMap = namedParameterjdbcTemplate
				.queryForList(queryContentious, namedParameter);
		List<Contentious> allContentious = new ArrayList<Contentious>();
		for (Map<String, Object> map : allContentiousMap) {
			Contentious contentious = new Contentious();
			contentious.setCycle((String) map.get("cycle"));
			contentious.setSemester((String) map.get("sem"));
			contentious.setUeCode((String) map.get("code_ue"));
			allContentious.add(contentious);
		}
		return allContentious;
	}

	@Override
	public boolean isStudentConcerned(String login) {
		return getAllStudentConcernedLogin().contains(login);
	}

	@Override
	public List<String> getAllStudentConcernedLogin() {
		String cycle = getCurrentCycle();
		String requeteEleves = "SELECT DISTINCT personne_id FROM eleves WHERE nom IN "
				+ "(SELECT nom FROM notes_details WHERE cycle=:cycle AND sem='SEM-7') AND entree_fil NOT IN ('etranger')";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", cycle);
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate
				.queryForList(requeteEleves, namedParameter);
		List<String> allStudentLogin = new ArrayList<String>();
		for (Map<String, Object> map : studentMap) {
			String login = (String) map.get("personne_id");
			allStudentLogin.add(login);
		}
		return allStudentLogin;
	}

	@Override
	public Map<String, Float> getUeGrade(String login) {
		List<String> ueCodes = allUeCode;
		String queryGradeGpa = "SELECT * FROM notes_details WHERE nom IN (SELECT nom FROM eleves WHERE personne_id=:login)";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("login", login);
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate
				.queryForList(queryGradeGpa, namedParameter);
		Map<String, Float> gpaByUeCode = new HashMap<String, Float>();
		for (Map<String, Object> map : studentMap) {
			String codeUe = (String) map.get("code_ue");
			if (ueCodes.contains(codeUe)) {
				Integer creditsEcts = (Integer) map.get("credits_ects");
				if ((creditsEcts != null) && (creditsEcts != 0)) {
					Float gradeGpa = (Float) map.get("grade_gpa");
					gpaByUeCode.put(codeUe, gradeGpa);
				}
			}
		}
		return gpaByUeCode;
	}

	@Override
	public void generateUeCode() {
		List<String> ueS5 = getUeCodeFromSemester("SEM-5");
		List<String> ueS6 = getUeCodeFromSemester("SEM-6");
		List<String> ueS7 = getUeCodeFromSemester("SEM-7");
		List<String> ueCode = new ArrayList<String>();
		for (String ue : ueS5) {
			ueCode.add(ue);
		}
		for (String ue : ueS6) {
			ueCode.add(ue);
		}
		for (String ue : ueS7) {
			ueCode.add(ue);
		}
		allUeCode = ueCode;
	}

	@Override
	public List<String> findAllValidForSpecUeCode() {
		generateUeCode();
		List<String> allValidUeCode = new ArrayList<String>();
		for (String code : allUeCode) {
			if (!code.substring(0, 3).equals("EAO")) {
				allValidUeCode.add(code);
			}
		}
		return allValidUeCode;
	}

	public List<String> getUeCodeFromSemester(String semester) {
		String queryUeCode = "SELECT DISTINCT code_ue FROM notes_details WHERE cycle=:cycle AND sem=:semester";
		Map<String, String> namedParameter = new HashMap<String, String>();
		if (semester == "SEM-7") {
			namedParameter.put("cycle", getCurrentCycle());
		} else {
			namedParameter.put("cycle", getLastCycle());
		}
		namedParameter.put("semester", semester);
		List<Map<String, Object>> ueCodeMap = namedParameterjdbcTemplate
				.queryForList(queryUeCode, namedParameter);
		List<String> ueCode = new ArrayList<String>();
		for (Map<String, Object> map : ueCodeMap) {
			ueCode.add((String) map.get("code_ue"));
		}
		return ueCode;
	}

	@Override
	public List<String> getRanking() {
		return ranking;
	}

	@Override
	public Student getStudent(String login) {
		generateRanking();
		generateUeCode();
		Student student = new Student();
		student.setDetails(getStudentDetailsFromLogin(login));
		student.setResults(getResultsFromLogin(login));
		return student;
	}

	@Override
	public List<Float> getMeans() {
		return means;
	}

	@Override
	public String getNameFromLogin(String login) {
		String queryGradeGpa = "SELECT nom FROM eleves WHERE personne_id=:login";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("login", login);
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate
				.queryForList(queryGradeGpa, namedParameter);
		if (studentMap.size() == 0){
			return login;
		}
		else{
			String name = login;
			for (Map<String, Object> map : studentMap) {
				name = (String) map.get("nom");
			}
			return name;
		}
	}

	@Override
	public List<String> getAllStudentConcernedName() {
		List<String> allStudentConcernedName = new ArrayList<String>();
		for (String login : getAllStudentConcernedLogin()){
			allStudentConcernedName.add(getNameFromLogin(login));
		}
		Collections.sort(allStudentConcernedName, new ComparatorName());
		return allStudentConcernedName;
	}

	@Override
	public String getLoginFromName(String name) {
		String queryGradeGpa = "SELECT personne_id FROM eleves WHERE nom=:nom";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("nom", name);
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate
				.queryForList(queryGradeGpa, namedParameter);
		if (studentMap.size() == 0){
			return name;
		}
		else{
			String login = name;
			for (Map<String, Object> map : studentMap) {
				name = (String) map.get("personne_id");
			}
			return login;
		}
	}

	@Override
	public List<SimpleStudent> findAllStudentsConcerned() {
		List<SimpleStudent> studentsConcerned = new ArrayList<SimpleStudent>();
		List<String> allLogin = getAllStudentConcernedLogin();
		for (String login : allLogin){
			SimpleStudent student = new SimpleStudent(login, getNameFromLogin(login));
			studentsConcerned.add(student);
		}
		Collections.sort(studentsConcerned, new ComparatorSimpleStudent());
		return studentsConcerned;
	}
	
	@Override
	public boolean checkStudent(String login){
		return !getNameFromLogin(login).equals(login);
	}

}
