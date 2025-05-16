package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;


public class SignInPage extends PageBase {
    private static final Logger logger = LogManager.getLogger(SignInPage.class);

    // Locators
    private static final By emailTxtBox = By.id("email");
    private static final By passwordTxtBox = By.id("pass");
    private static final By signInBtn = By.id("send2");
    private static final By errorMsg = By.cssSelector("[class*='mage-error'][generated='true']:not([style*='none'])");
    private static final By messageError = By.cssSelector(".message-error.message");

       public SignInPage(WebDriver driver) {
        super(driver);
        logger.debug("Initializing SignInPage elements");
    }


    public void userSignIn(String email, String pass) {
        try {
            logger.info("Attempting sign-in with email: {}", email);
            logger.debug("Entering email into email field");
            sendTxtToTxtBox(emailTxtBox, email);


            logger.info("Starting sign-in with password: {}", maskSensitiveData(pass));
            logger.debug("Entering password into password field");
            sendTxtToTxtBox(passwordTxtBox, pass);

            logger.debug("Clicking sign-in button");
            clickBtn(signInBtn);

            logger.info("Sign-in form submitted successfully");
        } catch (Exception e) {
            logger.error("Sign-in attempt failed for email {}: {}", email, e.getMessage());
            throw e;
        }
    }


    public String getErrorMsg() {
        try {
            logger.debug("Checking for sign-in error messages");

            List<WebElement> mageErrors = driver.findElements(errorMsg);
            if (!mageErrors.isEmpty() && mageErrors.getFirst().isDisplayed()) {
                String error = mageErrors.getFirst().getText().trim();
                logger.warn("Field validation error detected: {}", error);
                return error;
            }

            WebElement messageErrorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(messageError));
            String error = messageErrorElement.getText().trim();
            logger.warn("General sign-in error detected: {}", error);
            return error;

        } catch (TimeoutException e) {
            logger.error("No error message displayed within expected time");
            throw e;
        }
    }

}