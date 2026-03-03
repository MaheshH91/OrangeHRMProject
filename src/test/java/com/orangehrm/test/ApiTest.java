package com.orangehrm.test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class ApiTest {

	@Test(description = "Verify GET User API"/* , retryAnalyzer = RetryAnalyzer.class */)
	public void verifyGetUserApi() {

		SoftAssert softAssert = new SoftAssert();

		// Step 1: Define the API endpoint
		String endpoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API Endpoint: " + endpoint);

		// Step 2: Send GET request and get the response
		ExtentManager.logStep("Sending GET request to the API");
		Response response = ApiUtility.sendGetRequest(endpoint);

		// Step 3: Validate the status code
		ExtentManager.logStep("Validating the status code");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);

		softAssert.assertTrue(isStatusCodeValid, "Expected status code 200 but got " + response.getStatusCode());

		if (isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status code valid passed! ");
		} else {
			ExtentManager.logFailureAPI("Status code validation failed! ");
		}

		// Step 4: Validate user name
		ExtentManager.logStep("Validating response body for username ");
		String username = ApiUtility.getJsonValue(response, "username");
		boolean isUsernameValid = "Bret".equals(username);
		softAssert.assertTrue(isUsernameValid, "Username is not valid");

		if (isUsernameValid) {
			ExtentManager.logStepValidationForAPI("Username validation passed! ");
		} else {
			ExtentManager.logFailureAPI("Username validation failed! ");
		}

		// Step 4: Validate Email
		ExtentManager.logStep("Validating response body for email ");
		String email = ApiUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Bret".equals(username);
		softAssert.assertTrue(isEmailValid, "Email is not valid");

		if (isEmailValid) {
			ExtentManager.logStepValidationForAPI("Email validation passed! ");
		} else {
			ExtentManager.logFailureAPI("Email validation failed! ");
		}
		
		softAssert.assertAll();
	}
}
