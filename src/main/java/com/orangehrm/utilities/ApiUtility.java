package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiUtility {
//method to send GET request
	public static Response sendGetRequest(String endpoint) {
		return RestAssured.get(endpoint);
	}

	// method to send POST request with JSON payload
	public static Response sendPostRequest(String endpoint, String payload) {
		return RestAssured.given().header("Content-Type", "application/json").body(payload).post(endpoint);
	}

	// method to validate status code of the response
	public static boolean validateStatusCode(Response response, int statusCode) {
		return response.getStatusCode() == statusCode;
	}

	// method to extract value from JSON response using JsonPath
	public static String  getJsonValue(Response response, String value) {
		return response.jsonPath().getString(value);
	}
	
	

}
