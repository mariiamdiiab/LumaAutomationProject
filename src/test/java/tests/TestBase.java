package tests;

import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utilites.BrowserOptions;
import utilites.GlobalVariable;
import utilites.Helper;

public class TestBase {
    protected WebDriver driver;

    @BeforeClass
    @Parameters({"browser"})
    public void startDriver(@Optional("chrome") String browserName) {
        Allure.step("Open the Website", () -> {
            driver = BrowserOptions.browserOptions(browserName);
            driver.manage().window().maximize();
            driver.get(GlobalVariable.URL);
        });
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
        Allure.step("Close the Website",() ->{
        if (driver != null) {
            driver.quit();
        }
    });
}
}