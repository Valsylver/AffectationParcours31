package fr.affectation.service.agap;

import java.util.List;

import fr.affectation.domain.student.Contentious;
import fr.affectation.domain.student.UeResult;

public interface AgapService {

	public void updateFromTheRealAgap();

	public List<Contentious> findContentious(String login);

	public List<Float> findGpaMeans(String login);

	public List<UeResult> findUeResults(String login);
}
