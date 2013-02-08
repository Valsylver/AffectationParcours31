package fr.affectation.service.mail;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.affectation.domain.util.Mail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MailServiceTest {
	
	@Inject
	private MailService mailService;
	
	@Test
	public void saveFirstMail(){
		String object = "First mail object";
		String message = "Bonjour, premier message, avec accents : éà";
		Mail mail = new Mail((long) 1, object, message);
		mailService.save(mail);
		Assert.assertTrue(mailService.getFirstMail().getMessage().equals(message));
		Assert.assertTrue(mailService.getFirstMail().getObject().equals(object));
	}
	
	@Test
	public void saveSecondMail(){
		String object = "Second mail object";
		String message = "Bonjour, deuxième message, avec accents : éà";
		Mail mail = new Mail((long) 2, object, message);
		mailService.save(mail);
		Assert.assertTrue(mailService.getSecondMail().getMessage().equals(message));
		Assert.assertTrue(mailService.getSecondMail().getObject().equals(object));
	}

}
