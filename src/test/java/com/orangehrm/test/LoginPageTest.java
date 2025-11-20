package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;

public class LoginPageTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage=new LoginPage(getDriver());
		homePage=new HomePage(getDriver());
	}
	
	public void verifyValidLoginTest() {
		loginPage.login(prop.getProperty("validUsername"), prop.getProperty("validPassword"));
		Assert.assertTrue(homePage.isAdminTabVisible(), "Admin tab should be visible after successfull login.");
		
		homePage.logout();
		staticWait(2);
		
		
//		boolean isAdminTabVisible = homePage.isAdminTabVisible();
//		if (isAdminTabVisible) {
//			System.out.println("Valid Login Test Passed: Admin tab is visible.");
//		} else {
//			System.out.println("Valid Login Test Failed: Admin tab is not visible.");
//		}
	}
	
	
}
