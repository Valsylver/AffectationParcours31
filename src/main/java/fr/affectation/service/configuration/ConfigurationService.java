package fr.affectation.service.configuration;

import java.text.ParseException;
import java.util.Date;

import org.quartz.SchedulerException;

import fr.affectation.domain.configuration.Configuration;

public interface ConfigurationService {

	public boolean initialize(String path);

	public void stopProcess() throws SchedulerException;

	public void updateWhen(When whenToModify) throws SchedulerException, ClassNotFoundException, NoSuchMethodException, ParseException;

	public void sendFirstEmail();

	public void sendSecondEmail();

	public void endSubmission();

	public void endValidation();

	public void populateValidation();

	public boolean isValidationForAdminAvailable();

	public boolean isSubmissionAvailable();

	public When getWhen();

	public boolean isRunning();

	public boolean isValidating();

	public void setRun(boolean run);

	public boolean isConfig();

	public void setConfig(boolean config);

	public void setWhen(When when);

	public boolean isFirstMailActivated();

	public void setFirstMailActivated(boolean firstMailActivated);

	public boolean isSecondMailActivated();

	public void setSecondMailActivated(boolean secondMailActivated);

	public void saveConfiguration(Configuration configuration);
	
	public Configuration retrieveConfiguration();

	public void updateFirstMail(Date date);
	
	public void updateSecondMail(Date date);
	
	public void updateEndSubmission(Date date);
	
	public void updateEndValidation(Date date);

	public void deleteConfiguration();

	public void initializeFromDataBase();
	
	public boolean hasAlreadyBeenLaunched();

	public void setAlreadyBeenLaunched();

}