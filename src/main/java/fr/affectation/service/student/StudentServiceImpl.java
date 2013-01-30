package fr.affectation.service.student;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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

import fr.affectation.domain.comparator.ComparatorSimpleStudent;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.StudentToExclude;
import fr.affectation.service.agap.AgapService;

@Service
public class StudentServiceImpl implements StudentService {

	@Inject
	private SessionFactory sessionFactory;
	
	@Inject
	private AgapService agapService;

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
		List<StudentToExclude> allStudent = (List<StudentToExclude>) query
				.list();
		return allStudent;
	}
	
	@Override 
	@Transactional(readOnly = true)
	public boolean isExcluded(SimpleStudent student){
		return findByLogin(student.getLogin())!= null;
	}
	
	@Override 
	@Transactional(readOnly = true)
	public boolean isExcluded(String login){
		return findByLogin(login)!= null;
	}
	
	@Override 
	@Transactional(readOnly = true)
	public StudentToExclude findByLogin(String login){
		String queryStudent = "from StudentToExclude where login=:login";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(queryStudent);
		query.setString("login", login);
		List<StudentToExclude> allStudents= query.list();
		if (allStudents.size() == 1){
			return allStudents.get(0);
		}
		else{
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
				if (cell != null){
					if (!cell.toString().equals("")){
						String login = cell.toString();
						if ((!login.equals("")) && (agapService.checkStudent(login))){
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
	public List<SimpleStudent> findAllStudentsConcerned(){
		List<SimpleStudent> allStudents = agapService.findAllStudentsConcerned();
		List<SimpleStudent> allStudentsConcerned = new ArrayList<SimpleStudent>();
		for (SimpleStudent student : allStudents){
			if (!isExcluded(student)){
				allStudentsConcerned.add(student);
			}
		}
		Collections.sort(allStudentsConcerned, new ComparatorSimpleStudent());
		return allStudentsConcerned;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> findAllStudentsConcernedLogin(){
		List<SimpleStudent> allStudentsConcerned = findAllStudentsConcerned();
		List<String> allStudentsConcernedLogin = new ArrayList<String>();
		for (SimpleStudent student : allStudentsConcerned){
			allStudentsConcernedLogin.add(student.getLogin());
		}
		return allStudentsConcernedLogin;
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isStudentConcerned(String login){
		return findAllStudentsConcernedLogin().contains(login);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<SimpleStudent> findAllStudentsToExclude() {
		List<SimpleStudent> studentsToExclude = new ArrayList<SimpleStudent>();
		List<String> studentsLogin = findAllStudentToExcludeLogin();
		for (String login : studentsLogin) {
			SimpleStudent student = new SimpleStudent(login,
					agapService.getNameFromLogin(login));
			studentsToExclude.add(student);
		}
		Collections.sort(studentsToExclude, new ComparatorSimpleStudent());
		return studentsToExclude;
	}
	
	@Override
	@Transactional(readOnly = true)
	public int findNecessarySizeForStudentExclusion(){
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

}
