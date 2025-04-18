package tests.authentication;

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
    public void userCanSignInSuccessfully() {
        homePage = new HomePage(driver);
        singInPage = new SingInPage(driver);


        homePage.openSignInPage();
        singInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD);
        Assert.assertTrue(homePage.getWelcomeMessage().contains("Welcome"), "the website should include Welcome");
        System.out.println("tc id is: TC_SignIn_001 passed");

            homePage.SignOut();


    }

}
