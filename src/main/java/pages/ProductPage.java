package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class ProductPage extends PageBase {
    public ProductPage(WebDriver driver) {
        super(driver);
    }

    // Corrected locators
    private static final By addReviewBtn = By.xpath("//a[contains(@class,'action') and contains(@class,'add')]");
    private static final By addToWishListBtn = By.linkText("Add to Wish List");
    private static final By addToCompareBtn = By.xpath("//a[@data-role='add-to-links']");
    private static final By chooseSize = By.xpath("(//div[contains(@class,'swatch-option text')])[1]");
    private static final By chooseColor = By.xpath("(//div[contains(@class,'swatch-option color')])[1]");
    private static final By addToCartBtn = By.id("product-addtocart-button");
    private static final By openCartLink=By.linkText("shopping cart");

    public void addProductReview() {
        clickBtn(addReviewBtn);
    }

//    public void addProductToWishList() {
//        clickBtn(addToWishListBtn);
//    }

    public void addToCompare() {
        clickBtn(addToCompareBtn);
    }

    public void addToCart() {
        clickBtn(chooseSize);
        clickBtn(chooseColor);
        clickBtn(addToCartBtn);
    }


    public void openCartFromLink() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(openCartLink));
        clickBtn(openCartLink);
    }
}