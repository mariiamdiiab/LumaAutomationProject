package tests.authentication;

import data.ExcelReader;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;

import pages.UserRegistrationPage;
import tests.TestBase;

import java.io.IOException;

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

        homePage = new HomePage(driver);
         userRegistrationPage = new UserRegistrationPage(driver);

        homePage.openRegistrationPage();

        userRegistrationPage.userRegistrationInvalidData(
                firstName,
                lastName,
                email,
                password,
                confirm
        );

            Assert.assertEquals(userRegistrationPage.getErrorMsg(),error);
            System.out.println("test case id: "+ Tc_Id +" passed");


        }

}


