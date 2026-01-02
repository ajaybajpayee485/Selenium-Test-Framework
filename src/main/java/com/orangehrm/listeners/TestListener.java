package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

public class TestListener implements ITestListener, IAnnotationTransformer{

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		// TODO Auto-generated method stub
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

	@Override
	public void onTestStart(ITestResult result) {
	  
		String testName=result.getMethod().getMethodName();
		ExtentManager.startTest(testName);
		ExtentManager.logStep("test started "+"testName");
	
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
			
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Test passed successfully", "Test passed with start and end");
		}
		else {
			ExtentManager.logStepValidationForAPI("Tets end"+testName);
		}
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		String failureMessage=result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
			
			ExtentManager.logFailure(BaseClass.getDriver(), "Test failed", "Tets end"+testName);
		}
		else {
			ExtentManager.logFailureAPI("Tets end"+testName);
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String testName=result.getMethod().getMethodName();
		ExtentManager.logSkip("TestSkipped"+ testName);
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		ExtentManager.getReporter();
		
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		ExtentManager.endTest();
	}

	
	
}
