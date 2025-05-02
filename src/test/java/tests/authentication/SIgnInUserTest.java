package tests.authentication;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SingInPage;
import tests.TestBase;
import utilites.GlobalVariable;


public class SIgnInUserTest extends TestBase {
    HomePage homePage;
    SingInPage singInPage;



    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void userCanSignInSuccessfully() {
        homePage = new HomePage(driver);
        singInPage = new SingInPage(driver);


        homePage.openSignInPage();
        singInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD);
        Assert.assertTrue(homePage.getWelcomeMessage().contains("Welcome"), "the website should include Welcome");
        System.out.println("test case id: TC_SingIn_012 passed");

        //homePage.SignOut();


    }

}
