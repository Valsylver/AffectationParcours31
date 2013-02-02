package fr.affectation.service.statistics;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import fr.affectation.domain.comparator.ComparatorSimpleSpecialization;
import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.SimpleSpecialization;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.student.StudentService;

@Service
public class StatisticsServiceImpl implements StatisticsService {
	
	@Inject
	private StudentService studentService;
	
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
		List<JobSector> allJs = specializationService.findJobSectors();
		Map<String, Integer> jsStats = new HashMap<String, Integer>();
		for (JobSector jobSector : allJs){
			jsStats.put(jobSector.getAbbreviation(), studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, jobSector).size());
		}
		return jsStats;
	}
	
	public Map<String, Integer> findIcStats(){
		List<ImprovementCourse> allIc = specializationService.findImprovementCourses();
		Map<String, Integer> icStats = new HashMap<String, Integer>();
		for (ImprovementCourse improvementCourse : allIc){
			icStats.put(improvementCourse.getAbbreviation(), studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, improvementCourse).size());
		}
		return icStats;
	}



	@Override
	public void generateBarChartIc(String path) {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		Map<String, Integer> icStats = findIcStats();
		for (String key : icStats.keySet()){
			data.addValue(icStats.get(key), "Parcours", key);
		}
		JFreeChart chart = ChartFactory.createBarChart(null, null, null, data, PlotOrientation.HORIZONTAL, false, false, false);
		chart.setBackgroundPaint(Color.white);
		chart.getCategoryPlot().setRenderer(new ColorsRenderer()); 
		File file = new File(path + "/img/jspchart/barchartPa.png");
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		try{
			ChartUtilities.saveChartAsPNG(file, chart, 600, 400, info);
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
	



	@Override
	public void generateBarChartJs(String path) {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		Map<String, Integer> jsStats = findJsStats();
		for (String key : jsStats.keySet()){
			data.addValue(jsStats.get(key), "Parcours", key);
		}
		JFreeChart chart = ChartFactory.createBarChart(null, null, null, data, PlotOrientation.HORIZONTAL, false, false, false);
		chart.setBackgroundPaint(Color.white);
		chart.getCategoryPlot().setRenderer(new ColorsRenderer()); 
		File file = new File(path + "/img/jspchart/barchartFm.png");
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		try{
			ChartUtilities.saveChartAsPNG(file, chart, 600, 400, info);
		}
		catch (Exception e){
			System.out.println(e);
		}
	}



	@Override
	public List<SimpleSpecialization> findSimpleIcStats() {
		Map<String, Integer> icMap = findIcStats();
		List<SimpleSpecialization> specializations = new ArrayList<SimpleSpecialization>();
		for (String abbreviation : icMap.keySet()){
			SimpleSpecialization specialization = new SimpleSpecialization();
			specialization.setAbbreviation(abbreviation);
			specialization.setName(specializationService.findNameFromIcAbbreviation(abbreviation));
			specialization.setNumber(icMap.get(abbreviation));
			specializations.add(specialization);
		}
		Collections.sort(specializations, new ComparatorSimpleSpecialization()); 
		return specializations;
	}



	@Override
	public List<SimpleSpecialization> findSimpleJsStats() {
		Map<String, Integer> jsMap = findJsStats();
		List<SimpleSpecialization> specializations = new ArrayList<SimpleSpecialization>();
		for (String abbreviation : jsMap.keySet()){
			SimpleSpecialization specialization = new SimpleSpecialization();
			specialization.setAbbreviation(abbreviation);
			specialization.setName(specializationService.findNameFromJsAbbreviation(abbreviation));
			specialization.setNumber(jsMap.get(abbreviation));
			specializations.add(specialization);
		}
		Collections.sort(specializations, new ComparatorSimpleSpecialization()); 
		return specializations;
	}

}
