package testCases;

import static org.testng.Assert.fail;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.Homepage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;

/*Data is valid - login success -test pass- logout
 * Data is invalid - login success - test failed 
 * data is valid - login fail - test fail
 * Data is invalid - login fail -test pass
 * 
 */
public class TC003_LoginDDT extends BaseClass{
	@Test(dataProvider="excelData", dataProviderClass=DataProviders.class,groups="datadriven" )//getting data providers from different class
	public void verify_LoginDDT(String email, String pwd, String Status) {
		logger.info("*******Starting TC003_LoginDDT*************");
		try {
		Homepage hp = new Homepage(driver);
		hp.ClickMyAccount();
		hp.ClickLogin();
		
		//LoginPage
		LoginPage lp = new LoginPage(driver);
		lp.setEmail(email);
		lp.setPassword(pwd);
		lp.login();

		
	
		//MyAccountpage
		MyAccountPage macc = new MyAccountPage(driver);
		boolean targetPage = macc.isMyAccountPageexist();
		
		

/*Data is valid - login success -test pass- logout
 * * data is valid - login fail - test fail
 * Data is invalid - login success - test failed 
  * Data is invalid - login fail -test pass
 * 
 */
	if(Status.equalsIgnoreCase("valid")) {
		if(targetPage== true) {
			macc.clickLogout();
			Assert.assertTrue(true);
			}
		else {
			Assert.assertTrue(false);
		}
		
		if(Status.equalsIgnoreCase("invalid")) {
			macc.clickLogout();
			Assert.assertTrue(false);
		}else {
			Assert.assertTrue(true);
		}
	}
	}catch(Exception e) {
Assert.fail();
	}
		logger.info("*******Finished TC003_LoginDDT*************");

}
}
