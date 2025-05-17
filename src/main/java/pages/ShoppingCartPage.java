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

            // Use a more dynamic approach with a while loop instead of a for loop
            while (!driver.findElements(clearItemBtn).isEmpty()) {
                try {
                    WebElement deleteButton = wait.until(
                            ExpectedConditions.elementToBeClickable(clearItemBtn));

                    // Use JavaScript to click for better reliability
                    ((JavascriptExecutor)driver).executeScript("arguments[0].click();", deleteButton);
                    logger.debug("Removed an item from cart");

                    // Add a brief wait for the page to update
                    Thread.sleep(500);

                    // Wait for page to stabilize
                    wait.until(driver -> {
                        try {
                            return driver.findElements(clearItemBtn).isEmpty() ||
                                    driver.findElement(clearItemBtn).isDisplayed();
                        } catch (StaleElementReferenceException e) {
                            return false; // Page is still updating
                        }
                    });

                } catch (TimeoutException e) {
                    // This might happen if all items are suddenly gone
                    logger.debug("No more clickable delete buttons found");
                    break;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.warn("Interrupted while waiting for page update");
                }
            }

            // Final verification
            if (driver.findElements(clearItemBtn).isEmpty()) {
                logger.info("Cart cleared successfully");
            } else {
                logger.warn("Some items may remain in the cart");
            }

        } catch (Exception e) {
            logger.error("Failed to clear cart: {}", e.getMessage(), e);
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