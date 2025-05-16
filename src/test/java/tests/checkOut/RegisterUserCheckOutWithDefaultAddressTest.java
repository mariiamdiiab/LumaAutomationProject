package tests.checkOut;

import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import tests.TestBase;
import utilites.GlobalVariable;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Epic("Checkout")
@Feature("Checkout with Default Address")
public class RegisterUserCheckOutWithDefaultAddressTest extends TestBase {
    private HomePage homePage;
    private SignInPage signInPage;
    private ProductPage productPage;
    private ShoppingCartPage shoppingCartPage;
    private SearchPage searchPage;
    private CheckOutPage checkOutPage;
    private ExcelReader excel;

    @BeforeMethod
    public void pageSetup() {
        homePage = new HomePage(driver);
        signInPage = new SignInPage(driver);
        productPage = new ProductPage(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
        searchPage = new SearchPage(driver);
        checkOutPage = new CheckOutPage(driver);
        excel = new ExcelReader();
    }

    @DataProvider(name = "checkoutProductData")
    public Object[][] provideCheckoutProductData() throws IOException {
        return excel.getExcelDataForCompare();
    }

    @Test(groups = "ValidTests")
    @Severity(SeverityLevel.NORMAL)
    @Story("User Authentication")
    @Description("Validate that user can sign in successfully")
    public void verifyUserCanSignInSuccessfully() {
        Allure.step("1. Navigate to Sign In Page", () ->
                homePage.openSignInPage());

        Allure.step("2. Login using valid credentials", () ->
                signInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD));

        Allure.step("3. Verify welcome message is displayed", () -> {
            String welcomeMessage = homePage.getWelcomeMessage();
            Assert.assertTrue(welcomeMessage.contains("Welcome"),
                    "Expected 'Welcome' in welcome message but got: " + welcomeMessage);
        });

        Allure.addAttachment("Test Data", "text/plain",
                "Login credentials: " + GlobalVariable.EMAIL);
    }

    @Test(priority = 1, dependsOnMethods = "verifyUserCanSignInSuccessfully",
            dataProvider = "checkoutProductData", groups = "ValidTests")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Shopping Cart")
    @Description("Validate that user can add products to cart")
    public void verifyUserCanAddProductToCart(String productName, String productName2) {
        Allure.parameter("First Product", productName);
        Allure.parameter("Second Product", productName2);

        Allure.step("1. Search and add first product to cart", () -> {
            searchPage.productSearch(productName);
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName),
                    "Product page title should contain: " + productName);
            productPage.addToCart();
        });

        Allure.step("2. Search and add second product to cart", () -> {
            searchPage.productSearch(productName2);
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName2),
                    "Product page title should contain: " + productName2);
            productPage.addToCart();
        });

        Allure.step("3. Open shopping cart page", () ->
                productPage.openCartFromLink());

        Allure.step("4. Verify both products are in the cart", () -> {
            List<String> cartProductNames = shoppingCartPage.getCartProductNames();
            Assert.assertTrue(cartProductNames.contains(productName),
                    "Cart should contain: " + productName);
            Assert.assertTrue(cartProductNames.contains(productName2),
                    "Cart should contain: " + productName2);
        });

        Allure.addAttachment("Test Data", "text/plain",
                "Added products: " + productName + ", " + productName2);
    }

    @Test(priority = 2, dependsOnMethods = "verifyUserCanAddProductToCart",
            groups = "ValidTests")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Checkout Process")
    @Description("Validate checkout process using saved address")
    public void verifyCheckOutWithSavedAddressSuccessfully() {
        Allure.step("1. Proceed to checkout", () ->
                shoppingCartPage.goToCheckOut());

        Allure.step("2. Complete checkout using saved address", () ->
                checkOutPage.checkOutWithSavedAddress());

        Allure.step("3. Verify checkout success page", () -> {
            String title = Objects.requireNonNull(driver.getTitle());
            Assert.assertTrue(title.equalsIgnoreCase("success page"),
                    "Expected 'success page' but got: " + title);
        });

        Allure.step("4. Retrieve and save order ID", () -> {
            String orderId = checkOutPage.getOrderId();
            excel.writeNewOrderId(orderId);
            System.out.println("The order id is " + orderId);
        });

        Allure.addAttachment("Test Case Result",
                "Test case TC_CheckOut_029 passed successfully.");
    }
}