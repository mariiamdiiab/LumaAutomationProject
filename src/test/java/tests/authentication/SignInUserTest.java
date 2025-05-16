package tests.authentication;

import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SignInPage;
import tests.TestBase;
import utilites.GlobalVariable;
import java.io.IOException;

@Epic("Authentication")
@Feature("User Sign In")
public class SignInUserTest extends TestBase {
    private HomePage homePage;
    private SignInPage signInPage;

    @DataProvider(name = "validCredentials")
    public Object[][] provideValidCredentials() {
        return new Object[][]{
                {GlobalVariable.EMAIL, GlobalVariable.PASSWORD}
        };
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] provideInvalidCredentials() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForSignIn();
    }

    @Test(dataProvider = "validCredentials", groups = "ValidTests")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Successful Login")
    @Description("Verify user can successfully sign in with valid credentials")
    public void testSuccessfulSignIn(String email, String password) {
        try {
            homePage = new HomePage(driver);
            signInPage = new SignInPage(driver);

            Allure.step("1. Open sign-in page", homePage::openSignInPage);
            Allure.step("2. Enter valid credentials", () -> signInPage.userSignIn(email, password));
            Allure.step("3. Verify welcome message", () ->
                    Assert.assertTrue(homePage.getWelcomeMessage().contains("Welcome"),
                            "Welcome message not displayed"));
            Allure.step("4. Sign out",
                    () -> homePage.SignOut());
        } finally {
            Allure.addAttachment("Test Result", "text/plain", "Successful login test completed");
        }
    }

    @Test(dataProvider = "invalidCredentials" ,priority = 1,groups = "InvalidTests")
    @Severity(SeverityLevel.NORMAL)
    @Story("Failed Login")
    @Description("Verify error messages for invalid login attempts")
    public void testInvalidSignInAttempts(String tcId, String description,
                                          String email, String password,
                                          String expectedError) {
        try {
            Allure.description(description);
            Allure.link("Test Case ID", tcId);

            homePage = new HomePage(driver);
            signInPage = new SignInPage(driver);

            Allure.step("1. Open sign-in page", homePage::openSignInPage);
            Allure.step("2. Enter invalid credentials", () -> signInPage.userSignIn(email, password));
            Allure.step("3. Verify error message", () -> {
                String actualError = signInPage.getErrorMsg();
                Assert.assertEquals(actualError, expectedError,
                        String.format("Error mismatch for TC %s\nExpected: %s\nActual: %s",
                                tcId, expectedError, actualError));
            });
        } finally {
            Allure.addAttachment("Test Result", "text/plain",
                    String.format("TC %s completed with error: %s", tcId, expectedError));
        }
    }
}