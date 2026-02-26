package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.PIMPage;
import com.orangehrm.utilities.DataProviders;

public class PIMTest extends BaseClass {

	@Test(dataProvider = "emplVerification", dataProviderClass = DataProviders.class)
	public void verifyAddAndSearchEmployee(String username, String password, String fname, String mname, String lname,
			String empId) {
		LoginPage loginPage = new LoginPage(getDriver());

		
		// 1. Login and go to PIM
		loginPage.login(username, password).clickOnPIMTab(); // Reusing your existing method

		
		PIMPage pim = new PIMPage(getDriver());

		// 2. Add Employee
		logger.info("Adding employee: " + fname + " " + mname + " " + lname);
		pim.addEmployee(fname, mname, lname, empId);

		// 3. Validation
		Assert.assertTrue(pim.isSuccessMessageDisplayed(), "Success toast message should appear!");
		logger.info("Employee " + empId + " added successfully.");
	}
}
