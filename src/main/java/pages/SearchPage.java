package pages;

import org.openqa.selenium.*;



public class SearchPage extends PageBase {
    public SearchPage(WebDriver driver) {
        super(driver);
    }

    private static final By searchTxtBox = By.id("search");
    private static final By productTitleWrapper = By.className("page-title-wrapper");
    private static final By productName = By.xpath("//a[@class='product-item-link'][1]");

    public void productSearch(String productName) {
        sendTxtToTxtBox(searchTxtBox, productName);
        submitTxt(searchTxtBox);
    }

    public String getProductTitleWrapperInSearchPage() {
        return driver.findElement(productTitleWrapper).getText();
    }

    public String getProductName() {
        return driver.findElement(productName).getText();
    }

    public void openProductPage() {
        clickBtn(productName);
    }

}
