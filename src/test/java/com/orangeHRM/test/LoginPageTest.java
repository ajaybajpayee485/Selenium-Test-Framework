package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass{

	private LoginPage loginPage;
	private HomePage homePage;
	@BeforeMethod
	public void setupPages() {
		loginPage=new LoginPage(getDriver());
		homePage=new HomePage(getDriver());
	}
	@Test
	public void  verifyValidLoginTest() {
		ExtentManager.startTest("Valid Login Test");
		ExtentManager.logStep("Navigating to Login Page entering username and password" );
		loginPage.login("rootuser","Bajpayee@123");
		Assert.assertTrue(homePage.isAdminTabVisible(),"admin tab is not visible");
		homePage.logout();
		ExtentManager.logStep("Logged out Successfully");
		staticWait(2); 
	}
	@Test
	public void inValidLoginTest() {
	    ExtentManager.startTest("In-valid login tests");
		loginPage.login("rootuser","Bajpayee@123");
		String expectedErrorMessage="Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), "Test failed in invalid error");
		ExtentManager.logStep("validation successfully");
	}
	
}
