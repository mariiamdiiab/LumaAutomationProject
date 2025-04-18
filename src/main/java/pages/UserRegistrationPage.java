package pages;

import org.openqa.selenium.*;

public class UserRegistrationPage extends PageBase {
    // Use more reliable locators
    private static final By fnTxtBox = By.cssSelector("input#firstname");
    private static final By lnTxtBox = By.cssSelector("input#lastname");
    private static final By emailTxtBox = By.cssSelector("input#email_address");
    private static final By passwordTxtBox = By.cssSelector("input#password");
    private static final By confirmPasswordTxtBox = By.cssSelector("input#password-confirmation");
    private static final By registrationBtn=By.xpath("//button[@title='Create an Account']");
    private static final By errorMsg=By.className("mage-error");

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

    public String getErrorMsg(){
        return driver.findElement(errorMsg).getText();
    }
}