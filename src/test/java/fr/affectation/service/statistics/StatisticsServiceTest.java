package fr.affectation.service.statistics;

import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.affectation.domain.choice.ImprovementCourseChoice;
import fr.affectation.domain.choice.JobSectorChoice;
import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.SimpleSpecializationWithNumber;
import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.specialization.SpecializationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class StatisticsServiceTest {

	@Inject
	private StatisticsService statisticsService;

	@Inject
	private SpecializationService specializationService;

	@Inject
	private ChoiceService choiceService;

	@Test
	public void findIcStats() {
		for (int i = 0; i < 10; i++) {
			String abbreviation = "ic" + i;
			ImprovementCourse ic = new ImprovementCourse();
			ic.setAbbreviation(abbreviation);
			specializationService.save(ic);
			for (int j = 0; j < i + 1; j++) {
				String login = "login" + i + j;
				ImprovementCourseChoice icc = new ImprovementCourseChoice();
				icc.setLogin(login);
				icc.setChoice1(abbreviation);
				choiceService.save(icc);
			}
		}
		List<SimpleSpecializationWithNumber> icStats = statisticsService
				.findSimpleIcStats(1);
		int i = 0;
		for (SimpleSpecializationWithNumber spec : icStats) {
			if (!spec.getAbbreviation().equals(" Pas de choix")) {
				Assert.assertTrue(spec.getNumber() == i + 1);
				i += 1;
			}
		}
	}

	@Test
	public void findIJsStats() {
		for (int i = 0; i < 10; i++) {
			String abbreviation = "ic" + i;
			JobSector js = new JobSector();
			js.setAbbreviation(abbreviation);
			specializationService.save(js);
			for (int j = 0; j < i + 1; j++) {
				String login = "login" + i + j;
				JobSectorChoice icc = new JobSectorChoice();
				icc.setLogin(login);
				icc.setChoice1(abbreviation);
				choiceService.save(icc);
			}
		}
		List<SimpleSpecializationWithNumber> jsStats = statisticsService
				.findSimpleJsStats(1);
		int i = 0;
		for (SimpleSpecializationWithNumber spec : jsStats) {
			if (!spec.getAbbreviation().equals(" Pas de choix")) {
				Assert.assertTrue(spec.getNumber() == i + 1);
				i += 1;
			}
		}
	}

	@Before
	public void deleteBefore() {
		choiceService.deleteAll();
		specializationService.deleteAll();
	}

	@After
	public void deleteAfter() {
		choiceService.deleteAll();
		specializationService.deleteAll();
	}

}
