package tests.products;

import data.ExcelReader;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.SearchPage;
import tests.TestBase;

import java.io.IOException;

public class SearchProductTest extends TestBase {
    SearchPage searchPage;



    @DataProvider(name = "ExcelData")

    public Object[][]UserRegisterData() throws IOException {
        ExcelReader er=new ExcelReader();
        return er.getExcelDataForSearchProduct();
    }


    @Test(dataProvider = "ExcelData")
    @Severity(SeverityLevel.NORMAL)
    public void userCanSearchForProduct(String productName){
        searchPage=new SearchPage(driver);
        searchPage.productSearch(productName);

        Assert.assertTrue(searchPage.getProductTitleWrapperInSearchPage().contains(productName));
        Assert.assertEquals(searchPage.getProductName(),productName);
        searchPage.openProductPage();

        System.out.println("test case id:TC_SearchProduct_017 passed");

    }
}
