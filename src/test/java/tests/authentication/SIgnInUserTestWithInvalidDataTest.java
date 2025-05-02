package tests.authentication;

import data.ExcelReader;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SingInPage;
import tests.TestBase;

import java.io.IOException;

public class SIgnInUserTestWithInvalidDataTest extends TestBase {
    HomePage homePage;
    SingInPage singInPage;

    @DataProvider(name = "ExcelData")
    public Object[][] UserSignIn() throws IOException {
        ExcelReader er=new ExcelReader();
        return er.getExcelDataForSignIn();
    }


    @Test(dataProvider = "ExcelData")
    @Severity(SeverityLevel.NORMAL)
    public void userCanSignInSuccessfully(String Tc_Id,String description ,String email,String password,String error) {
        homePage = new HomePage(driver);
        singInPage = new SingInPage(driver);


        homePage.openSignInPage();
        singInPage.userSignIn(email, password);
        Assert.assertEquals(singInPage.getErrorMsg(),error);
        System.out.println("test case id: "+ Tc_Id +" passed");




    }

}
