package com.orangehrm.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;

public class DummyClass2 extends BaseClass {

	@Test
	public void test() {
		String title= driver.getTitle();
		assert title.equals( "OrangeHRM"):"Test Failed - Title does not match";
		
//		System.out.println("Test Passed - Title matches");
		
	}
}
