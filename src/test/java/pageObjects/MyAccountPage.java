package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyAccountPage extends BasePage{

	public MyAccountPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	@FindBy(xpath = "//h2[text() = 'My Account']")//my account page heading 
	WebElement msgHeader;
	
	@FindBy(xpath ="//a[@class='list-group-item'][normalize-space()='Logout']")//added in step 6
	WebElement Logout;
	
	public boolean isMyAccountPageexist() {
		try {
		return(msgHeader.isDisplayed());
	}catch(Exception e) {
		return false;
	}

}

public void clickLogout() {
	Logout.click();}
}