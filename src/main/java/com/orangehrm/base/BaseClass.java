package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;


public class BaseClass {

	protected static Properties prop;
	protected static WebDriver driver;


//	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
//
//	// Getter method for soft assert
//	public SoftAssert getSoftAssert() {
//		return softAssert.get();
//	}

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load the configuration file
	  
		// Start the Extent Report
		// ExtentManager.getReporter(); --This has been implemented in TestListener
	}

	@BeforeMethod
//	@Parameters("browser")
	public void setup() throws  IOException {
		System.out.println("Setting up WebDriver for:" + this.getClass().getSimpleName());
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);

		String browser=prop.getProperty("browser");

		if(browser.equalsIgnoreCase("chrome")) {
			driver=new ChromeDriver();
			
		}else if (browser.equalsIgnoreCase("firefox")) {
			// Initialize Firefox driver
			 driver = new FirefoxDriver();
		}else if (browser.equalsIgnoreCase("edge")) {
			// Initialize Firefox driver
			 driver = new EdgeDriver();
		}
		else {
			throw new IllegalArgumentException("Browser Not Supported: " + browser);
		}
		
		//implementing maximize and implicit wait
		driver.manage().window().maximize();
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait) );
		driver.get(prop.getProperty("url"));
//		launchBrowser(browser);
//		configureBrowser();
//		staticWait(2);
		// Sample logger message
		
		/*
		 * // Initialize the actionDriver only once if (actionDriver == null) {
		 * actionDriver = new ActionDriver(driver);
		 * logger.info("ActionDriver instance is created. "+Thread.currentThread().getId
		 * ()); }
		 */

		// Initialize ActionDriver for the current Thread
		

	}

	/*
	 * Initialize the WebDriver based on browser defined in config.properties file
	 */
	private synchronized void launchBrowser(String browser) {

		// String browser = prop.getProperty("browser");

		boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
		String gridURL = prop.getProperty("gridURL");

		if (seleniumGrid) {
			try {
				if (browser.equalsIgnoreCase("chrome")) {
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
				} else if (browser.equalsIgnoreCase("firefox")) {
					FirefoxOptions options = new FirefoxOptions();
					options.addArguments("-headless");
				} else if (browser.equalsIgnoreCase("edge")) {
					EdgeOptions options = new EdgeOptions();
					options.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
				} else {
					throw new IllegalArgumentException("Browser Not Supported: " + browser);
				}
			} catch (Exception e) {
				throw new RuntimeException("Invalid Grid URL", e);
			}
		} else {

			if (browser.equalsIgnoreCase("chrome")) {

				// Create ChromeOptions
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless"); // Run Chrome in headless mode
				options.addArguments("--disable-gpu"); // Disable GPU for headless mode
				// options.addArguments("--window-size=1920,1080"); // Set window size
				options.addArguments("--disable-notifications"); // Disable browser notifications
				options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
				options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments

				// driver = new ChromeDriver();
			} else if (browser.equalsIgnoreCase("firefox")) {

				// Create FirefoxOptions
				FirefoxOptions options = new FirefoxOptions();
				options.addArguments("--headless"); // Run Firefox in headless mode
				options.addArguments("--disable-gpu"); // Disable GPU rendering (useful for headless mode)
				options.addArguments("--width=1920"); // Set browser width
				options.addArguments("--height=1080"); // Set browser height
				options.addArguments("--disable-notifications"); // Disable browser notifications
				options.addArguments("--no-sandbox"); // Needed for CI/CD environments
				options.addArguments("--disable-dev-shm-usage"); // Prevent crashes in low-resource environments

				// driver = new FirefoxDriver();
			} else if (browser.equalsIgnoreCase("edge")) {

				EdgeOptions options = new EdgeOptions();
				options.addArguments("--headless"); // Run Edge in headless mode
				options.addArguments("--disable-gpu"); // Disable GPU acceleration
				options.addArguments("--window-size=1920,1080"); // Set window size
				options.addArguments("--disable-notifications"); // Disable pop-up notifications
				options.addArguments("--no-sandbox"); // Needed for CI/CD
				options.addArguments("--disable-dev-shm-usage"); // Prevent resource-limited crashes

				// driver = new EdgeDriver();
			} else {
				throw new IllegalArgumentException("Browser Not Supported:" + browser);
			}
		}
	}

	/*
	 * Configure browser settings such as implicit wait, maximize the browser and
	 * navigate to the URL
	 */

	private void configureBrowser() {
		// Implicit Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		boolean seleniumGrid = Boolean
				.parseBoolean(System.getProperty("seleniumGrid", prop.getProperty("seleniumGrid")));

		// maximize the browser

		// Navigate to URL
		/*
		 * try { getDriver().get(prop.getProperty("url")); } catch (Exception e) {
		 * System.out.println("Failed to Navigate to the URL:" + e.getMessage()); }
		 */

		if (seleniumGrid) {
		} else {
		}
	}

	@AfterMethod
	public synchronized void tearDown() {
		if (driver != null) {
			try {
				driver.quit();
			} catch (Exception e) {
				System.out.println("unable to quit the driver:" + e.getMessage());
			}
		}
		// driver = null;
		// actionDriver = null;
		// ExtentManager.endTest(); --This has been implemented in TestListener
	}

	/*
	 * 
	 * 
	 * //Driver getter method public WebDriver getDriver() { return driver; }
	 */

	// Getter Method for WebDriver
//	public static WebDriver getDriver() {
//
//		if (driver.get() == null) {
//			System.out.println("WebDriver is not initialized");
//			throw new IllegalStateException("WebDriver is not initialized");
//		}
//		return driver.get();
//
//	}

	// Getter Method for ActionDriver
//	public static ActionDriver getActionDriver() {
//
//		if (actionDriver.get() == null) {
//			System.out.println("ActionDriver is not initialized");
//			throw new IllegalStateException("ActionDriver is not initialized");
//		}
//		return actionDriver.get();
//	}

	// Getter method for prop
	public static Properties getProp() {
		return prop;
	}

	// Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
	}

	// Static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

}