package testCases;


import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.Homepage;
import pageObjects.LoginPage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;

public class TC002_LoginPage extends BaseClass{
	@Test(groups = {"Sanity","Master"})
	public void verify_login() {
		logger.info("********TC02 execution started******");
		try {
			//Homepage
		Homepage hp = new Homepage(driver);
		hp.ClickMyAccount();
		hp. ClickLogin();
		
		//LoginPage
		LoginPage lp = new LoginPage(driver);
		lp.setEmail(p.getProperty("email"));
		lp.setPassword(p.getProperty("password"));
		lp.login();
		
	
		//MyAccountpage
		MyAccountPage macc = new MyAccountPage(driver);
		boolean targetPage = macc.isMyAccountPageexist();
		
		Assert.assertTrue(targetPage);
		}catch(Exception e) {
			Assert.fail();
		}
		logger.info("*******Finished TC002 loginTest");
		
	}

}
