package fr.affectation.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.SimpleSpecializationWithNumber;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.configuration.ConfigurationService;
import fr.affectation.service.documents.DocumentService;
import fr.affectation.service.export.ExportService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.statistics.StatisticsService;
import fr.affectation.service.student.StudentService;
import fr.affectation.service.validation.ValidationService;

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

	@Inject
	private StatisticsService statisticsService;

	@Inject
	private ConfigurationService configurationService;

	@Inject
	private ValidationService validationService;
	
	@Inject
	private ExportService exportService;

	@RequestMapping(value = "/files/lettre_parcours_{login}", method = RequestMethod.GET)
	public void getLetterIc(@PathVariable("login") String login, HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/lettres/parcours/lettre_parcours_" + login;
		realPath += ".pdf";
		try {
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			is.close();
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}

	@RequestMapping(value = "/files/lettre_filiere_{login}", method = RequestMethod.GET)
	public void getLetterJs(@PathVariable("login") String login, HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/lettres/filieres/lettre_filiere_" + login;
		realPath += ".pdf";
		try {
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			is.close();
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}
	
	@RequestMapping(value = "/files/cv_{login}", method = RequestMethod.GET)
	public void getResume(@PathVariable("login") String login, HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/cv/cv_" + login;
		realPath += ".pdf";
		try {
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			is.close();
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}

	@RequestMapping(value = "/files/results/resultats-pdf", method = RequestMethod.GET)
	public void getResultsPdf(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/temp/resultats_affectation.pdf";
		try {
			exportService.generatePdfResults(path);
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
			File file = new File(path + "WEB-INF/resources/temp/resultats_affectation.pdf");
			file.delete();
			file = new File(path + "WEB-INF/resources/temp/pieChartPa.png");
			file.delete();
			file = new File(path + "WEB-INF/resources/temp/pieChartFm.png");
			file.delete();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}

	@RequestMapping(value = "/files/results/resultats-xls-eleves", method = RequestMethod.GET)
	public void getResultsXlsByStudents(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/temp/resultats_affectation.xls";

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Résultats");

		List<Short> colorIc = Arrays.asList(HSSFColor.ORANGE.index, HSSFColor.TURQUOISE.index, HSSFColor.SEA_GREEN.index, HSSFColor.ROSE.index,
				HSSFColor.TEAL.index, HSSFColor.GOLD.index, HSSFColor.AQUA.index, HSSFColor.OLIVE_GREEN.index, HSSFColor.RED.index, HSSFColor.ROYAL_BLUE.index,
				HSSFColor.GREY_50_PERCENT.index, HSSFColor.OLIVE_GREEN.index, HSSFColor.INDIGO.index);
		List<Short> colorJs = Arrays.asList(HSSFColor.LAVENDER.index, HSSFColor.BROWN.index, HSSFColor.CORNFLOWER_BLUE.index, HSSFColor.ORCHID.index,
				HSSFColor.LIME.index, HSSFColor.CORAL.index);

		List<String> allJs = specializationService.findJobSectorAbbreviations();
		List<String> allIc = specializationService.findImprovementCourseAbbreviations();

		Map<String, HSSFCellStyle> mapColorJs = new HashMap<String, HSSFCellStyle>();
		Map<String, HSSFCellStyle> mapColorIc = new HashMap<String, HSSFCellStyle>();
		
		boolean validationAvailable = configurationService.isValidationForAdminAvailable();

		int indexColorJs = 0;
		for (String abbreviation : allJs) {
			HSSFCellStyle s = workbook.createCellStyle();
			HSSFFont fontForSpec = workbook.createFont();
			fontForSpec.setColor(colorJs.get(indexColorJs));
			s.setFont(fontForSpec);
			indexColorJs += 1;
			if (indexColorJs > colorJs.size() - 1) {
				indexColorJs = 0;
			}
			mapColorJs.put(abbreviation, s);
		}

		int indexColorIc = 0;
		for (String abbreviation : allIc) {
			HSSFCellStyle s = workbook.createCellStyle();
			HSSFFont fontForSpec = workbook.createFont();
			fontForSpec.setColor(colorIc.get(indexColorIc));
			s.setFont(fontForSpec);
			indexColorIc += 1;
			if (indexColorIc > colorIc.size() - 1) {
				indexColorIc = 0;
			}
			mapColorIc.put(abbreviation, s);
		}

		List<JobSectorChoice> allJsChoices = choiceService.findJobSectorChoices();
		List<ImprovementCourseChoice> allIcChoices = choiceService.findImprovementCourseChoices();

		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		
		HSSFCellStyle redStyle = workbook.createCellStyle();
		HSSFFont fontForSpecR = workbook.createFont();
		fontForSpecR.setColor(HSSFColor.RED.index);
		redStyle.setFont(fontForSpecR);
		
		HSSFCellStyle greenStyle = workbook.createCellStyle();
		HSSFFont fontForSpecG = workbook.createFont();
		fontForSpecG.setColor(HSSFColor.GREEN.index);
		greenStyle.setFont(fontForSpecG);

		int rowNum = 0;
		HSSFRow firstRow = sheet.createRow(rowNum++);
		HSSFCell parcours = firstRow.createCell(2);
		parcours.setCellStyle(style);
		parcours.setCellValue(new HSSFRichTextString("Parcours"));
		HSSFCell filiere = firstRow.createCell(validationAvailable ? 8 : 7);
		filiere.setCellStyle(style);
		filiere.setCellValue(new HSSFRichTextString("Filières"));

		int cellNum = 0;
		HSSFRow secondRow = sheet.createRow(rowNum++);
		HSSFCell c = secondRow.createCell(cellNum++);
		c.setCellStyle(style);
		c.setCellValue(new HSSFRichTextString("Nom"));
		c = secondRow.createCell(cellNum++);
		c.setCellStyle(style);
		c.setCellValue(new HSSFRichTextString("Login"));
		for (int i = 1; i < 3; i++) {
			for (int j = 1; j < 6; j++) {
				c = secondRow.createCell(cellNum++);
				c.setCellStyle(style);
				c.setCellValue(new HSSFRichTextString("" + j));
			}
			if (validationAvailable){
				c = secondRow.createCell(cellNum++);
				c.setCellStyle(style);
				c.setCellValue(new HSSFRichTextString("Validation"));
			}
		}

		for (SimpleStudent student : studentService.findAllStudentsConcerned()) {
			HSSFRow row = sheet.createRow(rowNum++);
			cellNum = 0;
			HSSFCell cell = row.createCell(cellNum++);
			HSSFRichTextString name = new HSSFRichTextString(student.getName());
			cell.setCellValue(name);
			HSSFRichTextString login = new HSSFRichTextString(student.getLogin());
			cell = row.createCell(cellNum++);
			cell.setCellValue(login);
			int index = 19999;
			int iteration = 0;
			for (ImprovementCourseChoice choice : allIcChoices) {
				if (choice.getLogin().equals(student.getLogin())) {
					index = iteration;
					break;
				}
				iteration += 1;
			}
			ImprovementCourseChoice icc;
			if (index == 19999) {
				icc = new ImprovementCourseChoice();
			} else {
				icc = allIcChoices.get(iteration);
			}

			cell = row.createCell(cellNum++);
			if (icc.getChoice1() != null) {
				cell.setCellValue(new HSSFRichTextString(icc.getChoice1()));
				cell.setCellStyle(mapColorIc.get(icc.getChoice1()));
			} else {
				cell.setCellValue(new HSSFRichTextString("---"));
			}

			cell = row.createCell(cellNum++);
			if (icc.getChoice2() != null) {
				cell.setCellValue(new HSSFRichTextString(icc.getChoice2()));
				cell.setCellStyle(mapColorIc.get(icc.getChoice2()));
			} else {
				cell.setCellValue(new HSSFRichTextString("---"));
			}

			cell = row.createCell(cellNum++);
			if (icc.getChoice3() != null) {
				cell.setCellValue(new HSSFRichTextString(icc.getChoice3()));
				cell.setCellStyle(mapColorIc.get(icc.getChoice3()));
			} else {
				cell.setCellValue(new HSSFRichTextString("---"));
			}

			cell = row.createCell(cellNum++);
			if (icc.getChoice4() != null) {
				cell.setCellValue(new HSSFRichTextString(icc.getChoice4()));
				cell.setCellStyle(mapColorIc.get(icc.getChoice4()));
			} else {
				cell.setCellValue(new HSSFRichTextString("---"));
			}

			cell = row.createCell(cellNum++);
			if (icc.getChoice5() != null) {
				cell.setCellValue(new HSSFRichTextString(icc.getChoice5()));
				cell.setCellStyle(mapColorIc.get(icc.getChoice5()));
			} else {
				cell.setCellValue(new HSSFRichTextString("---"));
			}
			
			if (validationAvailable){
				cell = row.createCell(cellNum++);
				if (validationService.isValidatedIc(student.getLogin())){
					cell.setCellValue(new HSSFRichTextString("Accepté"));
					cell.setCellStyle(greenStyle);	
				}
				else{
					cell.setCellValue(new HSSFRichTextString("Refusé"));
					cell.setCellStyle(redStyle);	
				}
			}

			index = 19999;
			iteration = 0;
			for (JobSectorChoice choice : allJsChoices) {
				if (choice.getLogin().equals(student.getLogin())) {
					index = iteration;
					break;
				}
				iteration += 1;
			}
			JobSectorChoice jsc;
			if (index == 19999) {
				jsc = new JobSectorChoice();
			} else {
				jsc = allJsChoices.get(iteration);
			}

			cell = row.createCell(cellNum++);
			if (jsc.getChoice1() != null) {
				cell.setCellValue(new HSSFRichTextString(jsc.getChoice1()));
				cell.setCellStyle(mapColorJs.get(jsc.getChoice1()));
			} else {
				cell.setCellValue(new HSSFRichTextString("---"));
			}

			cell = row.createCell(cellNum++);
			if (jsc.getChoice2() != null) {
				cell.setCellValue(new HSSFRichTextString(jsc.getChoice2()));
				cell.setCellStyle(mapColorJs.get(jsc.getChoice2()));
			} else {
				cell.setCellValue(new HSSFRichTextString("---"));
			}

			cell = row.createCell(cellNum++);
			if (jsc.getChoice3() != null) {
				cell.setCellValue(new HSSFRichTextString(jsc.getChoice3()));
				cell.setCellStyle(mapColorJs.get(jsc.getChoice3()));
			} else {
				cell.setCellValue(new HSSFRichTextString("---"));
			}

			cell = row.createCell(cellNum++);
			if (jsc.getChoice4() != null) {
				cell.setCellValue(new HSSFRichTextString(jsc.getChoice4()));
				cell.setCellStyle(mapColorJs.get(jsc.getChoice4()));
			} else {
				cell.setCellValue(new HSSFRichTextString("---"));
			}

			cell = row.createCell(cellNum++);
			if (jsc.getChoice5() != null) {
				cell.setCellValue(new HSSFRichTextString(jsc.getChoice5()));
				cell.setCellStyle(mapColorJs.get(jsc.getChoice5()));
			} else {
				cell.setCellValue(new HSSFRichTextString("---"));
			}
			
			if (validationAvailable){
				cell = row.createCell(cellNum++);
				if (validationService.isValidatedJs(student.getLogin())){
					cell.setCellValue(new HSSFRichTextString("Accepté"));
					cell.setCellStyle(greenStyle);	
				}
				else{
					cell.setCellValue(new HSSFRichTextString("Refusé"));
					cell.setCellStyle(redStyle);	
				}
			}
		}

		List<SimpleSpecializationWithNumber> ic1 = studentService.findSimpleIcStats(1);
		List<SimpleSpecializationWithNumber> ic2 = studentService.findSimpleIcStats(2);
		List<SimpleSpecializationWithNumber> ic3 = studentService.findSimpleIcStats(3);
		List<SimpleSpecializationWithNumber> ic4 = studentService.findSimpleIcStats(4);
		List<SimpleSpecializationWithNumber> ic5 = studentService.findSimpleIcStats(5);

		rowNum += 2;
		for (ImprovementCourse ic : specializationService.findImprovementCourses()) {
			HSSFRow row = sheet.createRow(rowNum++);
			cellNum = 0;
			HSSFCell cell = row.createCell(cellNum++);
			cell.setCellValue(new HSSFRichTextString(ic.getAbbreviation()));
			cell.setCellStyle(mapColorIc.get(ic.getAbbreviation()));
			cell = row.createCell(cellNum++);
			cell.setCellValue(new HSSFRichTextString(ic.getName()));
			cell.setCellStyle(mapColorIc.get(ic.getAbbreviation()));
			row = sheet.createRow(rowNum++);
			cellNum = 0;
			cell = row.createCell(cellNum++);
			cell = row.createCell(cellNum++);
			for (SimpleSpecializationWithNumber sswn : ic1) {
				if (sswn.getAbbreviation().equals(ic.getAbbreviation())) {
					cell = row.createCell(cellNum++);
					cell.setCellValue(sswn.getNumber());
					break;
				}
			}
			for (SimpleSpecializationWithNumber sswn : ic2) {
				if (sswn.getAbbreviation().equals(ic.getAbbreviation())) {
					cell = row.createCell(cellNum++);
					cell.setCellValue(sswn.getNumber());
					break;
				}
			}
			for (SimpleSpecializationWithNumber sswn : ic3) {
				if (sswn.getAbbreviation().equals(ic.getAbbreviation())) {
					cell = row.createCell(cellNum++);
					cell.setCellValue(sswn.getNumber());
					break;
				}
			}
			for (SimpleSpecializationWithNumber sswn : ic4) {
				if (sswn.getAbbreviation().equals(ic.getAbbreviation())) {
					cell = row.createCell(cellNum++);
					cell.setCellValue(sswn.getNumber());
					break;
				}
			}
			for (SimpleSpecializationWithNumber sswn : ic5) {
				if (sswn.getAbbreviation().equals(ic.getAbbreviation())) {
					cell = row.createCell(cellNum++);
					cell.setCellValue(sswn.getNumber());
					break;
				}
			}
		}

		List<SimpleSpecializationWithNumber> js1 = studentService.findSimpleJsStats(1);
		List<SimpleSpecializationWithNumber> js2 = studentService.findSimpleJsStats(2);
		List<SimpleSpecializationWithNumber> js3 = studentService.findSimpleJsStats(3);
		List<SimpleSpecializationWithNumber> js4 = studentService.findSimpleJsStats(4);
		List<SimpleSpecializationWithNumber> js5 = studentService.findSimpleJsStats(5);

		rowNum += 1;
		for (JobSector js : specializationService.findJobSectors()) {
			HSSFRow row = sheet.createRow(rowNum++);
			cellNum = 0;
			HSSFCell cell = row.createCell(cellNum++);
			cell.setCellValue(new HSSFRichTextString(js.getAbbreviation()));
			cell.setCellStyle(mapColorJs.get(js.getAbbreviation()));
			cell = row.createCell(cellNum++);
			cell.setCellValue(new HSSFRichTextString(js.getName()));
			cell.setCellStyle(mapColorJs.get(js.getAbbreviation()));
			row = sheet.createRow(rowNum++);
			cellNum = validationAvailable ? 6 : 5;
			cell = row.createCell(cellNum++);
			cell = row.createCell(cellNum++);
			for (SimpleSpecializationWithNumber sswn : js1) {
				if (sswn.getAbbreviation().equals(js.getAbbreviation())) {
					cell = row.createCell(cellNum++);
					cell.setCellValue(sswn.getNumber());
					break;
				}
			}
			for (SimpleSpecializationWithNumber sswn : js2) {
				if (sswn.getAbbreviation().equals(js.getAbbreviation())) {
					cell = row.createCell(cellNum++);
					cell.setCellValue(sswn.getNumber());
					break;
				}
			}
			for (SimpleSpecializationWithNumber sswn : js3) {
				if (sswn.getAbbreviation().equals(js.getAbbreviation())) {
					cell = row.createCell(cellNum++);
					cell.setCellValue(sswn.getNumber());
					break;
				}
			}
			for (SimpleSpecializationWithNumber sswn : js4) {
				if (sswn.getAbbreviation().equals(js.getAbbreviation())) {
					cell = row.createCell(cellNum++);
					cell.setCellValue(sswn.getNumber());
					break;
				}
			}
			for (SimpleSpecializationWithNumber sswn : js5) {
				if (sswn.getAbbreviation().equals(js.getAbbreviation())) {
					cell = row.createCell(cellNum++);
					cell.setCellValue(sswn.getNumber());
					break;
				}
			}

		}

		try {
			FileOutputStream out = new FileOutputStream(new File(realPath));
			workbook.write(out);
			out.close();
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
			File file = new File(realPath);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/files/results/resultats-xls-spec", method = RequestMethod.GET)
	public void getResultsXlsBySpecialization(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/temp/resultats_affectation_spec.xls";
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Résultats");
		
		List<JobSector> allJs = specializationService.findJobSectors();
		List<ImprovementCourse> allIc = specializationService.findImprovementCourses();
		
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle boldStyle = workbook.createCellStyle();
		boldStyle.setFont(font);
		
		HSSFCellStyle redStyle = workbook.createCellStyle();
		HSSFFont fontForSpecR = workbook.createFont();
		fontForSpecR.setColor(HSSFColor.RED.index);
		redStyle.setFont(fontForSpecR);
		
		HSSFCellStyle greenStyle = workbook.createCellStyle();
		HSSFFont fontForSpecG = workbook.createFont();
		fontForSpecG.setColor(HSSFColor.GREEN.index);
		greenStyle.setFont(fontForSpecG);
		
		boolean validationAvailable = configurationService.isValidationForAdminAvailable();
		
		int rowNum = 0;
		int cellNum;
		HSSFRow row;
		HSSFCell cell;
		
		for (ImprovementCourse spec : allIc){
			row = sheet.createRow(rowNum++);
			cellNum = 0;
			cell = row.createCell(cellNum++);
			HSSFRichTextString stringForForm = new HSSFRichTextString(spec.getStringForForm());
			cell.setCellValue(stringForForm);
			cell.setCellStyle(boldStyle);
			List<SimpleStudent> students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, spec);
			for (SimpleStudent student : students){
				row = sheet.createRow(rowNum++);
				cellNum = 0;
				cell = row.createCell(cellNum++);
				cell.setCellValue(new HSSFRichTextString(student.getName()));
				ImprovementCourseChoice choice = choiceService.findIcChoicesByLogin(student.getLogin());
				cell = row.createCell(cellNum++);
				cell.setCellValue(choice.getChoice2() != null ? new HSSFRichTextString(choice.getChoice2()) : new HSSFRichTextString(""));
				cell = row.createCell(cellNum++);
				cell.setCellValue(choice.getChoice3() != null ? new HSSFRichTextString(choice.getChoice3()) : new HSSFRichTextString(""));
				cell = row.createCell(cellNum++);
				cell.setCellValue(choice.getChoice4() != null ? new HSSFRichTextString(choice.getChoice4()) : new HSSFRichTextString(""));
				cell = row.createCell(cellNum++);
				cell.setCellValue(choice.getChoice5() != null ? new HSSFRichTextString(choice.getChoice5()) : new HSSFRichTextString(""));
				if (validationAvailable){
					cell = row.createCell(cellNum++);
					boolean validation = validationService.isValidatedIc(student.getLogin());
					if (validation){
						cell.setCellValue(new HSSFRichTextString("Accepté"));
						cell.setCellStyle(greenStyle);
					}
					else{
						cell.setCellValue(new HSSFRichTextString("Refusé"));
						cell.setCellStyle(redStyle);
					}
				}
			}
			rowNum +=1;
		}
		
		row = sheet.createRow(rowNum++);
		cellNum = 0;
		cell = row.createCell(cellNum++);
		cell.setCellValue(new HSSFRichTextString("---------------------------------------------------"));
		rowNum++;
		
		for (JobSector spec : allJs){
			row = sheet.createRow(rowNum++);
			cellNum = 0;
			cell = row.createCell(cellNum++);
			HSSFRichTextString stringForForm = new HSSFRichTextString(spec.getStringForForm());
			cell.setCellValue(stringForForm);
			cell.setCellStyle(boldStyle);
			List<SimpleStudent> students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, spec);
			for (SimpleStudent student : students){
				row = sheet.createRow(rowNum++);
				cellNum = 0;
				cell = row.createCell(cellNum++);
				cell.setCellValue(new HSSFRichTextString(student.getName()));
				JobSectorChoice choice = choiceService.findJsChoicesByLogin(student.getLogin());
				cell = row.createCell(cellNum++);
				cell.setCellValue(choice.getChoice2() != null ? new HSSFRichTextString(choice.getChoice2()) : new HSSFRichTextString(""));
				cell = row.createCell(cellNum++);
				cell.setCellValue(choice.getChoice3() != null ? new HSSFRichTextString(choice.getChoice3()) : new HSSFRichTextString(""));
				cell = row.createCell(cellNum++);
				cell.setCellValue(choice.getChoice4() != null ? new HSSFRichTextString(choice.getChoice4()) : new HSSFRichTextString(""));
				cell = row.createCell(cellNum++);
				cell.setCellValue(choice.getChoice5() != null ? new HSSFRichTextString(choice.getChoice5()) : new HSSFRichTextString(""));
				if (validationAvailable){
					cell = row.createCell(cellNum++);
					boolean validation = validationService.isValidatedJs(student.getLogin());
					if (validation){
						cell.setCellValue(new HSSFRichTextString("Accepté"));
						cell.setCellStyle(greenStyle);
					}
					else{
						cell.setCellValue(new HSSFRichTextString("Refusé"));
						cell.setCellStyle(redStyle);
					}
				}
			}
			rowNum +=1;
		}
		
		try {
			FileOutputStream out = new FileOutputStream(new File(realPath));
			workbook.write(out);
			out.close();
			InputStream is = new FileInputStream(realPath);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
			File file = new File(realPath);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/files/admin/documents-eleves-affectation", method = RequestMethod.GET)
	public void getFullResults(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "temp/full-results.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			List<String> logins = studentService.findAllStudentsConcernedLogin();
			for (ImprovementCourseChoice icc : choiceService.findImprovementCourseChoices()) {
				String choice1 = icc.getChoice1();
				String login = icc.getLogin();
				if ((choice1 != null) && (logins.contains(login))) {
					if (documentService.hasFilledResume(path, login)) {
						addToZipFile(realPath + "cv/cv_" + login + ".pdf", zos, "Parcours/" + choice1 + "/" + login + "/cv_" + login + ".pdf");
					}
					if (documentService.hasFilledLetterIc(path, login)) {
						addToZipFile(realPath + "lettres/parcours/lettre_parcours_" + login + ".pdf", zos, "Parcours/" + choice1 + "/" + login
								+ "/lettre_parcours_" + login + ".pdf");
					}
				}
			}

			for (JobSectorChoice jsc : choiceService.findJobSectorChoices()) {
				String choice1 = jsc.getChoice1();
				String login = jsc.getLogin();
				if ((choice1 != null) && (logins.contains(login))) {
					if (documentService.hasFilledResume(path, login)) {
						addToZipFile(realPath + "cv/cv_" + login + ".pdf", zos, "Filieres/" + choice1 + "/" + login + "/cv_" + login + ".pdf");
					}
					if (documentService.hasFilledLetterJs(path, login)) {
						addToZipFile(realPath + "lettres/filieres/lettre_filiere_" + login + ".pdf", zos, "Filieres/" + choice1 + "/" + login
								+ "/lettre_filiere_" + login + ".pdf");
					}
				}
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "temp/full-results.zip");
			IOUtils.copy(is, response.getOutputStream());
			is.close();
			response.flushBuffer();				
			
			File file = new File(realPath + "temp/full-results.zip");
			file.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/files/admin/documents-eleves-cv", method = RequestMethod.GET)
	public void getFullResume(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "temp/full-resume.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			File resumeFolder = new File(realPath + "cv");

			for (File file : resumeFolder.listFiles()) {
				addToZipFile(file.getAbsolutePath(), zos, rmPath(file.getAbsolutePath()));
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "temp/full-resume.zip");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
			
			File file = new File(realPath + "temp/full-resume.zip");
			file.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/files/admin/documents-eleves-parcours", method = RequestMethod.GET)
	public void getFullResultsIc(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "temp/full-results-ic.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			List<String> logins = studentService.findAllStudentsConcernedLogin();
			for (ImprovementCourseChoice icc : choiceService.findImprovementCourseChoices()) {
				String choice1 = icc.getChoice1();
				String login = icc.getLogin();
				if ((choice1 != null) && (logins.contains(login))) {
					if (documentService.hasFilledResume(path, login)) {
						addToZipFile(realPath + "cv/cv_" + login + ".pdf", zos, "Parcours/" + choice1 + "/" + login + "/cv_" + login + ".pdf");
					}
					if (documentService.hasFilledLetterIc(path, login)) {
						addToZipFile(realPath + "lettres/parcours/lettre_parcours_" + login + ".pdf", zos, "Parcours/" + choice1 + "/" + login
								+ "/lettre_parcours_" + login + ".pdf");
					}
				}
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "temp/full-results-ic.zip");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
			
			File file = new File(realPath + "temp/full-results-ic.zip");
			file.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/files/admin/documents-eleves-filieres", method = RequestMethod.GET)
	public void getFullResultsJs(HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "temp/full-results-js.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);
			
			List<String> logins = studentService.findAllStudentsConcernedLogin();
			for (JobSectorChoice jsc : choiceService.findJobSectorChoices()) {
				String choice1 = jsc.getChoice1();
				String login = jsc.getLogin();
				if ((choice1 != null) && (logins.contains(login))) {
					if (documentService.hasFilledResume(path, login)) {
						addToZipFile(realPath + "cv/cv_" + login + ".pdf", zos, "Filieres/" + choice1 + "/" + login + "/cv_" + login + ".pdf");
					}
					if (documentService.hasFilledLetterJs(path, login)) {
						addToZipFile(realPath + "lettres/filieres/lettre_filiere_" + login + ".pdf", zos, "Filieres/" + choice1 + "/" + login
								+ "/lettre_filiere_" + login + ".pdf");
					}
				}
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "temp/full-results-js.zip");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
			
			File file = new File(realPath + "temp/full-results-js.zip");
			file.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/files/responsible/{type}/documents-eleves-{abbreviation}", method = RequestMethod.GET)
	public void getFullResultsSpec(@PathVariable String type, @PathVariable String abbreviation, HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "temp/full-results-responsible.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			List<SimpleStudent> students;
			if (type.equals("ic")) {
				students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1,
						specializationService.getImprovementCourseByAbbreviation(abbreviation));
			} else {
				students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, specializationService.getJobSectorByAbbreviation(abbreviation));
			}

			for (SimpleStudent student : students) {
				String login = student.getLogin();
				if (documentService.hasFilledResume(path, login)) {
					addToZipFile(realPath + "cv/cv_" + login + ".pdf", zos, abbreviation + "/" + login + "/cv_" + login + ".pdf");
				}
				if (type.equals("ic")) {
					if (documentService.hasFilledLetterIc(path, login)) {
						addToZipFile(realPath + "lettres/parcours/lettre_parcours_" + login + ".pdf", zos, abbreviation + "/" + login + "/lettre_parcours_"
								+ login + ".pdf");
					}
				} else {
					if (documentService.hasFilledLetterJs(path, login)) {
						addToZipFile(realPath + "lettres/filieres/lettre_filiere_" + login + ".pdf", zos, abbreviation + "/" + login + "/lettre_filiere_"
								+ login + ".pdf");
					}
				}
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "temp/full-results-responsible.zip");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
			
			File file = new File(realPath + "temp/full-results-responsible.zip");
			file.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/files/responsible/{type}/documents-eleves-{abbreviation}-cv", method = RequestMethod.GET)
	public void getFullResultsSpecResume(@PathVariable String type, @PathVariable String abbreviation, HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "temp/results-responsible-resume.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			List<SimpleStudent> students;
			if (type.equals("ic")) {
				students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1,
						specializationService.getImprovementCourseByAbbreviation(abbreviation));
			} else {
				students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, specializationService.getJobSectorByAbbreviation(abbreviation));
			}

			for (SimpleStudent student : students) {
				String login = student.getLogin();
				if (documentService.hasFilledResume(path, login)) {
					addToZipFile(realPath + "cv/cv_" + login + ".pdf", zos, abbreviation + "/cv_" + login + ".pdf");
				}
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "temp/results-responsible-resume.zip");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
			
			File file = new File(realPath + "temp/results-responsible-resume.zip");
			file.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/files/responsible/{type}/documents-eleves-{abbreviation}-lettres", method = RequestMethod.GET)
	public void getFullResultsSpecLetters(@PathVariable String type, @PathVariable String abbreviation, HttpServletResponse response, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/");
		String realPath = path + "WEB-INF/resources/";
		try {
			FileOutputStream fos = new FileOutputStream(realPath + "temp/results-responsible-letters.zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			List<SimpleStudent> students;
			if (type.equals("ic")) {
				students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1,
						specializationService.getImprovementCourseByAbbreviation(abbreviation));
			} else {
				students = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, specializationService.getJobSectorByAbbreviation(abbreviation));
			}

			for (SimpleStudent student : students) {
				String login = student.getLogin();
				if (type.equals("ic")) {
					if (documentService.hasFilledLetterIc(path, login)) {
						addToZipFile(realPath + "lettres/parcours/lettre_parcours_" + login + ".pdf", zos, abbreviation + "/lettre_parcours_" + login
								+ ".pdf");
					}
				} else {
					if (documentService.hasFilledLetterJs(path, login)) {
						addToZipFile(realPath + "lettres/filieres/lettre_filiere_" + login + ".pdf", zos, abbreviation + "/lettre_filiere_" + login + ".pdf");
					}
				}
			}

			zos.close();
			fos.close();

			InputStream is = new FileInputStream(realPath + "temp/results-responsible-letters.zip");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
			
			File file = new File(realPath + "temp/results-responsible-letters.zip");
			file.delete();
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
