package testCases;

import java.time.Duration;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPge;
import pageObjects.Homepage;
import testBase.BaseClass;

public class TC001_AccountRegistrationTest extends BaseClass {

	@Test(groups = {"Regression","Master"})
	public void verify_account_registartion() {
		logger.info("***********Account registartaion started*****");
		try {
		Homepage hp = new Homepage(driver);
		hp.ClickMyAccount();
		hp.Clickregister();
		logger.info("*****user registration started****");
		AccountRegistrationPge regpage = new AccountRegistrationPge(driver);
		regpage.setFirstNAme(randomString().toUpperCase());
		regpage.setLastName(randomString().toUpperCase());
		regpage.setEmail(randomString()+"@gmail.com");
		regpage.setTelephone("5612553");
		
		String password = randomAlphaNumeric();
		regpage.setPassword(password);
		regpage.setConfirmPassword(password);
		regpage.setPrivacyPolicy();
		regpage.clickContinue();
		String confmsg=regpage.getConfirmationMSG();
		logger.info("*******validating expected message******");
		Assert.assertEquals(confmsg,"Your Account Has Been Created!");
		}catch(Exception e)
		{
			logger.error("****Test failed****");
			logger.debug("Debug logs***");
			Assert.fail();
			}
		}
  
}
