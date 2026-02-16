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

    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        logger.info("WebDriver instance created");
    }

    // ================= COMMON LOGGER =================
    private void logStep(String message) {
        logger.info(message);
        ExtentManager.logStep(message);
    }

    // ================= BASIC ACTIONS =================
    public void click(By by) {
        String desc = getElementDescription(by);
        try {
            waitForElementToBeClickable(by);
            applyBorder(by, "green");
            driver.findElement(by).click();
            logStep("Clicked on " + desc);
        } catch (Exception e) {
            applyBorder(by, "red");
            ExtentManager.logFailure(driver,"Failed to click on " + desc,
                    ExtentManager.takeScreenshot(driver, desc));
            throw e;
        }
    }

    public void enterText(By by, String value) {
        String desc = getElementDescription(by);
        try {
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);
            applyBorder(by, "green");
            logStep("Entered text '" + value + "' into " + desc);
        } catch (Exception e) {
            applyBorder(by, "red");
            ExtentManager.logFailure(driver,"Failed to enter text into " + desc,
                    ExtentManager.takeScreenshot(driver, desc));
            throw e;
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
            ExtentManager.logFailure(driver,"Failed to get text from " + desc,
                    ExtentManager.takeScreenshot(driver, desc));
            return "";
        }
    }

    public boolean isDisplayed(By by) {
        String desc = getElementDescription(by);
        try {
            waitForElementToBeVisible(by);
            applyBorder(by, "green");
            boolean displayed = driver.findElement(by).isDisplayed();
            logStep(desc + " is displayed");
            return displayed;
        } catch (Exception e) {
            applyBorder(by, "red");
            ExtentManager.logFailure(driver,desc + " is NOT displayed",
                    ExtentManager.takeScreenshot(driver, desc));
            return false;
        }
    }

    // ================= WAIT HELPERS =================
    private void waitForElementToBeVisible(By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private void waitForElementToBeClickable(By by) {
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public void waitForPageLoad(int timeout) {
        wait.withTimeout(Duration.ofSeconds(timeout)).until(driver ->
                ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState")
                        .equals("complete"));
        logStep("Page loaded successfully");
    }

    // ================= JAVASCRIPT =================
    public void scrollToElement(By by) {
        String desc = getElementDescription(by);
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView(true);", element);
            logStep("Scrolled to " + desc);
        } catch (Exception e) {
            ExtentManager.logFailure(driver,"Failed to scroll to " + desc,
                    ExtentManager.takeScreenshot(driver, desc));
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
            ExtentManager.logFailure(driver,"JS click failed on " + desc,
                    ExtentManager.takeScreenshot(driver, desc));
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
            ExtentManager.logFailure(driver,"Dropdown selection failed on " + desc,
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
            ExtentManager.logFailure(driver,"Failed to fetch dropdown options from " + desc,
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
            ExtentManager.logFailure(driver,"Failed to move to " + desc,
                    ExtentManager.takeScreenshot(driver, desc));
            throw e;
        }
    }

    public void doubleClick(By by) {
        String desc = getElementDescription(by);
        try {
            new Actions(driver).doubleClick(driver.findElement(by)).perform();
            logStep("Double-clicked on " + desc);
        } catch (Exception e) {
            ExtentManager.logFailure(driver,"Double click failed on " + desc,
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
            ExtentManager.logFailure(driver,"Failed to switch to window " + title,
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
            ExtentManager.logFailure(driver,"Failed to switch to frame " + desc,
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
            ExtentManager.logFailure(driver,"Failed to accept alert",
                    ExtentManager.takeScreenshot(driver, "alert"));
            throw e;
        }
    }

    // ================= UTILITIES =================
    private void applyBorder(By by, String color) {
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].style.border='3px solid " + color + "'", element);
        } catch (Exception ignored) {
        }
    }

    private String getElementDescription(By by) {
        return "Element located by: " + by.toString();
    }
    public boolean compareText(By by, String expectedText) {
        String actualText = getText(by);
        boolean match = actualText.equals(expectedText);
        logStep("Comparing text for " + getElementDescription(by) + 
                ". Expected: [" + expectedText + "] Actual: [" + actualText + "]");
        return match;
    }
}