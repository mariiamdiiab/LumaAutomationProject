package tests.products;


import data.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.AddProductReviewPage;
import pages.ProductPage;
import pages.SearchPage;
import tests.TestBase;
import utilites.GlobalVariable;

import java.io.IOException;

public class AddProductReviewWithGuestUserInvalidTest extends TestBase {
    SearchPage searchPage;
    ProductPage productPage;

    @DataProvider(name = "ProductReviewData")
    public Object[][] getProductReviewData() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForAddingReview();
    }

    // Combined test for searching and adding a review
    @Test(dataProvider = "ProductReviewData")
    public void searchForProductAndAddReview(String tcId,String descrption ,String productName,String summary, String review,String error) {
        // Step 1: Search for the product
        searchPage = new SearchPage(driver);
        searchPage.productSearch(productName);

        // Assertions to verify the product appears in the search results


        Assert.assertTrue(searchPage.getProductTitleWrapperInSearchPage().contains(productName));
        Assert.assertEquals(searchPage.getProductName(),productName);

        //open product page
        searchPage.openProductPage();

        // Step 3: Add the review for the product
        productPage = new ProductPage(driver);
        AddProductReviewPage addProductReviewPage = new AddProductReviewPage(driver);

        productPage.addProductReview();
        addProductReviewPage.addReviewForGuestUsers(GlobalVariable.GUEST_NICKNAME, summary, review);

        // Validate the success message after submitting the review
        Assert.assertEquals(addProductReviewPage.getErrorMsg(),error);
        System.out.println("test case id: "+ tcId +" passed");

    }
}

