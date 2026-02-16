package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final Map<Long, WebDriver> driverMap = new HashMap<>();

    // Initialize the Extent Report
    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Automation Test Report");
            spark.config().setDocumentTitle("OrangeHRM Report");
            spark.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
        }
        return extent;
    }

    // Start the Test
    public synchronized static ExtentTest startTest(String testName) {
        ExtentTest extentTest = getReporter().createTest(testName);
        test.set(extentTest);
        return extentTest;
    }
    
    // Flush the report
    public synchronized static void endTest() {
        if (extent != null) {
            extent.flush();
        }
    }

    // Clears the ThreadLocal variable to prevent memory leaks in parallel execution
    public static void unload() {
        test.remove();
        driverMap.remove(Thread.currentThread().getId()); // Prevent memory leaks
    }
    
    // Get Current Thread's test instance
    public static ExtentTest getTest() {
        return test.get();
    }
    
    public static String getTestName() {
        ExtentTest currentTest = getTest();
        return (currentTest != null) ? currentTest.getModel().getName() : "No active test";
    }
    
    // ================= LOGGING METHODS =================

    public static void logStep(String logMessage) {
        if (getTest() != null) 
        	getTest().info(logMessage);
    }
    
    public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenShotMessage) {
        if (getTest() != null) {
            getTest().pass(logMessage);
            attachScreenshot(driver, screenShotMessage);
        }
    }

    public static void logStepValidationForAPI(String logMessage) {
        if (getTest() != null) getTest().pass(logMessage);
    }
    
    public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage) {
        if (getTest() != null) {
            String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
            getTest().fail(colorMessage);
            attachScreenshot(driver, screenShotMessage);
        }
    }

    public static void logFailureAPI(String logMessage) {
        if (getTest() != null) {
            String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
            getTest().fail(colorMessage);
        }
    }

    public static void logSkip(String logMessage) {
        if (getTest() != null) {
            String colorMessage = String.format("<span style='color:orange;'>%s</span>", logMessage);
            getTest().skip(colorMessage);
        }
    }

    // ================= SCREENSHOT METHODS =================

    public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) {
        if (driver == null) return "";
        
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String destPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/" + screenshotName + "_" + timeStamp + ".png";
        
        File finalPath = new File(destPath);
        try {
            FileUtils.copyFile(src, finalPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String base64Format = convertToBase64(finalPath);
        return base64Format;
    }
 // Optimization: Capture directly to Base64 without saving to disk
    public synchronized static String takeScreenshotBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }    
    public static String convertToBase64(File screenShotFile) {
    	 String base64Format = "";
    	try {
            byte[] fileContent = FileUtils.readFileToByteArray(screenShotFile);
            base64Format = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    	return base64Format;
    }
    
    public synchronized static void attachScreenshot(WebDriver driver, String message) {
        try {
            String screenShotBase64 = takeScreenshot(driver, getTestName());
            if (getTest() != null) {
                getTest().info(message, MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
            }
        } catch (Exception e) {
            if (getTest() != null) getTest().fail("Failed to attach screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void registerDriver(WebDriver driver) {
        driverMap.put(Thread.currentThread().getId(), driver);
        
    }
}