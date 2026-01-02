package com.orangeHRM.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass{
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage=new LoginPage(getDriver());
		homePage=new HomePage(getDriver());
	}
	@Test(dataProvider="emplVerification",dataProviderClass=DataProviders.class)
	public void verifyEmployeeNameVeificationFromDB() {
		SoftAssert softAssert=getSoftAssert();
		//ExtentManager.startTest("In-valid login tests");
		ExtentManager.logStep("Login with Admin credentials");
		loginPage.login(prop.getProperty("username"),prop.getProperty("password"));
		ExtentManager.logStep("click on PIM tab");
		homePage.clickOnPIMTab();
		ExtentManager.logStep("Search for Employee");
		homePage.employeeSearch("rahul");
		ExtentManager.logStep("Get the Employee Name from DB");
		String employee_id="2";
		
		Map<String,String> employeeDetails=DBConnection.getEmployeeDetails(employee_id);
		
		String emplFirstName=employeeDetails.get("firstName");
		String emplLastName=employeeDetails.get("lastName");
		//String emplFirstAndMiddleName=(emplFirstName+" ").trim();
		ExtentManager.logStep("Verify the employee first and middle name");
		softAssert.assertTrue(homePage.verifyEmployerFirstANdMiddleName(emplFirstName),"First and Middle are not matching");
		ExtentManager.logStep("verify the employee last name");
		softAssert.assertTrue(homePage.verifyEmployeeLastName(emplLastName),"last name is not verified");
		
		ExtentManager.logStep("Validation successfull");
		
		softAssert.assertAll();
	}

}
