package tests.products;


import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddProductReviewPage;
import pages.ProductPage;
import pages.SearchPage;
import tests.TestBase;
import utilites.GlobalVariable;


public class AddProductReviewWithGuestUserValidTest extends TestBase {
    SearchPage searchPage;
    ProductPage productPage;


    String summary="a nice shirt with good";
    String review="the best product i have ever bought";

    // Combined test for searching and adding a review
    @Test()
    @Severity(SeverityLevel.NORMAL)
    public void searchForProductAndAddReview() {
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

        System.out.println("test case id: TC_Review_018 passed");

    }
}

