package com.orangehrm.actiondriver;
import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger =BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait=Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));

	}

	private void waitforElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			System.out.println("element is not clickable" + e.getMessage());
			logger.error("unable to click element");
		}

	}

	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			System.out.println("element is not visible" + e.getMessage());
		}

	}

	public void click(By by) {
		
        String elementDescription=getElementDescription(by);
		try {
			applyBorder(by, "green");
			waitforElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("clicked an element"+elementDescription);
			logger.info("clicked a element"+elementDescription); 
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("unable to click" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(),"unable to clicked element", elementDescription+"_unable to click");
		}
	}

	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			//driver.findElement(by).clear();
			//driver.findElement(by).sendKeys(value);
			WebElement element=driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on"+getElementDescription(by)+" "+value);
		} catch (Exception e) {
			System.out.println("Unnable to enter value" + e.getMessage());
		}
	}

	public String getText(By by) {
		try {
			applyBorder(by, "green");
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("unable to get Text" + e.getMessage());
			return "";
		}
	}

	
	
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
				System.out.println("Text are matching");
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text", "text Verified"+actualText);
				return true;
			} else {
				applyBorder(by, "red");
				System.out.println("Text are not matching");
				ExtentManager.logFailure(BaseClass.getDriver(), "compared Text", "Text comparison faield"+expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to compare texts" + e.getMessage());
		}
		return false;
	}

	public boolean isDisplayed(By by) {
		try {
			applyBorder(by, "green");
			waitForElementToBeVisible(by);
			boolean isDisplayed = driver.findElement(by).isDisplayed();
			ExtentManager.logStep("element is displayed"+getElementDescription(by));
			if (isDisplayed) {
				System.out.println("element is visible");
				return isDisplayed;
			} else {
				return isDisplayed;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("unable to display"+e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(),"Element is not displayed" , "element is not displayed"+getElementDescription(by));
			return false;
		}
	}
	
	
	public void scrollToElement(By by) {
		try {
			applyBorder(by, "green");
			JavascriptExecutor js=(JavascriptExecutor) driver;
			WebElement element=driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("unable to scroll"+e.getMessage());
		}
	}
	
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver->((JavascriptExecutor)WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			System.out.println("Page Loaded successfully");
		}catch(Exception e) {
			System.out.println("page did not load within"+timeOutInSec+e.getMessage());
		}
	}
	
	
	public String getElementDescription(By locator) {
		if(driver==null)
			return "driver is null";
		if(locator==null) {
			return "Locator is null";
			
		}
		try {
			WebElement element =driver.findElement(locator);
			String name=element.getDomAttribute("name");
			String id=element.getDomAttribute("id");
			String text=element.getText();
			String className=element.getDomAttribute("class");
			String placeHolder=element.getDomAttribute("placeholder");
			
			if(isNotEmpty(name)) {
				return "Element with name"+name;
			}
			else if(isNotEmpty(id)) {
				return "Element with id:"+id;
			}
			else if(isNotEmpty(text)) {
				return "Element with text:"+text;
			}
			else {
				return "Element with placeholder:"+placeHolder;
			}
		} catch (Exception e) {
			logger.error("Unable to describe"+e.getMessage());
		}
		return "Unable to describe";
		
	}
	
	private boolean isNotEmpty(String value) {
		return value!=null && !value.isEmpty();
	}
	
	public void applyBorder(By by,String color) {
		try {
			WebElement element=driver.findElement(by);
			String script="arguments[0].style.border='3px solid "+color+"'";
			JavascriptExecutor js=(JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("applied border"+color+getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Failed to apply the border",getElementDescription(by),e);
		}
		
	}
	

}
