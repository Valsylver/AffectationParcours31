package fr.affectation.service.agap;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AgapServiceTest {

	@Inject
	private AgapService agapService;
	
	@Test
	public void fakeTest(){
		Assert.assertTrue(true);
	}
	
	
}
