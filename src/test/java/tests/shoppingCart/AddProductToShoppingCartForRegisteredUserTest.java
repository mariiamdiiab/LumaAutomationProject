package tests.shoppingCart;

import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
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

    private HomePage homePage;
    private SignInPage signInPage;
    private ProductPage productPage;
    private ShoppingCartPage shoppingCartPage;
    private SearchPage searchPage;

    @BeforeMethod
    public void pageSetup() {
        homePage = new HomePage(driver);
        signInPage = new SignInPage(driver);
        productPage = new ProductPage(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
        searchPage = new SearchPage(driver);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("User Sign In")
    @Description("Validate that registered user can sign in successfully")
    public void userCanSignInSuccessfully() {
        Allure.step("1. Open the sign-in page", () ->
                homePage.openSignInPage());

        Allure.step("2. Enter valid email and password, then submit", () ->
                signInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD));

        Allure.step("3. Verify welcome message is displayed", () -> {
            String welcomeMessage = homePage.getWelcomeMessage();
            Assert.assertTrue(welcomeMessage.contains("Welcome"),
                    "Expected 'Welcome' message not found in: " + welcomeMessage);

            Allure.addAttachment("Welcome Message", welcomeMessage);
        });
    }

    @DataProvider(name = "shoppingCartProductData")
    public Object[][] userShoppingCartData() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForCompare();
    }

    @Test(priority = 1,
            dependsOnMethods = "userCanSignInSuccessfully",
            dataProvider = "shoppingCartProductData",
            groups = "ValidTests")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Add Products to Cart")
    @Description("Validate the functionality of adding multiple products to cart as a registered user")
    public void userCanAddProductToCart(String productName, String productName2) {
        Allure.parameter("First Product", productName);
        Allure.parameter("Second Product", productName2);

        Allure.step("1. Search for the first product", () -> {
            searchPage.productSearch(productName);
            String searchResultsTitle = searchPage.getProductTitleWrapperInSearchPage();
            Assert.assertTrue(searchResultsTitle.contains(productName),
                    String.format("Product '%s' not found in search results. Actual title: %s",
                            productName, searchResultsTitle));
        });

        Allure.step("2. Open the first product page", () -> {
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName),
                    String.format("Product page title doesn't contain '%s'. Actual title: %s",
                            productName, driver.getTitle()));
        });

        Allure.step("3. Add first product to cart", () ->
                productPage.addToCart());

        Allure.step("4. Search for the second product", () -> {
            searchPage.productSearch(productName2);
            String searchResultsTitle = searchPage.getProductTitleWrapperInSearchPage();
            Assert.assertTrue(searchResultsTitle.contains(productName2),
                    String.format("Product '%s' not found in search results. Actual title: %s",
                            productName2, searchResultsTitle));
        });

        Allure.step("5. Open the second product page", () -> {
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName2),
                    String.format("Product page title doesn't contain '%s'. Actual title: %s",
                            productName2, driver.getTitle()));
        });

        Allure.step("6. Add second product to cart", () ->
                productPage.addToCart());

        Allure.step("7. Open the shopping cart page", () ->
                productPage.openCartFromLink());

        Allure.step("8. Verify both products exist in cart", () -> {
            List<String> cartProductNames = shoppingCartPage.getCartProductNames();
            Allure.addAttachment("Products in Cart", String.join(", ", cartProductNames));

            Assert.assertTrue(cartProductNames.contains(productName),
                    "Cart should contain: " + productName);
            Assert.assertTrue(cartProductNames.contains(productName2),
                    "Cart should contain: " + productName2);
        });

        Allure.addAttachment("Test Case ID", "TC_AddToCart_027");
        Allure.addAttachment("Test Result", "Test case passed successfully");
    }
}