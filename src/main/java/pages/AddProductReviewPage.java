package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AddProductReviewPage extends PageBase {
    public AddProductReviewPage(WebDriver driver) {
        super(driver);
    }

    // Using more specific CSS selector for the rating label
    private static final By rating4 = By.cssSelector("label.rating-4[for='Rating_4']");
    private static final By nicknameTxtBox = By.id("nickname_field");
    private static final By summaryTxtBox = By.id("summary_field");
    private static final By reviewTxtBox = By.id("review_field");
    private static final By submitBtn = By.xpath("//button[@class='action submit primary']");
    private static final By successMessage=By.xpath("//div[@class='message-success success message']");
    private By errorMsg = By.xpath("//*[contains(@class, 'mage-error')][@generated='true'][not(contains(@style, 'none'))]");



    public void addReviewForSignedInUsers(String summary, String review) {
        safeClickRating();
        sendTxtToTxtBox(summaryTxtBox, summary);
        sendTxtToTxtBox(reviewTxtBox, review);
        clickBtn(submitBtn);
    }

    public void addReviewForGuestUsers(String nickname, String summary, String review) {
        safeClickRating();
        sendTxtToTxtBox(nicknameTxtBox, nickname);
        sendTxtToTxtBox(summaryTxtBox, summary);
        sendTxtToTxtBox(reviewTxtBox, review);
        clickBtn(submitBtn);
    }

    private void safeClickRating() {
        try {
            clickBtn(rating4);
        } catch (ElementClickInterceptedException e) {
            WebElement ratingElement = driver.findElement(rating4);
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('click', {" +
                            "view: window," +
                            "bubbles: true," +
                            "cancelable: true" +
                            "}));",
                    ratingElement
            );

        }
    }

    public String getSuccessMessage(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
        return driver.findElement(successMessage).getText();
    }

        public String getErrorMsg(){
            return driver.findElement(errorMsg).getText();
        }
}