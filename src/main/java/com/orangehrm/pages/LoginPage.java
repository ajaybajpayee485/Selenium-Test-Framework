package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {
	
	private ActionDriver actionDriver;
	
	private By userNameField=By.name("username");
	private By passwordField=By.cssSelector("input[type='password']");
	private By loginButton =By.xpath("//button[text()=' Login ']");
	private By errorMessage=By.xpath("//p[text()='Invalid credentials']");
	
	
//	public LoginPage(WebDriver driver) {
//		this.actionDriver=new ActionDriver(driver);
//	}
	
	public LoginPage(WebDriver driver) {
		this.actionDriver=BaseClass.getActionDriver();
	}
	
	public void login(String username,String password)
	{
		actionDriver.enterText(userNameField, username);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginButton);
	}
	
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}
	
	public String getErrorMessageText() {
		return actionDriver.getText(errorMessage);
	}
	
	public boolean verifyErrorMessage(String expectedError) {
		return actionDriver.compareText(errorMessage, expectedError);
		
	}
	
	
	
	
	

}
