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
	
	private boolean firstMailActivated = true;
	
	private boolean secondMailActivated = true;

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
		scheduler.deleteJob("jobDetailEndValidation", "affectation");
		this.when = null;
		
		running = false;
		config = true;
		submissionAvailable = false;
		validating = false;
	}
	
	public void updateWhen(When whenToModify) throws SchedulerException, ClassNotFoundException, NoSuchMethodException, ParseException{
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		if (whenToModify.getNumber() == 1){
			if (when.getEndValidation().compareTo(whenToModify.getEndValidation()) != 0){
				scheduler.deleteJob("jobDetailEndValidation", "affectation");
				MethodInvokingJobDetailFactoryBean jobDetailEndValidation = createJobDetail("endValidation", "jobDetailEndValidation");
				SimpleTriggerBean triggerEndValidation = createTrigger("triggerEndValidation", whenToModify.getEndValidation(), jobDetailEndValidation);
				scheduler.scheduleJob((JobDetail) jobDetailEndValidation.getObject(), triggerEndValidation);
				when.setEndValidation(whenToModify.getEndValidation());
			}
		}
		else{
			if (whenToModify.getNumber() == 2){
				if (when.getEndSubmission().compareTo(whenToModify.getEndSubmission()) != 0){
					scheduler.deleteJob("jobDetailEndSubmission", "affectation");
					MethodInvokingJobDetailFactoryBean jobDetailEndSubmission = createJobDetail("endSubmission", "jobDetailEndSubmission");
					SimpleTriggerBean triggerEndSubmission = createTrigger("triggerEndSubmission", whenToModify.getEndSubmission(), jobDetailEndSubmission);
					scheduler.scheduleJob((JobDetail) jobDetailEndSubmission.getObject(), triggerEndSubmission);
					when.setEndSubmission(whenToModify.getEndSubmission());
				}
				if (when.getEndValidation().compareTo(whenToModify.getEndValidation()) != 0){
					scheduler.deleteJob("jobDetailEndValidation", "affectation");
					MethodInvokingJobDetailFactoryBean jobDetailEndValidation = createJobDetail("endValidation", "jobDetailEndValidation");
					SimpleTriggerBean triggerEndValidation = createTrigger("triggerEndValidation", whenToModify.getEndValidation(), jobDetailEndValidation);
					scheduler.scheduleJob((JobDetail) jobDetailEndValidation.getObject(), triggerEndValidation);
					when.setEndValidation(whenToModify.getEndValidation());
				}
			}
			else{
				if (whenToModify.getNumber() == 3){
					if (when.getSecondEmail().compareTo(whenToModify.getSecondEmail()) != 0){
						scheduler.deleteJob("jobDetailSecondEmail", "affectation");
						MethodInvokingJobDetailFactoryBean jobDetailSecondEmail = createJobDetail("sendSecondEmail", "jobDetailSecondEmail");
						SimpleTriggerBean triggerSecondEmail = createTrigger("triggerSecondEmail", whenToModify.getSecondEmail(), jobDetailSecondEmail);
						scheduler.scheduleJob((JobDetail) jobDetailSecondEmail.getObject(), triggerSecondEmail);
						when.setSecondEmail(whenToModify.getSecondEmail());
					}
					if (when.getEndSubmission().compareTo(whenToModify.getEndSubmission()) != 0){
						scheduler.deleteJob("jobDetailEndSubmission", "affectation");
						MethodInvokingJobDetailFactoryBean jobDetailEndSubmission = createJobDetail("endSubmission", "jobDetailEndSubmission");
						SimpleTriggerBean triggerEndSubmission = createTrigger("triggerEndSubmission", whenToModify.getEndSubmission(), jobDetailEndSubmission);
						scheduler.scheduleJob((JobDetail) jobDetailEndSubmission.getObject(), triggerEndSubmission);
						when.setEndSubmission(whenToModify.getEndSubmission());
					}
					if (when.getEndValidation().compareTo(whenToModify.getEndValidation()) != 0){
						scheduler.deleteJob("jobDetailEndValidation", "affectation");
						MethodInvokingJobDetailFactoryBean jobDetailEndValidation = createJobDetail("endValidation", "jobDetailEndValidation");
						SimpleTriggerBean triggerEndValidation = createTrigger("triggerEndValidation", whenToModify.getEndValidation(), jobDetailEndValidation);
						scheduler.scheduleJob((JobDetail) jobDetailEndValidation.getObject(), triggerEndValidation);
						when.setEndValidation(whenToModify.getEndValidation());
					}
				}
				else{
					if (when.getFirstEmail().compareTo(whenToModify.getFirstEmail()) != 0){
						scheduler.deleteJob("jobDetailFirstEmail", "affectation");
						MethodInvokingJobDetailFactoryBean jobDetailFirstEmail = createJobDetail("sendFirstEmail", "jobDetailFirstEmail");
						SimpleTriggerBean triggerFirstEmail = createTrigger("triggerFirstEmail", whenToModify.getFirstEmail(), jobDetailFirstEmail);
						scheduler.scheduleJob((JobDetail) jobDetailFirstEmail.getObject(), triggerFirstEmail);
						when.setFirstEmail(whenToModify.getFirstEmail());
					}
					if (when.getSecondEmail().compareTo(whenToModify.getSecondEmail()) != 0){
						scheduler.deleteJob("jobDetailSecondEmail", "affectation");
						MethodInvokingJobDetailFactoryBean jobDetailSecondEmail = createJobDetail("sendSecondEmail", "jobDetailSecondEmail");
						SimpleTriggerBean triggerSecondEmail = createTrigger("triggerSecondEmail", whenToModify.getSecondEmail(), jobDetailSecondEmail);
						scheduler.scheduleJob((JobDetail) jobDetailSecondEmail.getObject(), triggerSecondEmail);
						when.setSecondEmail(whenToModify.getSecondEmail());
					}
					if (when.getEndSubmission().compareTo(whenToModify.getEndSubmission()) != 0){
						scheduler.deleteJob("jobDetailEndSubmission", "affectation");
						MethodInvokingJobDetailFactoryBean jobDetailEndSubmission = createJobDetail("endSubmission", "jobDetailEndSubmission");
						SimpleTriggerBean triggerEndSubmission = createTrigger("triggerEndSubmission", whenToModify.getEndSubmission(), jobDetailEndSubmission);
						scheduler.scheduleJob((JobDetail) jobDetailEndSubmission.getObject(), triggerEndSubmission);
						when.setEndSubmission(whenToModify.getEndSubmission());
					}
					if (when.getEndValidation().compareTo(whenToModify.getEndValidation()) != 0){
						scheduler.deleteJob("jobDetailEndValidation", "affectation");
						MethodInvokingJobDetailFactoryBean jobDetailEndValidation = createJobDetail("endValidation", "jobDetailEndValidation");
						SimpleTriggerBean triggerEndValidation = createTrigger("triggerEndValidation", whenToModify.getEndValidation(), jobDetailEndValidation);
						scheduler.scheduleJob((JobDetail) jobDetailEndValidation.getObject(), triggerEndValidation);
						when.setEndValidation(whenToModify.getEndValidation());
					}
				}
			}
		}
	}
	
	public void sendFirstEmail(){
		System.out.println("firstEmail");
		//mailService.sendMail("valery.marmousez@centrale-marseille.fr", "valery.marmousez@gmail.com", "Rappel", "1er rappel");
	}
	
	public void sendSecondEmail(){
		System.out.println("secondEmail");
		//mailService.sendMail("valery.marmousez@centrale-marseille.fr", "valery.marmousez@gmail.com", "Rappel", "2ème rappel");
	}
	
	public void endSubmission(){
		System.out.println("endSubmission");
		submissionAvailable = false;
		validating = true;
		populateValidation();
	}
	
	public void endValidation(){
		System.out.println("endValidation");
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

	public boolean isFirstMailActivated() {
		return firstMailActivated;
	}

	public void setFirstMailActivated(boolean firstMailActivated) {
		this.firstMailActivated = firstMailActivated;
	}

	public boolean isSecondMailActivated() {
		return secondMailActivated;
	}

	public void setSecondMailActivated(boolean secondMailActivated) {
		this.secondMailActivated = secondMailActivated;
	}
	
}
