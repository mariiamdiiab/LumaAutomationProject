package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends PageBase {
    private static final Logger logger = LogManager.getLogger(HomePage.class);
    private final Actions actions;

    // Locators
    private static final By registerLink = By.linkText("Create an Account");
    private static final By signInLink = By.linkText("Sign In");
    private static final By welcomeTxt = By.className("logged-in");
    private static final By womenLink = By.id("ui-id-4");
    private static final By topsLink = By.id("ui-id-9");
    private static final By jacketsLink = By.id("ui-id-11");
    private static final By pageTitle = By.xpath("//div[@class='page-title-wrapper']");
    private static final By WelcomeBtn = By.xpath("//button[@class='action switch'][1]");
    private static final By SignOutBtn = By.linkText("Sign Out");
    private static final By welcomeMsg = By.xpath("//li[@class='greet welcome'][1]");
    private static final By MyAccountLink = By.linkText("My Account");

    public HomePage(WebDriver driver) {
        super(driver);
        this.actions = new Actions(driver);
        logger.debug("Initialized HomePage with driver: {}", driver);
    }

    public void openRegistrationPage() {
        try {
            logger.info("Opening registration page");
            clickBtn(registerLink);
            logger.debug("Clicked on registration link successfully");
        } catch (Exception e) {
            logger.error("Failed to open registration page: {}", e.getMessage());
            throw e;
        }
    }

    public void openSignInPage() {
        try {
            logger.info("Opening sign-in page");
            clickBtn(signInLink);
            logger.debug("Clicked on sign-in link successfully");
        } catch (Exception e) {
            logger.error("Failed to open sign-in page: {}", e.getMessage());
            throw e;
        }
    }

    public String getWelcomeMessage() {
        try {
            logger.debug("Waiting for welcome text to be present");
            wait.until(ExpectedConditions.presenceOfElementLocated(welcomeTxt));
            String message = driver.findElement(welcomeTxt).getText();
            logger.info("Retrieved welcome message: {}", message);
            return message;
        } catch (Exception e) {
            logger.error("Failed to get welcome message: {}", e.getMessage());
            throw e;
        }
    }

    public String getPageTitle() {
        try {
            logger.debug("Waiting for page title to be visible");
            wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
            String title = driver.findElement(pageTitle).getText();
            logger.info("Retrieved page title: {}", title);
            return title;
        } catch (Exception e) {
            logger.error("Failed to get page title: {}", e.getMessage());
            throw e;
        }
    }

    public void selectJacketMenu() {
        try {
            logger.info("Navigating to jackets menu");
            actions.moveToElement(driver.findElement(womenLink))
                    .moveToElement(driver.findElement(topsLink))
                    .moveToElement(driver.findElement(jacketsLink))
                    .click()
                    .build()
                    .perform();
            logger.debug("Successfully navigated to jackets menu");
        } catch (Exception e) {
            logger.error("Failed to select jacket menu: {}", e.getMessage());
            throw e;
        }
    }

    public void SignOut() {
        try {
            logger.info("Initiating sign out process");
            clickBtn(WelcomeBtn);
            wait.until(ExpectedConditions.visibilityOfElementLocated(SignOutBtn));
            clickBtn(SignOutBtn);
            logger.info("User signed out successfully");
        } catch (Exception e) {
            logger.error("Failed to sign out: {}", e.getMessage());
            throw e;
        }
    }

    public String getWelcome() {
        try {
            String welcomeText = driver.findElement(welcomeMsg).getText();
            logger.info("Retrieved welcome text: {}", welcomeText);
            return welcomeText;
        } catch (Exception e) {
            logger.error("Failed to get welcome text: {}", e.getMessage());
            throw e;
        }
    }

    public void openMyAccountPage() {
        try {
            logger.info("Opening My Account page");
            clickBtn(WelcomeBtn);
            wait.until(ExpectedConditions.visibilityOfElementLocated(MyAccountLink));
            clickBtn(MyAccountLink);
            logger.debug("My Account page opened successfully");
        } catch (Exception e) {
            logger.error("Failed to open My Account page: {}", e.getMessage());
            throw e;
        }
    }
}