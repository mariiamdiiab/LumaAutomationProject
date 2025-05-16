package tests.products;

import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.ComparePage;
import pages.ProductPage;
import pages.SearchPage;
import tests.TestBase;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Epic("Products")
@Feature("Compare Functionality")
public class AddProductToCompareTest extends TestBase {
    private ProductPage productPage;
    private ComparePage comparePage;
    private SearchPage searchPage;

    @BeforeMethod
    public void pageSetup() {
        productPage = new ProductPage(driver);
        comparePage = new ComparePage(driver);
        searchPage = new SearchPage(driver);
    }

    @DataProvider(name = "compareData")
    public Object[][] provideCompareData() throws IOException {
        ExcelReader er = new ExcelReader();
        return er.getExcelDataForCompare();
    }

    @Test(dataProvider = "compareData", groups = "ValidTests")
    @Severity(SeverityLevel.NORMAL)
    @Story("Product Comparison")
    @Description("Verify user can compare two products and view them in comparison table")
    public void verifyProductComparison(String productName1, String productName2) {
        Allure.parameter("Product 1", productName1);
        Allure.parameter("Product 2", productName2);

        Allure.step("1. Search and add first product to compare", () -> {
            searchPage.productSearch(productName1);
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName1),
                    String.format("Page title should contain '%s'", productName1));
            productPage.addToCompare();
        });

        Allure.step("2. Search and add second product to compare", () -> {
            searchPage.productSearch(productName2);
            searchPage.openProductPage();
            Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName2),
                    String.format("Page title should contain '%s'", productName2));
            productPage.addToCompare();
        });

        Allure.step("3. Verify products in comparison table", () -> {
            comparePage.openComparePage();
            List<String> comparedNames = comparePage.getComparedProductNames();

            Assert.assertTrue(comparedNames.contains(productName1),
                    String.format("Compare table should contain '%s'", productName1));
            Assert.assertTrue(comparedNames.contains(productName2),
                    String.format("Compare table should contain '%s'", productName2));
            Assert.assertEquals(comparedNames.size(), 2,
                    "There should be exactly 2 products in compare list");
        });

        Allure.step("4. Clear comparison table", () -> {
            comparePage.clearCompareTable();
            Assert.assertEquals(comparePage.getNoProductToCompareMsg(),
                    "You have no items to compare.",
                    "Comparison table should be empty after cleanup");
        });

        Allure.addAttachment("Test Result", "text/plain",
                String.format("Compared products: %s and %s", productName1, productName2));
    }
}