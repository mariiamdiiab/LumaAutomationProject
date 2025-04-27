package tests.products;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import tests.TestBase;

import java.util.Objects;

public class ProductHoverMenuTest extends TestBase {

    HomePage homePage;

    @Test
    public void userCanSelectSubCategoryFromMenu(){
        homePage=new HomePage(driver);
        homePage.selectJacketMenu();
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("jackets"));
        Assert.assertEquals(homePage.getPageTitle(),"Jackets");

        System.out.println("test case id: TC_Hover_026 passed");

    }
}
