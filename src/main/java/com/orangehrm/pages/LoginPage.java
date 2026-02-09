package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {

	private ActionDriver actionDriver;
	//define locators and methods for LoginPage
	
	private By usernameField = By.xpath("//input[@name='username']");
	private By passwordField = By.cssSelector("input[type='password']");
	private By loginButton = By.xpath("//button[@type='submit']");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");
	
	//constructor
	/*
	 * public LoginPage(WebDriver driver) { actionDriver = new ActionDriver(driver);
	 * 
	 * }
	 */
	
	public LoginPage(WebDriver driver) {
		actionDriver = BaseClass.getActionDriver();
		
	}
	// Method to perform login
	public void login(String username, String password) {
//		actionDriver = new ActionDriver();
		actionDriver.enterText(usernameField, username);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginButton);
	}
	
	//Method to check if error message is displayed
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}
	
	//Method to get error message text
	public String getErrorMessageText() {
		return actionDriver.getText(errorMessage);
	}
	
	// Verify if error message is correct or not
	public boolean verifyErrorMessage(String expectedMessage) {
		return actionDriver.compareText(errorMessage, expectedMessage);
	}
	
	
	
}
