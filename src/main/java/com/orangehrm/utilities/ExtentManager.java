package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
	
	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test=new ThreadLocal<>();
	private static Map<Long,WebDriver> driverMap=new HashMap<>();

	public synchronized static ExtentReports getReporter() {
		if(extent==null) {
		  String reportPath=System.getProperty("user.dir")+"/src/test/resources/ExtentReport/ExtentReport.html";
		  ExtentSparkReporter spark=new ExtentSparkReporter(reportPath);
		  spark.config().setReportName("Automation Test Report");
		  spark.config().setDocumentTitle("OrangeHRM Report");;
		  spark.config().setTheme(Theme.DARK);
		  
		  
		  extent=new ExtentReports();
		  extent.attachReporter(spark);
		  extent.setSystemInfo("Operating system", System.getProperty("os.name"));
		  extent.setSystemInfo("Java Version", System.getProperty("java.version"));
		  extent.setSystemInfo("User Name", System.getProperty("user.name"));
		}
		return extent;
	}
	
	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest extentTest=getReporter().createTest( testName);
		test.set(extentTest);
		return extentTest;
	}
	
	public synchronized static void endTest() {
		getReporter().flush();
	}
	
	public synchronized static ExtentTest getTest() {
		return test.get();
	}
	
	public static String getTestName() {
		ExtentTest currentTest=getTest();
		if(currentTest!=null) {
			return currentTest.getModel().getName();
		}
		else {
			return "No test is active";
		}
	}
	
	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}
	
	public static void logStepWithScreenshot(WebDriver driver,String logMessage,String ScreenshotMessage) {
		getTest().pass(logMessage);
		attachScreenshot(driver, ScreenshotMessage);
	}
	
	public static void logStepValidationForAPI(String logMessage) {
		getTest().pass(logMessage);
		//attachScreenshot(driver, ScreenshotMessage);
	}
	
	public static void logFailure(WebDriver driver,String logMessage,String ScreenshotMessage) {
		String colorMessage=String.format("<span style='color:red;>%s</span>",logMessage);
		getTest().fail(colorMessage);
		attachScreenshot(driver, ScreenshotMessage);
		
	}
	
	public static void logFailureAPI(String logMessage) {
		String colorMessage=String.format("<span style='color:red;>%s</span>",logMessage);
		getTest().fail(colorMessage);
		//attachScreenshot(driver, ScreenshotMessage);
		
	}
	
	public static void logSkip(String logMessage) {
		String colorMessage=String.format("<span style='color:orange;>%s</span>",logMessage);
		getTest().skip(colorMessage);
		
	}
	
	public synchronized static String takescreenshot(WebDriver driver,String screenshotName) {
		TakesScreenshot ts=(TakesScreenshot)driver;
		File src=ts.getScreenshotAs(OutputType.FILE);
		String timestamp=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		String desPath=System.getProperty("user.dir")+"/src/test/resources/ExtentReport/screenshots/"+screenshotName+"_"+timestamp+".png";
		
		File finalPath=new File(desPath);
		
		try {
			FileUtils.copyFile(src, finalPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String base64Format=convertToBase64(src);
		return base64Format;
		
		
	}
	public static String convertToBase64(File screenShotFile) {
		String base64Format="";
		//byte[] fileContent;
		try {
			byte [] fileContent=FileUtils.readFileToByteArray(screenShotFile);
			base64Format=Base64.getEncoder().encodeToString(fileContent);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return base64Format;
	}
	
	public synchronized static void attachScreenshot(WebDriver driver,String message) {
		try {
			String screenShotBase64=takescreenshot(driver, getTestName());
			getTest().info(message,com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
		} catch (Exception e) {
			getTest().fail("Failed to attach scrennshot"+message);
		}
	}
	
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}
	
	
}
