package fr.affectation.service.configuration;

import java.text.ParseException;
import java.util.Date;

import javax.inject.Inject;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerBean;
import org.springframework.stereotype.Service;

import fr.affectation.service.mail.MailService;
import fr.affectation.service.student.StudentService;

@Service
public class ConfigurationService{
	
	@Inject
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Inject
	private StudentService studentService;
	
	@Inject
	private MailService mailService;
	
	private boolean submissionAvailable = false;
	
	private boolean running = false;
	
	private boolean config = true;
	
	private boolean validating = false;

	private When when;
	
	public void initialize() throws ClassNotFoundException, NoSuchMethodException, ParseException, SchedulerException {
		MethodInvokingJobDetailFactoryBean jobDetailFirstEmail = createJobDetail("sendFirstEmail", "jobDetailFirstEmail");
		MethodInvokingJobDetailFactoryBean jobDetailSecondEmail = createJobDetail("sendSecondEmail", "jobDetailSecondEmail");
		MethodInvokingJobDetailFactoryBean jobDetailEndSubmission = createJobDetail("endSubmission", "jobDetailEndSubmission");
		MethodInvokingJobDetailFactoryBean jobDetailEndValidation = createJobDetail("endValidation", "jobDetailEndValidation");
		
		SimpleTriggerBean triggerFirstEmail = createTrigger("triggerFirstEmail", when.getFirstEmail(), jobDetailFirstEmail);
		SimpleTriggerBean triggerSecondEmail = createTrigger("triggerSecondEmail", when.getSecondEmail(), jobDetailSecondEmail);
		SimpleTriggerBean triggerEndSubmission = createTrigger("triggerEndSubmission", when.getEndSubmission(), jobDetailEndSubmission);
		SimpleTriggerBean triggerEndValidation = createTrigger("triggerEndValidation", when.getEndValidation(), jobDetailEndValidation);
		
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduler.scheduleJob((JobDetail) jobDetailFirstEmail.getObject(), triggerFirstEmail);
		scheduler.scheduleJob((JobDetail) jobDetailSecondEmail.getObject(), triggerSecondEmail);
		scheduler.scheduleJob((JobDetail) jobDetailEndSubmission.getObject(), triggerEndSubmission);
		scheduler.scheduleJob((JobDetail) jobDetailEndValidation.getObject(), triggerEndValidation);
		
		running = true;
		config = false;
		submissionAvailable = true;
	}
	
	public void stopProcess() throws SchedulerException{
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduler.deleteJob("jobDetailFirstEmail", "affectation");
		scheduler.deleteJob("jobDetailSecondEmail", "affectation");
		scheduler.deleteJob("jobDetailEndSubmission", "affectation");
		this.when = null;
		
		running = false;
		config = true;
		submissionAvailable = false;
	}
	
	public void sendFirstEmail(){
		//mailService.sendMail("valery.marmousez@centrale-marseille.fr", "valery.marmousez@gmail.com", "Rappel", "1er rappel");
	}
	
	public void sendSecondEmail(){
		//mailService.sendMail("valery.marmousez@centrale-marseille.fr", "valery.marmousez@gmail.com", "Rappel", "2ème rappel");
	}
	
	public void endSubmission(){
		submissionAvailable = false;
		validating = true;
		populateValidation();
	}
	
	public void endValidation(){
		validating = false;
	}
	
	public void populateValidation(){
		studentService.populateValidation();
	}
	
	public boolean isValidationForAdminAvailable(){
		return running && !submissionAvailable;
	}
	
	private MethodInvokingJobDetailFactoryBean createJobDetail(String method, String name) throws ClassNotFoundException, NoSuchMethodException{
		MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
		jobDetail.setTargetObject(this);
		jobDetail.setTargetMethod(method);
		jobDetail.setName(name);
		jobDetail.setGroup("affectation");
		jobDetail.setConcurrent(false);
		jobDetail.afterPropertiesSet();
		return jobDetail;
	}
	
	private SimpleTriggerBean createTrigger(String name, Date date, MethodInvokingJobDetailFactoryBean jobDetail) throws ParseException{
		SimpleTriggerBean trigger = new SimpleTriggerBean();
		trigger.setBeanName(name);
		trigger.setJobDetail((JobDetail) jobDetail.getObject());
		trigger.setStartTime(date);
		trigger.setRepeatCount(0);
		trigger.setRepeatInterval(0L);
		trigger.afterPropertiesSet();
		return trigger;
	}
	
	public boolean isSubmissionAvailable() {
		return submissionAvailable;
	}
	
	public When getWhen() {
		return when;
	}

	public boolean isRunning() {
		return running;
	}
	
	public boolean isValidating() {
		return validating;
	}

	public void setRun(boolean run) {
		this.running = run;
	}

	public boolean isConfig() {
		return config;
	}

	public void setConfig(boolean config) {
		this.config = config;
	}
	
	public void setWhen(When when){
		this.when = when;
	}
	
}
