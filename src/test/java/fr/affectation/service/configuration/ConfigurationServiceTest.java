package fr.affectation.service.configuration;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

	private Timestamp date1ts;

	private Timestamp date2ts;

	private Timestamp date3ts;

	private Timestamp date4ts;

	public ConfigurationServiceTest() {
		Calendar calendar = Calendar.getInstance();
		date1 = calendar.getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date1ts = Timestamp.valueOf(dateFormat.format(date1));
		calendar.set(2015, Calendar.JANUARY, 1, 4, 5, 2);
		date2 = calendar.getTime();
		date2ts = Timestamp.valueOf(dateFormat.format(date2));
		calendar.set(2018, Calendar.JULY, 1, 8, 2, 32);
		date3 = calendar.getTime();
		date3ts = Timestamp.valueOf(dateFormat.format(date3));
		calendar.set(2022, Calendar.DECEMBER, 1, 8, 2, 32);
		date4 = calendar.getTime();
		date4ts = Timestamp.valueOf(dateFormat.format(date4));
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
		Assert.assertTrue(retrievedConfiguration.getFirstMail().equals(date1ts));
	}

	@Test
	public void retrieveSecondMail() {
		saveConfig();
		Configuration retrievedConfiguration = configurationService.retrieveConfiguration();
		Assert.assertTrue(retrievedConfiguration.getSecondMail().equals(date2ts));
	}

	@Test
	public void retrieveEndSubmission() {
		saveConfig();
		Configuration retrievedConfiguration = configurationService.retrieveConfiguration();
		Assert.assertTrue(retrievedConfiguration.getEndSubmission().equals(date3ts));
	}

	@Test
	public void retrieveEndValidation() {
		saveConfig();
		Configuration retrievedConfiguration = configurationService.retrieveConfiguration();
		Assert.assertTrue(retrievedConfiguration.getEndValidation().equals(date4ts));
	}

	@Test
	public void updateFirstMail() {
		saveConfig();
		configurationService.updateFirstMail(date2);
		Assert.assertTrue(configurationService.retrieveConfiguration().getFirstMail().equals(date2ts));
	}

	@Test
	public void updateSecondMail() {
		saveConfig();
		configurationService.updateSecondMail(date4);
		Assert.assertTrue(configurationService.retrieveConfiguration().getSecondMail().equals(date4ts));
	}

	@Test
	public void updateEndSubmission() {
		saveConfig();
		configurationService.updateEndSubmission(date2);
		Assert.assertTrue(configurationService.retrieveConfiguration().getEndSubmission().equals(date2ts));
	}

	@Test
	public void updateEndValidation() {
		saveConfig();
		configurationService.updateEndValidation(date2);
		Assert.assertTrue(configurationService.retrieveConfiguration().getEndValidation().equals(date2ts));
	}

	@Test
	public void delete() {
		saveConfig();
		configurationService.deleteConfiguration();
		Assert.assertTrue(configurationService.retrieveConfiguration() == null);
	}

	public void saveConfig() {
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
