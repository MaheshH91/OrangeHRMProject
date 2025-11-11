package com.orangehrm.actiondriver;

import java.time.Duration;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.utilities.configReader;

public class ActionDriver {
	private WebDriver driver;
	private WebDriverWait wait;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		// set implicit wait from config
		try {
			int implicit = configReader.getImplicitWait();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit));
		} catch (Exception e) {
			// fallback handled inside configReader; ignore here
			System.out.println("Using default implicit wait due to config error: " + e.getMessage());
		}

		// initialize explicit wait using configured value
		int explicit = configReader.getExplicitWait();
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicit));

	}

	public void waitForPageLoad() {
		wait.until(webDriver -> ((String) ((JavascriptExecutor) webDriver).executeScript("return document.readyState"))
				.equals("complete"));
	}

	// Method to click an element
	public void click(By by) {
		try {
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
		} catch (Exception e) {
			System.out.println("Unable to click element:" + e.getMessage());

		}

	}

	// method to get text from an element or input field
	public String getText(By by) {
		String text = "";
		try {
			waitForElementToBeVisible(by);
			text = driver.findElement(by).getText();
		} catch (Exception e) {
			System.out.println("Unable to get text:" + e.getMessage());
		}
		return text;
	}
	// method to compare two texts
	public void compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			
			if(actualText.equals(expectedText)) {
				System.out.println("Text are matching: " + actualText +" equals " + expectedText);
				
			}else {
				System.out.println("Text are not matching: " + actualText +" not equals " + expectedText);
			}
		} catch (Exception e) {
			System.out.println("Unable to compare text:" + e.getMessage());
		}
	}
	// Method to enter text into an input field
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			driver.findElement(by).clear();
			driver.findElement(by).sendKeys(value);
		} catch (Exception e) {
			System.out.println("Unable to enter text:" + e.getMessage());
		}

	}

	// Wait for Element to be clickable
	public void waitForElementToBeClickable(By by) {
		wait.until(driver -> driver.findElement(by).isDisplayed() && driver.findElement(by).isEnabled());
	}
	public void waitForElementToBeVisible(By by) {
		wait.until(driver -> driver.findElement(by).isDisplayed());
	}

	// Method to check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			System.out.println("Element not displayed:" + e.getMessage());
			return false;
		}
	}

	// --- Additional helper methods added below ---

	// Click using JavaScript (useful when normal click fails)
	public void clickByJS(By by) {
		try {
			waitForElementToBeVisible(by);
			WebElement el = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
		} catch (Exception e) {
			System.out.println("Unable to click element by JS: " + e.getMessage());
		}
	}

	// Scroll element into view
	public void scrollIntoView(By by) {
		try {
			WebElement el = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
		} catch (Exception e) {
			System.out.println("Unable to scroll into view: " + e.getMessage());
		}
	}

	// Scroll to an element
	public void scrollToElement(By by) {
		try {
			waitForElementToBeVisible(by);
			WebElement el = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
		} catch (Exception e) {
			System.out.println("Unable to scroll to element: " + e.getMessage());
		}
	}
	// Select dropdown by visible text
	public void selectByVisibleText(By by, String visibleText) {
		try {
			waitForElementToBeVisible(by);
			WebElement el = driver.findElement(by);
			Select sel = new Select(el);
			sel.selectByVisibleText(visibleText);
		} catch (Exception e) {
			System.out.println("Unable to select by visible text: " + e.getMessage());
		}
	}

	// Select dropdown by value
	public void selectByValue(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			WebElement el = driver.findElement(by);
			Select sel = new Select(el);
			sel.selectByValue(value);
		} catch (Exception e) {
			System.out.println("Unable to select by value: " + e.getMessage());
		}
	}

	// Select dropdown by index
	public void selectByIndex(By by, int index) {
		try {
			waitForElementToBeVisible(by);
			WebElement el = driver.findElement(by);
			Select sel = new Select(el);
			sel.selectByIndex(index);
		} catch (Exception e) {
			System.out.println("Unable to select by index: " + e.getMessage());
		}
	}

	// Hover over element
	public void hoverOver(By by) {
		try {
			waitForElementToBeVisible(by);
			WebElement el = driver.findElement(by);
			Actions actions = new Actions(driver);
			actions.moveToElement(el).perform();
		} catch (Exception e) {
			System.out.println("Unable to hover over element: " + e.getMessage());
		}
	}

	// Double click
	public void doubleClick(By by) {
		try {
			waitForElementToBeVisible(by);
			WebElement el = driver.findElement(by);
			Actions actions = new Actions(driver);
			actions.doubleClick(el).perform();
		} catch (Exception e) {
			System.out.println("Unable to double click element: " + e.getMessage());
		}
	}

	// Right click / context click
	public void rightClick(By by) {
		try {
			waitForElementToBeVisible(by);
			WebElement el = driver.findElement(by);
			Actions actions = new Actions(driver);
			actions.contextClick(el).perform();
		} catch (Exception e) {
			System.out.println("Unable to right click element: " + e.getMessage());
		}
	}

	// Get attribute value
	public String getAttribute(By by, String attribute) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).getAttribute(attribute);
		} catch (Exception e) {
			System.out.println("Unable to get attribute: " + e.getMessage());
			return null;
		}
	}

	// Check if element is enabled
	public boolean isEnabled(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).isEnabled();
		} catch (Exception e) {
			System.out.println("Element not enabled/available: " + e.getMessage());
			return false;
		}
	}

	// Wait until element is present in DOM
	public void waitForElementToBePresent(By by) {
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
	}

	// Wait until element disappears / is not visible
	public void waitForElementToDisappear(By by) {
		wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
	}

	// Take screenshot and save to given path
	public void takeScreenshot(String destPath) {
		try {
			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			if (destPath == null || destPath.isEmpty()) {
				System.out.println("Destination path is empty, skipping screenshot save.");
				return;
			}
			if (Paths.get(destPath).getParent() != null) {
				Files.createDirectories(Paths.get(destPath).getParent());
			}
			Files.copy(src.toPath(), Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.out.println("Unable to save screenshot: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Screenshot failed: " + e.getMessage());
		}
	}

}