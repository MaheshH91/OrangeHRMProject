package com.orangehrm.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass2 extends BaseClass {

	@Test
	public void dummytest2() {
		// Access driver via the thread-safe getter
		ExtentManager.startTest("DummyTest2 Test");
		logger.info("Starting Dummy Test - Verifying page title...");
		
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verifying page title in DummyClass2. Actual: " + title);
		logger.info("Starting title verification for DummyClass2...");

		// Using TestNG Assert for proper reporting
		assert title.equals("OrangeHRM") : "Title mismatch found in DummyClass2";
		logger.info("DummyClass2: Title validation successful.");
		ExtentManager.logStep("DummyClass2: Title validation successful.");
	}
}