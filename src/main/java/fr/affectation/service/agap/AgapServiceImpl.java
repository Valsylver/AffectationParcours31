package fr.affectation.service.agap;

import java.math.BigDecimal;
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

import fr.affectation.domain.student.Contentious;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.UeResult;

@Service
public class AgapServiceImpl implements AgapService {

	@Inject
	private NamedParameterJdbcTemplate namedParameterjdbcTemplate;
	
	@Inject
	private JdbcTemplate jdbcTemplate;

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

	public List<Float> findGpaMeans(String login) {
		String requeteEleves = "SELECT * FROM 720_choix3A_gpa WHERE nom IN (SELECT nom FROM 720_choix3A_eleves WHERE uid=:login) and sem IN ('SEM-5', 'SEM-6', 'SEM-7')";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("login", login);
		List<Map<String, Object>> gpaMap = namedParameterjdbcTemplate.queryForList(requeteEleves, namedParameter);
		Map<String, List<Float>> gpaValues = new HashMap<String, List<Float>>();
		for (Map<String, Object> map : gpaMap) {
			String semester = (String) map.get("sem");
			String gpaS = (String) map.get("gpa");
			Float gpa;
			try{
				gpa = Float.parseFloat(gpaS);				
			}
			catch (Exception e){
				gpa = (float) 0.0;
			}
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
		String queryContentious = "SELECT * FROM 720_choix3A_contentieux WHERE nom IN (SELECT nom FROM 720_choix3A_eleves WHERE uid=:login)";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("login", login);
		List<Map<String, Object>> allContentiousMap = namedParameterjdbcTemplate.queryForList(queryContentious, namedParameter);
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
	public List<String> findCurrentPromotionStudentLogins() {
		String cycle = getCurrentCycle();
		String requeteEleves = "SELECT uid FROM 720_choix3A_eleves WHERE nom IN "
				+ "(SELECT nom FROM 720_choix3A_gpa WHERE cycle=:cycle AND sem='SEM-7') AND entree_fil NOT IN ('Etranger', 'Crédits', 'DD', 'Erasmus hors CEE')";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", cycle);
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate.queryForList(requeteEleves, namedParameter);
		List<String> allStudentLogin = new ArrayList<String>();
		for (Map<String, Object> map : studentMap) {
			String login = (String) map.get("uid");
			allStudentLogin.add(login);
		}
		return allStudentLogin;
	}
	
	@Override 
	public List<SimpleStudent> findCurrentPromotionSimpleStudents(){
		List<SimpleStudent> studentsConcerned = new ArrayList<SimpleStudent>();
		String requeteEleves = "SELECT nom, uid FROM 720_choix3A_eleves WHERE nom IN "
				+ "(SELECT nom FROM 720_choix3A_gpa WHERE cycle=:cycle AND sem='SEM-7') AND entree_fil NOT IN ('Etranger', 'Crédits', 'DD', 'Erasmus hors CEE')";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", getCurrentCycle());
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate.queryForList(requeteEleves, namedParameter);
		for (Map<String, Object> map : studentMap) {
			String login = (String) map.get("uid");
			String name = (String) map.get("nom");
			studentsConcerned.add(new SimpleStudent(login, name));
		}
		return studentsConcerned;
	}
	
	@Override
	public List<String> findCesureStudentLogins() {
		String queryCesure = "SELECT uid FROM 720_choix3A_eleves WHERE nom IN (SELECT nom FROM 720_choix3A_cesure WHERE nom IN "
				+ "(SELECT nom FROM 720_choix3A_gpa WHERE cycle=:cycle AND sem='SEM-7'))";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", getLastCycle());
		List<Map<String, Object>> resultsMap = namedParameterjdbcTemplate.queryForList(queryCesure, namedParameter);
		List<String> logins = new ArrayList<String>();
		for (Map<String, Object> map : resultsMap) {
			logins.add((String) map.get("uid"));
		}
		return logins;
	}
	
	@Override
	public List<SimpleStudent> findCesureSimpleStudents() {
		List<SimpleStudent> studentsConcerned = new ArrayList<SimpleStudent>();
		String requeteEleves = "SELECT nom, uid FROM 720_choix3A_eleves WHERE nom IN (SELECT nom FROM 720_choix3A_cesure WHERE nom IN "
				+ "(SELECT nom FROM 720_choix3A_gpa WHERE cycle=:cycle AND sem='SEM-7'))";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", getLastCycle());
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate.queryForList(requeteEleves, namedParameter);
		for (Map<String, Object> map : studentMap) {
			String login = (String) map.get("uid");
			String name = (String) map.get("nom");
			studentsConcerned.add(new SimpleStudent(login, name));
		}
		return studentsConcerned;
	}

	public List<String> getUeCodeFromSemester(String semester) {
		String queryUeCode = "SELECT code_ue FROM 720_choix3A_notes_details WHERE cycle=:cycle AND sem=:semester";
		Map<String, String> namedParameter = new HashMap<String, String>();
		if (semester == "SEM-7") {
			namedParameter.put("cycle", getCurrentCycle());
		} else {
			namedParameter.put("cycle", getLastCycle());
		}
		namedParameter.put("semester", semester);
		List<Map<String, Object>> ueCodeMap = namedParameterjdbcTemplate.queryForList(queryUeCode, namedParameter);
		List<String> ueCode = new ArrayList<String>();
		for (Map<String, Object> map : ueCodeMap) {
			ueCode.add((String) map.get("code_ue"));
		}
		return ueCode;
	}

	@Override
	public String findNameFromLogin(String login) {
		String queryGradeGpa = "SELECT nom FROM 720_choix3A_eleves WHERE uid=:login";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("login", login);
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate.queryForList(queryGradeGpa, namedParameter);
		if (studentMap.size() == 0) {
			return login;
		} else {
			String name = login;
			for (Map<String, Object> map : studentMap) {
				name = (String) map.get("nom");
			}
			return name;
		}
	}

	@Override
	public List<SimpleStudent> findStudentsConcerned() {
		List<SimpleStudent> studentsConcerned = new ArrayList<SimpleStudent>();
		List<SimpleStudent> currentPromotion = findCurrentPromotionSimpleStudents();
		List<SimpleStudent> cesure = findCesureSimpleStudents();
		studentsConcerned.addAll(currentPromotion);
		studentsConcerned.addAll(cesure);
		Collections.sort(studentsConcerned);
		return studentsConcerned;
	}
	
	@Override
	public List<String> findStudentConcernedLogins() {
		List <String> logins = findCurrentPromotionStudentLogins();
		logins.addAll(findCesureStudentLogins());
		return logins;
	}

	@Override
	public List<UeResult> findUeResults(String login) {
		String queryGradeGpa = "SELECT * FROM 720_choix3A_notes_details WHERE nom IN (SELECT nom FROM 720_choix3A_eleves WHERE uid=:login)";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("login", login);
		List<Map<String, Object>> resultsMap = namedParameterjdbcTemplate.queryForList(queryGradeGpa, namedParameter);
		List<UeResult> results = new ArrayList<UeResult>();
		for (Map<String, Object> map : resultsMap) {
			Integer creditsEcts = ((BigDecimal) map.get("credits_ects")).toBigInteger().intValue();
			if ((creditsEcts != null) && (creditsEcts != 0)) {
				UeResult result = new UeResult();
				result.setCode((String) map.get("code_ue"));
				result.setGpa(Float.valueOf((String) map.get("grade_gpa")));
				result.setSession((Integer) map.get("session"));
				result.setSemester((String) map.get("sem"));
				result.setEcts((String) map.get("grade_ects"));
				result.setCycle((String) map.get("cycle"));
				results.add(result);
			}
		}
		return results;
	}

	@Override
	public Map<String, String> findNamesForAListOfLogins(List<String> allLogins) {
		Map<String, String> mapReturn = new HashMap<String, String>();
		if (allLogins.size() > 0){
			String query = "SELECT nom, uid FROM 720_choix3A_eleves WHERE uid IN (";
			int index = 0;
			int size = allLogins.size();
			for (String login : allLogins){
				query += "'";
				query += login;
				query += "'";
				if (index != size-1){
					query += ", ";
				}
				index += 1;
			}
			query += ")";
			List<Map<String, Object>> resultsMap = jdbcTemplate.queryForList(query);
			for (Map<String, Object> map : resultsMap) {
				mapReturn.put((String) map.get("uid"), (String) map.get("nom"));
			}
		}
		return mapReturn;
	}
}
