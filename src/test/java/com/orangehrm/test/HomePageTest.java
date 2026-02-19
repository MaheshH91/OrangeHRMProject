package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}


	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class ,  description = "Verify OrangeHRM Logo visibility on Home Page")
	public void verifyOrangeHRMLogo(String username, String password) {
		logger.info("--- Starting verifyOrangeHRMLogo Test ---");
//		ExtentManager.startTest("Verify OrangeHRM Logo Test");//This has been implemented in the TestListener
		// Log in using credentials from ConfigReader
		logger.info("Starting valid login test...");
//	        ExtentManager.startTest("Valid Login Test");//This has been implemented in the TestListener
		ExtentManager.logStep("Navigating to Login page entering valid credentials.");
		// Use ConfigReader for cleaner code
//	        loginPage.login(ConfigReader.get("username"), ConfigReader.get("password"));
		loginPage.login(username, password);

		logger.info("Validating OrangeHRM Logo...");
		ExtentManager.logStep("Logged in successfully, now checking for OrangeHRM logo visibility.");
		boolean isLogoVisible = homePage.isOrangeHrmLogoVisible();

		Assert.assertTrue(isLogoVisible, "OrangeHRM logo should be visible on the home page.");
		ExtentManager.logStep("Validation successful: OrangeHRM logo is visible.");

		logger.info("--- verifyOrangeHRMLogo Test Passed ---");
	}
}