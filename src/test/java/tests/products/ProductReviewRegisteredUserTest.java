package tests.products;

import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import tests.TestBase;
import utilites.GlobalVariable;

import java.io.IOException;
import java.util.Arrays;

@Epic("Products")
@Feature("Product Review Functionality")
public class ProductReviewRegisteredUserTest extends TestBase {
    private SearchPage searchPage;
    private ProductPage productPage;
    private HomePage homePage;
    private SignInPage signInPage;
    private AddProductReviewPage addProductReviewPage;

    // Test Data
    private static final String VALID_SUMMARY = "a nice shirt with good";
    private static final String VALID_REVIEW = "the best product i have ever bought";

    @DataProvider(name = "invalidReviewData")
    public Object[][] getInvalidReviewData() throws IOException {
        ExcelReader er = new ExcelReader();
        Object[][] allData = er.getExcelDataForAddingReview();
        return Arrays.copyOfRange(allData, 3, allData.length);
    }

    @BeforeMethod
    public void pageSetup() {
        homePage = new HomePage(driver);
        signInPage = new SignInPage(driver);
        searchPage = new SearchPage(driver);
        productPage = new ProductPage(driver);
        addProductReviewPage = new AddProductReviewPage(driver);
    }

    @Test(groups = "ValidTests")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Authentication")
    @Description("Verify user can successfully sign in with valid credentials")
    public void userCanSignInSuccessfully() {
        Allure.step("1. Open the sign-in page", homePage::openSignInPage);

        Allure.step("2. Enter valid credentials", () ->
                signInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD));

        Allure.step("3. Verify welcome message", () -> {
            String welcomeMessage = homePage.getWelcomeMessage();
            Assert.assertTrue(welcomeMessage.contains("Welcome"),
                    String.format("Expected 'Welcome' message not found. Actual: %s", welcomeMessage));
        });
    }

    @Test(priority = 1,groups = "ValidTests")
    @Severity(SeverityLevel.NORMAL)
    @Story("Valid Review")
    @Description("Validate successful product review submission by registered user")
    public void submitValidProductReview() {
        Allure.step("1. Search for product", () ->
                searchPage.productSearch(GlobalVariable.PRODUCT_NAME));

        Allure.step("2. Verify search results", () -> {
            Assert.assertTrue(
                    searchPage.getProductTitleWrapperInSearchPage().contains(GlobalVariable.PRODUCT_NAME),
                    "Product not found in search results");
            Assert.assertEquals(
                    searchPage.getProductName(), GlobalVariable.PRODUCT_NAME,
                    "Product name mismatch in search results");
        });

        Allure.step("3. Open product page", searchPage::openProductPage);
        Allure.step("4. Open review form", productPage::addProductReview);
        Allure.step("5. Submit review", () ->
                addProductReviewPage.addReviewForSignedInUsers(VALID_SUMMARY, VALID_REVIEW));

        Allure.step("6. Verify success message", () ->
                Assert.assertEquals(
                        addProductReviewPage.getSuccessMessage(),
                        "You submitted your review for moderation.",
                        "Review submission confirmation message mismatch"));
    }

    @Test(dataProvider = "invalidReviewData", priority = 2,groups = "InvalidTests")
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid Review")
    @Description("Validate error handling for invalid review submissions")
    public void submitInvalidProductReview(String tcId, String description,
                                           String productName, String guest,
                                           String summary, String review, String expectedError) {
        Allure.description(description);
        Allure.parameter("Test Case ID", tcId);
        Allure.parameter("Product", productName);

        Allure.step("1. Search for product", () -> searchPage.productSearch(productName));

        Allure.step("2. Verify search results", () -> {
            Assert.assertTrue(
                    searchPage.getProductTitleWrapperInSearchPage().contains(productName),
                    "Product not found in search results");
            Assert.assertEquals(
                    searchPage.getProductName(), productName,
                    "Product name mismatch in search results");
        });

        Allure.step("3. Open product page", searchPage::openProductPage);
        Allure.step("4. Open review form", productPage::addProductReview);
        Allure.step("5. Submit invalid review", () ->
                addProductReviewPage.addReviewForSignedInUsers(summary, review));

        Allure.step("6. Verify error message", () -> {
            String actualError = addProductReviewPage.getErrorMsg();
            Assert.assertEquals(actualError, expectedError,
                    String.format("Error message mismatch for TC %s\nExpected: %s\nActual: %s",
                            tcId, expectedError, actualError));
        });
    }

    @Test(priority = 3, dependsOnMethods = {"submitValidProductReview", "submitInvalidProductReview"},groups = "ValidTests")
    @Severity(SeverityLevel.MINOR)
    @Story("Authentication")
    @Description("Sign out from the account after review tests")
    public void signOutAfterTests() {
        Allure.step("Sign out from account", homePage::SignOut);
    }
}