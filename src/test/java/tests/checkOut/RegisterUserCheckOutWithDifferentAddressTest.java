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
@Feature("Checkout with New Address")
public class RegisterUserCheckOutWithDifferentAddressTest extends TestBase {

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

    @DataProvider(name = "newAddress")
    public Object[][] newAddressData() throws IOException {
        return excel.getExcelDataForNewAddress();
    }

    @Test
    public void userCanSignInSuccessfully() {
        homePage = new HomePage(driver);
        signInPage = new SignInPage(driver);

        Allure.step("Navigate to Sign In Page", homePage::openSignInPage);

        Allure.step("Sign in with valid credentials", () ->
                signInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD));

        Allure.step("Verify Welcome Message", () -> {
            String welcomeMessage = homePage.getWelcomeMessage();
            Assert.assertTrue(welcomeMessage.contains("Welcome"),
                    "Expected 'Welcome' in the welcome message, but got: " + welcomeMessage);
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

    @Test(priority = 2, dependsOnMethods = "userCanAddProductToCart", dataProvider = "newAddress")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Checkout")
    public void userCanCheckOutWithNewAddressSuccessfully(
            String Tc_Id, String description, String street, String city, String state,
            String zip, String county, String phone) {

        Allure.description(description);

        checkOutPage = new CheckOutPage(driver);

        Allure.step("Proceed to Checkout", shoppingCartPage::goToCheckOut);

        Allure.step("Enter and Confirm New Address Details", () ->
                checkOutPage.checkOutWithNewAddress(street, city, state, zip, county, phone));

        Allure.step("Verify Checkout Success Page", () -> {
            String title = Objects.requireNonNull(driver.getTitle());
            Assert.assertTrue(title.equalsIgnoreCase("success page"),
                    "Expected 'success page', but got: " + title);
        });

        Allure.step("Retrieve and Log Order ID", () -> {
            String orderId = checkOutPage.getOrderId();
            excel.writeNewOrderId(orderId);
            Allure.addAttachment("Order ID", orderId);
            System.out.println("The order ID is: " + orderId);
        });

        Allure.addAttachment("Test Case Execution", "Test Case ID: " + Tc_Id + " passed.");
        System.out.println("Test Case ID: " + Tc_Id + " passed");
    }
}
