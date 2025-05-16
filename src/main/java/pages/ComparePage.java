package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.ArrayList;
import java.util.List;

public class ComparePage extends PageBase {
    private static final Logger logger = LogManager.getLogger(ComparePage.class);

    // Locators
    private static final By openComparePageBtn = By.partialLinkText("Compare Products");
    private static final By clearItemBtn = By.cssSelector("a.action.delete");
    private static final By noProductToCompareMsg = By.cssSelector(".message.info.empty");
    private static final By acceptRemoveItem = By.cssSelector("button.action-primary.action-accept");
    private static final By productName = By.cssSelector("strong.product-item-name a");

    public ComparePage(WebDriver driver) {
        super(driver);
        logger.debug("Initializing ComparePage elements");
    }

    public void openComparePage() {
        try {
            logger.info("Opening compare products page");
            wait.until(ExpectedConditions.visibilityOfElementLocated(openComparePageBtn));
            clickBtn(openComparePageBtn);
            logger.info("Compare products page opened successfully");
        } catch (Exception e) {
            logger.error("Failed to open compare page: {}", e.getMessage());
            throw e;
        }
    }

    public List<String> getComparedProductNames() {
        try {
            logger.debug("Retrieving names of compared products");
            List<WebElement> productNameElements = driver.findElements(productName);
            List<String> productNames = new ArrayList<>();

            for (WebElement element : productNameElements) {
                String name = element.getText().trim();
                productNames.add(name);
                logger.trace("Found product in comparison: {}", name);
            }

            logger.info("Retrieved {} products from comparison table", productNames.size());
            return productNames;
        } catch (Exception e) {
            logger.error("Failed to get compared product names: {}", e.getMessage());
            throw e;
        }
    }

    public void clearCompareTable() {
        try {
            logger.info("Clearing comparison table");
            for (int i = 0; i < 2; i++) {
                if (!driver.findElements(clearItemBtn).isEmpty()) {
                    logger.debug("Removing item {} from comparison", i+1);
                    clickBtn(clearItemBtn);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(acceptRemoveItem));
                    clickBtn(acceptRemoveItem);
                    logger.debug("Item removal confirmed");
                } else {
                    logger.debug("No more items to remove");
                    break;
                }
            }
            logger.info("Comparison table cleared successfully");
        } catch (Exception e) {
            logger.error("Failed to clear comparison table: {}", e.getMessage());
            throw e;
        }
    }

    public String getNoProductToCompareMsg() {
        try {
            logger.debug("Checking for empty comparison message");
            String message = driver.findElement(noProductToCompareMsg).getText();
            logger.info("Retrieved empty comparison message: {}", message);
            return message;
        } catch (Exception e) {
            logger.error("Failed to get empty comparison message: {}", e.getMessage());
            throw e;
        }
    }
}