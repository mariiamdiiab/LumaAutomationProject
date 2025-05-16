package tests;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utilites.BrowserOptions;
import utilites.GlobalVariable;
import utilites.Helper;

import java.lang.reflect.Method;

public class TestBase {
    protected WebDriver driver;
    private static final Logger logger = LogManager.getLogger(TestBase.class);
    protected String testName;


    @BeforeClass
    @Parameters({"browser"})
    public void startDriver(@Optional("headless") String browserName) {
        try {
            Allure.step("Initialize WebDriver and open website", () -> {
                logger.info("Starting {} browser", browserName);
                driver = BrowserOptions.browserOptions(browserName);

                logger.debug("Maximizing browser window");
                driver.manage().window().maximize();

                logger.info("Navigating to: {}", GlobalVariable.URL);
                driver.get(GlobalVariable.URL);

                Allure.addAttachment("Browser Info", "text/plain",
                        "Browser: " + browserName + "\n" +
                                "URL: " + GlobalVariable.URL);
            });
        } catch (Exception e) {
            logger.error("Failed to initialize driver: {}", e.getMessage());
            throw e;
        }
    }
    @BeforeMethod
    public void setTestName(ITestContext context, Method method) {
        this.testName = method.getName();
        ThreadContext.put("testName", testName);
        logger.info("====== Starting test: {} ======", testName);
    }

    @AfterMethod
    public void takeScreenShotWhenFail(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String testName = result.getName();
            logger.error("Test failed: {}. Capturing screenshot...", testName);

            try {
                Allure.step("Capture screenshot for failed test", () -> {
                    Helper.attachScreenshotToAllure(driver, testName);
                    Helper.saveScreenshotLocally(driver, testName);

                    Throwable throwable = result.getThrowable();
                    if (throwable != null) {
                        Allure.addAttachment("Failure Details", "text/plain",
                                "Test: " + testName + "\n" +
                                        "Failure: " + throwable.getMessage());
                    }
                });
            } catch (Exception e) {
                logger.error("Failed to capture screenshot: {}", e.getMessage());
            }
        }

        logger.info("====== Finished test: {} (Status: {}) ======\n",
                testName,
                result.isSuccess() ? "PASSED" : "FAILED");
        ThreadContext.remove("testName");
    }

    @AfterClass
    public void stopDriver() {
        try {
            Allure.step("Close browser and cleanup", () -> {
                if (driver != null) {
                    logger.info("Closing browser");
                    driver.quit();
                    logger.debug("Browser closed successfully");
                }
            });
        } catch (Exception e) {
            logger.error("Error while closing browser: {}", e.getMessage());
        }
    }
}