package com.orangehrm.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class TestListener extends BaseClass implements ITestListener, IAnnotationTransformer {

	@Override
	public void onStart(ITestContext context) {
		ExtentManager.getReporter();
		logger.info("Test Suite started: " + context.getName());
	}

	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		logger.info("Test Started: " + testName);
		ExtentManager.startTest(testName);
		ExtentManager.getTest().assignCategory(result.getTestContext().getSuite().getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		logger.info("Test Passed: " + testName);

		// Directly log success with a final screenshot of the state
		// where the test finished successfully.
		ExtentManager.logStepWithScreenshot(getDriver(), "Test Case: " + testName + " passed successfully.",
				"Success_Screenshot_" + testName);

		// Standard Extent pass log
		ExtentManager.getTest().pass("Verified: " + testName);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		logger.error("Test Failed: " + testName + " | Error: " + failureMessage);

		// One call handles: Red Text Log + Screenshot + Exception Message
		ExtentManager.logFailure(getDriver(), "<b>Test Failed: " + testName + "</b><br>Reason: " + failureMessage,
				"Failure Screenshot");
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentManager.logSkip("Test Skipped: " + result.getMethod().getMethodName());
	}

	@Override
	public void onFinish(ITestContext context) {
		logger.info("Test Suite Finished: " + context.getName());
		ExtentManager.endTest();
		// Crucial: Clear thread locals to prevent memory leaks if running in parallel
		ExtentManager.unload();
	}
}
//public class TestListener extends BaseClass implements ITestListener, IAnnotationTransformer {
//	//Triggered when the test suite starts
//    @Override
//    public void onStart(ITestContext context) {
//    	
//    	ExtentManager.getReporter(); // Initialize the report
//    	logger.info("Test Suite started: " + context.getName());
//   
//    }
//
//    //Triggered when each test method starts
//    @Override
//    public void onTestStart(ITestResult result) {
//    	String testName = result.getMethod().getMethodName();
//
//        logger.info("Test Started: " + testName);
//        // Start the extent report entry for this specific thread
//        ExtentManager.startTest(testName);
//        ExtentManager.getTest().assignCategory(result.getTestContext().getSuite().getName());
//        ExtentManager.logStep("Execution Started for: " + testName);
//    }
//
//    //Triggered when a test method passes
//    @Override
//    public void onTestSuccess(ITestResult result) {
//    	String testName = result.getMethod().getMethodName();
//
//		if (!result.getTestClass().getName().toLowerCase().contains("api")) {
//			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed Successfully!",
//					"Test End: " + testName + " - ✔ Test Passed");
//		} else {
//			ExtentManager.logStepValidationForAPI("Test End: " + testName + " - ✔ Test Passed");
//		}
//    	ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed successfully! " + "Test End: "+testName+" - Test Passed", testName);
//        ExtentManager.getTest().pass("Test Case: " + result.getMethod().getMethodName() + " is Passed");
//    }
//
//    //triggered when a test method fails
//    @Override
//    public void onTestFailure(ITestResult result) {
//    	String testName = result.getMethod().getMethodName();
//
//    	ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed: " + testName + " - ✘ Test Failed", "Failure Screenshot");
//    	logger.error("Test Failed: " + result.getMethod().getMethodName());
//        WebDriver driver = getDriver(); // Retrieve the thread-local driver
//        
//        String failureMessage = result.getThrowable().getMessage();
//        
//        // Log the failure and automatically attach a screenshot
//        ExtentManager.logStep(failureMessage); // Log the failure message in the report
//        ExtentManager.logFailure(driver, 
//            "Test Failed: " + testName + "<br>Exception: " + failureMessage, 
//            "Failure Screenshot");
//    }
//
//    @Override
//    public void onTestSkipped(ITestResult result) {
//    	String testName = result.getMethod().getMethodName();
//
//    	logger.warn("Test Skipped: " + testName);
//        ExtentManager.logSkip("Test Skipped: " + testName + "");
//    }
//
//    // Triggered when the test suite finishes
//    @Override
//    public void onFinish(ITestContext context) {
//        logger.info("Test Suite Finished: " + context.getName());
//        // Flush the report to the HTML file
//        ExtentManager.endTest();
//    }
//}