package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class SearchPage extends PageBase {
    private static final Logger logger = LogManager.getLogger(SearchPage.class);

    // Locators
    private static final By searchTxtBox = By.id("search");
    private static final By productTitleWrapper = By.className("page-title-wrapper");
    private static final By productName = By.xpath("//a[@class='product-item-link'][1]");


    public SearchPage(WebDriver driver) {
        super(driver);
        logger.debug("Initializing SearchPage elements");
    }


    public void productSearch(String productName) {
        try {
            logger.info("Searching for product: {}", productName);
            sendTxtToTxtBox(searchTxtBox, productName);
            submitTxt(searchTxtBox);
            logger.debug("Search submitted successfully");
        } catch (Exception e) {
            logger.error("Failed to search for product '{}': {}", productName, e.getMessage());
            throw e;
        }
    }


    public String getProductTitleWrapperInSearchPage() {
        try {
            logger.debug("Retrieving search results page title");
            String title = driver.findElement(productTitleWrapper).getText();
            logger.info("Search results page title: {}", title);
            return title;
        } catch (Exception e) {
            logger.error("Failed to get search results title: {}", e.getMessage());
            throw e;
        }
    }


    public String getProductName() {
        try {
            logger.debug("Waiting for product name visibility");
            wait.until(ExpectedConditions.visibilityOfElementLocated(productName));
            String name = driver.findElement(productName).getText();
            logger.info("First product name in results: {}", name);
            return name;
        } catch (Exception e) {
            logger.error("Failed to get product name: {}", e.getMessage());
            throw e;
        }
    }


    public void openProductPage() {
        try {
            logger.debug("Attempting to open product page");
            clickBtn(productName);
            logger.info("Product page opened successfully");
        } catch (Exception e) {
            logger.error("Failed to open product page: {}", e.getMessage());
            throw e;
        }
    }
}