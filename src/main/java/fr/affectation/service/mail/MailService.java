package fr.affectation.service.mail;

import org.springframework.stereotype.Service;

import fr.affectation.domain.util.Mail;

@Service
public interface MailService {
	
	public void sendMail(String from, String to, String subject, String body);
	
	public Mail getFirstMail();
	
	public Mail getSecondMail();
	
	public void save(Mail mail);
}
