package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class ApiTest {
	@Test
	public void verifyGetUserAPI() {
		SoftAssert softAssert=new SoftAssert();
		String endpoint="https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("endpoint"+endpoint);
		
		Response response=ApiUtility.sendGetReqquest(endpoint);
		
		boolean isStatusCodeValid=ApiUtility.validateStatusCode(response, 200);
		softAssert.assertTrue(isStatusCodeValid,"Status code is not as expected");
		
		if(isStatusCodeValid) {
		    ExtentManager.logStepValidationForAPI("Status Code validation passed");
		    
		}
		else {
			ExtentManager.logFailureAPI("Status Code Validation failed");
		}
		
		ExtentManager.logStep("Validation");
		String userName=ApiUtility.getJsonvalue(response, "username");
		boolean isUserValid="Bret".equals(userName);
		if(isUserValid) {
			ExtentManager.logStepValidationForAPI("Username validation passed");
		}
		else {
			ExtentManager.logFailureAPI("Username Validation failed");
		}
		softAssert.assertAll();
	}

	

}
