package com.orangeHRM.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass extends BaseClass {

	@Test
	public void dummyTest() {
        //ExtentManager.startTest("DummyTest1 test");
		String title = getDriver().getTitle();
		assert title.equals("OrangeHRM") : "Test Failed";

		System.out.println("Test Passed");
		throw new SkipException("Skipping the test as part of testing");
	}

}
