package tests.authentication;

import data.ExcelReader;
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
    public void userCanRegisterSuccessfully(String tcId,String description ,String firstName, String lastName, String email, String password) {
        //ADD ERROR FILED TO ASSERT WITH AND CHANGE IN EXCEL READER
         homePage = new HomePage(driver);
         userRegistrationPage = new UserRegistrationPage(driver);

        homePage.openRegistrationPage();

        userRegistrationPage.userRegistration(
                firstName,
                lastName,
                email,
                password
        );

//            Assert.assertTrue(Objects.requireNonNull(userRegistrationPage.getErrorMsg().));
            System.out.println("test case id: "+ tcId +" passed");


        }

}


