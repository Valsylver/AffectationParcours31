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

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.SimpleSpecializationWithNumber;
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
		Map<String, Integer> icStats = findIcStats(1);
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
		Map<String, Integer> icStats = findJsStats(1);
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
	
	public Map<String, Integer> findJsStats(int choice){
		List<JobSector> allJs = specializationService.findJobSectors();
		Map<String, Integer> jsStats = new HashMap<String, Integer>();
		for (JobSector jobSector : allJs){
			jsStats.put(jobSector.getAbbreviation(), studentService.findSimpleStudentsByOrderChoiceAndSpecialization(choice, jobSector).size());
		}
		return jsStats;
	}
	
	public Map<String, Integer> findIcStats(int choice){
		List<ImprovementCourse> allIc = specializationService.findImprovementCourses();
		Map<String, Integer> icStats = new HashMap<String, Integer>();
		for (ImprovementCourse improvementCourse : allIc){
			icStats.put(improvementCourse.getAbbreviation(), studentService.findSimpleStudentsByOrderChoiceAndSpecialization(choice, improvementCourse).size());
		}
		return icStats;
	}



	@Override
	public void generateBarChartIc(String path) {
		DefaultCategoryDataset data = new DefaultCategoryDataset();
		Map<String, Integer> icStats = findIcStats(1);
		for (String key : icStats.keySet()){
			data.addValue(icStats.get(key), "Parcours", key);
		}
		JFreeChart chart = ChartFactory.createBarChart3D(null, null, null, data, PlotOrientation.HORIZONTAL, false, false, false);
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
		Map<String, Integer> jsStats = findJsStats(1);
		for (String key : jsStats.keySet()){
			data.addValue(jsStats.get(key), "Parcours", key);
		}
		JFreeChart chart = ChartFactory.createBarChart3D(null, null, null, data, PlotOrientation.HORIZONTAL, false, false, false);
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
	public List<SimpleSpecializationWithNumber> findSimpleIcStats(int choice) {
		Map<String, Integer> icMap = findIcStats(choice);
		List<SimpleSpecializationWithNumber> specializations = new ArrayList<SimpleSpecializationWithNumber>();
		for (String abbreviation : icMap.keySet()){
			SimpleSpecializationWithNumber specialization = new SimpleSpecializationWithNumber(abbreviation, specializationService.findNameFromIcAbbreviation(abbreviation), icMap.get(abbreviation));
			specializations.add(specialization);
		}
		Collections.sort(specializations); 
		return specializations;
	}



	@Override
	public List<SimpleSpecializationWithNumber> findSimpleJsStats(int choice) {
		Map<String, Integer> jsMap = findJsStats(choice);
		List<SimpleSpecializationWithNumber> specializations = new ArrayList<SimpleSpecializationWithNumber>();
		for (String abbreviation : jsMap.keySet()){
			SimpleSpecializationWithNumber specialization = new SimpleSpecializationWithNumber(abbreviation, specializationService.findNameFromJsAbbreviation(abbreviation), jsMap.get(abbreviation));
			specializations.add(specialization);
		}
		Collections.sort(specializations); 
		return specializations;
	}



	@Override
	public String getLink() {
		// TODO Auto-generated method stub
		return null;
	}

}
