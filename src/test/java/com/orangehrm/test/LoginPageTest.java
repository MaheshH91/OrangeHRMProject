package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;
    
    @BeforeMethod
    public void setupPages() {
        // Always get the driver from the ThreadLocal via getDriver()
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class ,  description = "Verify user can login with valid credentials")
    public void verifyValidLoginTest(String username, String password) {
        logger.info("Starting valid login test...");
//        ExtentManager.startTest("Valid Login Test");//This has been implemented in the TestListener
        ExtentManager.logStep("Navigating to Login page entering valid credentials.");
        // Use ConfigReader for cleaner code
//        loginPage.login(ConfigReader.get("username"), ConfigReader.get("password"));
        loginPage.login(username, password);
        ExtentManager.logStep("Verifying Admin table is visible after successfull login.");
        Assert.assertTrue(homePage.isAdminTabVisible(), "Admin tab should be visible after successful login.");
        ExtentManager.logStep("Validation successfull.");
        homePage.logout();
        ExtentManager.logStep("Logged out successfully.");
        staticWait(2);
        logger.info("Valid login test passed and user logged out.");
    }

    @Test(dataProvider = "inValidLoginData", dataProviderClass = DataProviders.class, description = "Verify error message with invalid credentials")
    public void invalidLoginTest(String username, String password) {
        logger.info("Starting invalid login test...");
//        ExtentManager.startTest("Invalid Login Test1");//This has been implemented in the TestListener
        ExtentManager.logStep("Navigating to Login page entering valid credentials.");
        loginPage.login(username, password);
        
        String expectedErrorMessage = "Invalid credentials1";
        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), 
            "Test Failed: The expected error message '" + expectedErrorMessage + "' was not displayed.");
        ExtentManager.logStep("Verified error message for invalid login.");
        logger.info("Invalid login test passed: Error message verified.");
    }
}