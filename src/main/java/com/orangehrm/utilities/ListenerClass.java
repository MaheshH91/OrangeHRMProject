package com.orangehrm.utilities;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.orangehrm.base.BaseClass;
import com.aventstack.extentreports.Status;

public class ListenerClass implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        // Initialize the single report instance
        ExtentManager.getReporter();
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Create a new test node in the report for the current thread
        ExtentManager.startTest(result.getMethod().getMethodName());
        ExtentManager.getTest().info("Execution Started for: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.PASS, "Test Case: " + result.getName() + " PASSED");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.FAIL, "Test Case: " + result.getName() + " FAILED");
            ExtentManager.getTest().log(Status.FAIL, "Reason: " + result.getThrowable());

            // Check if it's a UI test (to avoid taking screenshots for API tests)
            if (!result.getTestClass().getName().toLowerCase().contains("api")) {
                try {
                    String base64 = ((TakesScreenshot) BaseClass.getDriver()).getScreenshotAs(OutputType.BASE64);
                    ExtentManager.getTest().addScreenCaptureFromBase64String(base64, "Failure Screenshot");
                } catch (Exception e) {
                    ExtentManager.getTest().warning("Screenshot capture failed: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (ExtentManager.getTest() != null) {
            ExtentManager.getTest().log(Status.SKIP, "Test Case: " + result.getName() + " SKIPPED");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        // Flush the report to generate the physical HTML file
        ExtentManager.endTest();
        // Clear the ThreadLocal reference to prevent memory leaks/data pollution
        ExtentManager.unload();
    }
}