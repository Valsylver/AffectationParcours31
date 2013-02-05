package fr.affectation.service.statistics;

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

import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.PieChart;
import com.googlecode.charts4j.Slice;

import fr.affectation.domain.comparator.ComparatorSimpleSpecialization;
import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.SimpleSpecialization;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.student.StudentService;


//@Service
public class StatisticsServiceCharts4j implements StatisticsService {
	
	public static final Color[] colors = {Color.FORESTGREEN, Color.RED, Color.SKYBLUE, Color.CHOCOLATE, Color.ORANGE, Color.GRAY,
		Color.PURPLE, Color.YELLOW, Color.TEAL, Color.TAN, Color.SIENNA, Color.BROWN};
	public static String link;
 	
	@Inject
	private StudentService studentService;
	
	@Inject
	private SpecializationService specializationService;

	@Override
	public void generatePieChartIc(String path) {
		Map<String, Integer> icStats = findIcStats();
		List<Slice> slices = new ArrayList<Slice>();
		int indexColor = 0;
		for (String key : icStats.keySet()){
			//slices.add(Slice.newSlice(icStats.get(key), colors[indexColor], key));
			slices.add(Slice.newSlice(icStats.get(key), key));
			System.out.println("keyyyyy " + key);
			indexColor += 1;
			if (indexColor == colors.length){
				indexColor = 0;
			}
		}
		
        PieChart chart2 = GCharts.newPieChart(slices);
        chart2.setSize(600, 400);
        chart2.setThreeD(true);
        link = chart2.toURLString();
        
        DefaultPieDataset data = new DefaultPieDataset();
		for (String key : icStats.keySet()){
			data.setValue(key, icStats.get(key));
		}
		JFreeChart chart = ChartFactory.createPieChart("", data, true, true, true);
		File file = new File(path + "/img/jspchart/piechartPa.png");
		ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		try{
			ChartUtilities.saveChartAsPNG(file, chart, 800, 600, info);
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
		int sum = 0;
		for (ImprovementCourse improvementCourse : allIc){
			int number = studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, improvementCourse).size();
			icStats.put(improvementCourse.getAbbreviation(), number);
			sum += number;
		}
		for (String key : icStats.keySet()){
			float lol = (float) sum;
			System.out.println("float(sum) : " + lol);
			float value = (float) icStats.get(key);
			System.out.println("1 : " + value);
			float value2 = (value/((float) sum));
			System.out.println("2 : " + value2);
			int value3 = (int) (100*value2);
			System.out.println("3 : " + value3);
			//int valueIntDivBySum = (int) (value / (float) sum); 
			//System.out.println("3 : " + valueIntDivBySum);
			//System.out.println(key + " : " + valueIntDivBySum);
			icStats.put(key, value3); 
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
		JFreeChart chart = ChartFactory.createBarChart3D(null, null, null, data, PlotOrientation.HORIZONTAL, false, false, false);
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
		JFreeChart chart = ChartFactory.createBarChart3D(null, null, null, data, PlotOrientation.HORIZONTAL, false, false, false);
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



	@Override
	public String getLink() {
		return link;
	}

}
