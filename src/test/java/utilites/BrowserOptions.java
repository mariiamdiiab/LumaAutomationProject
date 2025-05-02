package utilites;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserOptions {
    public static WebDriver browserOptions(String browserName) {
        return switch (browserName.toLowerCase()) {
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-gpu");
                yield new ChromeDriver();
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions options=new EdgeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-gpu");
                yield new EdgeDriver();
            }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options=new FirefoxOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-gpu");
                yield new FirefoxDriver();
            }
            case "headless" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");
                yield new ChromeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        };
    }
}