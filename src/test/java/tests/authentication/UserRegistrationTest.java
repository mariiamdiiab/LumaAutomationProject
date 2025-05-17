package tests.authentication;

import com.github.javafaker.Faker;
import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import tests.TestBase;
import java.io.IOException;

@Epic("Authentication")
@Feature("User Registration")
public class UserRegistrationTest extends TestBase {

    private HomePage homePage;
    private UserRegistrationPage userRegistrationPage;
    private MyAccountPage myAccountPage;

    private final Faker faker = new Faker();
    private final String firstName = faker.name().firstName();
    private final String lastName = faker.name().lastName();
    private final String email = faker.internet().emailAddress();
    private final String password = "SecurePass12@";

    @DataProvider(name = "invalidRegistrationData")
    public Object[][] provideInvalidRegistrationData() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForRegistration();
    }

    @BeforeMethod
    public void pageSetup() {
        homePage = new HomePage(driver);
        userRegistrationPage = new UserRegistrationPage(driver);
        myAccountPage = new MyAccountPage(driver);
    }

    @Test(groups ="ValidTests")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate registration with valid data")
    public void testSuccessfulRegistration() {
        Allure.step("Open registration page", homePage::openRegistrationPage);

        Allure.step("Enter valid registration data", () ->
                userRegistrationPage.userRegistration(firstName, lastName, email, password));

        Allure.step("Verify success message", () -> {
            String actualMessage = myAccountPage.getSuccessMessage();
            Assert.assertTrue(actualMessage.contains("Thank you for registering"),
                    "Expected success message not found. Actual: " + actualMessage);
        });

        Allure.step("Sign out", () -> homePage.SignOut());
    }

    @Test(dataProvider = "invalidRegistrationData",groups = "InvalidTests" ,priority =1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate registration error messages")
    public void testInvalidRegistration(String tcId, String description,
                                        String firstName, String lastName,
                                        String email, String password,
                                        String confirm, String expectedError) {
        Allure.description(description);
        Allure.link("Test Case ID", tcId);

        Allure.step("Open registration page", homePage::openRegistrationPage);

        Allure.step("Enter invalid data", () ->
                userRegistrationPage.userRegistrationInvalidData(
                        firstName, lastName, email, password, confirm));

        Allure.step("Verify error message", () ->
                Assert.assertEquals(userRegistrationPage.getErrorMsg(), expectedError,
                        String.format("Error mismatch for TC %s", tcId)));
    }
}