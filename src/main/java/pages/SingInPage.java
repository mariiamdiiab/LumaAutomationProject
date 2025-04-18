package pages;

import org.openqa.selenium.*;

public class SingInPage extends PageBase{
    public SingInPage(WebDriver driver) {
        super(driver);
    }

    private static final By emailTxtBox=By.id("email");
    private static final By passwordTxtBox=By.id("pass");
    private static final By signInBtn=By.id("send2");


    public void userSignIn(String email,String pass){
        sendTxtToTxtBox(emailTxtBox,email);
        sendTxtToTxtBox(passwordTxtBox,pass);
        clickBtn(signInBtn);

    }

}
