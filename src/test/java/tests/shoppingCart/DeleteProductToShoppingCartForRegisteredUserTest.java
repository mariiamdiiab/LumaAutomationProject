package tests.shoppingCart;

import data.ExcelReader;
import io.qameta.allure.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
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

    HomePage homePage;
    SignInPage signInPage;
    ProductPage productPage;
    ShoppingCartPage shoppingCartPage;
    SearchPage searchPage;

    @DataProvider(name = "ExcelData")
    public Object[][] UserRegisterData() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForCompare();
    }

    @Test
    public void userCanSignInSuccessfully() {
        homePage = new HomePage(driver);
        signInPage = new SignInPage(driver);

        Allure.step("Open the sign-in page", homePage::openSignInPage);
        Allure.step("Sign in using valid credentials", () -> signInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD));

        Allure.step("Verify welcome message is displayed", () -> {
            String welcomeMessage = homePage.getWelcomeMessage();
            Allure.addAttachment("Welcome Message", welcomeMessage);
            Assert.assertTrue(welcomeMessage.contains("Welcome"),
                    "Expected 'Welcome' message not found in: " + welcomeMessage);
        });
    }

    @Test(priority = 1, dataProvider = "ExcelData", dependsOnMethods = "userCanSignInSuccessfully")
    @Story("Add Product to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a registered user can add multiple products to the cart")
    public void userCanAddProductToCart(String productName, String productName2) {
        productPage = new ProductPage(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
        searchPage = new SearchPage(driver);

        Allure.step("Add first product to cart", () -> {
            searchPage.productSearch(productName);
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName));
            productPage.addToCart();
        });

        Allure.step("Add second product to cart", () -> {
            searchPage.productSearch(productName2);
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName2));
            productPage.addToCart();
        });

        Allure.step("Open the shopping cart", productPage::openCartFromLink);

        Allure.step("Verify both products are in the cart", () -> {
            List<String> cartProductNames = shoppingCartPage.getCartProductNames();
            Allure.addAttachment("Products in Cart", String.join(", ", cartProductNames));
            Assert.assertTrue(cartProductNames.contains(productName), "Expected cart to contain: " + productName);
            Assert.assertTrue(cartProductNames.contains(productName2), "Expected cart to contain: " + productName2);
        });
    }

    @Test(priority = 2, dataProvider = "ExcelData", dependsOnMethods = "userCanAddProductToCart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate the functionality of deleting a product from the cart as a registered user")
    public void userCanDeleteFromShoppingCart(String productName, String productName2) {
        List<String> updatedProducts;

        Allure.step("Verify product exists before deletion", () -> {
            List<String> initialProducts = shoppingCartPage.getCartProductNames();
            Allure.addAttachment("Initial Cart Products", String.join(", ", initialProducts));
            Assert.assertTrue(initialProducts.contains(productName), "Product should exist before deletion: " + productName);
        });

        Allure.step("Delete the product from cart", () ->
                shoppingCartPage.deleteItemByName(productName)
        );

        updatedProducts = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> {
                    List<String> current = shoppingCartPage.getCartProductNames();
                    return !current.contains(productName) ? current : null;
                });

        Allure.step("Verify deleted product is no longer in the cart", () -> {
            Allure.addAttachment("Updated Cart Products", String.join(", ", updatedProducts));
            Assert.assertFalse(updatedProducts.contains(productName), "Product should be removed from cart: " + productName);
        });

        Allure.step("Verify the second product is still in the cart", () -> Assert.assertTrue(updatedProducts.contains(productName2),
                "Expected remaining product in cart: " + productName2));
    }

    @Test(priority = 3)
    @Story("Clear Shopping Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate that user can clear the shopping cart completely")
    public void userClearTheCart() {
        Allure.step("Clear the shopping cart", () ->
                shoppingCartPage.clearCart()
        );

        Allure.step("Verify cart is empty", () -> {
            String emptyMessage = shoppingCartPage.getCartEmptyMessage();
            Allure.addAttachment("Empty Cart Message", emptyMessage);
            Assert.assertTrue(emptyMessage.contains("You have no items in your shopping cart"),
                    "Cart should be empty, but message was: " + emptyMessage);
        });
        Allure.addAttachment("Test Case Result", "Test case TC_DeleteFromCart_028 passed successfully.");


    }
}
