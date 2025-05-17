package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
public class PageBase {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected JavascriptExecutor jse;
    protected static final Logger log = LogManager.getLogger();
    protected static final Duration DEFAULT_WAIT_TIMEOUT = Duration.ofSeconds(20);
    protected static final Duration POLLING_INTERVAL = Duration.ofMillis(500);

    public PageBase(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_WAIT_TIMEOUT, POLLING_INTERVAL);
        this.actions = new Actions(driver);
        this.jse = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    // region Original Methods (unchanged signatures)

    protected void clickBtn(By element) {
        try {
            WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(element));
            highlightElement(webElement);
            webElement.click();
            log.info("Clicked button: {}", element);
        } catch (Exception e) {
            log.error("Failed to click button: {}", element, e);
            throw e;
        }
    }

    protected void submitTxt(By element) {
        try {
            WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            highlightElement(webElement);
            webElement.submit();
            log.info("Submitted text field: {}", element);
        } catch (Exception e) {
            log.error("Failed to submit text field: {}", element, e);
            throw e;
        }
    }

    protected void waitForLoaderToDisappear() {
        try {
            log.info("Waiting for loader to disappear");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading-mask")));
        } catch (Exception e) {
            log.error("Loader did not disappear", e);
            throw e;
        }
    }

    public void sendTxtToTxtBox(By element, String value) {
        try {
            WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            highlightElement(webElement);
            webElement.clear();
            webElement.sendKeys(value);
            log.info("Sent text '{}' to text box: {}", value, element);
        } catch (Exception e) {
            log.error("Failed to send text to text box: {}", element, e);
            throw e;
        }
    }

    public void setSelect(By element, String value) {
        try {
            WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            highlightElement(webElement);
            Select dropdown = new Select(webElement);
            dropdown.selectByVisibleText(value);
            log.info("Selected '{}' from dropdown: {}", value, element);
        } catch (Exception e) {
            log.error("Failed to set select: {}", element, e);
            throw e;
        }
    }

    public void scrollToElement(By elementLocator) {
        try {
            WebElement element = driver.findElement(elementLocator);
            jse.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            log.info("Scrolled to element: {}", elementLocator);
        } catch (Exception e) {
            log.error("Failed to scroll to element: {}", elementLocator, e);
            throw e;
        }
    }
    // endregion

    // region New Utility Methods

    /**
     * Highlights an element with a red border (for debugging)
     */
    protected void highlightElement(WebElement element) {
        if (log.isDebugEnabled()) {
            jse.executeScript(
                    "arguments[0].style.border='2px solid red'; arguments[0].style.boxShadow='0 0 5px red';",
                    element
            );
        }
    }



    protected String getText(By element) {
        try {
            WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            return webElement.getText();
        } catch (Exception e) {
            log.error("Failed to get text from element: {}", element, e);
            throw e;
        }
    }

    String maskSensitiveData(String sensitiveValue) {
        if (sensitiveValue == null || sensitiveValue.isEmpty()) {
            return "[EMPTY]";
        }
         return sensitiveValue.replaceAll("\\.", "*");
    }
}