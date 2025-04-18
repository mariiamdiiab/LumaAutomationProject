package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PageBase {
    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    public JavascriptExecutor jse ;


    public PageBase(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    protected void clickBtn(By element){
        driver.findElement(element).click();
    }
    protected void submitTxt(By element){
        driver.findElement(element).submit();
    }
    protected void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading-mask")));
    }



    public void sendTxtToTxtBox(By element, String value){
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        driver.findElement(element).sendKeys(value);
    }

    public void setSelect(By element,String value){
        Select item=new Select(driver.findElement(element));
        item.selectByVisibleText(value);
    }

    public void scrollToElement(By elementLocator) {
        WebElement element=driver.findElement(elementLocator);
        jse.executeScript("arguments[0].scrollIntoView(true);", element);

        // Optional: Add some offset from top (in pixels) if needed
        // jse.executeScript("window.scrollBy(0, -100);");
    }

}
