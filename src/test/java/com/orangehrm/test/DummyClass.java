package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.orangehrm.base.BaseClass;

public class DummyClass extends BaseClass {

    @Test
    public void dummyTest() {
        // Use getDriver() for thread safety
        String title = getDriver().getTitle();
        
        logger.info("Verifying page title. Actual: " + title);

        // Use TestNG Assertions for reliability
        Assert.assertEquals(title, "OrangeHRM", "Test Failed - Title does not match");
        
        logger.info("Test Passed - Title matches OrangeHRM");
    }
}