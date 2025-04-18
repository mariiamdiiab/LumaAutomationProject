package tests.shoppingCart;

import data.ExcelReader;
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

public class DeleteProductToShoppingCartForRegisteredUserTest extends TestBase {

    HomePage homePage;
    SingInPage singInPage;
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
        singInPage = new SingInPage(driver);
        homePage.openSignInPage();
        singInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD);
        Assert.assertTrue(homePage.getWelcomeMessage().contains("Welcome"));
    }

    @Test(priority = 1, dependsOnMethods = "userCanSignInSuccessfully", dataProvider = "ExcelData")
    public void userCanAddProductToCart(String productName, String productName2) {
        productPage = new ProductPage(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
        searchPage = new SearchPage(driver);

        searchPage.productSearch(productName);
        searchPage.openProductPage();
        Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName));
        productPage.addToCart();


        searchPage.productSearch(productName2);
        searchPage.openProductPage();
        Assert.assertTrue(driver.getTitle().contains(productName2));
        productPage.addToCart();

        productPage.openCartFromLink();
        List<String> cartProductNames = shoppingCartPage.getCartProductNames();
        Assert.assertTrue(cartProductNames.contains(productName), "Cart should contain: " + productName);
        Assert.assertTrue(cartProductNames.contains(productName2), "Cart should contain: " + productName2);
    }

    @Test(dependsOnMethods = "userCanAddProductToCart", dataProvider = "ExcelData",priority = 2)
    public void userCanDeleteFromShoppingCart(String productName, String productName2) {
        // Verify product exists before deletion
        List<String> initialProducts = shoppingCartPage.getCartProductNames();
        Assert.assertTrue(initialProducts.contains(productName),
                "Product should exist before deletion");

        // Delete the product
        shoppingCartPage.deleteItemByName(productName);

        // Get updated products with wait
        List<String> updatedProducts = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver -> {
                    List<String> current = shoppingCartPage.getCartProductNames();
                    return !current.contains(productName) ? current : null;
                });

        // Verify assertions
        Assert.assertTrue(updatedProducts.contains(productName2),
                "Cart should contain: " + productName2);
//        Assert.assertTrue(updatedProducts.contains(productName3),
//                "Cart should contain: " + productName3);

        // Clear remaining items

    }
    @Test(priority = 3)
    public void userClearTheCart(){
        shoppingCartPage.clearCart();
        Assert.assertTrue(shoppingCartPage.getCartEmptyMessage()
                        .contains("You have no items in your shopping cart"),
                "Cart should be empty");
    }
}
