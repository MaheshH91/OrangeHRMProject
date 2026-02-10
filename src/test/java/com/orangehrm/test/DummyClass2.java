package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.orangehrm.base.BaseClass;

public class DummyClass2 extends BaseClass {

    @Test
    public void test() {
        // Access driver via the thread-safe getter
        String title = getDriver().getTitle();
        
        logger.info("Starting title verification for DummyClass2...");

        // Using TestNG Assert for proper reporting
        Assert.assertEquals(title, "OrangeHRM", "Title mismatch found in DummyClass2");
        
        logger.info("DummyClass2: Title validation successful.");
    }
}