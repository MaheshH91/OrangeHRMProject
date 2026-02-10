package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ConfigReader;

public class HomePageTest extends BaseClass {
    
    private LoginPage loginPage;
    private HomePage homePage;
    
    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(description = "Verify OrangeHRM Logo visibility on Home Page")
    public void verifyOrangeHRMLogo() {
        logger.info("--- Starting verifyOrangeHRMLogo Test ---");
        
        // Log in using credentials from ConfigReader
        loginPage.login(ConfigReader.get("username"), ConfigReader.get("password"));
        
        logger.info("Validating OrangeHRM Logo...");
        boolean isLogoVisible = homePage.isOrangeHrmLogoVisible();
        
        Assert.assertTrue(isLogoVisible, "OrangeHRM logo should be visible on the home page.");
        
        logger.info("--- verifyOrangeHRMLogo Test Passed ---");
    }
}