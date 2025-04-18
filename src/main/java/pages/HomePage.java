package pages;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends PageBase{
    public HomePage(WebDriver driver) {
        super(driver);
        actions=new Actions(driver);

    }
    private static final By registerLink=By.linkText("Create an Account");
    private static final By signInLink= By.linkText("Sign In");
    private static final By welcomeTxt=By.className("logged-in");
    private static final By womenLink=By.id("ui-id-4");
    private static final By topsLink=By.id("ui-id-9");
    private static final By jacketsLink=By.id("ui-id-11");
    private static final By pageTitle=By.xpath("//div[@class='page-title-wrapper']");
    private static final By WelcomeBtn=By.xpath("//button[@class='action switch'][1]");
    private static final By SignOutBtn=By.linkText("Sign Out");
    private static final By welcomeMsg=By.xpath("//li[@class='greet welcome'][1]");
    private static final By MyAccountLink=By.linkText("My Account");



    public void openRegistrationPage(){
        clickBtn(registerLink);
    }

    public void openSignInPage(){
        clickBtn(signInLink);
    }
    public String getWelcomeMessage(){
        wait.until(ExpectedConditions.presenceOfElementLocated(welcomeTxt));
        return driver.findElement(welcomeTxt).getText();
    }
    public String getPageTitle(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
        return driver.findElement(pageTitle).getText();
    }


    public void selectJacketMenu(){
        actions.moveToElement(driver.findElement(womenLink))
                .moveToElement(driver.findElement(topsLink))
                .moveToElement(driver.findElement(jacketsLink))
                .click()
                .build()
                .perform();
    }

    public void SignOut(){
        clickBtn(WelcomeBtn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(SignOutBtn));
        clickBtn(SignOutBtn);
    }
    public String getWelcome(){
        return driver.findElement(welcomeMsg).getText();
    }


    public void openMyAccountPage(){
        clickBtn(WelcomeBtn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(MyAccountLink));
        clickBtn(MyAccountLink);

    }
}
