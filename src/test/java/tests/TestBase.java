package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;
import utilites.BrowserOptions;

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

    @AfterClass
    public void stopDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}