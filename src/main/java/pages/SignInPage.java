package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class SignInPage extends PageBase{
    public SignInPage(WebDriver driver) {
        super(driver);
    }

    private static final By emailTxtBox=By.id("email");
    private static final By passwordTxtBox=By.id("pass");
    private static final By signInBtn=By.id("send2");
    private static final By errorMsg = By.cssSelector("[class*='mage-error'][generated='true']:not([style*='none'])");
    private static final By messageError=  By.cssSelector(".message-error.message");


    public void userSignIn(String email,String pass){
        sendTxtToTxtBox(emailTxtBox,email);
        sendTxtToTxtBox(passwordTxtBox,pass);
        clickBtn(signInBtn);
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
