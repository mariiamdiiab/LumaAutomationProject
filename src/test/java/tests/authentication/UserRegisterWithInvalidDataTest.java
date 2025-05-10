package tests.authentication;

import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;

import pages.UserRegistrationPage;
import tests.TestBase;

import java.io.IOException;

@Epic("Authentication")
@Feature("User Registration")

public class UserRegisterWithInvalidDataTest extends TestBase {
    HomePage homePage;
    UserRegistrationPage userRegistrationPage;

    @DataProvider(name = "ExcelData")
    public Object[][] UserRegisterData() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForRegistration();
    }

    @Test(dataProvider = "ExcelData")
    @Severity(SeverityLevel.NORMAL)
    public void userCanRegisterSuccessfully(String Tc_Id,String description ,String firstName, String lastName, String email, String password, String confirm,String error) {
        Allure.description(description);
        homePage = new HomePage(driver);
        userRegistrationPage = new UserRegistrationPage(driver);

        Allure.step("Open the registration page", () -> homePage.openRegistrationPage());

        Allure.step("Enter registration data", () -> userRegistrationPage.userRegistrationInvalidData(
                firstName,
                lastName,
                email,
                password,
                confirm
        ));
        Allure.step("Verify Error message is displayed", () -> Assert.assertEquals(userRegistrationPage.getErrorMsg(), error));

        Allure.addAttachment("Test Case Result", Tc_Id + " passed successfully.");
    }
}


