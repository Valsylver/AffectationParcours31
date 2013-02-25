package fr.affectation.service.responsible;

import javax.inject.Inject;

import junit.framework.Assert;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.Specialization;
import fr.affectation.service.specialization.SpecializationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ResponsibleServiceTest {
	
	@Inject
	private ResponsibleService responsibleService;
	
	@Inject
	private SpecializationService specializationService;
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Before
	public void cleanDbBefore() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from JobSector").executeUpdate();
		session.createQuery("delete from ImprovementCourse").executeUpdate();
		transaction.commit();
		session.close();
	}
	
	@After
	public void cleanDbAfter() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from JobSector").executeUpdate();
		session.createQuery("delete from ImprovementCourse").executeUpdate();
		transaction.commit();
		session.close();
	}
	
	@Test
	public void findResponsibles(){
		for (int i=0; i<10; i++){
			createAndSaveSpecialization("JobSector", "responsibleJS" + i, "abbJs" + i);
			createAndSaveSpecialization("ImprovementCourse", "responsibleIC" + i, "abbIc" + i);
		}
		Assert.assertEquals(20, responsibleService.findResponsibles().size());
	}
	
	@Test
	public void forWhichSpecialization(){
		createAndSaveSpecialization("JobSector", "responsibleJS", "abbJs");
		createAndSaveSpecialization("ImprovementCourse", "responsibleIC", "abbIc");
		Assert.assertEquals("abbJs", responsibleService.forWhichSpecialization("responsibleJS"));
		Assert.assertEquals("abbIc", responsibleService.forWhichSpecialization("responsibleIC"));
	}
	
	@Test
	public void forWhichSpecializationTypeJs(){
		createAndSaveSpecialization("JobSector", "resp", "abb");
		Assert.assertTrue(responsibleService.forWhichSpecializationType("resp") == Specialization.JOB_SECTOR);
	}
	
	@Test
	public void isResponsibleJs(){
		createAndSaveSpecialization("JobSector", "resp", "abb");
		Assert.assertTrue(responsibleService.isResponsible("resp"));
	}
	
	@Test
	public void forWhichSpecializationTypeIc(){
		createAndSaveSpecialization("ImprovementCourse", "resp", "abb");
		Assert.assertTrue(responsibleService.forWhichSpecializationType("resp") == Specialization.IMPROVEMENT_COURSE);
	}
	
	@Test
	public void isResponsibleIc(){
		createAndSaveSpecialization("ImprovementCourse", "resp", "abb");
		Assert.assertTrue(responsibleService.isResponsible("resp"));
	}
	
	public void createAndSaveSpecialization(String type, String loginResponsible, String abbreviation){
		Specialization specialization = (type.equals("JobSector")) ? new JobSector() : new ImprovementCourse();
		specialization.setResponsibleLogin(loginResponsible);
		specialization.setAbbreviation(abbreviation);
		specializationService.save(specialization);
	}

}
