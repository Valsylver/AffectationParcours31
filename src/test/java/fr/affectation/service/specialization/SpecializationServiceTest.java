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
		Assert.assertTrue(specializationService.findJobSectors().size() == 1);
	}
	
	@Test
	public void saveImprovementCourse(){
		specializationService.save(createSpecialization("ImprovementCourse"));
		Assert.assertTrue(specializationService.findImprovementCourses().size() == 1);
	}
	
	@Test 
	public void duplicateJobSector(){
		JobSector jobSector = new JobSector();
		jobSector.setAbbreviation("AAA");
		JobSector jobSector2 = new JobSector();
		jobSector2.setAbbreviation("AAA");
		specializationService.save(jobSector);
		specializationService.save(jobSector2);
		Assert.assertTrue(specializationService.findJobSectors().size() == 1);
	}
	
	@Test 
	public void duplicateImprovementCourse(){
		ImprovementCourse improvementCourse = new ImprovementCourse();
		improvementCourse.setAbbreviation("AAA");
		ImprovementCourse improvementCourse2 = new ImprovementCourse();
		improvementCourse2.setAbbreviation("AAA");
		specializationService.save(improvementCourse);
		specializationService.save(improvementCourse2);
		Assert.assertTrue(specializationService.findImprovementCourses().size() == 1);
	}

	@Test
	public void deleteJobSector(){
		JobSector jobSector = (JobSector) createSpecialization("JobSector");
		specializationService.save(jobSector);
		specializationService.delete(jobSector);
		Assert.assertTrue(specializationService.findJobSectors().size() == 0);
	}
	
	@Test
	public void deleteImprovementCourse(){
		ImprovementCourse improvementCourse = (ImprovementCourse) createSpecialization("ImprovementCourse");
		specializationService.save(improvementCourse);
		specializationService.delete(improvementCourse);
		Assert.assertTrue(specializationService.findImprovementCourses().size() == 0);
	}
	
	@Test
	public void retrieveJobSectorFromAbbreviation(){
		saveJobSector();
		JobSector js = specializationService.getJobSectorByAbbreviation("S2I");
		Assert.assertTrue(js != null);
		Assert.assertTrue(js.getResponsibleLogin().equals("login"));
	}
	
	@Test
	public void retrieveImprovementCourseFromAbbreviation(){
		saveImprovementCourse();
		ImprovementCourse ic = specializationService.getImprovementCourseByAbbreviation("S2I");
		Assert.assertTrue(ic != null);
		Assert.assertTrue(ic.getResponsibleLogin().equals("login"));
	}
	
	@Test
	public void jobSectorAbbreviation(){
		JobSector js = new JobSector();
		js.setAbbreviation("AAA");
		specializationService.save(js);
		js.setAbbreviation("BBB");
		specializationService.save(js);
		Assert.assertTrue(specializationService.findJobSectorAbbreviations().contains("AAA"));
		Assert.assertTrue(specializationService.findJobSectorAbbreviations().contains("BBB"));
	}
	
	@Test
	public void improvementCourseAbbreviation(){
		ImprovementCourse ic = new ImprovementCourse();
		ic.setAbbreviation("AAA");
		specializationService.save(ic);
		ic.setAbbreviation("BBB");
		specializationService.save(ic);
		Assert.assertTrue(specializationService.findImprovementCourseAbbreviations().contains("AAA"));
		Assert.assertTrue(specializationService.findImprovementCourseAbbreviations().contains("BBB"));
	}
	
	@Test
	public void findNameFromIcAbbreviation(){
		ImprovementCourse ic = new ImprovementCourse();
		ic.setAbbreviation("AAA");
		ic.setName("An improvement course");
		specializationService.save(ic);
		Assert.assertTrue(specializationService.findNameFromIcAbbreviation("AAA").equals("An improvement course"));
	}
	
	@Test
	public void findNameFromJsAbbreviation(){
		JobSector js = new JobSector();
		js.setAbbreviation("AAA");
		js.setName("A job sector");
		specializationService.save(js);
		Assert.assertTrue(specializationService.findNameFromJsAbbreviation("AAA").equals("A job sector"));
	}
	
	@Test
	public void findNameFromAbbreviationWhenDuplicate(){
		JobSector js = new JobSector();
		js.setAbbreviation("AAA");
		js.setName("A job sector");
		ImprovementCourse ic = new ImprovementCourse();
		ic.setAbbreviation("AAA");
		ic.setName("An improvement course");
		specializationService.save(ic);
		specializationService.save(js);
		Assert.assertTrue(specializationService.findNameFromJsAbbreviation("AAA").equals("A job sector"));
		Assert.assertTrue(specializationService.findNameFromIcAbbreviation("AAA").equals("An improvement course"));
	}
	
	@Test
	public void findJsStringsForForm(){
		JobSector js = new JobSector();
		js.setAbbreviation("AAA");
		js.setName("A job sector");
		specializationService.save(js);
		Assert.assertTrue(specializationService.findJobSectorStringsForForm().contains("A job sector (AAA)"));
	}
	
	@Test
	public void findIcStringsForForm(){
		ImprovementCourse ic = new ImprovementCourse();
		ic.setAbbreviation("AAA");
		ic.setName("An improvement course");
		specializationService.save(ic);
		Assert.assertTrue(specializationService.findImprovementCourseStringsForForm().contains("An improvement course (AAA)"));
	}
	
	@Test
	public void deleteAll(){
		for (int i=0; i<10; i++){
			JobSector js = new JobSector();
			js.setAbbreviation("js" + i);
			ImprovementCourse ic = new ImprovementCourse();
			ic.setAbbreviation("ic" + i);
			specializationService.save(js);
			specializationService.save(ic);
		}
		specializationService.deleteAll();
		Assert.assertTrue(specializationService.findJobSectors().size() == 0);
		Assert.assertTrue(specializationService.findImprovementCourses().size() == 0);
	}
	
	public void retrieveAbbreviationFromStringForForm(){
		Assert.assertTrue(specializationService.getAbbreviationFromStringForForm("A specialization (AAA)").equals("AAA"));
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
		specialization.setResponsibleLogin("login");
		return specialization;
	}
	
}
