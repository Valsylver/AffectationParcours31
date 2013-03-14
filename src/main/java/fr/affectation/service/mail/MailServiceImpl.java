package fr.affectation.service.mail;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.affectation.domain.util.Mail;
import fr.affectation.domain.util.SimpleMail;

@Service
public class MailServiceImpl implements MailService {
	
	@Resource(name="from")
	private String from;
	
	@Inject
	private JavaMailSenderImpl mailSender;
	
	@Inject
	private SessionFactory sessionFactory;
	
	@Override
	public void sendMail(String from, String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);       
	}

	@Override
	@Transactional(readOnly=true)
	public Mail getFirstMail() {
		Session session = sessionFactory.getCurrentSession();
		Mail firstMail = (Mail) session.get(Mail.class, (long) 1);
		return firstMail != null ? firstMail : new Mail((long) 1, "Object", "Message");
	}

	@Override
	@Transactional(readOnly=true)
	public Mail getSecondMail() {
		Session session = sessionFactory.getCurrentSession();
		Mail secondMail = (Mail) session.get(Mail.class, (long) 2);
		return secondMail != null ? secondMail : new Mail((long) 2, "Object", "Message");
	}

	@Override
	@Transactional
	public void save(Mail mail) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(mail);
	}

	@Override
	public void sendSimpleMail(SimpleMail mail, List<String> addressees) {
		for (String login : addressees){
			sendMail(from, login + "@centrale-marseille.fr", mail.getObject(), mail.getMessage());
		}
	}

}
