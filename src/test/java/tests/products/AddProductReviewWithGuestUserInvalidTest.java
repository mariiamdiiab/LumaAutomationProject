package tests.products;


import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.AddProductReviewPage;
import pages.ProductPage;
import pages.SearchPage;
import tests.TestBase;

import java.io.IOException;
import java.util.Objects;

@Epic("Products")
@Feature("Add Review Functionality")
public class AddProductReviewWithGuestUserInvalidTest extends TestBase {
    SearchPage searchPage;
    ProductPage productPage;

    @DataProvider(name = "ProductReviewData")
    public Object[][] getProductReviewData() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForAddingReview();
    }

    @Test(dataProvider = "ProductReviewData")
    @Severity(SeverityLevel.MINOR)
    public void searchForProductAndAddReview(String Tc_Id,String description ,String productName,String guest,String summary, String review,String error) {
        Allure.description(description);

        searchPage = new SearchPage(driver);
        searchPage.productSearch(productName);

        // Assertions to verify the product appear in the search results

        Allure.step("Search and add first product to compare: " + productName, () -> {
            searchPage.productSearch(productName);
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName));
        });

        // Step 3: Add the review for the product
        productPage = new ProductPage(driver);
        AddProductReviewPage addProductReviewPage = new AddProductReviewPage(driver);

        productPage.addProductReview();
        addProductReviewPage.addReviewForGuestUsers(guest, summary, review);

        // Validate the success message after submitting the review
        Assert.assertEquals(addProductReviewPage.getErrorMsg(),error);
        System.out.println("test case id: "+ Tc_Id +" passed");

    }
}

