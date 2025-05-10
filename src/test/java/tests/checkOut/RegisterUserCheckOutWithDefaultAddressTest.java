package tests.checkOut;

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

@Epic("Checkout")
@Feature("Checkout with Default Address")
public class RegisterUserCheckOutWithDefaultAddressTest extends TestBase {

    HomePage homePage;
    SignInPage signInPage;
    ProductPage productPage;
    ShoppingCartPage shoppingCartPage;
    SearchPage searchPage;
    CheckOutPage checkOutPage;

    ExcelReader excel = new ExcelReader();

    @DataProvider(name = "ExcelData")
    public Object[][] userRegisterData() throws IOException {
        return excel.getExcelDataForCompare();
    }

    @Test
    public void userCanSignInSuccessfully() {
        homePage = new HomePage(driver);
        signInPage = new SignInPage(driver);

        Allure.step("Navigate to Sign In Page", () -> homePage.openSignInPage());

        Allure.step("Login using valid credentials", () ->
                signInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD));

        Allure.step("Validate Welcome Message", () -> {
            String welcomeMessage = homePage.getWelcomeMessage();
            Assert.assertTrue(welcomeMessage.contains("Welcome"),
                    "Expected 'Welcome' in welcome message but got: " + welcomeMessage);
        });
    }

    @Test(priority = 1, dependsOnMethods = "userCanSignInSuccessfully", dataProvider = "ExcelData")
    public void userCanAddProductToCart(String productName, String productName2) {
        productPage = new ProductPage(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
        searchPage = new SearchPage(driver);

        Allure.step("Search and Add First Product to Cart", () -> {
            searchPage.productSearch(productName);
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName));
            productPage.addToCart();
        });

        Allure.step("Search and Add Second Product to Cart", () -> {
            searchPage.productSearch(productName2);
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName2));
            productPage.addToCart();
        });

        Allure.step("Open Cart Page", productPage::openCartFromLink);

        Allure.step("Verify both products are in the cart", () -> {
            List<String> cartProductNames = shoppingCartPage.getCartProductNames();
            Allure.addAttachment("Products in Cart", String.join(", ", cartProductNames));
            Assert.assertTrue(cartProductNames.contains(productName), "Cart should contain: " + productName);
            Assert.assertTrue(cartProductNames.contains(productName2), "Cart should contain: " + productName2);
        });
    }

    @Test(priority = 2, dependsOnMethods = "userCanAddProductToCart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate checkout process using saved address")
    public void userCheckOutWithSavedAddressSuccessfully() {
        checkOutPage = new CheckOutPage(driver);

        Allure.step("Proceed to Checkout", () -> shoppingCartPage.goToCheckOut());

        Allure.step("Checkout using Saved Address", checkOutPage::checkOutWithSavedAddress);

        Allure.step("Validate Checkout Success Page", () -> {
            String title = Objects.requireNonNull(driver.getTitle());
            Assert.assertTrue(title.equalsIgnoreCase("success page"), "Expected 'success page' but got: " + title);
        });

        Allure.step("Retrieve and Save Order ID", () -> {
            String orderId = checkOutPage.getOrderId();
            excel.writeNewOrderId(orderId);
            Allure.addAttachment("Order ID", orderId);
            System.out.println("The order id is " + orderId);
        });

        Allure.addAttachment("Test Case Result", "Test case TC_CheckOut_029 passed successfully.");
    }
}
