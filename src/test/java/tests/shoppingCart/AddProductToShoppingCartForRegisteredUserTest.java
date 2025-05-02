package tests.shoppingCart;

import data.ExcelReader;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import tests.TestBase;
import utilites.GlobalVariable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AddProductToShoppingCartForRegisteredUserTest extends TestBase {


    HomePage homePage;
    SingInPage singInPage;
    ProductPage productPage;
    ShoppingCartPage shoppingCartPage;
    SearchPage searchPage;

    @Test
    public void userCanSignInSuccessfully()  {
        homePage=new HomePage(driver);
        singInPage=new SingInPage(driver);
        homePage.openSignInPage();
        singInPage.userSignIn(GlobalVariable.EMAIL,GlobalVariable.PASSWORD);
        Assert.assertTrue(homePage.getWelcomeMessage().contains("Welcome"));
    }


    @DataProvider(name = "ExcelData")
    public Object[][] UserRegisterData() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForCompare();
    }



    @Test(priority = 1,dependsOnMethods = "userCanSignInSuccessfully",dataProvider = "ExcelData")
    @Severity(SeverityLevel.CRITICAL)
    public void userCanAddProductToCart(String productName,String productName2) {
        productPage=new ProductPage(driver);
        shoppingCartPage =new ShoppingCartPage(driver);
        searchPage=new SearchPage(driver);

        searchPage.productSearch(productName);
        searchPage.openProductPage();
        Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName) );
        productPage.addToCart();


        searchPage.productSearch(productName2);
        searchPage.openProductPage();
        Assert.assertTrue(driver.getTitle().contains(productName2) );
        productPage.addToCart();

        productPage.openCartFromLink();
        List<String> cartProductNames=shoppingCartPage.getCartProductNames();
        Assert.assertTrue(cartProductNames.contains(productName),"Cart should contain: "+ productName);
        Assert.assertTrue(cartProductNames.contains(productName2),"Cart should contain: "+ productName2);


        System.out.println("test case id:TC_AddToCart_027 passed");

    }


}
