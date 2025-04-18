package tests.products;

import data.ExcelReader;
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
    public void userCanCompareProducts(String productName1, String productName2) {
        productPage = new ProductPage(driver);
        comparePage = new ComparePage(driver);
        searchPage = new SearchPage(driver);

        // Add first product to compare
        searchPage.productSearch(productName1);
        searchPage.openProductPage();
        Assert.assertTrue(Objects.requireNonNull(driver.getTitle()).contains(productName1));
        productPage.addToCompare();

        // Add second product to compare
        searchPage.productSearch(productName2);
        searchPage.openProductPage();
        Assert.assertTrue(driver.getTitle().contains(productName2));
        productPage.addToCompare();

        // Verify both products in comparison
        comparePage.openComparePage();
        List<String> comparedNames = comparePage.getComparedProductNames();

        Assert.assertTrue(comparedNames.contains(productName1),
                "Compare table should contain: " + productName1);
        Assert.assertTrue(comparedNames.contains(productName2),
                "Compare table should contain: " + productName2);
        Assert.assertEquals(comparedNames.size(), 2,
                "There should be exactly 2 products in the compare list");

        // Clean up
        comparePage.clearCompareTable();
        Assert.assertEquals(comparePage.getNoProductToCompareMsg(),
                "You have no items to compare.");
    }
}