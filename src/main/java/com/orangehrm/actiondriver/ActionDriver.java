package com.orangehrm.actiondriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;
	// Main constructor that pulls from properties
	public ActionDriver(WebDriver driver) {
	    this(driver, Integer.parseInt(BaseClass.getProperty("explicitWait")));
	}

	// Overloaded constructor for manual overrides
	public ActionDriver(WebDriver driver, int timeout) {
	    this.driver = driver;
	    // Default to 30 if a non-positive timeout is passed
	    int finalTimeout = (timeout > 0) ? timeout : 30;
	    this.wait = new WebDriverWait(driver, Duration.ofSeconds(finalTimeout));
	    logger.info("ActionDriver initialized with " + finalTimeout + "s wait on thread: " + Thread.currentThread().getId());
	}

	/*
	 * public ActionDriver(WebDriver driver) { this.driver = driver; // Get timeout,
	 * if missing or invalid, default to 30 String propTimeout =
	 * BaseClass.getProperty("explicitWait"); int explicitWait = (propTimeout !=
	 * null) ? Integer.parseInt(propTimeout) : 30;
	 * 
	 * this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
	 * logger.info("ActionDriver initialized for thread: " +
	 * Thread.currentThread().getId()); }
	 * 
	 * // In ActionDriver.java public ActionDriver(WebDriver driver, int timeout) {
	 * this.driver = driver;
	 * 
	 * this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
	 * logger.info("ActionDriver initialized with " + timeout + "s explicit wait.");
	 * this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
	 * logger.info("WebDriver instance and Wait created"); }
	 */
	// ================= COMMON LOGGER =================
	private void logStep(String message) {
		logger.info(message);
		ExtentManager.logStep(message);
	}

	// ================= BASIC ACTIONS =================
	public void click(By by) {
		String desc = getElementDescription(by);
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked an element: " + desc);
			logStep("Clicked on " + desc);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(BaseClass.getDriver(),
					"Unable to click on element" + desc + ". Exception: " + e.getMessage(),
					ExtentManager.takeScreenshot(BaseClass.getDriver(), desc));
			logger.error("Click action failed on " + desc + ". Exception: " + e.getMessage());
			throw e;
		}
	}

	public void enterText(By by, String value) {
		String desc = getElementDescription(by);
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);

			logStep("Entered text '" + value + "' into " + desc);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(BaseClass.getDriver(), "Failed to enter text into " + desc,
					ExtentManager.takeScreenshot(BaseClass.getDriver(), desc));
			logger.error("Enter text action failed on " + desc + ". Exception: " + e.getMessage());
		}
	}

	public String getText(By by) {
		String desc = getElementDescription(by);
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			String text = driver.findElement(by).getText();
			logStep("Captured text from " + desc + " : " + text);
			return text;
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(driver, "Failed to get text from " + desc,
					ExtentManager.takeScreenshot(driver, desc));
			logger.error("Get text action failed on " + desc + ". Exception: " + e.getMessage());
			return "";
		}
	}

	public boolean isDisplayed(By by) {
		String desc = getElementDescription(by);
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Element is displayed " + getElementDescription(by));
			ExtentManager.logStep("Element is displayed: " + getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed: ",
					"Element is displayed: " + getElementDescription(by));
			logStep(desc + " is displayed");
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Element is not displayed: " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed: ",
					"Elemenet is not displayed: " + getElementDescription(by));
			return false;
		}
	}

	// ================= WAIT HELPERS =================
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element not visible: " + getElementDescription(by) + ". Exception: " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element not visible: " + getElementDescription(by),
					ExtentManager.takeScreenshot(BaseClass.getDriver(), getElementDescription(by)));
			throw e;
		}
	}

	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Element not clickable: " + getElementDescription(by) + ". Exception: " + e.getMessage());
			throw e;
		}
	}

	public void waitForPageLoad(int timeout) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeout)).until(driver -> ((JavascriptExecutor) driver)
					.executeScript("return document.readyState").equals("complete"));
			logStep("Page loaded successfully");
		} catch (Exception e) {
			ExtentManager.logFailure(driver, "Page did not load within " + timeout + " seconds",
					ExtentManager.takeScreenshot(driver, "page_load_timeout"));
			logger.error("Page load timeout: " + e.getMessage());
			throw e;
		}
	}

	// Method to compare text of an element with expected text and log the result
//	public boolean compareText(By by, String expectedText) {
//		String actualText = getText(by);
//		boolean match = actualText.equals(expectedText);
//		logStep("Comparing text for " + getElementDescription(by) + ". Expected: [" + expectedText + "] Actual: ["
//				+ actualText + "]");
//		return match;
//	}
	// ================= JAVASCRIPT =================
	public void scrollToElement(By by) {
		String desc = getElementDescription(by);
		try {
			applyBorder(by, "green");
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			logStep("Scrolled to " + desc);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(driver, "Failed to scroll to " + desc, ExtentManager.takeScreenshot(driver, desc));
			throw e;
		}
	}

	public void clickUsingJS(By by) {
		String desc = getElementDescription(by);
		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			logStep("Clicked using JS on " + desc);
		} catch (Exception e) {
			ExtentManager.logFailure(driver, "JS click failed on " + desc, ExtentManager.takeScreenshot(driver, desc));
			throw e;
		}
	}

	// ================= DROPDOWNS =================
	public void selectByVisibleText(By by, String value) {
		String desc = getElementDescription(by);
		try {
			new Select(driver.findElement(by)).selectByVisibleText(value);
			applyBorder(by, "green");
			logStep("Selected '" + value + "' from dropdown " + desc);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(driver, "Dropdown selection failed on " + desc,
					ExtentManager.takeScreenshot(driver, desc));
			throw e;
		}
	}

	public List<String> getDropdownOptions(By by) {
		List<String> options = new ArrayList<>();
		String desc = getElementDescription(by);
		try {
			Select select = new Select(driver.findElement(by));
			for (WebElement option : select.getOptions()) {
				options.add(option.getText());
			}
			logStep("Fetched dropdown options from " + desc);
		} catch (Exception e) {
			ExtentManager.logFailure(driver, "Failed to fetch dropdown options from " + desc,
					ExtentManager.takeScreenshot(driver, desc));
		}
		return options;
	}

	// ================= ACTIONS =================
	public void moveToElement(By by) {
		String desc = getElementDescription(by);
		try {
			new Actions(driver).moveToElement(driver.findElement(by)).perform();
			logStep("Moved to element " + desc);
		} catch (Exception e) {
			ExtentManager.logFailure(driver, "Failed to move to " + desc, ExtentManager.takeScreenshot(driver, desc));
			throw e;
		}
	}

	public void doubleClick(By by) {
		String desc = getElementDescription(by);
		try {
			new Actions(driver).doubleClick(driver.findElement(by)).perform();
			logStep("Double-clicked on " + desc);
		} catch (Exception e) {
			ExtentManager.logFailure(driver, "Double click failed on " + desc,
					ExtentManager.takeScreenshot(driver, desc));
			throw e;
		}
	}

	// ================= WINDOWS / FRAMES =================
	public void switchToWindow(String title) {
		try {
			for (String window : driver.getWindowHandles()) {
				driver.switchTo().window(window);
				if (driver.getTitle().equals(title)) {
					logStep("Switched to window: " + title);
					return;
				}
			}
		} catch (Exception e) {
			ExtentManager.logFailure(driver, "Failed to switch to window " + title,
					ExtentManager.takeScreenshot(driver, "window_switch"));
			throw e;
		}
	}

	public void switchToFrame(By by) {
		String desc = getElementDescription(by);
		try {
			driver.switchTo().frame(driver.findElement(by));
			logStep("Switched to frame " + desc);
		} catch (Exception e) {
			ExtentManager.logFailure(driver, "Failed to switch to frame " + desc,
					ExtentManager.takeScreenshot(driver, desc));
			throw e;
		}
	}

	// ================= ALERTS =================
	public void acceptAlert() {
		try {
			wait.until(ExpectedConditions.alertIsPresent()).accept();
			logStep("Alert accepted");
		} catch (Exception e) {
			ExtentManager.logFailure(driver, "Failed to accept alert", ExtentManager.takeScreenshot(driver, "alert"));
			throw e;
		}
	}

	// ================= UTILITIES =================
	private void applyBorder(By by, String color) {
		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid " + color + "'", element);
			logger.info("Applied " + color + " border to element" + getElementDescription(by));
		} catch (Exception ignored) {
			logger.warn("Unable to apply border to element: " + getElementDescription(by) + ". Exception: "
					+ ignored.getMessage());
		}
	}

	private String getElementDescription(By by) {
		return "Element located by: " + by.toString();
	}

	// Method to compare Two text -- changed the return type
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
				logger.info("Texts are Matching:" + actualText + " equals " + expectedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text",
						"Text Verified Successfully! " + actualText + " equals " + expectedText);
				return true;
			} else {
				applyBorder(by, "red");
				logger.error("Texts are not Matching:" + actualText + " not equals " + expectedText);
				ExtentManager.logFailure(BaseClass.getDriver(), "Text Comparison Failed!",
						"Text Comparison Failed! " + actualText + " not equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to compare Texts:" + e.getMessage());
		}
		return false;
	}
}