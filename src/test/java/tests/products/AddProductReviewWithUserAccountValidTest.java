package tests.products;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import tests.TestBase;
import utilites.GlobalVariable;

@Epic("Products")
@Feature("Add Review Functionality")

public class AddProductReviewWithUserAccountValidTest extends TestBase {
    SearchPage searchPage;
    ProductPage productPage;
    HomePage homePage;
    SignInPage signInPage;
    AddProductReviewPage addProductReviewPage;


    String summary="a nice shirt with good";
    String review="the best product i have ever bought";




    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can successfully sign in with valid credentials")
    public void userCanSignInSuccessfully() {
        homePage = new HomePage(driver);
        signInPage = new SignInPage(driver);

        Allure.step("Open the sign-in page", () -> homePage.openSignInPage());

        Allure.step("Enter valid email and password, then submit", () -> signInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD));

        Allure.step("Verify welcome message is displayed", () -> {
            String welcomeMessage = homePage.getWelcomeMessage();
            Assert.assertTrue(welcomeMessage.contains("Welcome"),
                    "Expected 'Welcome' message not found in: " + welcomeMessage);
        });

    }
    @Test(priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate the functionality of reviewing item as a registered user")
    public void userCanAddReviewSuccessfully() {
        searchPage = new SearchPage(driver);
        searchPage = new SearchPage(driver);
        productPage = new ProductPage(driver);
        addProductReviewPage = new AddProductReviewPage(driver);

        Allure.step("Enter product name in the search bar", () -> searchPage.productSearch(GlobalVariable.PRODUCT_NAME));

        Allure.step("Verify product appears in search results", () -> {
            Assert.assertTrue(searchPage.getProductTitleWrapperInSearchPage().contains(GlobalVariable.PRODUCT_NAME),
                    "Product not found in search results.");
            Assert.assertEquals(searchPage.getProductName(), GlobalVariable.PRODUCT_NAME,
                    "Product name mismatch in search result.");
        });

        Allure.step("Open the product page", () -> searchPage.openProductPage());

        Allure.step("Open review Page",() -> productPage.addProductReview());

        Allure.step("Add review and summary", () ->addProductReviewPage.addReviewForGuestUsers(GlobalVariable.GUEST_NICKNAME, summary, review));

        Allure.step("Validate the success message after submitting the review",()->
        Assert.assertEquals(addProductReviewPage.getSuccessMessage(),
                "You submitted your review for moderation.",
                "Review submission Done successfully"));

        Allure.addAttachment("Test Case Result", "Test case TC_Review_022 passed successfully.");
    }
    @Test(dependsOnMethods = "userCanAddReviewSuccessfully",priority = 2)
        public void UserCanSignOut(){
            homePage.SignOut();
        }

}