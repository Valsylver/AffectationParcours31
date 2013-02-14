package fr.affectation.web.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FilesController {

	@RequestMapping(value = "/files/lettre_parcours_{login}", method = RequestMethod.GET)
	public void getLetterIc(@PathVariable("file_name") String login,
			HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/" + "/lettres/parcours/lettre_parcours_" + login;
		realPath += ".pdf";
		try {
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}
	
	@RequestMapping(value = "/files/lettre_filiere_{login}", method = RequestMethod.GET)
	public void getLetterJs(@PathVariable("file_name") String login,
			HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/" + "/lettres/filieres/lettre_filiere_" + login;
		realPath += ".pdf";
		try {
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}
	
	@RequestMapping(value = "/files/results/index", method = RequestMethod.GET)
	public void getResults(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/pdf/resultats_affectation.pdf";
		try {
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}
	
	@RequestMapping(value = "/files/cv_{login}", method = RequestMethod.GET)
	public void getResume(@PathVariable("login") String login,
			HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/" + "cv/cv_" + login;
		realPath += ".pdf";
		try {
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}

	}
}
