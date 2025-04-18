package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartPage extends PageBase{
    public ShoppingCartPage(WebDriver driver) {
        super(driver);
    }
    // Updated locators to match your HTML
    private static final By productName = By.cssSelector("strong.product-item-name a");
    private static final By clearItemBtn = By.cssSelector("a.action-delete");
    private static final By row = By.tagName("tr");
    private static final By emptyMessage=By.className("cart-empty");
    private static final By checkOutBtn = By.cssSelector("button.action.primary.checkout[data-role='proceed-to-checkout']");




    public void deleteItemByName(String product) {
        try {
            // Wait for cart to be ready
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("shopping-cart-table")));

            // Find the specific product row
            WebElement productRow = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//tr[contains(@class,'item-info')][.//a[normalize-space()='" + product + "']]")
            ));

            // Find and click the delete button
            WebElement deleteBtn = productRow.findElement(
                    By.xpath("./following-sibling::tr//a[contains(@class,'action-delete')]")
            );

            // Scroll and click
            ((JavascriptExecutor)driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
                    deleteBtn
            );
            deleteBtn.click();

            // Wait for the product to be removed from DOM
            wait.until(ExpectedConditions.stalenessOf(productRow));

            // Additional wait for cart to update
            wait.until(ExpectedConditions.invisibilityOfElementWithText(
                    By.xpath("//tr[contains(@class,'item-info')]//a[normalize-space()='" + product + "']"),
                    product
            ));

        } catch (TimeoutException e) {
            throw new RuntimeException("Failed to delete product '" + product + "'", e);
        }
    }


    public List<String> getCartProductNames() {
        wait.until(ExpectedConditions.titleContains("Shopping Cart"));
        List<WebElement> productNameElements = driver.findElements(productName);
        List<String> productNames = new ArrayList<>();
        for (WebElement element : productNameElements) {
            productNames.add(element.getText().trim());
        }
        return productNames;
    }



    public void clearCart() {
        List<WebElement> rowSize=driver.findElements(row);
        int getRowSize=rowSize.size();
        for (int i = 0; i < getRowSize; i++) {
            if (!driver.findElements(clearItemBtn).isEmpty()) {
                clickBtn(clearItemBtn);
            } else {
                break;
            }
        }
    }


    public String getCartEmptyMessage(){
        return driver.findElement(emptyMessage).getText();
    }

    public void goToCheckOut(){
        clickBtn(checkOutBtn);
    }


}
