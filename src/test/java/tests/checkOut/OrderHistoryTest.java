package tests.checkOut;

import data.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.MyAccountPage;
import pages.SingInPage;
import tests.TestBase;
import utilites.GlobalVariable;

import java.io.IOException;
import java.util.Objects;

public class OrderHistoryTest extends TestBase {
    HomePage homePage;
    SingInPage singInPage;
    MyAccountPage myAccountPage;


    @DataProvider(name = "OrderIDData")
    public Object[][] orderIdDataProvider() throws IOException {
        ExcelReader er = new ExcelReader();
        String lastOrderId = er.getLastOrderId();
        // Wrap the String in a 2D Object array
        return new Object[][] {{ lastOrderId }};
    }

    @Test
    public void userCanSignInSuccessfully()  {
        homePage=new HomePage(driver);
        singInPage=new SingInPage(driver);
        homePage.openSignInPage();
        singInPage.userSignIn(GlobalVariable.EMAIL,GlobalVariable.PASSWORD);
        Assert.assertTrue(homePage.getWelcomeMessage().contains("Welcome"));
    }

    @Test(priority = 1,dataProvider = "OrderIDData")
    public void userCheckIfOrderAppearInOrderHistory(String orderId){
        myAccountPage=new MyAccountPage(driver);

        if(!Objects.equals(driver.getTitle(), "My Account")){
            homePage.openMyAccountPage();
        }
        myAccountPage.getOrderIDs();
        Assert.assertTrue(myAccountPage.getOrderIDs().contains(orderId));
        System.out.println("order Id Is "+orderId);
    }
}
