package fr.affectation.service.agap;

import java.util.List;

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
	public void checkStudentFalse() {
		String fakeLogin = "thisLoginIsProbablyNotExisting";
		Assert.assertFalse(agapService.findStudentConcernedLogins().contains(fakeLogin));
	}

	@Test
	public void checkStudentTrue() {
		String existingLogin = findRandomExistingLoginFromCurrentPromotion();
		if (!existingLogin.equals("")) {
			Assert.assertTrue(agapService.findStudentConcernedLogins().contains(existingLogin));
		}
	}

	@Test
	public void nameFromLogin() {
		for (int iteration = 0; iteration < 10; iteration++) {
			String existingLogin = findRandomExistingLogin();
			if (!existingLogin.equals("")) {
				Assert.assertFalse(agapService.findNameFromLogin(existingLogin)
						.equals(existingLogin));
			}
		}
	}

	public String findRandomExistingLogin() {
		List<String> existingLogins = agapService.findStudentConcernedLogins();
		if (existingLogins.size() != 0) {
			return existingLogins.get((int) (Math.random() * existingLogins
					.size()));
		}
		return "";
	}

	public String findRandomExistingLoginFromCurrentPromotion() {
		List<String> existingLogins = agapService
				.findCurrentPromotionStudentLogins();
		if (existingLogins.size() != 0) {
			return existingLogins.get((int) (Math.random() * existingLogins
					.size()));
		}
		return "";
	}

}
