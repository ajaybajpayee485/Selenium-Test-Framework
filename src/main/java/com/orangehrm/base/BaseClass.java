package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

	protected static Properties prop;
//	protected static WebDriver driver;
//	private static ActionDriver actionDriver;
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
	private boolean seleniumGrid;

	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	@BeforeSuite
	public void loadConfig() throws IOException {
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");
	}

	@BeforeMethod
	@Parameters("browser")
	public synchronized void setup(String browser) throws IOException {
		launchBrowser(browser);
		configureBrowser();
		staticWait(2);
		logger.info("Webdriver intialized and browser maximized");
		logger.trace("Trace message");
		logger.error("this is a error message");
		logger.debug("this is a debug message");
		logger.fatal("this is a fatal message");
//			if(actionDriver==null) {
//				actionDriver=new ActionDriver(driver);
//				logger.info("ActionDriver instance is created "+Thread.currentThread().getId());
//			}

		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("action driver " + Thread.currentThread().getId());

		// ExtentManager.getReporter();

	}

	private synchronized void launchBrowser(String browser) {
		// String browser = prop.getProperty("browser");
		boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
		String gridURL = prop.getProperty("gridURL");
		if (seleniumGrid) {
			try {

				if (browser.equalsIgnoreCase("chrome")) {
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--headless");
					options.addArguments("--disable-gpu");
					options.addArguments("--disable-notification");
					options.addArguments("--no-sandbox");
					driver.set(new RemoteWebDriver(new URL(gridURL), options));
					// options.addArguments("--window-size=1920,1080");
					// driver = new ChromeDriver();
//        		WebDriverManager.chromedriver().setup();
//        		// driver = new ChromeDriver();
//        		driver.set(new ChromeDriver());
					//// ExtentManager.registerDriver(getDriver());
//        		logger.info("chromeDriver Intialized");
				} else if (browser.equalsIgnoreCase("firefox")) {
					FirefoxOptions options = new FirefoxOptions();
					options.addArguments("--headless");
					options.addArguments("--disable-gpu");
					options.addArguments("--disable-notification");
					options.addArguments("--no-sandbox");
					driver.set(new RemoteWebDriver(new URL(gridURL), options));
					// driver = new FirefoxDriver();
//        			driver.set(new FirefoxDriver());
//        			ExtentManager.registerDriver(getDriver());
				} else if (browser.equalsIgnoreCase("edge")) {
					EdgeOptions options = new EdgeOptions();
					options.addArguments("--headless");
					options.addArguments("--disable-gpu");
					options.addArguments("--disable-notification");
					options.addArguments("--no-sandbox");

					driver.set(new RemoteWebDriver(new URL(gridURL), options));
					// driver = new EdgeDriver();
//        			driver.set(new EdgeDriver());
//        			ExtentManager.registerDriver(getDriver());
				} else {
					throw new IllegalArgumentException("Browser not supported" + browser);
				}
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invalid", e);
			}
		}

	}

	public static Properties getProp() {
		return prop;
	}

	private void configureBrowser() {
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));

		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		getDriver().manage().window().maximize();
//		try {
//			getDriver().get(prop.getProperty("url"));
//		} catch (Exception e) {
//			System.out.println("Failed to Naviagte to the URL:" + e.getMessage());
//
//		}
		
		if(seleniumGrid) {
			getDriver().get(prop.getProperty("url_grid"));
		}
		else {
			getDriver().get(prop.getProperty("url"));
		}
	}

//	public ThreadLocal<WebDriver> getDriver() {
//		return driver;
//		}
//	
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	public static WebDriver getDriver() {
		if (driver.get() == null) {
			System.out.println("driver is not intialized");
			throw new IllegalStateException("webdriver is not intialized");
		}
		return driver.get();
	}

	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not intialized");
			throw new IllegalStateException("Actiondriver is not intialized");
		}
		return actionDriver.get();
	}

	@AfterMethod
	public synchronized void tearDown() {
		if (driver != null) {
			try {
				driver.get().quit();
			} catch (Exception e) {
				System.out.println("unable to quit  the driver" + e.getMessage());
			}
		}
//		driver=null;
//		actionDriver=null;
		driver.remove();
		actionDriver.remove();
		// ExtentManager.endTest();
	}
	// driver =null;

	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

}
