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

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;

public class BaseClass {

    protected static Properties prop;
    // ThreadLocal ensures each test thread gets its own WebDriver instance
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    
    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    @BeforeSuite
    public void loadConfig() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
        prop.load(fis);
        logger.info("Configuration properties loaded.");
    }

    @BeforeMethod
    public void setup() {
        launchBrowser();
        
        // Initialize ActionDriver for the current thread's driver
        actionDriver.set(new ActionDriver(getDriver()));
        
        getDriver().manage().window().maximize();
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        
        getDriver().get(prop.getProperty("url"));
        logger.info("Navigated to URL and setup completed for: " + this.getClass().getSimpleName());
    }

    private void launchBrowser() {
        String browser = prop.getProperty("browser");
        WebDriver driverInstance;

        if (browser.equalsIgnoreCase("chrome")) {
            driverInstance = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            driverInstance = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            driverInstance = new EdgeDriver();
        } else {
            logger.fatal("Browser type not supported: " + browser);
            throw new IllegalArgumentException("Unsupported browser!");
        }
        
        driver.set(driverInstance); // Set the driver to the ThreadLocal
        logger.info(browser + " browser launched for thread: " + Thread.currentThread().getId());
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static ActionDriver getActionDriver() {
        return actionDriver.get();
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove(); // Clean up memory
            actionDriver.remove();
            logger.info("Browser closed and ThreadLocal cleaned.");
        }
    }

    public static Properties getProp() { return prop; }

    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }
}