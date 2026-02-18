package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
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

	public static Properties prop;
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
//		ExtentManager.getReporter(); //This has been implemented in the TestListener class itself to ensure it's initialized before any test starts, and only once.
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
	    WebDriver driverInstance = null;

	    if (browser.equalsIgnoreCase("chrome")) {
	        ChromeOptions chOptions = new ChromeOptions();
	        chOptions.addArguments("--remote-allow-origins=*");
	        driverInstance = new ChromeDriver(chOptions);
	        logger.debug("ChromeDriver instance created for thread: " + Thread.currentThread().getId());

	    } else if (browser.equalsIgnoreCase("firefox")) {
	        driverInstance = new FirefoxDriver();
	        logger.debug("FirefoxDriver instance created for thread: " + Thread.currentThread().getId());

	    } else if (browser.equalsIgnoreCase("edge")) {
	        EdgeOptions options = new EdgeOptions();
	        options.setPageLoadStrategy(PageLoadStrategy.NORMAL); 
	        options.addArguments("--remote-allow-origins=*");
	        driverInstance = new EdgeDriver(options);
	        logger.debug("EdgeDriver instance created for thread: " + Thread.currentThread().getId());

	    } else {
	        // This block only runs if NO browser matches
	        logger.fatal("Browser type not supported: " + browser);
	        ExtentManager.logFailure(null, "Unsupported browser: " + browser, "N/A");
	        throw new IllegalArgumentException("Unsupported browser: " + browser);
	    }

	    // IMPORTANT: Set to ThreadLocal and Register ONCE at the end
	    driver.set(driverInstance);
	    ExtentManager.registerDriver(driverInstance);
	    
	    logger.info(browser + " browser launched successfully for thread: " + Thread.currentThread().getId());
	}
	@AfterMethod
	public synchronized void tearDown() {
	    WebDriver currentDriver = driver.get(); 
	    if (currentDriver != null) {
	        try {
	            // Optional: currentDriver.manage().deleteAllCookies(); 
	            // Note: quit() usually cleans up session data anyway
	            currentDriver.quit();
	            logger.info("Browser closed for thread: " + Thread.currentThread().getId());
	        } catch (Exception e) {
	            logger.error("Error during tearDown for thread " + Thread.currentThread().getId() + ": " + e.getMessage());
	        } finally {
	            // 1. Remove WebDriver from ThreadLocal
	            driver.remove();
	            
	            // 2. Remove ActionDriver from ThreadLocal
	            if (actionDriver != null) {
	                actionDriver.remove();
	            }

	            // 3. Clean up Extent Reporting references
//	            ExtentManager.unload(); //This has been implemented in the TestListener 
	            
	            logger.debug("ThreadLocal cleanup completed for thread: " + Thread.currentThread().getId());
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
	    try {
	        // 1. Parse and set timeouts first
	        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait").trim());
	        int pageLoadTimeout = Integer.parseInt(prop.getProperty("pageLoadTimeout", "30").trim());
	        
	        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
	        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));

	        // 2. Maximize window
	        getDriver().manage().window().maximize();

	        // 3. Navigate to URL
	        String url = prop.getProperty("url");
	        logger.info("Navigating to: " + url + " on thread: " + Thread.currentThread().getId());
	        getDriver().get(url);

	    } catch (NumberFormatException e) {
	        logger.error("Invalid numeric value in config properties: " + e.getMessage());
	        throw e;
	    } catch (Exception e) {
	        logger.error("Critical error during browser configuration: " + e.getMessage());
	        // If we can't navigate, there's no point in continuing the test
	        throw new RuntimeException("Could not configure browser or navigate to URL", e);
	    }
	    
	    logger.info("Browser configured and navigated successfully for thread: " + Thread.currentThread().getId());
	}
	public static WebDriver getDriver() {
	    WebDriver dr = driver.get();
	    if (dr == null) {
	        throw new IllegalStateException("WebDriver not initialized for thread: " + Thread.currentThread().getId());
	    }
	    
	    // Additional check: Ensure the browser session hasn't been closed/quit already
	    try {
	        if (dr.toString().contains("null")) { 
	            throw new IllegalStateException("WebDriver session is null/closed for thread: " + Thread.currentThread().getId());
	        }
	    } catch (Exception e) {
	        throw new IllegalStateException("WebDriver session is unreachable for thread: " + Thread.currentThread().getId());
	    }
	    
	    return dr;
	}
	public static ActionDriver getActionDriver() {
	    if (actionDriver.get() == null) {
	        // Ensure we actually have a browser before creating the ActionDriver
	        WebDriver currentDriver = getDriver(); 
	        
	        logger.debug("ActionDriver not found for thread " + Thread.currentThread().getId() + ". Initializing...");
	        actionDriver.set(new ActionDriver(currentDriver));
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

	public static String getProperty(String key) {
	    String value = prop.getProperty(key);
	    if (value == null) {
	        logger.error("Key '" + key + "' not found in config.properties!");
	        throw new RuntimeException("Missing configuration key: " + key);
	    }
	    return value.trim();
	}

	/**
	 * Sets the WebDriver instance for the current thread.
	 * Use this if you are initializing the driver outside of launchBrowser() 
	 * (e.g., in a specialized test or via dependency injection).
	 */
	public void setDriver(WebDriver driverInstance) {
	    driver.set(driverInstance);
	    logger.debug("Manual driver injection successful for thread: " + Thread.currentThread().getId());
	}
	public void staticWait(int seconds) {
	    if (seconds > 0) {
	        logger.debug("Applying static wait: " + seconds + " seconds on thread " + Thread.currentThread().getId());
	        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	    }
	}}