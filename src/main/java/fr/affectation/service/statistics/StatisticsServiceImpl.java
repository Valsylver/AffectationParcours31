package fr.affectation.service.statistics;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.specialization.SpecializationService;

@Service
public class StatisticsServiceImpl implements StatisticsService {
	
	@Inject
	private ChoiceService choiceService;
	
	@Inject
	private SpecializationService specializationService;

	@Override
	public void generatePieChartIc(String path) {
		DefaultPieDataset data = new DefaultPieDataset();
		Map<String, Integer> icStats = findIcStats();
		for (String key : icStats.keySet()){
			data.setValue(key, icStats.get(key));
		}
		JFreeChart chart = ChartFactory.createPieChart("", data, true, true, true);
		File file = new File(path + "/img/jspchart/piechartPa.png");
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		try{
			ChartUtilities.saveChartAsPNG(file, chart, 600, 400, info);
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
	
	

	@Override
	public void generatePieChartJs(String path) {
		DefaultPieDataset data = new DefaultPieDataset();
		Map<String, Integer> icStats = findJsStats();
		for (String key : icStats.keySet()){
			data.setValue(key, icStats.get(key));
		}
		JFreeChart chart = ChartFactory.createPieChart("", data, true, true, true);
		File file = new File(path + "/img/jspchart/piechartFm.png");
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		try{
			ChartUtilities.saveChartAsPNG(file, chart, 600, 400, info);
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
	
	public Map<String, Integer> findJsStats(){
		List<JobSector> allJs = specializationService.findAllJobSector();
		Map<String, Integer> jsStats = new HashMap<String, Integer>();
		for (JobSector jobSector : allJs){
			jsStats.put(jobSector.getAbbreviation(), choiceService.getLoginsByOrderChoiceAndSpecialization(1, jobSector).size());
		}
		return jsStats;
	}
	
	public Map<String, Integer> findIcStats(){
		List<ImprovementCourse> allIc = specializationService.findAllImprovementCourse();
		Map<String, Integer> icStats = new HashMap<String, Integer>();
		for (ImprovementCourse improvementCourse : allIc){
			icStats.put(improvementCourse.getAbbreviation(), choiceService.getLoginsByOrderChoiceAndSpecialization(1, improvementCourse).size());
		}
		return icStats;
	}

}
