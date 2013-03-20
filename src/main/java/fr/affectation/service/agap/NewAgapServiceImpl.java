package fr.affectation.service.agap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
public class NewAgapServiceImpl implements AgapCacheService {
	
	@Inject
	private NamedParameterJdbcTemplate namedParameterjdbcTemplate;
	
	@Inject
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<SimpleStudent> findStudentsConcerned() {
		String requeteEleves = "SELECT nom, uid FROM \"z720_choix3A_eleves\" WHERE nom IN "
				+ "(SELECT nom FROM \"720_choix3A_gpa\" WHERE cycle=:cycle AND sem='SEM-7') AND entree_fil NOT IN ('Etranger', 'Crédits', 'DD', 'Erasmus hors CEE')";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", getCurrentCycle());
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate.queryForList(requeteEleves, namedParameter);
		List<SimpleStudent> students = new ArrayList<SimpleStudent>();
		for (Map<String, Object> map : studentMap) {
			students.add(new SimpleStudent((String) map.get("uid"), (String) map.get("nom")));
		}
		
		requeteEleves = "SELECT nom, uid FROM \"z720_choix3A_eleves\" WHERE nom IN (SELECT nom FROM \"720_choix3A_cesure\" WHERE nom IN "
				+ "(SELECT nom FROM \"720_choix3A_gpa\" WHERE cycle=:cycle AND sem='SEM-7'))";
		namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", getLastCycle());
		studentMap = namedParameterjdbcTemplate.queryForList(requeteEleves, namedParameter);
		for (Map<String, Object> map : studentMap) {
			students.add(new SimpleStudent((String) map.get("uid"), (String) map.get("nom")));
		}
		return students;
	}

	@Override
	public List<String> findStudentConcernedLogins() {
		String requeteEleves = "SELECT uid FROM \"z720_choix3A_eleves\" WHERE nom IN "
				+ "(SELECT nom FROM \"720_choix3A_gpa\" WHERE cycle=:cycle AND sem='SEM-7') AND entree_fil NOT IN ('Etranger', 'Crédits', 'DD', 'Erasmus hors CEE')";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", getCurrentCycle());
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate.queryForList(requeteEleves, namedParameter);
		List<String> studentLogins = new ArrayList<String>();
		for (Map<String, Object> map : studentMap) {
			studentLogins.add((String) map.get("uid"));
		}
		
		requeteEleves = "SELECT uid FROM \"z720_choix3A_eleves\" WHERE nom IN (SELECT nom FROM \"720_choix3A_cesure\" WHERE nom IN "
				+ "(SELECT nom FROM \"720_choix3A_gpa\" WHERE cycle=:cycle AND sem='SEM-7'))";
		namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", getLastCycle());
		studentMap = namedParameterjdbcTemplate.queryForList(requeteEleves, namedParameter);
		for (Map<String, Object> map : studentMap) {
			studentLogins.add((String) map.get("uid"));
		}
		return studentLogins;
	}

	@Override
	public List<String> findCurrentPromotionStudentLogins() {
		String requeteEleves = "SELECT uid FROM \"z720_choix3A_eleves\" WHERE nom IN "
				+ "(SELECT nom FROM \"720_choix3A_gpa\" WHERE cycle=:cycle AND sem='SEM-7') AND entree_fil NOT IN ('Etranger', 'Crédits', 'DD', 'Erasmus hors CEE')";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", getCurrentCycle());
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate.queryForList(requeteEleves, namedParameter);
		List<String> studentLogins = new ArrayList<String>();
		for (Map<String, Object> map : studentMap) {
			studentLogins.add((String) map.get("uid"));
		}
		return studentLogins;
	}

	@Override
	public List<String> findCesureStudentLogins() {
		String requeteEleves = "SELECT uid FROM \"z720_choix3A_eleves\" WHERE nom IN (SELECT nom FROM \"720_choix3A_cesure\" WHERE nom IN "
				+ "(SELECT nom FROM \"720_choix3A_gpa\" WHERE cycle=:cycle AND sem='SEM-7'))";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", getLastCycle());
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate.queryForList(requeteEleves, namedParameter);
		List<String> studentLogins = new ArrayList<String>();
		for (Map<String, Object> map : studentMap) {
			studentLogins.add((String) map.get("uid"));
		}
		return studentLogins;
	}

	@Override
	public List<SimpleStudent> findCurrentPromotionSimpleStudents() {
		String requeteEleves = "SELECT uid, nom FROM \"z720_choix3A_eleves\" WHERE nom IN "
				+ "(SELECT nom FROM \"720_choix3A_gpa\" WHERE cycle=:cycle AND sem='SEM-7') AND entree_fil NOT IN ('Etranger', 'Crédits', 'DD', 'Erasmus hors CEE')";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", getCurrentCycle());
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate.queryForList(requeteEleves, namedParameter);
		List<SimpleStudent> students = new ArrayList<SimpleStudent>();
		for (Map<String, Object> map : studentMap) {
			students.add(new SimpleStudent((String) map.get("uid"), (String) map.get("nom")));
		}
		return students;
	}

	@Override
	public List<SimpleStudent> findCesureSimpleStudents() {
		String requeteEleves = "SELECT uid, nom FROM \"z720_choix3A_eleves\" WHERE nom IN (SELECT nom FROM \"720_choix3A_cesure\" WHERE nom IN "
				+ "(SELECT nom FROM \"720_choix3A_gpa\" WHERE cycle=:cycle AND sem='SEM-7'))";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", getLastCycle());
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate.queryForList(requeteEleves, namedParameter);
		List<SimpleStudent> students = new ArrayList<SimpleStudent>();
		for (Map<String, Object> map : studentMap) {
			students.add(new SimpleStudent((String) map.get("uid"), (String) map.get("nom")));
		}
		return students;
	}

	@Override
	public String findNameFromLogin(String login) {
		String query = "SELECT nom FROM \"z720_choix3A_eleves\" WHERE uid=:uid";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("uid", login);
		List<Map<String, Object>> studentMap = namedParameterjdbcTemplate.queryForList(query, namedParameter);
		return studentMap.size() == 1 ? (String) studentMap.get(0).get("nom") : login;
	}

	@Override
	public List<Contentious> findContentious(String login) {
		String queryContentious = "SELECT * FROM \"720_choix3A_contentieux\" WHERE nom IN (SELECT nom FROM \"z720_choix3A_eleves\" WHERE uid=:login)";
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
	public List<Float> findGpaMeans(String login) {
		String requeteEleves = "SELECT * FROM \"720_choix3A_gpa\" WHERE nom IN (SELECT nom FROM \"z720_choix3A_eleves\" WHERE uid=:login) and sem IN ('SEM-5', 'SEM-6', 'SEM-7')";
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

	@Override
	public List<UeResult> findUeResults(String login) {
		String queryGradeGpa = "SELECT * FROM \"z720_choix3A_notes_details\" WHERE nom IN (SELECT nom FROM \"z720_choix3A_eleves\" WHERE uid=:login)";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("login", login);
		List<Map<String, Object>> resultsMap = namedParameterjdbcTemplate.queryForList(queryGradeGpa, namedParameter);
		List<UeResult> results = new ArrayList<UeResult>();
		for (Map<String, Object> map : resultsMap) {
			if ((map.get("credits_ects") != null)) {
				Integer creditsEcts = ((BigDecimal) map.get("credits_ects")).toBigInteger().intValue();
				if (creditsEcts > 0){
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
		}
		return results;
	}

	@Override
	public Map<String, String> findNamesForAListOfLogins(List<String> allLogins) {
		Map<String, String> results = new HashMap<String, String>();
		if (allLogins.size() > 0){
			String query = "SELECT nom, uid FROM \"z720_choix3A_eleves\" WHERE uid IN (";
			int size = allLogins.size();
			int index = 0;
			for (String login : allLogins){
				query += "'" + login + "'";
				if (!(index == size-1)){
					query += ", ";
				}
				index += 1;
			}
			query += ")";
			List<Map<String, Object>> studentMap = jdbcTemplate.queryForList(query);
			for (Map<String, Object> student : studentMap){
				results.put((String) student.get("uid"), (String) student.get("nom"));
			}
		}
		return results;
	}

	@Override
	public boolean isStudent(String login) {
		String query = "SELECT count(*) FROM \"z720_choix3A_eleves\" WHERE nom IN "
				+ "(SELECT nom FROM \"720_choix3A_gpa\" WHERE cycle=:cycle AND sem='SEM-7') AND entree_fil NOT IN ('Etranger', 'Crédits', 'DD', 'Erasmus hors CEE') AND uid=:uid";
		Map<String, String> namedParameter = new HashMap<String, String>();
		namedParameter.put("cycle", getCurrentCycle());
		namedParameter.put("uid", login);
		if (namedParameterjdbcTemplate.queryForInt(query, namedParameter) > 0){
			return true;
		}
		query = "SELECT count(*) FROM \"z720_choix3A_eleves\" WHERE nom IN (SELECT nom FROM \"720_choix3A_cesure\" WHERE nom IN "
				+ "(SELECT nom FROM \"720_choix3A_gpa\" WHERE cycle=:cycle AND sem='SEM-7')) AND uid=:uid";
		namedParameter.put("cycle", getLastCycle());
		namedParameter.put("uid", login);
		if (namedParameterjdbcTemplate.queryForInt(query, namedParameter) > 0){
			return true;
		}
		return false;
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

}
