package fr.affectation.service.export;

import java.awt.Color;
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
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.SimpleSpecializationWithNumber;
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
			PdfWriter.getInstance(document, new FileOutputStream(path + "/WEB-INF/resources/temp/resultats_affectation.pdf"));
			document.open();
			Image logo = Image.getInstance(path + "/img/logo_ecm.png");
			logo.scalePercent(50);
			logo.setAlignment(Element.ALIGN_CENTER);
			document.add(logo);
			Paragraph mainParagraph = new Paragraph("Affectation parcours/filières 3ème année", FontFactory.getFont(FontFactory.COURIER, 40, Font.BOLD,
					Color.BLUE));
			mainParagraph.setAlignment(Element.ALIGN_CENTER);
			document.add(new Paragraph("Hidden text, Hidden text, Hidden text", FontFactory.getFont(FontFactory.COURIER, 40, Font.BOLD, Color.WHITE)));
			document.add(mainParagraph);

			document.newPage();
			Paragraph title1 = new Paragraph("Parcours d'approfondissement", FontFactory.getFont(FontFactory.COURIER, 20, Font.BOLD, Color.BLACK));
			Chapter chapter1 = new Chapter(title1, 1);
			Paragraph title11 = new Paragraph("Statistiques", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD, Color.BLACK));
			Section section1 = chapter1.addSection(title11);
			statisticsService.generatePieChartIc(path);
			Image pieChartIc = Image.getInstance(path + "/WEB-INF/resources/temp/piechartPa.png");
			pieChartIc.setAlignment(Element.ALIGN_CENTER);
			pieChartIc.scalePercent(50);
			section1.add(pieChartIc);
			section1.add(createTable("ic"));

			document.newPage();
			Paragraph title12 = new Paragraph("Détails", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD, Color.BLACK));
			Section section2 = chapter1.addSection(title12);

			if (!configurationService.isSubmissionAvailable()) {
				List<List<SimpleStudentWithValidation>> studentsBySpec = studentService.findSimpleStudentsWithValidationForAllIcByOrder(1);
				List<ImprovementCourse> improvementCourses = specializationService.findImprovementCourses();
				int index = 0;
				for (ImprovementCourse ic : improvementCourses) {
					Section icSection = section2.addSection(ic.getName() + "(" + ic.getAbbreviation() + ")");
					List<SimpleStudentWithValidation> students = studentsBySpec.get(index);
					if (students.size() == 0) {
						icSection.add(new Paragraph("Aucun élève n'a choisi ce parcours d'approfondissement.", FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, Color.BLACK)));
					} else {
						PdfPTable table = new PdfPTable(2);
						table.setSpacingBefore(25);
						table.setSpacingAfter(25);
						Phrase phrase = new Phrase("Elève", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
						PdfPCell c1 = new PdfPCell(phrase);
						table.addCell(c1);
						Phrase phrase2 = new Phrase("Validation", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
						PdfPCell c2 = new PdfPCell(phrase2);
						table.addCell(c2);
						for (SimpleStudentWithValidation student : students) {
							table.addCell(student.getName());
							table.addCell(student.isValidated() ? "Accepté" : "Refusé");
						}
						icSection.add(table);
					}
					index += 1;
				}
			} else {
				List<List<SimpleStudent>> studentsBySpec = studentService.findSimpleStudentsForAllIcByOrder(1);
				List<ImprovementCourse> improvementCourses = specializationService.findImprovementCourses();
				int index = 0;
				for (ImprovementCourse ic : improvementCourses) {
					Section icSection = section2.addSection(ic.getName() + "(" + ic.getAbbreviation() + ")");
					List<SimpleStudent> students = studentsBySpec.get(index);
					if (students.size() == 0) {
						icSection.add(new Paragraph("Aucun élève n'a choisi ce parcours d'approfondissement.", FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, Color.BLACK)));
					} else {
						PdfPTable table = new PdfPTable(1);
						table.setSpacingBefore(25);
						table.setSpacingAfter(25);
						PdfPCell c1 = new PdfPCell(new Phrase("Elève", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
						table.addCell(c1);
						for (SimpleStudent student : students) {
							table.addCell(student.getName());
						}
						icSection.add(table);
					}
					index += 1;
				}
			}
			document.add(chapter1);

			document.newPage();
			Paragraph title2 = new Paragraph("Filières métier", FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD, Color.BLACK));

			Chapter chapter2 = new Chapter(title2, 2);
			Paragraph title21 = new Paragraph("Statistiques", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD, Color.BLACK));
			Section section3 = chapter2.addSection(title21);
			statisticsService.generatePieChartJs(path);
			Image pieChartJs = Image.getInstance(path + "/WEB-INF/resources/temp/piechartFm.png");
			pieChartJs.scalePercent(50);
			pieChartJs.setAlignment(Element.ALIGN_CENTER);
			section3.add(pieChartJs);
			section3.add(createTable("js"));

			document.newPage();
			Paragraph title22 = new Paragraph("Détails", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD, Color.BLACK));
			Section section4 = chapter2.addSection(title22);

			if (!configurationService.isSubmissionAvailable()) {
				List<List<SimpleStudentWithValidation>> studentsBySpec = studentService.findSimpleStudentsWithValidationForAllJsByOrder(1);
				List<JobSector> jobSectors = specializationService.findJobSectors();
				int index = 0;
				for (JobSector js : jobSectors) {
					List<SimpleStudentWithValidation> students = studentsBySpec.get(index);
					Section jsSection = section4.addSection(js.getName() + "(" + js.getAbbreviation() + ")");
					if (students.size() == 0) {
						jsSection.add(new Paragraph("Aucun élève n'a choisi cette filière métier.", FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, Color.BLACK)));
					} else {
						PdfPTable table = new PdfPTable(2);
						table.setSpacingBefore(25);
						table.setSpacingAfter(25);
						PdfPCell c1 = new PdfPCell(new Phrase("Elève", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
						table.addCell(c1);
						PdfPCell c2 = new PdfPCell(new Phrase("Validation", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
						table.addCell(c2);
						for (SimpleStudentWithValidation student : students) {
							table.addCell(student.getName());
							table.addCell(student.isValidated() ? "Accepté" : "Refusé");
						}
						
						jsSection.add(table);
					}
					index += 1;
				}
			} else {
				List<List<SimpleStudent>> studentsBySpec = studentService.findSimpleStudentsForAllJsByOrder(1);
				List<JobSector> jobSectors = specializationService.findJobSectors();
				int index = 0;
				for (JobSector js : jobSectors) {
					Section jsSection = section4.addSection(js.getName() + "(" + js.getAbbreviation() + ")");
					List<SimpleStudent> students = studentsBySpec.get(index);
					if (students.size() == 0) {
						jsSection.add(new Paragraph("Aucun élève n'a choisi cette filière métier.", FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, Color.BLACK)));
					} else {
						PdfPTable table = new PdfPTable(1);
						table.setSpacingBefore(25);
						table.setSpacingAfter(25);
						PdfPCell c1 = new PdfPCell(new Phrase("Elève", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
						table.addCell(c1);
						for (SimpleStudent student : students) {
							table.addCell(student.getName());
						}
						jsSection.add(table);
					}
					index += 1;

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

		List<SimpleSpecializationWithNumber> results = type.equals("ic") ? statisticsService.findSimpleIcStats(1) : statisticsService.findSimpleJsStats(1);

		PdfPCell c1 = new PdfPCell(new Phrase(type.equals("ic") ? "Parcours" : "Filières", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		table.addCell(c1);
		PdfPCell c2 = new PdfPCell(new Phrase("Nombre d'élèves", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
		table.addCell(c2);

		for (SimpleSpecializationWithNumber specialization : results) {
			table.addCell(specialization.getName() + " (" + specialization.getAbbreviation() + ")");
			table.addCell("" + specialization.getNumber());
		}

		return table;
	}
}
