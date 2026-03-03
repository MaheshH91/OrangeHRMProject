package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.PIMPage;
import com.orangehrm.utilities.DataProviders;

public class PIMTest extends BaseClass {

	@Test(dataProvider = "emplVerification", dataProviderClass = DataProviders.class)
	public void verifyAddAndSearchEmployee(String empId, String fname) {
	    // Note: I reduced it to 2 parameters to match your current Excel output
	    
	    LoginPage loginPage = new LoginPage(getDriver());
	    
	    // If you don't have the username/password in Excel, 
	    // use the properties file for now to unblock the test:
	    loginPage.login(prop.getProperty("username"), prop.getProperty("password"))
	             .clickOnPIMTab();

	    PIMPage pim = new PIMPage(getDriver());

	    // Adding employee using the 2 parameters we have
	    logger.info("Adding employee: " + fname + " with ID: " + empId);
	    
	    // If your addEmployee method requires 4 strings, pass empty strings for mname/lname
	    pim.addEmployee(fname, "", "", empId);

	    Assert.assertTrue(pim.isSuccessMessageDisplayed(), "Success toast message should appear!");
	}
}
