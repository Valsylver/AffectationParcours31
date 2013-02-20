package fr.affectation.service.statistics;

import java.util.List;

import fr.affectation.domain.specialization.SimpleSpecialization;
import fr.affectation.domain.specialization.SimpleSpecializationWithNumber;

public interface StatisticsService {
	
	public void generatePieChartIc(String path);
	
	public void generatePieChartJs(String path);
	
	public void generateBarChartIc(String path);
	
	public void generateBarChartJs(String path);

	public List<SimpleSpecializationWithNumber> findSimpleIcStats(int choice);

	public List<SimpleSpecializationWithNumber> findSimpleJsStats(int choice);
	
	public String getLink();

}
