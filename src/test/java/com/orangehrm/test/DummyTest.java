package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;

public class DummyTest extends BaseClass {

	@Test
	public void test() {
		String title= driver.getTitle();
		Assert.assertEquals(title, "OrangeHRM","Title does not match");
		
		System.out.println("Test Passed - Title matches");
		
	}
}
