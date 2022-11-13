package LocalClient;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;


public class LocalClient {
	
	static Logger logger = LoggerFactory.getLogger("LocalClient");

	public static void run() {
		String baseUrl = "https://google.com";
		
		run(baseUrl, "about");
	}
	
	//TODO determine if that can be a static
	public static void run (String start_uri, String end_uri_regex) {

		WebDriverManager.firefoxdriver().setup();
		WebDriver myWebDriver = new FirefoxDriver();
		myWebDriver.get(start_uri);
		
		//Opened now check it
		logger.info(myWebDriver.getCurrentUrl());

		// get the actual value of the title
		logger.info(myWebDriver.getTitle());
		
		//TODO change to regex
		 boolean resultedInURI = new WebDriverWait(myWebDriver, Duration.ofSeconds(3))
        .until(driver -> (myWebDriver.getCurrentUrl().contains(end_uri_regex) ) );
		 
		 //If user hits the url we get this to be true
		 logger.info(Boolean.toString(resultedInURI));
	}

}

