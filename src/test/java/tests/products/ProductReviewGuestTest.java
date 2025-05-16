package tests.products;

import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.AddProductReviewPage;
import pages.ProductPage;
import pages.SearchPage;
import tests.TestBase;
import utilites.GlobalVariable;

import java.io.IOException;

@Epic("Products")
@Feature("Guest Review Functionality")
public class ProductReviewGuestTest extends TestBase {
    private SearchPage searchPage;
    private ProductPage productPage;
    private AddProductReviewPage reviewPage;

    // Test Data
    private static final String VALID_SUMMARY = "a nice shirt with good";
    private static final String VALID_REVIEW = "the best product i have ever bought";

    @DataProvider(name = "reviewData")
    public Object[][] getReviewData() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForAddingReview();
    }
    @BeforeMethod
    public void pageSetup() {
        searchPage = new SearchPage(driver);
        productPage = new ProductPage(driver);
        reviewPage = new AddProductReviewPage(driver);
    }

    @Test(groups = "ValidTests")
    @Severity(SeverityLevel.NORMAL)
    @Story("Valid Guest Review")
    @Description("Verify guest user can submit valid product review")
    public void submitValidGuestReview() {
        Allure.step("1. Search for product", () -> {
            searchPage.productSearch(GlobalVariable.PRODUCT_NAME);
            Assert.assertTrue(
                    searchPage.getProductTitleWrapperInSearchPage().contains(GlobalVariable.PRODUCT_NAME),
                    "Product not found in search results");
            Assert.assertEquals(
                    searchPage.getProductName(), GlobalVariable.PRODUCT_NAME,
                    "Product name mismatch");
        });

        Allure.step("2. Open product page", searchPage::openProductPage);
        Allure.step("3. Open review form", productPage::addProductReview);

        Allure.step("4. Submit valid review", () ->
                reviewPage.addReviewForGuestUsers(GlobalVariable.GUEST_NICKNAME, VALID_SUMMARY, VALID_REVIEW));

        Allure.step("5. Verify success message", () ->
                Assert.assertEquals(
                        reviewPage.getSuccessMessage(),
                        "You submitted your review for moderation.",
                        "Review submission confirmation mismatch"));
    }

    @Test(dataProvider = "reviewData",groups = "InvalidTests")
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid Guest Reviews")
    @Description("Verify validation of invalid guest review submissions")
    public void submitInvalidGuestReviews(String tcId, String description,
                                          String productName, String guest,
                                          String summary, String review,
                                          String expectedError) {
        Allure.description(description);
        Allure.parameter("Test Case ID", tcId);
        Allure.parameter("Product", productName);

        Allure.step("1. Search for product", () -> {
            searchPage.productSearch(productName);
            Assert.assertTrue(
                    searchPage.getProductTitleWrapperInSearchPage().contains(productName),
                    "Product not found in search results");
            Assert.assertEquals(
                    searchPage.getProductName(), productName,
                    "Product name mismatch");
        });

        Allure.step("2. Open product page", searchPage::openProductPage);
        Allure.step("3. Open review form", productPage::addProductReview);
        Allure.step("4. Submit invalid review", () ->
                reviewPage.addReviewForGuestUsers(guest, summary, review));

        Allure.step("5. Verify error message", () -> {
            String actualError = reviewPage.getErrorMsg();
            Assert.assertEquals(actualError, expectedError,
                    String.format("Error message mismatch for TC %s\nExpected: %s\nActual: %s",
                            tcId, expectedError, actualError));
        });
    }
    
}
