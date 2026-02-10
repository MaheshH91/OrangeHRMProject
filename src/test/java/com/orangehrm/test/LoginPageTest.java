package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ConfigReader; // Use your new utility!

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;
    
    @BeforeMethod
    public void setupPages() {
        // Always get the driver from the ThreadLocal via getDriver()
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(priority = 1, description = "Verify user can login with valid credentials")
    public void verifyValidLoginTest() {
        logger.info("Starting valid login test...");
        
        // Use ConfigReader for cleaner code
        loginPage.login(ConfigReader.get("username"), ConfigReader.get("password"));
        
        Assert.assertTrue(homePage.isAdminTabVisible(), "Admin tab should be visible after successful login.");
        
        homePage.logout();
        logger.info("Valid login test passed and user logged out.");
    }

    @Test(priority = 2, description = "Verify error message with invalid credentials")
    public void invalidLoginTest() {
        logger.info("Starting invalid login test...");
        
        loginPage.login("wrongUser", "wrongPass");
        
        String expectedErrorMessage = "Invalid credentials1";
        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), 
            "Test Failed: The expected error message '" + expectedErrorMessage + "' was not displayed.");
        
        logger.info("Invalid login test passed: Error message verified.");
    }
}