package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class MyAccountPage extends PageBase{
    public MyAccountPage(WebDriver driver) {
        super(driver);
        jse = (JavascriptExecutor) driver;

    }

    private final By successMessage = By.cssSelector("div.message-success > div");
    private final By orderHistoryId=By.cssSelector("td.col.id");
    private final By orderTable=By.id("my-orders-table");


    public String getSuccessMessage(){
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(successMessage));
        return driver.findElement(successMessage).getText();
    }

    public List<String> getOrderIDs(){
        List<WebElement> orderIdElement=driver.findElements(orderHistoryId);
        List<String> orderId=new ArrayList<>();
        for(WebElement element:orderIdElement){
            orderId.add(element.getText().trim());
        }
        scrollToElement(orderTable);
        return orderId;
    }
}
