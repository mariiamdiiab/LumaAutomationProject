package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class UserRegistrationPage extends PageBase {
    // Use more reliable locators
    private static final By fnTxtBox = By.cssSelector("input#firstname");
    private static final By lnTxtBox = By.cssSelector("input#lastname");
    private static final By emailTxtBox = By.cssSelector("input#email_address");
    private static final By passwordTxtBox = By.cssSelector("input#password");
    private static final By confirmPasswordTxtBox = By.cssSelector("input#password-confirmation");
    private static final By registrationBtn=By.xpath("//button[@title='Create an Account']");
    private static final By errorMsg = By.cssSelector("[class*='mage-error'][generated='true']:not([style*='none'])");
    private static final By messageError=  By.cssSelector(".message-error.message");

    public UserRegistrationPage(WebDriver driver) {
        super(driver);
    }

    public void userRegistration(String firstName, String lastName, String email, String password) {
        sendTxtToTxtBox(fnTxtBox, firstName);
        sendTxtToTxtBox(lnTxtBox, lastName);
        sendTxtToTxtBox(emailTxtBox, email);
        sendTxtToTxtBox(passwordTxtBox, password);
        sendTxtToTxtBox(confirmPasswordTxtBox, password);
        clickBtn(registrationBtn);
    }

    public void userRegistrationInvalidData(String firstName, String lastName, String email, String password,String confirmPass) {
        sendTxtToTxtBox(fnTxtBox, firstName);
        sendTxtToTxtBox(lnTxtBox, lastName);
        sendTxtToTxtBox(emailTxtBox, email);
        sendTxtToTxtBox(passwordTxtBox, password);
        sendTxtToTxtBox(confirmPasswordTxtBox, confirmPass);
        clickBtn(registrationBtn);
    }

    public String getErrorMsg() {
            List<WebElement> mageErrors = driver.findElements(errorMsg);
            if (!mageErrors.isEmpty() && mageErrors.getFirst().isDisplayed()) {
                return mageErrors.getFirst().getText().trim();
            }
            WebElement messageErrorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(messageError));
            return messageErrorElement.getText().trim();

        }
    }

