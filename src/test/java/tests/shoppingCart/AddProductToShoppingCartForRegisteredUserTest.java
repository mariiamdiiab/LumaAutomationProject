package tests.shoppingCart;

import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import tests.TestBase;
import utilites.GlobalVariable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
@Epic("Shopping Cart")
@Feature("Add Product To Cart Functionality")
public class AddProductToShoppingCartForRegisteredUserTest extends TestBase {


    HomePage homePage;
    SignInPage signInPage;
    ProductPage productPage;
    ShoppingCartPage shoppingCartPage;
    SearchPage searchPage;

    @Test
    public void userCanSignInSuccessfully() {
        homePage = new HomePage(driver);
        signInPage = new SignInPage(driver);

        Allure.step("Open the sign-in page", () -> homePage.openSignInPage());

        Allure.step("Enter valid email and password, then submit", () -> signInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD));

        Allure.step("Verify welcome message is displayed", () -> {
            String welcomeMessage = homePage.getWelcomeMessage();
            Assert.assertTrue(welcomeMessage.contains("Welcome"),
                    "Expected 'Welcome' message not found in: " + welcomeMessage);
        });
    }


    @DataProvider(name = "ExcelData")
    public Object[][] UserRegisterData() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForCompare();
    }



    @Test(priority = 1,dependsOnMethods = "userCanSignInSuccessfully",dataProvider = "ExcelData")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate the functionality of adding product to cart as a registered user")
    public void userCanAddProductToCart(String productName,String productName2) {
        productPage=new ProductPage(driver);
        shoppingCartPage =new ShoppingCartPage(driver);
        searchPage=new SearchPage(driver);

        Allure.step("Add First Product To Cart",()-> {
                    searchPage.productSearch(productName);
                    searchPage.openProductPage();
                    Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName));
                    productPage.addToCart();
                });

        Allure.step("Add Second Product To Cart",()-> {
                    searchPage.productSearch(productName2);
                    searchPage.openProductPage();
                    Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName2));
                    productPage.addToCart();
                });

        Allure.step("Open Cart Page",()-> productPage.openCartFromLink());

        Allure.step("Verify both products exist in cart", () -> {
            List<String> cartProductNames = shoppingCartPage.getCartProductNames();
            Allure.addAttachment("Products in Cart", String.join(", ", cartProductNames));
            Assert.assertTrue(cartProductNames.contains(productName), "Cart should contain: " + productName);
            Assert.assertTrue(cartProductNames.contains(productName2), "Cart should contain: " + productName2);
        });

        Allure.addAttachment("Test Case Result", "Test case TC_AddToCart_027 passed successfully.");
    }


}
