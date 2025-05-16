package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AddProductReviewPage extends PageBase {
    private static final Logger logger = LogManager.getLogger(AddProductReviewPage.class);

    // Locators
    private static final By rating4 = By.cssSelector("label.rating-4[for='Rating_4']");
    private static final By nicknameTxtBox = By.id("nickname_field");
    private static final By summaryTxtBox = By.id("summary_field");
    private static final By reviewTxtBox = By.id("review_field");
    private static final By submitBtn = By.xpath("//button[@class='action submit primary']");
    private static final By successMessage = By.xpath("//div[@class='message-success success message']");
    private static final By errorMsg = By.xpath("//*[contains(@class, 'mage-error')][@generated='true'][not(contains(@style, 'none'))]");


    public AddProductReviewPage(WebDriver driver) {
        super(driver);
        logger.debug("Initializing AddProductReviewPage elements");
    }


    public void addReviewForSignedInUsers(String summary, String review) {
        logger.info("Starting review submission for signed-in user");
        submitReview(null, summary, review);
    }


    public void addReviewForGuestUsers(String nickname, String summary, String review) {
        logger.info("Starting review submission for guest user: {}", nickname);
        submitReview(nickname, summary, review);
    }


    private void submitReview(String nickname, String summary, String review) {
        try {
            logger.debug("Beginning review submission process");

            // Replace standard click with JavaScript click for the rating
            logger.debug("Attempting JavaScript click on rating");
            // Use JavaScript executor to click the element
            WebElement ratingElement = driver.findElement(rating4);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ratingElement);

            if (nickname != null) {
                logger.debug("Entering nickname for guest review");
                sendTxtToTxtBox(nicknameTxtBox, nickname);
            }

            logger.debug("Entering review summary: {}", summary);
            sendTxtToTxtBox(summaryTxtBox, summary);

            logger.debug("Entering review content");
            sendTxtToTxtBox(reviewTxtBox, review);

            logger.debug("Submitting review form");
            clickBtn(submitBtn);

            waitForLoaderToDisappear();
            logger.info("Review submitted successfully");
        } catch (Exception e) {
            logger.error("Failed to submit review: {}", e.getMessage());
            throw e;
        }
    }

    public String getSuccessMessage() {
        try {
            logger.debug("Waiting for success message");
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            String messageText = message.getText();
            logger.info("Success message displayed: {}", messageText);
            return messageText;
        } catch (TimeoutException e) {
            logger.error("Success message not displayed within expected time");
            throw e;
        }
    }


    public String getErrorMsg() {
        try {
            logger.debug("Waiting for error message");
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMsg));
            String errorText = message.getText();
            logger.warn("Error message displayed: {}", errorText);
            return errorText;
        } catch (TimeoutException e) {
            logger.error("Error message not displayed within expected time");
            throw e;
        }
    }
}