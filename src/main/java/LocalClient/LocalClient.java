package LocalClient;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;


public class LocalClient {
	
	static Logger logger = LoggerFactory.getLogger("LocalClient");

	public static void run() {
		String baseUrl = "https://google.com";

		WebDriverManager.firefoxdriver().setup();
		WebDriver myWebDriver = new FirefoxDriver();
		myWebDriver.get(baseUrl);
		
		//Opened now check it
		logger.info(myWebDriver.getCurrentUrl());

		// get the actual value of the title
		logger.info(myWebDriver.getTitle());
	}

}

