package fr.affectation.service.specialization;

import javax.inject.Inject;

import junit.framework.Assert;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.Specialization;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SpecializationServiceTest {
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Inject 
	private SpecializationService specializationService;
	
	@After
	public void cleanDb() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from JobSector").executeUpdate();
		session.createQuery("delete from ImprovementCourse").executeUpdate();
		transaction.commit();
		session.close();
	}

	@Test
	public void saveJobSector(){
		specializationService.save(createSpecialization("JobSector"));
	}
	
	@Test
	public void saveImprovementCourse(){
		specializationService.save(createSpecialization("ImprovementCourse"));
	}
	
	@Test
	public void distinctionBetweenSpecialization(){
		saveJobSector();
		saveImprovementCourse();
		JobSector specializationJS = specializationService.getJobSectorByAbbreviation("S2I");
		ImprovementCourse specializationIC = specializationService.getImprovementCourseByAbbreviation("S2I");
		Assert.assertTrue(specializationJS.getType() != specializationIC.getType());
	}
	
	public Specialization createSpecialization(String type){
		Specialization specialization;
		if (type == "JobSector"){
			specialization = new JobSector();
		}
		else {
			specialization = new ImprovementCourse();
		}
		specialization.setAbbreviation("S2I");
		specialization.setCodeUe1("ALG-1");
		specialization.setCodeUe2("ALG-1");
		specialization.setCodeUe3("ALG-1");
		specialization.setCodeUe4("ALG-1");
		specialization.setCodeUe5("ALG-1");
		specialization.setResponsibleLogin("login");
		return specialization;
	}
	
	@Test
	public void deleteJobSector(){
		Specialization jobSector = createSpecialization("JobSector");
		specializationService.save(jobSector);
		specializationService.delete(jobSector);
		Assert.assertTrue(specializationService.findAllJobSector().size() == 0);
	}
	
}
