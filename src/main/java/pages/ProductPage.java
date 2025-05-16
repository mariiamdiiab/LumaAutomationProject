package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends PageBase {
    private static final Logger logger = LogManager.getLogger(ProductPage.class);

    // Locators
    private static final By addReviewBtn = By.xpath("//a[contains(@class,'action') and contains(@class,'add')]");
    private static final By addToCompareBtn = By.xpath("//a[@data-role='add-to-links']");
    private static final By chooseSize = By.xpath("(//div[contains(@class,'swatch-option text')])[1]");
    private static final By chooseColor = By.xpath("(//div[contains(@class,'swatch-option color')])[1]");
    private static final By addToCartBtn = By.id("product-addtocart-button");
    private static final By openCartLink = By.linkText("shopping cart");
    private static final By errorMsg = By.xpath("//*[contains(@class, 'mage-error')][@generated='true'][not(contains(@style, 'none'))]");

    public ProductPage(WebDriver driver) {
        super(driver);
        logger.debug("Initializing ProductPage elements");
    }

    public void addProductReview() {
        try {
            logger.info("Attempting to add product review");
            clickBtn(addReviewBtn);
            logger.info("Product review button clicked successfully");
        } catch (Exception e) {
            logger.error("Failed to add product review: {}", e.getMessage());
            throw e;
        }
    }

    public void addToCompare() {
        try {
            logger.info("Attempting to add product to compare list");
            clickBtn(addToCompareBtn);
            logger.info("Product added to compare list successfully");
        } catch (Exception e) {
            logger.error("Failed to add product to compare list: {}", e.getMessage());
            throw e;
        }
    }

    public void addToCart() {
        try {
            logger.info("Starting process to add product to cart");

            logger.debug("Selecting product size");
            clickBtn(chooseSize);

            logger.debug("Selecting product color");
            clickBtn(chooseColor);

            logger.debug("Clicking add to cart button");
            clickBtn(addToCartBtn);

            logger.info("Product added to cart successfully");
        } catch (Exception e) {
            logger.error("Failed to add product to cart: {}", e.getMessage());
            throw e;
        }
    }

    public void openCartFromLink() {
        try {
            logger.info("Attempting to open shopping cart");
            wait.until(ExpectedConditions.visibilityOfElementLocated(openCartLink));
            clickBtn(openCartLink);
            logger.info("Shopping cart opened successfully");
        } catch (Exception e) {
            logger.error("Failed to open shopping cart: {}", e.getMessage());
            throw e;
        }
    }
}