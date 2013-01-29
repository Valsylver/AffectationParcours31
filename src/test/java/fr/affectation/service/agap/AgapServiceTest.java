package fr.affectation.service.agap;

import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AgapServiceTest {

	@Inject
	private AgapService agapService;
	
	@Test
	public void getStudent(){
		if (agapService.getAllStudentConcernedLogin().size() > 0){
			for (int i=0; i<10; i++){
				agapService.getStudent(chooseRandomLogin());
			}
		}
	}
	
	@Test
	public void getStudentDetails(){
		if (agapService.getAllStudentConcernedLogin().size() > 0){
			for (int i=0; i<10; i++){
				agapService.getStudentDetailsFromLogin(chooseRandomLogin());
			}
		}
	}
	
	@Test
	public void getResults(){
		agapService.generateRanking();
		if (agapService.getAllStudentConcernedLogin().size() > 0){
			for (int i=0; i<10; i++){
				agapService.getResultsFromLogin(chooseRandomLogin());
			}
		}
	}
	
	@Test
	public void generateRanking(){
		agapService.generateRanking();
		List<String> ranking = agapService.getRanking();
		List<Float> means = agapService.getMeans();
		Assert.assertTrue(means.size() == ranking.size());
		if (means.size() > 1){
			for (int index=0; index < means.size()-1; index++){
				Assert.assertTrue(means.get(index) >= means.get(index + 1));
			}
		}
	}
	
	@Test
	public void getUeGrade(){
		agapService.generateRanking();
		if (agapService.getAllStudentConcernedLogin().size() > 0){
			for (int i=0; i<10; i++){
				agapService.getUeGrade(chooseRandomLogin());
			}
		}
	}
	
	@Test
	public void isStudent(){
		Assert.assertTrue(agapService.isStudentConcerned(chooseRandomLogin()));
	}

	
	@Test
	public void ueCode(){
		agapService.findAllValidForSpecUeCode();
	}
	
	@Test
	public void nameFromLogin(){
		if (agapService.getAllStudentConcernedLogin().size() > 0){
			for (int i=0; i<10; i++){
				agapService.getNameFromLogin(chooseRandomLogin());
			}
		}
	}
	
	public String chooseRandomLogin(){
		List<String> allLogin = agapService.getAllStudentConcernedLogin();
		return allLogin.get((int) (Math.random() * allLogin.size()));   
	}
	
	
}
