package tests.authentication;

import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SignInPage;
import tests.TestBase;

import java.io.IOException;


@Epic("Authentication")
@Feature("User Sign In")
public class SIgnInUserTestWithInvalidDataTest extends TestBase {
    HomePage homePage;
    SignInPage signInPage;

    @DataProvider(name = "ExcelData")
    public Object[][] UserSignIn() throws IOException {
        ExcelReader er=new ExcelReader();
        return er.getExcelDataForSignIn();
    }


    @Test(dataProvider = "ExcelData")
    @Severity(SeverityLevel.NORMAL)
    public void userCanSignInSuccessfully(String Tc_Id,String description ,String email,String password,String error) {
        Allure.description(description);

        homePage = new HomePage(driver);
        signInPage = new SignInPage(driver);

        Allure.step("Open the sign-in page", () -> homePage.openSignInPage());

        Allure.step("Enter valid email and password, then submit", () -> signInPage.userSignIn(email, password));

        Allure.step("Verify Error message is displayed", () -> Assert.assertEquals(signInPage.getErrorMsg(), error));

        Allure.addAttachment("Test Case Result", Tc_Id +" passed successfully.");
    }

}
