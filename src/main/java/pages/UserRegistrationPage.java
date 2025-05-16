package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

/**
 * Page Object for user registration functionality
 * Handles both valid and invalid registration attempts
 */
public class UserRegistrationPage extends PageBase {
    private static final Logger logger = LogManager.getLogger(UserRegistrationPage.class);

    // Locators
    private static final By fnTxtBox = By.cssSelector("input#firstname");
    private static final By lnTxtBox = By.cssSelector("input#lastname");
    private static final By emailTxtBox = By.cssSelector("input#email_address");
    private static final By passwordTxtBox = By.cssSelector("input#password");
    private static final By confirmPasswordTxtBox = By.cssSelector("input#password-confirmation");
    private static final By registrationBtn = By.xpath("//button[@title='Create an Account']");
    private static final By errorMsg = By.cssSelector("[class*='mage-error'][generated='true']:not([style*='none'])");
    private static final By messageError = By.cssSelector(".message-error.message");

    /**
     * Initializes page elements
     * @param driver WebDriver instance
     */
    public UserRegistrationPage(WebDriver driver) {
        super(driver);
        logger.debug("Initializing UserRegistrationPage elements");
    }

    /**
     * Attempts user registration with valid data
     * @param firstName User's first name
     * @param lastName User's last name
     * @param email User's email address
     * @param password User's password
     * @throws ElementNotInteractableException if fields are not interactable
     */
    public void userRegistration(String firstName, String lastName, String email, String password) {
        try {
            logger.info("Attempting sign-in with email: {}", email);
            logger.debug("Entering email into email field");
            sendTxtToTxtBox(emailTxtBox, email);

            logger.debug("Entering first name");
            sendTxtToTxtBox(fnTxtBox, firstName);

            logger.debug("Entering last name");
            sendTxtToTxtBox(lnTxtBox, lastName);

            logger.debug("Entering email");
            sendTxtToTxtBox(emailTxtBox, email);

            logger.info("Starting registration with password: {}", maskSensitiveData(password));
            logger.debug("Entering password into password field");
            sendTxtToTxtBox(passwordTxtBox, password);

            logger.info("Confirming registration with password: {}", maskSensitiveData(password));
            logger.debug("Confirming password");
            sendTxtToTxtBox(confirmPasswordTxtBox, password);

            logger.debug("Submitting registration form");
            clickBtn(registrationBtn);

            logger.info("Registration form submitted successfully");
        } catch (Exception e) {
            logger.error("Registration failed for email {}: {}", email, e.getMessage());
            throw e;
        }
    }


    public void userRegistrationInvalidData(String firstName, String lastName, String email,
                                            String password, String confirmPass) {
        try {
            logger.warn("Attempting registration with potentially invalid data for email: {}", email);

            logger.debug("Entering first name: {}", firstName);
            sendTxtToTxtBox(fnTxtBox, firstName);

            logger.debug("Entering last name: {}", lastName);
            sendTxtToTxtBox(lnTxtBox, lastName);

            logger.debug("Entering email: {}", email);
            sendTxtToTxtBox(emailTxtBox, email);

            logger.debug("Entering password");
            sendTxtToTxtBox(passwordTxtBox, password);

            logger.debug("Entering confirm password");
            sendTxtToTxtBox(confirmPasswordTxtBox, confirmPass);

            logger.debug("Submitting registration form");
            clickBtn(registrationBtn);

            logger.warn("Invalid registration attempt submitted");
        } catch (Exception e) {
            logger.error("Invalid registration attempt failed for email {}: {}", email, e.getMessage());
            throw e;
        }
    }


    public String getErrorMsg() {
        try {
            logger.debug("Checking for registration error messages");

            List<WebElement> mageErrors = driver.findElements(errorMsg);
            if (!mageErrors.isEmpty() && mageErrors.getFirst().isDisplayed()) {
                String error = mageErrors.getFirst().getText().trim();
                logger.warn("Field validation error detected: {}", error);
                return error;
            }

            WebElement messageErrorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(messageError));
            String error = messageErrorElement.getText().trim();
            logger.warn("General registration error detected: {}", error);
            return error;

        } catch (TimeoutException e) {
            logger.error("No error message displayed within expected time");
            throw e;
        }
    }
}