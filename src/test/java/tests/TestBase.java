package tests;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utilites.BrowserOptions;
import utilites.Helper;

public class TestBase {
    protected WebDriver driver;

    @BeforeClass
    @Parameters({"browser"})
    public void startDriver(@Optional("chrome") String browserName) {
        // Get initialized driver from BrowserOptions
        driver = BrowserOptions.browserOptions(browserName);

        // Configure browser
        driver.manage().window().maximize();
        driver.get("https://magento.softwaretestingboard.com/");
    }

    @AfterMethod
    public void takeScreenShotWhenFail(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String testName = result.getName();
            System.out.println("Capturing screenshot for failed test: " + testName);

            Helper.attachScreenshotToAllure(driver, testName);

            Helper.saveScreenshotLocally(driver, testName);
        }
    }

    @AfterClass
    public void stopDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}