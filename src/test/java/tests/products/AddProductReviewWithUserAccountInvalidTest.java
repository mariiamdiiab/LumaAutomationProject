    package tests.products;
    import data.ExcelReader;
    import io.qameta.allure.*;
    import org.testng.Assert;
    import org.testng.annotations.DataProvider;
    import org.testng.annotations.Test;
    import pages.*;
    import tests.TestBase;
    import utilites.GlobalVariable;
    
    import java.io.IOException;
    import java.util.Arrays;

    @Epic("Products")
    @Feature("Add Review Functionality")

    public class AddProductReviewWithUserAccountInvalidTest extends TestBase {
        SearchPage searchPage;
        ProductPage productPage;
        HomePage homePage;
        SignInPage signInPage;
        AddProductReviewPage addProductReviewPage;

        @DataProvider(name = "ProductReviewData")
        public Object[][] getProductReviewData() throws IOException {
            ExcelReader er = new ExcelReader();
            Object[][] allData = er.getExcelDataForAddingReview();
            return Arrays.copyOfRange(allData, 3, allData.length);
        }


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
    
        @Test(dataProvider = "ProductReviewData",priority = 1)
        @Severity(SeverityLevel.NORMAL)
        public void searchForProductAndAddReview(String Tc_Id,String description ,String productName,String guest,String summary, String review,String error) {

            Allure.description(description);

            searchPage = new SearchPage(driver);
            searchPage = new SearchPage(driver);
            productPage = new ProductPage(driver);
            addProductReviewPage = new AddProductReviewPage(driver);

            Allure.step("Enter product name in the search bar", () -> searchPage.productSearch(productName));

            Allure.step("Verify product appears in search results", () -> {
                Assert.assertTrue(searchPage.getProductTitleWrapperInSearchPage().contains(productName),
                        "Product not found in search results.");
                Assert.assertEquals(searchPage.getProductName(), productName,
                        "Product name mismatch in search result.");
            });

            Allure.step("Open the product page", () -> searchPage.openProductPage());

            Allure.step("Open review Page",() -> productPage.addProductReview());

            Allure.step("Add Product Review And summary", ()-> addProductReviewPage.addReviewForSignedInUsers(summary, review));

            Allure.step("Verify Error message is displayed" ,()-> Assert.assertEquals(addProductReviewPage.getErrorMsg(),error));

            Allure.addAttachment("Test Case Result", Tc_Id + " passed successfully.");


        }
    
        @Test(dependsOnMethods = "searchForProductAndAddReview",priority = 2)
        @Description("Sign Out From The Account")
            public void UserCanSignOut(){
            Allure.step("Sign out from the account", () -> homePage.SignOut());
            }
    
    }