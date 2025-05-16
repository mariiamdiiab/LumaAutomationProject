package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckOutPage extends PageBase {
    private static final Logger logger = LogManager.getLogger(CheckOutPage.class);

    // Locators
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
    private static final By orderId = By.cssSelector("a.order-number");

    public CheckOutPage(WebDriver driver) {
        super(driver);
        logger.debug("Initializing CheckOutPage elements");
    }

    public void checkOutWithSavedAddress() {
        try {
            logger.info("Starting checkout with saved address");
            waitForLoaderToDisappear();

            logger.debug("Waiting for Next button to be clickable");
            wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
            clickBtn(nextBtn);

            waitForLoaderToDisappear();

            logger.debug("Waiting for Place Order button to be clickable");
            wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));
            clickBtn(placeOrderBtn);

            logger.debug("Waiting for success page title");
            wait.until(ExpectedConditions.titleContains("Success Page"));

            logger.info("Checkout with saved address completed successfully");
        } catch (Exception e) {
            logger.error("Checkout with saved address failed: {}", e.getMessage());
            throw e;
        }
    }

    public void checkOutWithNewAddress(String street, String city, String state,
                                       String zip, String country, String phone) {
        try {
            logger.info("Starting checkout with new address");
            waitForLoaderToDisappear();

            logger.debug("Opening new address form");
            wait.until(ExpectedConditions.elementToBeClickable(newAddressBtn));
            clickBtn(newAddressBtn);

            logger.debug("Filling address details");
            sendTxtToTxtBox(streetAddressTxtBox, street);
            sendTxtToTxtBox(cityTxtBox, city);
            setSelect(countrySelect, country);
            sendTxtToTxtBox(stateSelect, state);
            sendTxtToTxtBox(zipCodeTxtBox, zip);
            sendTxtToTxtBox(phoneTxtBox, phone);

            logger.debug("Unchecking 'Save in address book' checkbox");
            clickBtn(checkBoxUncheck);

            logger.debug("Clicking 'Ship Here' button");
            clickBtn(shipHereBtn);

            waitForLoaderToDisappear();

            logger.debug("Proceeding to next step");
            wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
            clickBtn(nextBtn);

            waitForLoaderToDisappear();

            logger.debug("Placing final order");
            wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));
            clickBtn(placeOrderBtn);

            logger.debug("Waiting for success page to load");
            wait.until(ExpectedConditions.titleContains("Success Page"));

            logger.info("Checkout with new address completed successfully");
        } catch (Exception e) {
            logger.error("Checkout with new address failed: {}", e.getMessage());
            throw e;
        }
    }

    public String getOrderId() {
        try {
            logger.debug("Retrieving order ID");
            String id = driver.findElement(orderId).getText();
            logger.info("Retrieved order ID: {}", id);
            return id;
        } catch (Exception e) {
            logger.error("Failed to retrieve order ID: {}", e.getMessage());
            throw e;
        }
    }
}