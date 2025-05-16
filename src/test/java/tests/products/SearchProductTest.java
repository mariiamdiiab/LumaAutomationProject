package tests.products;

import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.SearchPage;
import tests.TestBase;
import java.io.IOException;

@Epic("Products")
@Feature("Search Functionality")
public class SearchProductTest extends TestBase {
    private SearchPage searchPage;

    @BeforeMethod
    public void pageSetup() {
        searchPage = new SearchPage(driver);
    }

    @DataProvider(name = "searchProductData")
    public Object[][] provideSearchProductData() throws IOException {
        ExcelReader er = new ExcelReader();
        Object[][] allData = er.getExcelDataForSearchProduct();

        if (allData.length > 0) {
            return new Object[][] { allData[0] };
        }
        return new Object[0][0];
    }

    @Test(dataProvider = "searchProductData",groups = "ValidTests")
    @Severity(SeverityLevel.NORMAL)
    @Story("Product Search")
    @Description("Validate that user can search for existing products by name")
    public void verifyUserCanSearchExistingProduct(String productName) {
        Allure.parameter("Product Name", productName);

        Allure.step("1. Enter product name in search bar", () ->
                searchPage.productSearch(productName));

        Allure.step("2. Verify product appears in search results", () -> {
            String resultsTitle = searchPage.getProductTitleWrapperInSearchPage();
            Assert.assertTrue(resultsTitle.contains(productName),
                    String.format("Product '%s' not found in search results. Actual title: %s",
                            productName, resultsTitle));

            String firstResultName = searchPage.getProductName();
            Assert.assertEquals(firstResultName, productName,
                    String.format("Product name mismatch. Expected: %s, Actual: %s",
                            productName, firstResultName));
        });

        Allure.step("3. Open the product page", () ->
                searchPage.openProductPage());

        Allure.addAttachment("Test Data", "text/plain",
                "Searched product: " + productName);
    }
}