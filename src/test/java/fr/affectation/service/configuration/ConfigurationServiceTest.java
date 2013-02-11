package fr.affectation.service.configuration;

import java.util.Calendar;
import java.util.Date;

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

import fr.affectation.domain.configuration.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ConfigurationServiceTest {

	@Inject
	private SessionFactory sessionFactory;

	@Inject
	private ConfigurationService configurationService;
	
	private Date date1;
	
	private Date date2;
	
	private Date date3;
	
	private Date date4;
	
	public ConfigurationServiceTest(){
		Calendar calendar = Calendar.getInstance();
		date1 = calendar.getTime();
		calendar.set(2015, Calendar.JANUARY, 1, 4, 5, 2);
		date2 = calendar.getTime();
		calendar.set(2018, Calendar.JULY, 1, 8, 2, 32);
		date3 = calendar.getTime();
		calendar.set(2022, Calendar.DECEMBER, 1, 8, 2, 32);
		date4 = calendar.getTime();
	}

	@Test
	public void saveConfiguration() {
		Date currentDate = new Date();
		configurationService.saveConfiguration(new Configuration(currentDate, currentDate, currentDate, currentDate));
	}

	@Test
	public void retrieveExistingConfiguration() {
		saveConfig();
		Assert.assertTrue(configurationService.retrieveConfiguration() != null);
	}

	@Test
	public void retrieveNoneConfiguration() {
		Assert.assertTrue(configurationService.retrieveConfiguration() == null);
	}

	@Test
	public void retrieveFirstMail() {
		saveConfig();
		Configuration retrievedConfiguration = configurationService.retrieveConfiguration();
		Assert.assertTrue(retrievedConfiguration.getFirstMail().compareTo(date1) == 0);
	}

	@Test
	public void retrieveSecondMail() {
		saveConfig();
		Configuration retrievedConfiguration = configurationService.retrieveConfiguration();
		Assert.assertTrue(retrievedConfiguration.getSecondMail().compareTo(date2) == 0);
	}

	@Test
	public void retrieveEndSubmission() {
		saveConfig();
		Configuration retrievedConfiguration = configurationService.retrieveConfiguration();
		Assert.assertTrue(retrievedConfiguration.getEndSubmission().compareTo(date3) == 0);
	}

	@Test
	public void retrieveEndValidation() {
		saveConfig();
		Configuration retrievedConfiguration = configurationService.retrieveConfiguration();
		Assert.assertTrue(retrievedConfiguration.getEndValidation().compareTo(date4) == 0);
	}
	
	@Test
	public void updateFirstMail(){
		saveConfig();
		configurationService.updateFirstMail(date2);
		Assert.assertTrue(configurationService.retrieveConfiguration().getFirstMail().compareTo(date2) == 0);
	}
	
	@Test
	public void updateSecondMail(){
		saveConfig();
		configurationService.updateSecondMail(date4);
		Assert.assertTrue(configurationService.retrieveConfiguration().getSecondMail().compareTo(date4) == 0);
	}
	
	@Test
	public void updateEndSubmission(){
		saveConfig();
		configurationService.updateEndSubmission(date2);
		Assert.assertTrue(configurationService.retrieveConfiguration().getEndSubmission().compareTo(date2) == 0);
	}
	
	@Test
	public void updateEndValidation(){
		saveConfig();
		configurationService.updateEndValidation(date2);
		Assert.assertTrue(configurationService.retrieveConfiguration().getEndValidation().compareTo(date2) == 0);
	}
	
	@Test
	public void delete(){
		saveConfig();
		configurationService.deleteConfiguration();
		Assert.assertTrue(configurationService.retrieveConfiguration() == null);
	}
	
	public void saveConfig(){
		Configuration configuration = new Configuration(date1, date2, date3, date4);
		configurationService.saveConfiguration(configuration);
	}

	@After
	public void cleanDb() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.createQuery("delete from Configuration").executeUpdate();
		transaction.commit();
		session.close();
	}

}
