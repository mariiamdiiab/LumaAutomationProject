    package tests.products;
    import data.ExcelReader;
    import org.testng.Assert;
    import org.testng.annotations.DataProvider;
    import org.testng.annotations.Test;
    import pages.*;
    import tests.TestBase;
    import utilites.GlobalVariable;
    
    import java.io.IOException;
    import java.util.Arrays;

    public class AddProductReviewWithUserAccountInvalidTest extends TestBase {
        SearchPage searchPage;
        ProductPage productPage;
        HomePage homePage;
        SingInPage singInPage;

        @DataProvider(name = "ProductReviewData")
        public Object[][] getProductReviewData() throws IOException {
            ExcelReader er = new ExcelReader();
            Object[][] allData = er.getExcelDataForAddingReview();

            // Skip first 2 rows (start from index 2)
            return Arrays.copyOfRange(allData, 3, allData.length);
        }
    
    
        @Test
        public void userCanSignInSuccessfully() {
            homePage = new HomePage(driver);
            singInPage = new SingInPage(driver);
            homePage.openSignInPage();
            singInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD);
            Assert.assertTrue(homePage.getWelcomeMessage().contains("Welcome"), "the website should include Welcome");
    
        }
    
        @Test(dataProvider = "ProductReviewData",priority = 1)
        public void searchForProductAndAddReview(String tcId,String description ,String productName,String guest,String summary, String review,String error) {
            // Step 1: Search for the product
            searchPage = new SearchPage(driver);
            searchPage.productSearch(productName);
    
            // Assertions to verify the product appear in the search results
    
    
            Assert.assertTrue(searchPage.getProductTitleWrapperInSearchPage().contains(productName));
            Assert.assertEquals(searchPage.getProductName(),productName);
    
            //open product page
            searchPage.openProductPage();
    
            // Step 3: Add the review for the product
            productPage = new ProductPage(driver);
            AddProductReviewPage addProductReviewPage = new AddProductReviewPage(driver);
    
            productPage.addProductReview();
            addProductReviewPage.addReviewForSignedInUsers(summary, review);
    
            // Validate the success message after submitting the review
            Assert.assertEquals(addProductReviewPage.getErrorMsg(),error);
            System.out.println("test case id: "+ tcId +" passed");
    
        }
    
        @Test(dependsOnMethods = "searchForProductAndAddReview",priority = 2)
            public void UserCanSignOut(){
                homePage.SignOut();
    
            }
    
    }