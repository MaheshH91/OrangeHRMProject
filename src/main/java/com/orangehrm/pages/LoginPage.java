package com.orangehrm.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.LoggerManager;

public class LoginPage {

    private ActionDriver actionDriver;
    // Specific logger for LoginPage
    private static final Logger logger = LoggerManager.getLogger(LoginPage.class);
    
    // Locators
    private By usernameField = By.xpath("//input[@name='username']");
    private By passwordField = By.cssSelector("input[type='password']");
    private By loginButton = By.xpath("//button[@type='submit']");
    private By errorMessage = By.xpath("//p[text()='Invalid credentials']");
    
 
    public LoginPage(WebDriver driver) {
        // This calls your ThreadLocal getter which handles all the setup logic
        this.actionDriver = BaseClass.getActionDriver();
        logger.debug("LoginPage initialized successfully using ThreadLocal ActionDriver.");
    }
   

    // Method to perform login
//    public void login(String username, String password) {
//        logger.info("Attempting login with username: " + username);
//        actionDriver.enterText(usernameField, username);
//        
//        // We log that we're entering the password, but we don't log the actual password string for security
//        logger.debug("Entering password for user: " + username); 
//        actionDriver.enterText(passwordField, password);
//        
//        actionDriver.click(loginButton);
//        logger.info("Login button clicked.");
//    }
    public HomePage login(String username, String password) {
        logger.info("Attempting login with username: " + username);
        actionDriver.enterText(usernameField, username);
        actionDriver.enterText(passwordField, password);
        actionDriver.click(loginButton);
        
        // Return a new HomePage instance so the test can immediately 
        // call methods like homePage.verifyAdminTab();
        return new HomePage(BaseClass.getDriver());
    }
    public boolean isErrorMessageDisplayed() {
        boolean isDisplayed = actionDriver.isDisplayed(errorMessage);
        logger.warn("Checking for error message display. Status: " + isDisplayed);
        return isDisplayed;
    }
    
    public String getErrorMessageText() {
        String text = actionDriver.getText(errorMessage);
        logger.info("Captured error message text: " + text);
        return text;
    }
    
    public boolean verifyErrorMessage(String expectedMessage) {
        logger.info("Verifying if error message matches: " + expectedMessage);
        return actionDriver.compareText(errorMessage, expectedMessage);
    }
}