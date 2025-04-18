package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class ComparePage extends PageBase {
    // Locators should remain static final
    private static final By openComparePageBtn = By.partialLinkText("Compare Products");
    private static final By clearItemBtn = By.cssSelector("a.action.delete");
    //    private static final By col = By.tagName("td");
    private static final By noProductToCompareMsg = By.cssSelector(".message.info.empty");
    private static final By acceptRemoveItem = By.cssSelector("button.action-primary.action-accept");
    private static final By productName=By.cssSelector("strong.product-item-name a");



    public ComparePage(WebDriver driver) {
        super(driver);
    }

    public void openComparePage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(openComparePageBtn));
        clickBtn(openComparePageBtn);
    }



    public List<String> getComparedProductNames() {
        List<WebElement> productNameElements = driver.findElements(productName);
        List<String> productNames = new ArrayList<>();
        for (WebElement element : productNameElements) {
            productNames.add(element.getText().trim());
        }
        return productNames;
    }




    public void clearCompareTable() {
        for (int i = 0; i < 2; i++) {
            if (!driver.findElements(clearItemBtn).isEmpty()) {
                clickBtn(clearItemBtn);
                wait.until(ExpectedConditions.visibilityOfElementLocated(acceptRemoveItem));
                clickBtn(acceptRemoveItem);
            } else {
                break;
            }
        }
    }


    public String getNoProductToCompareMsg(){
        return driver.findElement(noProductToCompareMsg).getText();
    }
}