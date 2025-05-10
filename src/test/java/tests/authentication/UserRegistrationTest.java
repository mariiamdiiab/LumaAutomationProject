package tests.authentication;

import com.github.javafaker.Faker;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.MyAccountPage;
import pages.UserRegistrationPage;
import tests.TestBase;

@Epic("Authentication")
@Feature("User Registration")

public class UserRegistrationTest extends TestBase {
    HomePage homePage;
    UserRegistrationPage userRegistrationPage;
    MyAccountPage myAccountPage;


   Faker fakeDate=new Faker();
   String firstName=fakeDate.name().firstName();
   String lastName=fakeDate.name().lastName();
   String email=fakeDate.internet().emailAddress();
   String password="SecurePass12@";


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate the functionality of registration using valid data at register page")
        public void userCanRegisterSuccessfully() {

        homePage = new HomePage(driver);
        userRegistrationPage = new UserRegistrationPage(driver);
        myAccountPage = new MyAccountPage(driver);

        Allure.step("Open the registration page", homePage::openRegistrationPage);

            Allure.step("Enter Valid Registration Data", () -> userRegistrationPage.userRegistration(
                    firstName,
                    lastName,
                    email,
                    password
            ));

        Allure.step("Verify Registration message is displayed", () -> {
            String actualMessage = myAccountPage.getSuccessMessage();
            Assert.assertTrue(actualMessage.contains("Thank you for registering"),
                    "Expected success message not found. Actual: " + actualMessage);
        });

        Allure.step("Sign out from the account", () -> homePage.SignOut());

        Allure.addAttachment("Test Case Result", "Test case TC_REG_001 passed successfully.");
    }
}

