package fr.affectation.web.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FilesForStudentController {

	@RequestMapping(value = "/filestudent/cv", method = RequestMethod.GET)
	public void getResume(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String login = auth.getName();
		String realPath = path + "WEB-INF/resources/cv/cv_" + login + ".pdf";
		try {
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}
	
	@RequestMapping(value = "/filestudent/lettre-parcours", method = RequestMethod.GET)
	public void getLetterIc(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String login = auth.getName();
		String realPath = path + "WEB-INF/resources/lettres/parcours/lettre_parcours_" + login + ".pdf";
		try {
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}
	
	@RequestMapping(value = "/filestudent/lettre-filiere", method = RequestMethod.GET)
	public void getLetterJs(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String login = auth.getName();
		String realPath = path + "WEB-INF/resources/lettres/filieres/lettre_filiere_" + login + ".pdf";
		try {
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}
}
