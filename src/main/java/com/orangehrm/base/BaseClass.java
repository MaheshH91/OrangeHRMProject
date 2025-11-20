package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
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
	protected static WebDriver driver;


	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load the configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);

		// Start the Extent Report
		// ExtentManager.getReporter(); --This has been implemented in TestListener
	}

	/*
	 * Initialize WebDriver based on Browser defined in config.properties file
	 */
	private void launchBrowser() {
		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();

		} else if (browser.equalsIgnoreCase("firefox")) {
			// Initialize Firefox driver
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			// Initialize Firefox driver
			driver = new EdgeDriver();
		} else {
			throw new IllegalArgumentException("Browser Not Supported: " + browser);
		}

	}

	/*
	 * Configure browser settings such as implicit wait, maximize the browser and
	 * navigate to the URL
	 */
	private void configureBrowser() {
		// Implicit Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		// Maximize the browser
		driver.manage().window().maximize();
		// Navigate to URL
		try {
			driver.get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to Navigate to the URL:" + e.getMessage());
		}
	}

	@BeforeMethod
//	@Parameters("browser")
	public void setup() throws IOException {
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());

		launchBrowser();
		configureBrowser();

	}

	/*
	 * Initialize the WebDriver based on browser defined in config.properties file
	 */
	/*
	 * Configure browser settings such as implicit wait, maximize the browser and
	 * navigate to the URL
	 */

	@AfterMethod
	public synchronized void tearDown() {
		if (driver != null) {
			try {
				driver.quit();
			} catch (Exception e) {
				System.out.println("Unable to close the browser: " + e.getMessage());
			}
		}
	}

//	Getter method for driver
	public WebDriver getDriver() {
		return driver;
	}
	// Driver setter method
		public void setDriver(WebDriver driver) {
			this.driver = driver;
		}
	// Getter method for prop
	public static Properties getProp() {
		return prop;
	}

	

	// Static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

}