package fr.affectation.service.statistics;

import java.util.List;

import fr.affectation.domain.specialization.SimpleSpecialization;

public interface StatisticsService {
	
	public void generatePieChartIc(String path);
	
	public void generatePieChartJs(String path);
	
	public void generateBarChartIc(String path);
	
	public void generateBarChartJs(String path);

	public List<SimpleSpecialization> findSimpleIcStats();

	public List<SimpleSpecialization> findSimpleJsStats();

}
