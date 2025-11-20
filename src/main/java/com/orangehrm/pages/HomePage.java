package com.orangehrm.pages;

import org.openqa.selenium.By;

import com.orangehrm.actiondriver.ActionDriver;

public class HomePage {
	private ActionDriver actionDriver;
	// Define locators and methods for HomePage
	
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By UserIdButton = By.cssSelector("oxd-userdropdown-name");
	private By logoutButton = By.xpath("//a[text()='Logout']");
	private By orangeHrmLogo = By.xpath("//div[@class='oxd-brand-banner']/img");
	
	// Initialize ActionDriver in the constructor
	public HomePage(ActionDriver actionDriver) {
		this.actionDriver = actionDriver;
	}
	
	// Method to verify if Admin tab is visible
	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(adminTab);
	}
	
	// Method to verify if OrangeHRM logo is visible
	public boolean isOrangeHrmLogoVisible() {
		return actionDriver.isDisplayed(orangeHrmLogo);
	}
	//method to perform logout
	public void logout() {
		actionDriver.click(UserIdButton);
		actionDriver.click(logoutButton);
	}
	// Method to click on Admin tab
	public void clickAdminTab() {
		actionDriver.click(adminTab);
	}
}
