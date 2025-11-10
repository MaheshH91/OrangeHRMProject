package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.locks.LockSupport;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseClass {

	protected static Properties prop;
	protected WebDriver driver;

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load config properties file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
	}

	@BeforeMethod
	public void setUp() throws IOException {
		// Load config properties file

		// Initialize WebDriver based on the browser specified in config properties
		String browser = prop.getProperty("browser");
		if (browser.equalsIgnoreCase("chrome")) {
			// Initialize ChromeDriver
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			// Initialize FirefoxDriver
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			// Initialize EdgeDriver
			driver = new EdgeDriver();
		} else {
			throw new IllegalArgumentException("Unsupported browser: " + browser);
		}

		// Implicit wait
		int implicitWait = prop.getProperty("implicitWait") != null ? Integer.parseInt(prop.getProperty("implicitWait"))
				: 10;
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// Maximize browser window
		driver.manage().window().maximize();

		// Navigate to the URL specified in config properties
		driver.get(prop.getProperty("url"));

		staticWait(2); // Static wait for 2 seconds

	}

	@AfterMethod
	public void tearDown() {
		try {
			if (driver != null) {
				driver.quit();
			}
		} catch (Exception e) {
			System.out.println("Unable to quit browser: " + e.getMessage());
		}

	}

	// static wait method for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(Duration.ofSeconds(seconds).toNanos());
	}

}
