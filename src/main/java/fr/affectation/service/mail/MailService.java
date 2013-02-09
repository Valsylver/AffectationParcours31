package fr.affectation.service.mail;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.affectation.domain.util.Mail;
import fr.affectation.domain.util.SimpleMail;

@Service
public interface MailService {
	
	public void sendMail(String from, String to, String subject, String body);
	
	public Mail getFirstMail();
	
	public Mail getSecondMail();
	
	public void save(Mail mail);
	
	public void sendSimpleMail(SimpleMail mail, List<String> addressees);
}
