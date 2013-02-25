package fr.affectation.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.documents.DocumentService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.student.StudentService;

@Controller
public class FilesController {

	@Inject
	private ChoiceService choiceService;

	@Inject
	private StudentService studentService;

	@Inject
	private DocumentService documentService;

	@Inject
	private SpecializationService specializationService;

	@RequestMapping(value = "/files/lettre_parcours_{login}", method = RequestMethod.GET)
	public void getLetterIc(@PathVariable("file_name") String login, HttpServletResponse response, HttpServletRequest request) {
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
	public void getLetterJs(@PathVariable("file_name") String login, HttpServletResponse response, HttpServletRequest request) {
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
	public void getResume(@PathVariable("login") String login, HttpServletResponse response, HttpServletRequest request) {
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

	@RequestMapping(value = "/files/admin/documents-eleves-affectation", method = RequestMethod.GET)
	public void getFullResults(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF\\resources\\";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "other\\full-results.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (ImprovementCourseChoice icc : choiceService.findImprovementCourseChoices()) {
				String choice1 = icc.getChoice1();
				String login = icc.getLogin();
				if ((choice1 != null) && (studentService.isStudentConcerned(login))) {
					if (documentService.hasFilledResume(path, login)) {
						addToZipFile(realPath + "cv\\cv_" + login + ".pdf", zos, "Parcours\\" + choice1 + "\\" + login + "\\cv_" + login + ".pdf");
					}
					if (documentService.hasFilledLetterIc(path, login)) {
						addToZipFile(realPath + "lettres\\parcours\\lettre_parcours_" + login + ".pdf", zos, "Parcours\\" + choice1 + "\\" + login
								+ "\\lettre_parcours_" + login + ".pdf");
					}
				}
			}

			for (JobSectorChoice jsc : choiceService.findJobSectorChoices()) {
				String choice1 = jsc.getChoice1();
				String login = jsc.getLogin();
				if ((choice1 != null) && (studentService.isStudentConcerned(login))) {
					if (documentService.hasFilledResume(path, login)) {
						addToZipFile(realPath + "cv\\cv_" + login + ".pdf", zos, "Filieres\\" + choice1 + "\\" + login + "\\cv_" + login + ".pdf");
					}
					if (documentService.hasFilledLetterJs(path, login)) {
						addToZipFile(realPath + "lettres\\filieres\\lettre_filiere_" + login + ".pdf", zos, "Filieres\\" + choice1 + "\\" + login
								+ "\\lettre_filiere_" + login + ".pdf");
					}
				}
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "other/full-results.zip");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/files/admin/documents-eleves-cv", method = RequestMethod.GET)
	public void getFullResume(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF\\resources\\";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "other\\full-resume.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			File resumeFolder = new File(realPath + "cv");

			for (File file : resumeFolder.listFiles()) {
				addToZipFile(file.getAbsolutePath(), zos, rmPath(file.getAbsolutePath()));
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "other/full-resume.zip");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/files/admin/documents-eleves-parcours", method = RequestMethod.GET)
	public void getFullResultsIc(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF\\resources\\";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "other\\full-results-ic.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (ImprovementCourseChoice icc : choiceService.findImprovementCourseChoices()) {
				String choice1 = icc.getChoice1();
				String login = icc.getLogin();
				if ((choice1 != null) && (studentService.isStudentConcerned(login))) {
					if (documentService.hasFilledResume(path, login)) {
						addToZipFile(realPath + "cv\\cv_" + login + ".pdf", zos, "Parcours\\" + choice1 + "\\" + login + "\\cv_" + login + ".pdf");
					}
					if (documentService.hasFilledLetterIc(path, login)) {
						addToZipFile(realPath + "lettres\\parcours\\lettre_parcours_" + login + ".pdf", zos, "Parcours\\" + choice1 + "\\" + login
								+ "\\lettre_parcours_" + login + ".pdf");
					}
				}
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "other/full-results-ic.zip");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/files/admin/documents-eleves-filieres", method = RequestMethod.GET)
	public void getFullResultsJs(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF\\resources\\";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "other\\full-results-js.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (JobSectorChoice jsc : choiceService.findJobSectorChoices()) {
				String choice1 = jsc.getChoice1();
				String login = jsc.getLogin();
				if ((choice1 != null) && (studentService.isStudentConcerned(login))) {
					if (documentService.hasFilledResume(path, login)) {
						addToZipFile(realPath + "cv\\cv_" + login + ".pdf", zos, "Filieres\\" + choice1 + "\\" + login + "\\cv_" + login + ".pdf");
					}
					if (documentService.hasFilledLetterJs(path, login)) {
						addToZipFile(realPath + "lettres\\filieres\\lettre_filiere_" + login + ".pdf", zos, "Filieres\\" + choice1 + "\\" + login
								+ "\\lettre_filiere_" + login + ".pdf");
					}
				}
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "other/full-results-js.zip");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/files/responsible/{type}/documents-eleves-{abbreviation}", method = RequestMethod.GET)
	public void getFullResultsSpec(@PathVariable String type, @PathVariable String abbreviation, HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF\\resources\\";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "other\\full-results-responsible.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			List<SimpleStudent> students;
			if (type.equals("ic")) {
				students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, specializationService.getImprovementCourseByAbbreviation(abbreviation));
			} else {
				students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, specializationService.getJobSectorByAbbreviation(abbreviation));
			}

			for (SimpleStudent student : students) {
				String login = student.getLogin();
				if (documentService.hasFilledResume(path, login)) {
					addToZipFile(realPath + "cv\\cv_" + login + ".pdf", zos, abbreviation + "\\" + login + "\\cv_" + login + ".pdf");
				}
				if (type.equals("ic")){
					if (documentService.hasFilledLetterIc(path, login)) {
						addToZipFile(realPath + "lettres\\parcours\\lettre_parcours_" + login + ".pdf", zos, abbreviation + "\\" + login + "\\lettre_parcours_" + login + ".pdf");
					}
				}
				else{
					if (documentService.hasFilledLetterJs(path, login)) {
						addToZipFile(realPath + "lettres\\filieres\\lettre_filiere_" + login + ".pdf", zos, abbreviation + "\\" + login + "\\lettre_filiere_" + login + ".pdf");
					}
				}
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "other/full-results-responsible.zip");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/files/responsible/{type}/documents-eleves-{abbreviation}-cv", method = RequestMethod.GET)
	public void getFullResultsSpecResume(@PathVariable String type, @PathVariable String abbreviation, HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF\\resources\\";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "other\\results-responsible-resume.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			List<SimpleStudent> students;
			if (type.equals("ic")) {
				students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, specializationService.getImprovementCourseByAbbreviation(abbreviation));
			} else {
				students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, specializationService.getJobSectorByAbbreviation(abbreviation));
			}

			for (SimpleStudent student : students) {
				String login = student.getLogin();
				if (documentService.hasFilledResume(path, login)) {
					addToZipFile(realPath + "cv\\cv_" + login + ".pdf", zos, abbreviation + "\\cv_" + login + ".pdf");
				}
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "other/results-responsible-resume.zip");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/files/responsible/{type}/documents-eleves-{abbreviation}-lettres", method = RequestMethod.GET)
	public void getFullResultsSpecLetters(@PathVariable String type, @PathVariable String abbreviation, HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF\\resources\\";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "other\\results-responsible-letters.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			List<SimpleStudent> students;
			if (type.equals("ic")) {
				students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, specializationService.getImprovementCourseByAbbreviation(abbreviation));
			} else {
				students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, specializationService.getJobSectorByAbbreviation(abbreviation));
			}

			for (SimpleStudent student : students) {
				String login = student.getLogin();
				if (type.equals("ic")){
					if (documentService.hasFilledLetterIc(path, login)) {
						addToZipFile(realPath + "lettres\\parcours\\lettre_parcours_" + login + ".pdf", zos, abbreviation + "\\lettre_parcours_" + login + ".pdf");
					}
				}
				else{
					if (documentService.hasFilledLetterJs(path, login)) {
						addToZipFile(realPath + "lettres\\filieres\\lettre_filiere_" + login + ".pdf", zos, abbreviation + "\\lettre_filiere_" + login + ".pdf");
					}
				}
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "other/results-responsible-letters.zip");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addToZipFile(String fileName, ZipOutputStream zos, String path) throws FileNotFoundException, IOException {
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(path);
		zos.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}
		zos.closeEntry();
		fis.close();
	}

	static String rmPath(String fName) {
		int pos = fName.lastIndexOf(File.separatorChar);
		if (pos > -1)
			fName = fName.substring(pos + 1);
		return fName;
	}
}
