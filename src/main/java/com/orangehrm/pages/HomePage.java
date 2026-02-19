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
		return actionDriver.isDisplayed(orangeHrmLogo);
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
//    public void clickAdminTab() {
//        logger.info("Clicking on Admin tab.");
//        actionDriver.click(adminTab);
//    }
}