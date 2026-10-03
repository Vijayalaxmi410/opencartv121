package testBase;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseClass {
	
	public WebDriver driver;
public Logger logger;
public Properties p;
@BeforeClass(groups={"Sanity","Regression","Master"})
@Parameters({"os","browser"})
public void setup(@Optional("windows") String os, @Optional("chrome") String br) throws IOException {

    p = new Properties();
    FileReader file = new FileReader("./src/test/resources/config.properties");
    p.load(file);

    logger = (Logger) LogManager.getLogger(this.getClass());

    String env = p.getProperty("execution_env").trim();

    if (env.equalsIgnoreCase("remote")) {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (os.equalsIgnoreCase("windows")) {
            capabilities.setPlatform(Platform.WIN11);
        } else if (os.equalsIgnoreCase("mac")) {
            capabilities.setPlatform(Platform.MAC);
        } else {
            throw new IllegalArgumentException("No matching os: " + os);
        }

        switch (br.toLowerCase()) {
            case "chrome":  capabilities.setBrowserName("chrome");  break;
            case "edge":    capabilities.setBrowserName("edge");    break;
            case "firefox": capabilities.setBrowserName("firefox"); break;
            default: throw new IllegalArgumentException("No matching browser: " + br);
        }

        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);

    } else if (env.equalsIgnoreCase("local")) {
        switch (br.toLowerCase()) {
            case "chrome":  driver = new ChromeDriver();  break;
            case "edge":    driver = new EdgeDriver();    break;
            case "firefox": driver = new FirefoxDriver(); break;
            default: throw new IllegalArgumentException("Invalid browser: " + br);
        }
    } else {
        throw new IllegalArgumentException("execution_env must be 'local' or 'remote', got: " + env);
    }

    driver.manage().deleteAllCookies();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    driver.get(p.getProperty("app_url"));
    driver.manage().window().maximize();
}
	
	/*@AfterClass (groups={"Sanity","Regression"})
	public void tearDowm() {
		driver.quit();
	}*/
	  public String randomString() {
	      String generatedstring = RandomStringUtils.randomAlphabetic(5);
	      return generatedstring;
	}
	      public String randomNumber() {
	          String generatedNumber = RandomStringUtils.randomNumeric(10);
	          return generatedNumber;
	    }
	      public String randomAlphaNumeric() {
	    	  String generatedstring = RandomStringUtils.randomAlphabetic(5);
	          String generatedNumber = RandomStringUtils.randomNumeric(3);
	          return (generatedstring+"@"+generatedNumber);
	    }
}
