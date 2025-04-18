package tests.authentication;

import data.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SingInPage;
import tests.TestBase;

import java.io.IOException;
import java.util.Objects;

public class SIgnInUserTestWithInvalidDataTest extends TestBase {
    HomePage homePage;
    SingInPage singInPage;
    @DataProvider(name = "ExcelData")

    public Object[][]UserRegisterData() throws IOException {
        ExcelReader er=new ExcelReader();
        return er.getExcelDataForSignIn();
    }


    @Test(dataProvider = "ExcelData")
    public void userCanSignInSuccessfully(String tcId,String description ,String email,String password) {
        homePage = new HomePage(driver);
        singInPage = new SingInPage(driver);


        homePage.openSignInPage();
        singInPage.userSignIn(email, password);

        Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains("Login"));
        System.out.println("test case id: "+ tcId +" passed");




    }

}
