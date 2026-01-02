package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiUtility {

	public static Response sendGetReqquest(String endpoint) {
		return RestAssured.get(endpoint);
	}
	
	public static Response sendPostRequest(String endpoint,String payload) {
		return RestAssured.given().header("Content-type","application/json")
				.body(payload).post();
	}
	
	public static boolean validateStatusCode(Response response,int statusCode) {
		return response.getStatusCode()==statusCode; 
	}
	
	public static String getJsonvalue(Response response,String value) {
		return response.jsonPath().getString(value);
	}
}
