package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseClass {

	protected Properties prop;
	protected WebDriver driver;

	@BeforeMethod
	public void setUp() throws IOException {
		// Load config properties file
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
		prop.load(fis);
		
		// Initialize WebDriver based on the browser specified in config properties
		String browser = prop.getProperty("browser");
		if (browser.equalsIgnoreCase("chrome")) {
			// Initialize ChromeDriver
			 driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			// Initialize FirefoxDriver
			 driver = new FirefoxDriver();
		} else if(browser.equalsIgnoreCase("edge")) {
			// Initialize EdgeDriver
			 driver = new EdgeDriver();
		} else {
			throw new IllegalArgumentException("Unsupported browser: " + browser);
		}

		// Implicit wait
		int implicitWait = prop.getProperty("implicitWait") != null ? 
				Integer.parseInt(prop.getProperty("implicitWait")) : 10;
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		
		// Maximize browser window
		driver.manage().window().maximize();
		
		// Navigate to the URL specified in config properties
		driver.get(prop.getProperty("url"));
		
		

		
	}
	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
