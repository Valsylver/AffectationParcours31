package fr.affectation.service.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.SimpleSpecialization;
import fr.affectation.domain.student.SimpleStudent;
import fr.affectation.domain.student.SimpleStudentWithValidation;
import fr.affectation.service.configuration.ConfigurationService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.statistics.StatisticsService;
import fr.affectation.service.student.StudentService;

@Service
public class ExportServiceImpl implements ExportService {

	@Inject
	private StatisticsService statisticsService;
	
	@Inject
	private StudentService studentService;
	
	@Inject
	private ConfigurationService configurationService;
	
	@Inject
	private SpecializationService specializationService;

	@Override
	public void generatePdfResults(String path) {
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(path + "/pdf/resultats_affectation.pdf"));
			document.open();
			document.add(new Paragraph("Affectation parcours/filières 3ème année Centrale Marseille", FontFactory.getFont(FontFactory.COURIER, 20, Font.BOLD,
					new CMYKColor(0, 255, 0, 0))));

			document.newPage();
			Paragraph title1 = new Paragraph("Statistiques", FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC, new CMYKColor(0, 255, 255, 17)));

			Chapter chapter1 = new Chapter(title1, 1);
			Paragraph title11 = new Paragraph("Parcours d'approfondissement", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new CMYKColor(0, 255,
					255, 17)));
			Section section1 = chapter1.addSection(title11);
			section1.add(createTable("ic"));
			statisticsService.generateBarChartIc(path);
			statisticsService.generatePieChartIc(path);
			Image pieChartIc = Image.getInstance(path + "/img/jspchart/piechartPa.png");
			Image barChartIc = Image.getInstance(path + "/img/jspchart/barchartPa.png");
			section1.add(pieChartIc);
			section1.add(barChartIc);

			Paragraph title12 = new Paragraph("Filières métier", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new CMYKColor(0, 255, 255, 17)));
			Section section2 = chapter1.addSection(title12);
			section2.add(createTable("js"));
			statisticsService.generateBarChartIc(path);
			statisticsService.generatePieChartIc(path);
			Image pieChartJs = Image.getInstance(path + "/img/jspchart/piechartFm.png");
			Image barChartJs = Image.getInstance(path + "/img/jspchart/barchartFm.png");
			section2.add(pieChartJs);
			section2.add(barChartJs);

			document.add(chapter1);

			document.newPage();
			Paragraph title2 = new Paragraph("Détails des résultats", FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC, new CMYKColor(0, 255,
					255, 17)));
			Chapter chapter2 = new Chapter(title2, 2);
			Paragraph title21 = new Paragraph("Parcours d'approfondissement", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new CMYKColor(0, 255,
					255, 17)));
			section1 = chapter2.addSection(title21);
			
			if (!configurationService.isSubmissionAvailable()){
				List<List<SimpleStudentWithValidation>> studentsBySpec = studentService.findSimpleStudentsWithValidationForAllIcByOrder(1);
				List<ImprovementCourse> improvementCourses = specializationService.findImprovementCourses();
				int index = 0;
				for (ImprovementCourse ic : improvementCourses){
					Section icSection = section1.addSection(ic.getName() + "(" + ic.getAbbreviation() + ")");
					PdfPTable table = new PdfPTable(2);
					table.setSpacingBefore(25);
					table.setSpacingAfter(25);
					PdfPCell c1 = new PdfPCell(new Phrase("Elève"));
					table.addCell(c1);
					PdfPCell c2 = new PdfPCell(new Phrase("Validation"));
					table.addCell(c2);
					List<SimpleStudentWithValidation> students = studentsBySpec.get(index);
					for (SimpleStudentWithValidation student : students){
						table.addCell(student.getName());
						table.addCell(student.isValidated() ? "Accepté" : "Refusé");
					}
					index += 1;
					icSection.add(table);
				}
			}
			else{
				List<List<SimpleStudent>> studentsBySpec = studentService.findSimpleStudentsForAllIcByOrder(1);
				List<ImprovementCourse> improvementCourses = specializationService.findImprovementCourses();
				int index = 0;
				for (ImprovementCourse ic : improvementCourses){
					Section icSection = section1.addSection(ic.getName() + "(" + ic.getAbbreviation() + ")");
					PdfPTable table = new PdfPTable(1);
					table.setSpacingBefore(25);
					table.setSpacingAfter(25);
					PdfPCell c1 = new PdfPCell(new Phrase("Elève"));
					table.addCell(c1);
					List<SimpleStudent> students = studentsBySpec.get(index);
					for (SimpleStudent student : students){
						table.addCell(student.getName());
					}
					index += 1;
					icSection.add(table);
				}
			}
			
			Paragraph title22 = new Paragraph("Filières métier", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new CMYKColor(0, 255,
					255, 17)));
			section2 = chapter2.addSection(title22);

			if (!configurationService.isSubmissionAvailable()){
				List<List<SimpleStudentWithValidation>> studentsBySpec = studentService.findSimpleStudentsWithValidationForAllJsByOrder(1);
				List<JobSector> jobSectors = specializationService.findJobSectors();
				int index = 0;
				for (JobSector js : jobSectors){
					Section jsSection = section2.addSection(js.getName() + "(" + js.getAbbreviation() + ")");
					PdfPTable table = new PdfPTable(2);
					table.setSpacingBefore(25);
					table.setSpacingAfter(25);
					PdfPCell c1 = new PdfPCell(new Phrase("Elève"));
					table.addCell(c1);
					PdfPCell c2 = new PdfPCell(new Phrase("Validation"));
					table.addCell(c2);
					List<SimpleStudentWithValidation> students = studentsBySpec.get(index);
					for (SimpleStudentWithValidation student : students){
						table.addCell(student.getName());
						table.addCell(student.isValidated() ? "Accepté" : "Refusé");
					}
					index += 1;
					jsSection.add(table);
				}
			}
			else{
				List<List<SimpleStudent>> studentsBySpec = studentService.findSimpleStudentsForAllJsByOrder(1);
				List<JobSector> jobSectors = specializationService.findJobSectors();
				int index = 0;
				for (JobSector js : jobSectors){
					Section jsSection = section2.addSection(js.getName() + "(" + js.getAbbreviation() + ")");
					PdfPTable table = new PdfPTable(1);
					table.setSpacingBefore(25);
					table.setSpacingAfter(25);
					PdfPCell c1 = new PdfPCell(new Phrase("Elève"));
					table.addCell(c1);
					List<SimpleStudent> students = studentsBySpec.get(index);
					for (SimpleStudent student : students){
						table.addCell(student.getName());
					}
					index += 1;
					jsSection.add(table);
				}
			}
			
			document.add(chapter2);
			document.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PdfPTable createTable(String type) {
		PdfPTable table = new PdfPTable(2);
		table.setSpacingBefore(25);
		table.setSpacingAfter(25);

		List<SimpleSpecialization> results = type.equals("ic") ? statisticsService.findSimpleIcStats() : statisticsService.findSimpleJsStats();

		PdfPCell c1 = new PdfPCell(new Phrase(type.equals("ic") ? "Parcours" : "Filières"));
		table.addCell(c1);
		PdfPCell c2 = new PdfPCell(new Phrase("Nombre d'élèves"));
		table.addCell(c2);

		for (SimpleSpecialization specialization : results) {
			table.addCell(specialization.getName() + " (" + specialization.getAbbreviation() + ")");
			table.addCell("" + specialization.getNumber());
		}

		return table;
	}
}
