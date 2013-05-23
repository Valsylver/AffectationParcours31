package fr.affectation.service.configuration;

import java.text.ParseException;
import java.util.Date;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.affectation.domain.configuration.AlreadyLaunched;
import fr.affectation.domain.configuration.Configuration;
import fr.affectation.service.mail.MailService;
import fr.affectation.service.student.StudentService;

@Service
public class ConfigurationServiceImpl implements ConfigurationService{
	
	public static final int CONFIGURATION_ID = 42;
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Inject
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Inject
	private StudentService studentService;
	
	@Inject
	private MailService mailService;
	
	private String path;
	
	private boolean submissionAvailable = false;
	
	private boolean running = false;
	
	private boolean config = true;
	
	private boolean validating = false;
	
	private boolean firstMailActivated = false;
	
	private boolean secondMailActivated = false;

	private When when;
	
	@Override
	@Transactional
	public boolean initialize(String path) {
		MethodInvokingJobDetailFactoryBean jobDetailFirstEmail;
		try {
			jobDetailFirstEmail = createJobDetail("sendFirstEmail", "jobDetailFirstEmail");
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
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
		running = true;
		config = false;
		submissionAvailable = true;
			
		Configuration configuration = new Configuration(when.getFirstEmail(), when.getSecondEmail(), when.getEndSubmission(), when.getEndValidation());
		saveConfiguration(configuration);
			
		studentService.populateValidation();
		this.path = path;
		
		return true;
	}
	
	@Override
	@Transactional
	public void saveConfiguration(Configuration configuration){
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(configuration);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Configuration retrieveConfiguration(){
		Session session = sessionFactory.getCurrentSession();
		return (Configuration) session.get(Configuration.class, CONFIGURATION_ID);
	}
	
	@Override
	@Transactional
	public void updateFirstMail(Date date){
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("update Configuration set firstMail = :firstMail" +
				" where id = :id");
		query.setParameter("firstMail", date);
		query.setParameter("id", CONFIGURATION_ID);
		query.executeUpdate();
	}
	
	@Override
	@Transactional
	public void updateSecondMail(Date date){
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("update Configuration set secondMail = :secondMail" +
				" where id = :id");
		query.setParameter("secondMail", date);
		query.setParameter("id", CONFIGURATION_ID);
		query.executeUpdate();
	}
	
	@Override
	@Transactional
	public void updateEndSubmission(Date date){
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("update Configuration set endSubmission = :endSubmission" +
				" where id = :id");
		query.setParameter("endSubmission", date);
		query.setParameter("id", CONFIGURATION_ID);
		query.executeUpdate();
	}
	
	@Override
	@Transactional
	public void updateEndValidation(Date date){
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("update Configuration set endValidation = :endValidation" +
				" where id = :id");
		query.setParameter("endValidation", date);
		query.setParameter("id", CONFIGURATION_ID);
		query.executeUpdate();
	}
	
	@Override
	@Transactional
	public void deleteConfiguration(){
		Session session = sessionFactory.getCurrentSession();
		Configuration configuration = retrieveConfiguration();
		if (configuration != null){
			session.delete(configuration);
		}
	}
	
	@Override
	@Transactional
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
		
		deleteConfiguration();
	}
	
	@Override
	@Transactional(readOnly = true)
	public void initializeFromDataBase(){
		Configuration configuration = retrieveConfiguration();
		if (configuration != null){
			Date currentDate = new Date();
			Date firstMail = configuration.getFirstMail();
			Date secondMail = configuration.getSecondMail();
			Date endSubmission = configuration.getEndSubmission();
			Date endValidation = configuration.getEndValidation();
			try{
				Scheduler scheduler = schedulerFactoryBean.getScheduler();
				scheduler.deleteJob("jobDetailFirstEmail", "affectation");
				scheduler.deleteJob("jobDetailSecondEmail", "affectation");
				scheduler.deleteJob("jobDetailEndSubmission", "affectation");
				scheduler.deleteJob("jobDetailEndValidation", "affectation");
				when = new When();
				when.setFirstEmail(firstMail);
				when.setSecondEmail(secondMail);
				when.setEndSubmission(endSubmission);
				when.setEndValidation(endValidation);
				if (currentDate.before(firstMail)){
					MethodInvokingJobDetailFactoryBean jobDetailFirstEmail = createJobDetail("sendFirstEmail", "jobDetailFirstEmail");
					MethodInvokingJobDetailFactoryBean jobDetailSecondEmail = createJobDetail("sendSecondEmail", "jobDetailSecondEmail");
					MethodInvokingJobDetailFactoryBean jobDetailEndSubmission = createJobDetail("endSubmission", "jobDetailEndSubmission");
					MethodInvokingJobDetailFactoryBean jobDetailEndValidation = createJobDetail("endValidation", "jobDetailEndValidation");
					
					SimpleTriggerBean triggerFirstEmail = createTrigger("triggerFirstEmail", firstMail, jobDetailFirstEmail);
					SimpleTriggerBean triggerSecondEmail = createTrigger("triggerSecondEmail", secondMail, jobDetailSecondEmail);
					SimpleTriggerBean triggerEndSubmission = createTrigger("triggerEndSubmission", endSubmission, jobDetailEndSubmission);
					SimpleTriggerBean triggerEndValidation = createTrigger("triggerEndValidation", endValidation, jobDetailEndValidation);
					
					scheduler.scheduleJob((JobDetail) jobDetailFirstEmail.getObject(), triggerFirstEmail);
					scheduler.scheduleJob((JobDetail) jobDetailSecondEmail.getObject(), triggerSecondEmail);
					scheduler.scheduleJob((JobDetail) jobDetailEndSubmission.getObject(), triggerEndSubmission);
					scheduler.scheduleJob((JobDetail) jobDetailEndValidation.getObject(), triggerEndValidation);
					
					running = true;
					config = false;
					submissionAvailable = true;
				}
				else{
					if (currentDate.before(secondMail)){
						MethodInvokingJobDetailFactoryBean jobDetailSecondEmail = createJobDetail("sendSecondEmail", "jobDetailSecondEmail");
						MethodInvokingJobDetailFactoryBean jobDetailEndSubmission = createJobDetail("endSubmission", "jobDetailEndSubmission");
						MethodInvokingJobDetailFactoryBean jobDetailEndValidation = createJobDetail("endValidation", "jobDetailEndValidation");
						
						SimpleTriggerBean triggerSecondEmail = createTrigger("triggerSecondEmail", secondMail, jobDetailSecondEmail);
						SimpleTriggerBean triggerEndSubmission = createTrigger("triggerEndSubmission", endSubmission, jobDetailEndSubmission);
						SimpleTriggerBean triggerEndValidation = createTrigger("triggerEndValidation", endValidation, jobDetailEndValidation);
						
						scheduler.scheduleJob((JobDetail) jobDetailSecondEmail.getObject(), triggerSecondEmail);
						scheduler.scheduleJob((JobDetail) jobDetailEndSubmission.getObject(), triggerEndSubmission);
						scheduler.scheduleJob((JobDetail) jobDetailEndValidation.getObject(), triggerEndValidation);
						
						running = true;
						config = false;
						submissionAvailable = true;
					}
					else{
						if (currentDate.before(endSubmission)){
							MethodInvokingJobDetailFactoryBean jobDetailEndSubmission = createJobDetail("endSubmission", "jobDetailEndSubmission");
							MethodInvokingJobDetailFactoryBean jobDetailEndValidation = createJobDetail("endValidation", "jobDetailEndValidation");
							
							SimpleTriggerBean triggerEndSubmission = createTrigger("triggerEndSubmission", endSubmission, jobDetailEndSubmission);
							SimpleTriggerBean triggerEndValidation = createTrigger("triggerEndValidation", endValidation, jobDetailEndValidation);
							
							scheduler.scheduleJob((JobDetail) jobDetailEndSubmission.getObject(), triggerEndSubmission);
							scheduler.scheduleJob((JobDetail) jobDetailEndValidation.getObject(), triggerEndValidation);
							
							running = true;
							config = false;
							submissionAvailable = true;
						}
						else{
							if (currentDate.before(endValidation)){
								MethodInvokingJobDetailFactoryBean jobDetailEndValidation = createJobDetail("endValidation", "jobDetailEndValidation");
								
								SimpleTriggerBean triggerEndValidation = createTrigger("triggerEndValidation", endValidation, jobDetailEndValidation);
								
								scheduler.scheduleJob((JobDetail) jobDetailEndValidation.getObject(), triggerEndValidation);
								
								running = true;
								config = false;
								submissionAvailable = false;
								validating = true;
							}
							else{
								running = true;
								config = false;
								submissionAvailable = false;
								validating = false;
							}
						}
					}
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	@Transactional
	public void updateWhen(When whenToModify) throws SchedulerException, ClassNotFoundException, NoSuchMethodException, ParseException{
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		if (whenToModify.getNumber() == 1){
			if (when.getEndValidation().compareTo(whenToModify.getEndValidation()) != 0){
				scheduler.deleteJob("jobDetailEndValidation", "affectation");
				MethodInvokingJobDetailFactoryBean jobDetailEndValidation = createJobDetail("endValidation", "jobDetailEndValidation");
				SimpleTriggerBean triggerEndValidation = createTrigger("triggerEndValidation", whenToModify.getEndValidation(), jobDetailEndValidation);
				scheduler.scheduleJob((JobDetail) jobDetailEndValidation.getObject(), triggerEndValidation);
				when.setEndValidation(whenToModify.getEndValidation());
				updateEndValidation(whenToModify.getEndValidation());
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
					updateEndSubmission(whenToModify.getEndSubmission());
				}
				if (when.getEndValidation().compareTo(whenToModify.getEndValidation()) != 0){
					scheduler.deleteJob("jobDetailEndValidation", "affectation");
					MethodInvokingJobDetailFactoryBean jobDetailEndValidation = createJobDetail("endValidation", "jobDetailEndValidation");
					SimpleTriggerBean triggerEndValidation = createTrigger("triggerEndValidation", whenToModify.getEndValidation(), jobDetailEndValidation);
					scheduler.scheduleJob((JobDetail) jobDetailEndValidation.getObject(), triggerEndValidation);
					when.setEndValidation(whenToModify.getEndValidation());
					updateEndValidation(whenToModify.getEndValidation());
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
						updateSecondMail(whenToModify.getSecondEmail());
					}
					if (when.getEndSubmission().compareTo(whenToModify.getEndSubmission()) != 0){
						scheduler.deleteJob("jobDetailEndSubmission", "affectation");
						MethodInvokingJobDetailFactoryBean jobDetailEndSubmission = createJobDetail("endSubmission", "jobDetailEndSubmission");
						SimpleTriggerBean triggerEndSubmission = createTrigger("triggerEndSubmission", whenToModify.getEndSubmission(), jobDetailEndSubmission);
						scheduler.scheduleJob((JobDetail) jobDetailEndSubmission.getObject(), triggerEndSubmission);
						when.setEndSubmission(whenToModify.getEndSubmission());
						updateEndSubmission(whenToModify.getEndSubmission());
					}
					if (when.getEndValidation().compareTo(whenToModify.getEndValidation()) != 0){
						scheduler.deleteJob("jobDetailEndValidation", "affectation");
						MethodInvokingJobDetailFactoryBean jobDetailEndValidation = createJobDetail("endValidation", "jobDetailEndValidation");
						SimpleTriggerBean triggerEndValidation = createTrigger("triggerEndValidation", whenToModify.getEndValidation(), jobDetailEndValidation);
						scheduler.scheduleJob((JobDetail) jobDetailEndValidation.getObject(), triggerEndValidation);
						when.setEndValidation(whenToModify.getEndValidation());
						updateEndValidation(whenToModify.getEndValidation());
					}
				}
				else{
					if (when.getFirstEmail().compareTo(whenToModify.getFirstEmail()) != 0){
						scheduler.deleteJob("jobDetailFirstEmail", "affectation");
						MethodInvokingJobDetailFactoryBean jobDetailFirstEmail = createJobDetail("sendFirstEmail", "jobDetailFirstEmail");
						SimpleTriggerBean triggerFirstEmail = createTrigger("triggerFirstEmail", whenToModify.getFirstEmail(), jobDetailFirstEmail);
						scheduler.scheduleJob((JobDetail) jobDetailFirstEmail.getObject(), triggerFirstEmail);
						when.setFirstEmail(whenToModify.getFirstEmail());
						updateFirstMail(whenToModify.getSecondEmail());
					}
					if (when.getSecondEmail().compareTo(whenToModify.getSecondEmail()) != 0){
						scheduler.deleteJob("jobDetailSecondEmail", "affectation");
						MethodInvokingJobDetailFactoryBean jobDetailSecondEmail = createJobDetail("sendSecondEmail", "jobDetailSecondEmail");
						SimpleTriggerBean triggerSecondEmail = createTrigger("triggerSecondEmail", whenToModify.getSecondEmail(), jobDetailSecondEmail);
						scheduler.scheduleJob((JobDetail) jobDetailSecondEmail.getObject(), triggerSecondEmail);
						when.setSecondEmail(whenToModify.getSecondEmail());
						updateSecondMail(whenToModify.getSecondEmail());
					}
					if (when.getEndSubmission().compareTo(whenToModify.getEndSubmission()) != 0){
						scheduler.deleteJob("jobDetailEndSubmission", "affectation");
						MethodInvokingJobDetailFactoryBean jobDetailEndSubmission = createJobDetail("endSubmission", "jobDetailEndSubmission");
						SimpleTriggerBean triggerEndSubmission = createTrigger("triggerEndSubmission", whenToModify.getEndSubmission(), jobDetailEndSubmission);
						scheduler.scheduleJob((JobDetail) jobDetailEndSubmission.getObject(), triggerEndSubmission);
						when.setEndSubmission(whenToModify.getEndSubmission());
						updateEndSubmission(whenToModify.getEndSubmission());
					}
					if (when.getEndValidation().compareTo(whenToModify.getEndValidation()) != 0){
						scheduler.deleteJob("jobDetailEndValidation", "affectation");
						MethodInvokingJobDetailFactoryBean jobDetailEndValidation = createJobDetail("endValidation", "jobDetailEndValidation");
						SimpleTriggerBean triggerEndValidation = createTrigger("triggerEndValidation", whenToModify.getEndValidation(), jobDetailEndValidation);
						scheduler.scheduleJob((JobDetail) jobDetailEndValidation.getObject(), triggerEndValidation);
						when.setEndValidation(whenToModify.getEndValidation());
						updateEndValidation(whenToModify.getEndValidation());
					}
				}
			}
		}
	}
	
	@Override
	public void sendFirstEmail(){
		if (firstMailActivated){			
			studentService.sendSimpleMail(mailService.getFirstMail().toSimpleMail(), path);
		}
	}
	
	@Override
	public void sendSecondEmail(){
		if (secondMailActivated){			
			studentService.sendSimpleMail(mailService.getSecondMail().toSimpleMail(), path);
		}
	}
	
	@Override
	public void endSubmission(){
		submissionAvailable = false;
		validating = true;
	}
	
	@Override
	public void endValidation(){
		validating = false;
	}
	
	@Override
	public void populateValidation(){
		studentService.populateValidation();
	}
	
	@Override
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
	
	@Override
	public boolean isSubmissionAvailable() {
		return submissionAvailable;
	}
	
	@Override
	public When getWhen() {
		return when;
	}

	@Override
	public boolean isRunning() {
		return running;
	}
	
	@Override
	public boolean isValidating() {
		return validating;
	}

	@Override
	public void setRun(boolean run) {
		this.running = run;
	}

	@Override
	public boolean isConfig() {
		return config;
	}

	@Override
	public void setConfig(boolean config) {
		this.config = config;
	}
	
	@Override
	public void setWhen(When when){
		this.when = when;
	}

	@Override
	public boolean isFirstMailActivated() {
		return firstMailActivated;
	}

	
	@Override
	public void setFirstMailActivated(boolean firstMailActivated) {
		this.firstMailActivated = firstMailActivated;
	}

	@Override
	public boolean isSecondMailActivated() {
		return secondMailActivated;
	}

	@Override
	public void setSecondMailActivated(boolean secondMailActivated) {
		this.secondMailActivated = secondMailActivated;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasAlreadyBeenLaunched() {
		Session session = sessionFactory.getCurrentSession();
		AlreadyLaunched al = (AlreadyLaunched) session.get(AlreadyLaunched.class, CONFIGURATION_ID);
		return al != null;
	}
	
	@Override
	@Transactional
	public void setAlreadyBeenLaunched() {
		Session session = sessionFactory.getCurrentSession();
		AlreadyLaunched al = new AlreadyLaunched();
		session.saveOrUpdate(al);
	}
}
