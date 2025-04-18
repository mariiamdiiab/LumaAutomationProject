package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class CheckOutPage extends PageBase{
    public CheckOutPage(WebDriver driver) {
        super(driver);
    }

    private static final By nextBtn = By.cssSelector("button[data-role='opc-continue']");
    private static final By placeOrderBtn = By.cssSelector("button[title='Place Order']");
    private static final By newAddressBtn = By.cssSelector("button.action.action-show-popup");
    private static final By streetAddressTxtBox = By.cssSelector("input[name='street[0]']");
    private static final By cityTxtBox = By.cssSelector("input[name='city']");
    private static final By stateSelect = By.cssSelector("input[name='region']");
    private static final By zipCodeTxtBox = By.cssSelector("input[name='postcode']");
    private static final By countrySelect = By.cssSelector("select[name='country_id']");
    private static final By phoneTxtBox = By.cssSelector("input[name='telephone']");
    private static final By checkBoxUncheck = By.cssSelector("input#shipping-save-in-address-book");
    private static final By shipHereBtn = By.cssSelector("button.action-save-address");
    private static final By orderId=By.cssSelector("a.order-number");




    public void checkOutWithSavedAddress(){
        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
        clickBtn(nextBtn);
        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));
        clickBtn(placeOrderBtn);
        wait.until(ExpectedConditions.titleContains("Success Page"));
    }


    public void checkOutWithNewAddress(String street, String city, String state, String zip, String country, String phone) {
        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.elementToBeClickable(newAddressBtn));
        clickBtn(newAddressBtn);

        sendTxtToTxtBox(streetAddressTxtBox, street);
        sendTxtToTxtBox(cityTxtBox, city);
        setSelect(countrySelect, country);
        sendTxtToTxtBox(stateSelect, state);
        sendTxtToTxtBox(zipCodeTxtBox, zip);
        sendTxtToTxtBox(phoneTxtBox, phone);
        clickBtn(checkBoxUncheck);
        clickBtn(shipHereBtn);

        waitForLoaderToDisappear(); // <== important here
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
        clickBtn(nextBtn);

        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));
        clickBtn(placeOrderBtn);

        wait.until(ExpectedConditions.titleContains("Success Page"));
    }

    public  String getOrderId(){
        return driver.findElement(orderId).getText();
    }



}
