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

import com.aventstack.extentreports.ExtentTest;
import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;

public class BaseClass {

	protected static Properties prop;
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	@BeforeSuite
	public void loadConfig() throws IOException {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties")) {
			prop.load(fis);
		}
		logger.info("Configuration properties loaded.");
		ExtentManager.getReporter();
	}

	@BeforeMethod
	public synchronized void setup() {
		launchBrowser();

		// Map the ActionDriver to the specific WebDriver of this thread
		actionDriver.set(new ActionDriver(getDriver()));

		configureBrowser();
		staticWait(2); // Small wait to ensure page loads before tests interact with it
		logger.info("Setup and Navigation completed for thread: " + Thread.currentThread().getId());
	}

	private synchronized void launchBrowser() {
		String browser = prop.getProperty("browser");
		WebDriver driverInstance;

		// Initialize the specific driver instance
		if (browser.equalsIgnoreCase("chrome")) {
//			driverInstance = new ChromeDriver();
			driver.set(new ChromeDriver());// new changes as per thread local
			 driverInstance = driver.get(); // Get the instance from ThreadLocal
			 logger.debug("ChromeDriver instance created for thread: " + Thread.currentThread().getId());
			ExtentManager.registerDriver(getDriver());
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver.set(new FirefoxDriver());// new changes as per thread local
			 driverInstance = driver.get(); // Get the instance from ThreadLocal
			 logger.debug("FirefoxDriver instance created for thread: " + Thread.currentThread().getId());
			driverInstance = new FirefoxDriver();
			ExtentManager.registerDriver(getDriver());
		} else if (browser.equalsIgnoreCase("edge")) {
			driver.set(new EdgeDriver());// new changes as per thread local
			 driverInstance = driver.get(); // Get the instance from ThreadLocal
			 logger.debug("EdgeDriver instance created for thread: " + Thread.currentThread().getId());
			driverInstance = new EdgeDriver();
			ExtentManager.registerDriver(getDriver());
		} else {
			logger.fatal("Browser type not supported: " + browser);
			ExtentManager.logFailure(null, "Unsupported browser: " + browser, null);
			throw new IllegalArgumentException("Unsupported browser!");
		}

		// Store it in ThreadLocal once
		driver.set(driverInstance);
		logger.info(browser + " browser launched for thread: " + Thread.currentThread().getId());
	}

	@AfterMethod
	public synchronized void tearDown() {
		WebDriver currentDriver = driver.get(); // Get the driver for this thread
		if (currentDriver != null) {
			try {
				currentDriver.manage().deleteAllCookies();
				currentDriver.quit();
				logger.info("Browser closed for thread: " + Thread.currentThread().getId());
			} catch (Exception e) {
				logger.error("Error during tearDown: " + e.getMessage());
			} finally {
				// ALWAYS remove from ThreadLocal to prevent memory leaks
				driver.remove();
				actionDriver.remove();
				ExtentManager.endTest(); // Clear the ExtentTest reference for this thread
			}
		}
	}
//	private void launchBrowser() {
//		String browser = prop.getProperty("browser");
//		WebDriver driverInstance;
//
//		if (browser.equalsIgnoreCase("chrome")) {
////			driverInstance = new ChromeDriver();
//			driver.set(new ChromeDriver());// new changes as per thread local
//			 driverInstance = driver.get(); // Get the instance from ThreadLocal
//			 logger.debug("ChromeDriver instance created for thread: " + Thread.currentThread().getId());
//		} else if (browser.equalsIgnoreCase("firefox")) {
////			driverInstance = new FirefoxDriver();
//			driver.set(new FirefoxDriver());// new changes as per thread local
//			 driverInstance = driver.get(); // Get the instance from ThreadLocal
//			 logger.debug("FirefoxDriver instance created for thread: " + Thread.currentThread().getId());
//		} else if (browser.equalsIgnoreCase("edge")) {
//			driver.set(new EdgeDriver());// new changes as per thread local
//			 driverInstance = driver.get(); // Get the instance from ThreadLocal
//			 logger.debug("EdgeDriver instance created for thread: " + Thread.currentThread().getId());
////			driverInstance = new EdgeDriver();
//		} else {
//			logger.fatal("Browser type not supported: " + browser);
//			throw new IllegalArgumentException("Unsupported browser!");
//		}
//
//		// Correct way to set the ThreadLocal value
//		driver.set(driverInstance);
//		logger.info(browser + " browser launched for thread: " + Thread.currentThread().getId());
//	}

	private void configureBrowser() {
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait").trim());
		getDriver().manage().window().maximize();
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
	try {
		getDriver().get(prop.getProperty("url"));
	}catch (Exception e) {
		logger.error("Failed to navigate to URL: " + e.getMessage());
	}
		logger.info("Browser configured and navigated to URL for thread: " + Thread.currentThread().getId());
	}

	public static WebDriver getDriver() {
		if (driver.get() == null) {
			throw new IllegalStateException("WebDriver not initialized for thread: " + Thread.currentThread().getId());
		}
		return driver.get();
	}

	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			// Fallback: if ActionDriver isn't set, initialize it using the current thread's
			// driver
			actionDriver.set(new ActionDriver(getDriver()));
			logger.debug("ActionDriver lazy-initialized for thread: " + Thread.currentThread().getId());
		}
		return actionDriver.get();
	}

//	@AfterMethod
//	public void tearDown() {
//		if (getDriver() != null) {
//			try {
//				getDriver().manage().deleteAllCookies();
//				getDriver().quit();
//			} catch (Exception e) {
//				logger.warn("Failed to delete cookies: " + e.getMessage());
//			}
//			driver.remove();
//			actionDriver.remove();
//			logger.info("Browser closed and ThreadLocal cleaned for thread: " + Thread.currentThread().getId());
//		}
//	}

	public static Properties getProp() {
		return prop;
	}

	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;

	}

	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}