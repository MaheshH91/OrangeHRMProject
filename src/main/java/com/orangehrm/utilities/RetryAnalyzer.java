package com.orangehrm.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0; //number of retries attempted
    private static final int MAX_RETRY_COUNT = 2; //maximum no of retries

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
        	retryCount++;
            return true; // Tells TestNG to run the test again
        }
        return false;
    }
}