package com.orangehrm.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.LoggerManager;

public class HomePage {
	private ActionDriver actionDriver;

	// Create a logger specific to HomePage
	private static final Logger logger = LoggerManager.getLogger(HomePage.class);

	// Locators
	private By userIDButton = By.className("oxd-userdropdown-name");
	private By oranageHRMlogo = By.xpath("//div[@class='oxd-brand-banner']//img");

	private By pimTab = By.xpath("//span[text()='PIM']");
	private By employeeSearch = By
			.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
	private By searchButton = By.xpath("//button[@type='submit']");
	private By emplFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
	private By emplLastName = By.xpath("//div[@class='oxd-table-card']/div/div[4]");

	private By adminTab = By.xpath("//span[text()='Admin']");

	private By userIdButton = By.cssSelector(".oxd-userdropdown-name");
	private By logoutButton = By.xpath("//a[text()='Logout']");
	private By orangeHrmLogo = By.xpath("//div[@class='oxd-brand-banner']/img");

	public HomePage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
		logger.debug("HomePage initialized with ActionDriver.");
	}

	public boolean isAdminTabVisible() {
		boolean isVisible = actionDriver.isDisplayed(adminTab);
		logger.info("Checking Admin Tab visibility: " + isVisible);
		return isVisible;
	}

	public boolean isOrangeHrmLogoVisible() {
		logger.info("Checking if OrangeHRM logo is visible on HomePage.");
		return actionDriver.isDisplayed(orangeHrmLogo);
	}

	public boolean verifyOrangeHRMlogo() {
		logger.info("Verifying OrangeHRM logo visibility on HomePage.");
		return actionDriver.isDisplayed(oranageHRMlogo);
	}

//    public void logout() {
//        logger.info("Attempting to logout...");
//        actionDriver.click(userIdButton);
//        
//        // Instead of Thread.sleep, rely on ActionDriver's internal waits
//        actionDriver.click(logoutButton);
//        logger.info("Logout sequence completed.");
//    }
	public LoginPage logout() {
		logger.info("Attempting to logout...");
		actionDriver.click(userIdButton);
		actionDriver.click(logoutButton);
		logger.info("Logout sequence completed.");
		// Return LoginPage to continue the chain
		return new LoginPage(BaseClass.getDriver());
	}

	public void clickAdminTab() {
		logger.info("Clicking on Admin tab.");
		actionDriver.click(adminTab);
	}

	public void clickOnPIMTab() {
		logger.info("Clicking on PIM tab.");
		actionDriver.click(pimTab);
	}

	public void employeeSearch(String value) {
		logger.info("Searching for employee: " + value);
		actionDriver.enterText(employeeSearch, value);
		actionDriver.click(searchButton);
		actionDriver.scrollToElement(emplFirstAndMiddleName);
	}

	public boolean verifyEmployeeFirstAndMiddleName(String empFirstAndMiddleNameFromDB) {
		String actualEmpFirstAndMiddleName = actionDriver.getText(emplFirstAndMiddleName);
//		actionDriver.compareText(emplFirstAndMiddleName, empFirstAndMiddleNameFromDB);
		logger.info("UI First+Middle Name: " + actualEmpFirstAndMiddleName + " | Expected: " + empFirstAndMiddleNameFromDB);
		return actionDriver.compareText(emplFirstAndMiddleName, empFirstAndMiddleNameFromDB);
	}

	public boolean verifyEmployeeLastName(String expectedLastNameFromDB) {
		String actualLastName = actionDriver.getText(emplLastName);
		logger.info("UI Last Name: " + actualLastName + " | Expected: " + expectedLastNameFromDB);
		return actionDriver.compareText(emplLastName, expectedLastNameFromDB);
	}

}