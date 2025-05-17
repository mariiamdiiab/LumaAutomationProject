package tests.shoppingCart;

import data.ExcelReader;
import io.qameta.allure.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import tests.TestBase;
import utilites.GlobalVariable;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Epic("Shopping Cart")
@Feature("Delete Product From Cart Functionality")
public class DeleteProductToShoppingCartForRegisteredUserTest extends TestBase {

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

    @DataProvider(name = "cartProductData")
    public Object[][] getShoppingCartData() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForCompare();
    }

    @Test
    @Story("User Authentication")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a registered user can sign in successfully")
    public void userCanSignInSuccessfully() {
        Allure.step("1. Open the sign-in page", homePage::openSignInPage);

        Allure.step("2. Sign in using valid credentials", () ->
                signInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD));

        Allure.step("3. Verify welcome message is displayed", () -> {
            String welcomeMessage = homePage.getWelcomeMessage();
            Allure.addAttachment("Welcome Message", welcomeMessage);
            Assert.assertTrue(welcomeMessage.contains("Welcome"),
                    "Expected 'Welcome' message not found in: " + welcomeMessage);
        });
    }

    @Test(priority = 1,
            dataProvider = "cartProductData",
            dependsOnMethods = "userCanSignInSuccessfully",
            groups = "ValidTests")
    @Story("Add Products to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a registered user can add multiple products to the cart")
    public void userCanAddProductToCart(String productName, String productName2) {
        Allure.parameter("First Product", productName);
        Allure.parameter("Second Product", productName2);

        Allure.step("1. Search for the first product", () -> {
            searchPage.productSearch(productName);
            String searchResults = searchPage.getProductTitleWrapperInSearchPage();
            Assert.assertTrue(searchResults.contains(productName),
                    String.format("Product '%s' not found in search results. Actual: %s",
                            productName, searchResults));
        });

        Allure.step("2. Open the first product page and add to cart", () -> {
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName),
                    String.format("Product page title doesn't contain '%s'. Actual: %s",
                            productName, driver.getTitle()));
            productPage.addToCart();
        });

        Allure.step("3. Search for the second product", () -> {
            searchPage.productSearch(productName2);
            String searchResults = searchPage.getProductTitleWrapperInSearchPage();
            Assert.assertTrue(searchResults.contains(productName2),
                    String.format("Product '%s' not found in search results. Actual: %s",
                            productName2, searchResults));
        });

        Allure.step("4. Open the second product page and add to cart", () -> {
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName2),
                    String.format("Product page title doesn't contain '%s'. Actual: %s",
                            productName2, driver.getTitle()));
            productPage.addToCart();
        });

        Allure.step("5. Open the shopping cart", productPage::openCartFromLink);

        Allure.step("6. Verify both products are in the cart", () -> {
            List<String> cartProductNames = shoppingCartPage.getCartProductNames();
            Allure.addAttachment("Products in Cart" ,String.join(" ",cartProductNames));
            Assert.assertTrue(cartProductNames.contains(productName),
                    "Expected cart to contain: " + productName);
            Assert.assertTrue(cartProductNames.contains(productName2),
                    "Expected cart to contain: " + productName2);
        });
    }

    @Test(priority = 2,
            dataProvider = "cartProductData",
            dependsOnMethods = "userCanAddProductToCart",
            groups = "ValidTests")
    @Story("Delete Product from Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate the functionality of deleting a product from the cart as a registered user")
    public void userCanDeleteFromShoppingCart(String productName, String productName2) {
        Allure.parameter("Product to Delete", productName);
        Allure.parameter("Product to Retain", productName2);

        final List<String> initialProducts = shoppingCartPage.getCartProductNames();

        Allure.step("1. Verify product exists before deletion", () -> {
            Allure.addAttachment("Initial Cart Products", String.join(", ", initialProducts));
            Assert.assertTrue(initialProducts.contains(productName),
                    "Product should exist before deletion: " + productName);
        });

        Allure.step("2. Delete the product from cart", () ->
                shoppingCartPage.deleteItemByName(productName)
        );

        final List<String> updatedProducts = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> {
                    List<String> current = shoppingCartPage.getCartProductNames();
                    return !current.contains(productName) ? current : null;
                });

        Allure.step("3. Verify cart contents after deletion", () -> {
            Allure.addAttachment("Updated Cart Products", String.join(", ", updatedProducts));

            Assert.assertFalse(updatedProducts.contains(productName),
                    "Product should be removed from cart: " + productName);

            Assert.assertTrue(updatedProducts.contains(productName2),
                    "Expected remaining product in cart: " + productName2);
        });
    }

    @Test(priority = 3,
            dependsOnMethods = "userCanDeleteFromShoppingCart",
            groups = "ValidTests")
    @Story("Clear Shopping Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate that user can clear the shopping cart completely")

    public void userCanClearTheCart() {
        Allure.step("1. Clear the shopping cart", () ->
                shoppingCartPage.clearCart()
        );

        Allure.step("2. Verify cart is empty", () -> {
            String emptyMessage = shoppingCartPage.getCartEmptyMessage();
            Allure.addAttachment("Empty Cart Message", emptyMessage);
            Assert.assertTrue(emptyMessage.contains("You have no items in your shopping cart"),
                    "Cart should be empty, but message was: " + emptyMessage);
            Assert.assertEquals(shoppingCartPage.getCartProductNames().size(), 0,
                    "Cart should have no products after clearing");
        });

        Allure.addAttachment("Test Case ID", "TC_DeleteFromCart_028");
        Allure.addAttachment("Test Result", "Test case passed successfully");
    }
}