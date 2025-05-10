package tests.products;

import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.SearchPage;
import tests.TestBase;

import java.io.IOException;

@Epic("Products")
@Feature("Search Functionality")
public class SearchProductTest extends TestBase {
    SearchPage searchPage;

    @DataProvider(name = "searchProductData")
    public Object[][] searchProductData() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForSearchProduct();
    }

    @Test(dataProvider = "searchProductData")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate that the user can search for an existing product by name")
    public void verifyUserCanSearchExistingProduct(String productName) {
        searchPage = new SearchPage(driver);

        Allure.step("Enter product name in the search bar", () -> searchPage.productSearch(productName));

        Allure.step("Verify product appears in search results", () -> {
            Assert.assertTrue(searchPage.getProductTitleWrapperInSearchPage().contains(productName),
                    "Product not found in search results.");
            Assert.assertEquals(searchPage.getProductName(), productName,
                    "Product name mismatch in search result.");
        });

        Allure.step("Open the product page", () -> searchPage.openProductPage());

        Allure.addAttachment("Test Case Result", "Search product test passed for: " + productName);
    }
}
