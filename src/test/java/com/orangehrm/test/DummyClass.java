package com.orangehrm.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass extends BaseClass {

	@Test
	public void dummyTest() {
		// Use getDriver() for thread safety
//		ExtentManager.startTest("DummyTest1 Test");  --//This has been implemented in the TestListener
		logger.info("Starting Dummy Test - Verifying page title...");
		String title = getDriver().getTitle();

		logger.info("Verifying page title. Actual: " + title);
		ExtentManager.logStep("Verifying page title. Actual: " + title);

		assert title.equals("OrangeHRM") : "Test Failed - Title does not match";

//		logger.info("Test Passed - Title matches OrangeHRM");
		ExtentManager.logStep("This case is skipped as part of testing.");
		throw new SkipException("Skipping the test as part of testing");
	}
}