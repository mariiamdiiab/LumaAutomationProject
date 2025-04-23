package tests.products;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import tests.TestBase;
import utilites.GlobalVariable;

public class AddProductReviewWithUserAccountValidTest extends TestBase {
    SearchPage searchPage;
    ProductPage productPage;
    HomePage homePage;
    SingInPage singInPage;
    String summary="a nice shirt with good";
    String review="the best product i have ever bought";




    @Test
    public void userCanSignInSuccessfully() {
        homePage = new HomePage(driver);
        singInPage = new SingInPage(driver);
        homePage.openSignInPage();
        singInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD);
        Assert.assertTrue(homePage.getWelcomeMessage().contains("Welcome"), "the website should include Welcome");

    }

    @Test(priority = 1)
    public void userCanAddReviewSuccessfully() {
        // Step 1: Search for the product
        searchPage = new SearchPage(driver);
        searchPage.productSearch(GlobalVariable.PRODUCT_NAME);

        // Assertions to verify the product appear in the search results
        Assert.assertTrue(searchPage.getProductTitleWrapperInSearchPage().contains(GlobalVariable.PRODUCT_NAME),
                "Product not found in search results");
        Assert.assertEquals(searchPage.getProductName(), GlobalVariable.PRODUCT_NAME,
                "Product name mismatch");

        // Step 2: Open the product page
        searchPage.openProductPage();

        // Step 3: Add the review for the product
        productPage = new ProductPage(driver);
        AddProductReviewPage addProductReviewPage = new AddProductReviewPage(driver);

        productPage.addProductReview();
        addProductReviewPage.addReviewForGuestUsers(GlobalVariable.GUEST_NICKNAME, summary, review);

        // Validate the success message after submitting the review
        Assert.assertEquals(addProductReviewPage.getSuccessMessage(),
                "You submitted your review for moderation.",
                "Review submission failed");
        System.out.println("test case id: TC_Review_022 passed");


    }
    @Test(dependsOnMethods = "userCanAddReviewSuccessfully",priority = 2)
        public void UserCanSignOut(){
            homePage.SignOut();

        }

}