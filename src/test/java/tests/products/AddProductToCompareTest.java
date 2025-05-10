package tests.products;

import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ComparePage;
import pages.ProductPage;
import pages.SearchPage;
import tests.TestBase;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Epic("products")
@Feature("Add To compare Functionality")
public class AddProductToCompareTest extends TestBase {

    ProductPage productPage;
    ComparePage comparePage;
    SearchPage searchPage;

    @DataProvider(name = "ExcelData")
    public Object[][] UserRegisterData() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForCompare();
    }

    @Test(dataProvider = "ExcelData")
    @Severity(SeverityLevel.MINOR)
    @Description("Validate the functionality of comparing 2 items")
    public void userCanCompareProducts(String productName1, String productName2) {
        productPage = new ProductPage(driver);
        comparePage = new ComparePage(driver);
        searchPage = new SearchPage(driver);

        // Step 1: Search & add first product
        Allure.step("Search and add first product to compare: " + productName1, () -> {
            searchPage.productSearch(productName1);
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName1));
            productPage.addToCompare();
        });

        Allure.step("Search and add second product to compare: " + productName2, () -> {
            searchPage.productSearch(productName2);
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName2));
            productPage.addToCompare();
        });

        Allure.step("Verify both products appear in the comparison list", () -> {
            comparePage.openComparePage();
            List<String> comparedNames = comparePage.getComparedProductNames();

            Assert.assertTrue(comparedNames.contains(productName1),
                    "Compare table should contain: " + productName1);
            Assert.assertTrue(comparedNames.contains(productName2),
                    "Compare table should contain: " + productName2);
            Assert.assertEquals(comparedNames.size(), 2,
                    "There should be exactly 2 products in the compare list");
        });

        Allure.step("Clear comparison table and verify cleanup", () -> {
            comparePage.clearCompareTable();
            Assert.assertEquals(comparePage.getNoProductToCompareMsg(),
                    "You have no items to compare.");
        });

        Allure.addAttachment("Test Case Result", "Test case TC_Compare_025 passed successfully.");
    }
}