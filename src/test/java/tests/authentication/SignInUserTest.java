package tests.authentication;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SignInPage;
import tests.TestBase;
import utilites.GlobalVariable;

@Epic("Authentication")
@Feature("User Sign In")
public class SignInUserTest extends TestBase {
    HomePage homePage;
    SignInPage signInPage;


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can successfully sign in with valid credentials")
    public void userCanSignInSuccessfully() {
        homePage = new HomePage(driver);
        signInPage = new SignInPage(driver);

        Allure.step("Open the sign-in page", () -> homePage.openSignInPage());

        Allure.step("Enter valid email and password, then submit", () -> signInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD));

        Allure.step("Verify welcome message is displayed", () -> {
            String welcomeMessage = homePage.getWelcomeMessage();
            Assert.assertTrue(welcomeMessage.contains("Welcome"),
                    "Expected 'Welcome' message not found in: " + welcomeMessage);
        });

        Allure.step("Sign out from the account", () -> homePage.SignOut());

        Allure.addAttachment("Test Case Result", "Test case TC_SignIn_012 passed successfully.");
    }
}
