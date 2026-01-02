package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {
	private By adminTab=By.xpath("//span[text()='Admin']");
	private By userIdButton=By.className("oxd-userdropdown-name");
	private By logoutButton=By.xpath("//a[text()='Logout']");
	private By orangeHRMlogo=By.xpath("//div[@class='oxd-brand-banner']//img");
	private By pimTab=By.xpath("//span[text()='PIM']");
	private By employeeSearch=By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div[1]/div[2]/form/div[1]/div/div[1]/div/div[2]/div/div/input");
	//private By employeeSearch=By.xpath("//input[starts-with(@placeholder,\"Search\")]");
	private By searchButton=By.xpath("//button[@type=\"submit\"]");
	private By emplFirstAndLastName=By.xpath("//*[@id='app']/div[1]/div[2]/div[2]/div/div[2]/div[3]/div/div/div/div/div/div[2]/div[1]/div/div[2]");
	private By emplLastName=By.xpath("//*[@id='app']/div[1]/div[2]/div[2]/div/div[2]/div[3]/div/div/div/div/div/div[2]/div[2]/div/div[2]");
	private ActionDriver actionDriver;
	
//	public HomePage(WebDriver driver) {
//		this.actionDriver=new ActionDriver(driver);
//	}
	
	
	
	public HomePage(WebDriver driver) {
		this.actionDriver=BaseClass.getActionDriver();
	}

	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(adminTab);
	}
	
	public void clickOnPIMTab() {
		actionDriver.click(pimTab);
	}
	
	public void employeeSearch(String value) {
		actionDriver.enterText(employeeSearch, value);;
		actionDriver.click(searchButton);
		actionDriver.scrollToElement(emplFirstAndLastName);
	}
	
	public boolean verifyEmployerFirstANdMiddleName(String empFirstAndMiddleNameFromDB) {
		return actionDriver.compareText(emplFirstAndLastName,empFirstAndMiddleNameFromDB );
	}
	
	public boolean verifyEmployeeLastName(String empLastFromDB) {
		return actionDriver.compareText(emplLastName,empLastFromDB );
	}
	
	public boolean verifyOrangeHRMlogo() {
		return actionDriver.isDisplayed(orangeHRMlogo);
	}
	
	public void logout() {
		actionDriver.click(userIdButton);
		actionDriver.click(logoutButton);
	}

}
