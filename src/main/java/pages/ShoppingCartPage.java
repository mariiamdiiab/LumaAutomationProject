package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartPage extends PageBase {
    private static final Logger logger = LogManager.getLogger(ShoppingCartPage.class);

    // Locators
    private static final By productName = By.cssSelector("strong.product-item-name a");
    private static final By clearItemBtn = By.cssSelector("a.action-delete");
    private static final By row = By.tagName("tr");
    private static final By emptyMessage = By.className("cart-empty");
    private static final By checkOutBtn = By.cssSelector("button.action.primary.checkout[data-role='proceed-to-checkout']");
    private static final By cartTable = By.id("shopping-cart-table");

    public ShoppingCartPage(WebDriver driver) {
        super(driver);
        logger.debug("Initializing ShoppingCartPage elements");
    }

    public void deleteItemByName(String product) {
        try {
            logger.info("Attempting to delete product: {}", product);
            wait.until(ExpectedConditions.presenceOfElementLocated(cartTable));

            String xpath = "//tr[contains(@class,'item-info')][.//a[normalize-space()='" + product + "']]";
            WebElement productRow = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            logger.debug("Found product row for: {}", product);

            WebElement deleteBtn = productRow.findElement(
                    By.xpath("./following-sibling::tr//a[contains(@class,'action-delete')]"));
            logger.debug("Located delete button for product");

            ((JavascriptExecutor)driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
                    deleteBtn
            );
            deleteBtn.click();
            logger.debug("Clicked delete button");

            wait.until(ExpectedConditions.stalenessOf(productRow));
            wait.until(ExpectedConditions.invisibilityOfElementWithText(
                    By.xpath("//tr[contains(@class,'item-info')]//a[normalize-space()='" + product + "']"),
                    product
            ));
            logger.info("Successfully deleted product: {}", product);

        } catch (TimeoutException e) {
            logger.error("Failed to delete product '{}': {}", product, e.getMessage());
            throw new RuntimeException("Failed to delete product '" + product + "'", e);
        }
    }

    public List<String> getCartProductNames() {
        try {
            logger.debug("Retrieving product names from cart");
            wait.until(ExpectedConditions.titleContains("Shopping Cart"));
            List<WebElement> productNameElements = driver.findElements(productName);
            List<String> productNames = new ArrayList<>();

            for (WebElement element : productNameElements) {
                productNames.add(element.getText().trim());
            }

            logger.info("Found {} products in cart: {}", productNames.size(), productNames);
            return productNames;

        } catch (Exception e) {
            logger.error("Failed to retrieve product names: {}", e.getMessage());
            throw e;
        }
    }

    public void clearCart() {
        try {
            logger.info("Starting cart clearance");
            List<WebElement> rowSize = driver.findElements(row);
            int getRowSize = rowSize.size();
            logger.debug("Found {} items to remove", getRowSize);

            for (int i = 0; i < getRowSize; i++) {
                if (!driver.findElements(clearItemBtn).isEmpty()) {
                    clickBtn(clearItemBtn);
                    logger.debug("Removed item {} of {}", i+1, getRowSize);
                } else {
                    logger.debug("No more items to remove");
                    break;
                }
            }
            logger.info("Cart cleared successfully");

        } catch (Exception e) {
            logger.error("Failed to clear cart: {}", e.getMessage());
            throw e;
        }
    }

    public String getCartEmptyMessage() {
        try {
            logger.debug("Checking for empty cart message");
            String message = driver.findElement(emptyMessage).getText();
            logger.info("Empty cart message: {}", message);
            return message;
        } catch (Exception e) {
            logger.error("Failed to get empty cart message: {}", e.getMessage());
            throw e;
        }
    }

    public void goToCheckOut() {
        try {
            logger.info("Proceeding to checkout");
            clickBtn(checkOutBtn);
            logger.debug("Clicked checkout button");
        } catch (Exception e) {
            logger.error("Failed to proceed to checkout: {}", e.getMessage());
            throw e;
        }
    }
}