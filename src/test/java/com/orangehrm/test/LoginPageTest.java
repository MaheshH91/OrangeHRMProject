package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
	@Test
	public void verifyValidLoginTest() throws InterruptedException {
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
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
	@Test
	public void invalidLoginTest() {
		loginPage.login("admin","admin");
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test Failed: Invalid error message.");
		
	}
	
	
}
