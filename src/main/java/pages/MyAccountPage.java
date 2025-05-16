package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for My Account section, handling success messages and order history
 */
public class MyAccountPage extends PageBase {
    private static final Logger logger = LogManager.getLogger(MyAccountPage.class);

    // Locators
    private final By successMessage = By.cssSelector("div.message-success > div");
    private final By orderHistoryId = By.cssSelector("td.col.id");
    private final By orderTable = By.id("my-orders-table");


    public MyAccountPage(WebDriver driver) {
        super(driver);
        jse = (JavascriptExecutor) driver;
        logger.debug("Initializing MyAccountPage elements");
    }


    public String getSuccessMessage() {
        logger.debug("Waiting for success message visibility");
        try {
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(successMessage));
            String message = driver.findElement(successMessage).getText();
            logger.info("Success message retrieved: {}", message);
            return message;
        } catch (TimeoutException e) {
            logger.error("Success message did not appear within expected time");
            throw e;
        }
    }


    public List<String> getOrderIDs() {
        logger.debug("Starting order ID collection");
        List<WebElement> orderIdElements = driver.findElements(orderHistoryId);
        List<String> orderIds = new ArrayList<>();

        if (orderIdElements.isEmpty()) {
            logger.warn("No order IDs found in order history");
        } else {
            logger.debug("Found {} order elements", orderIdElements.size());
        }

        for (WebElement element : orderIdElements) {
            String orderId = element.getText().trim();
            orderIds.add(orderId);
            logger.trace("Processing order ID: {}", orderId); // Verbose logging
        }

        logger.debug("Scrolling to order table for visibility");
        scrollToElement(orderTable);
        logger.info("Completed order ID collection. Found {} orders", orderIds.size());
        return orderIds;
    }
}