package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class PIMPage {
	private ActionDriver actionDriver;

	// Locators
	private By addEmployeeTab = By.xpath("//button[contains(normalize-space(.),'Add')]");
	private By firstNameField = By.xpath("//*[@name='firstName']");
	private By middleNameField = By.xpath("//*[@name='middleName']");
	private By lastNameField = By.xpath("//*[@name='lastName']");
	private By employeeIdField = By.xpath("//label[text()='Employee Id']/following::input[1]");
	private By saveButton = By.xpath("//button[contains(normalize-space(.),'Save')]");
	private By successToast = By.xpath("//div[contains(@class,'oxd-toast--success')]");

	public PIMPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	public void addEmployee(String fname, String mname,String lname, String empId) {
		actionDriver.click(addEmployeeTab);
		actionDriver.enterText(firstNameField, fname);
		actionDriver.enterText(middleNameField, mname);
		actionDriver.enterText(lastNameField, lname);

		// Clear default ID and enter our own
		actionDriver.click(employeeIdField);
		// Hint: You might need a 'clear and type' method in ActionDriver
		actionDriver.enterText(employeeIdField, empId);

		actionDriver.click(saveButton);
	}

	public boolean isSuccessMessageDisplayed() {
		return actionDriver.isDisplayed(successToast);
	}
}