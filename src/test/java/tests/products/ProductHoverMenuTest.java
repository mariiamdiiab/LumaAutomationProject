package tests.products;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import tests.TestBase;

import java.util.Objects;

@Epic("products")
@Feature("Hover Functionality")
public class ProductHoverMenuTest extends TestBase {

    HomePage homePage;

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Validate the functionality of hovering")
    public void userCanSelectSubCategoryFromMenu() {
        homePage = new HomePage(driver);

        Allure.step("Hover over Men > Tops > Jackets menu and click on Jackets", () -> {
            homePage.selectJacketMenu();
        });

        Allure.step("Verify that the URL contains 'jackets'", () -> {
            Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("jackets"));
        });

        Allure.step("Verify that the page title is 'Jackets'", () -> {
            Assert.assertEquals(homePage.getPageTitle(), "Jackets");
        });

        Allure.addAttachment("Test Case Result", "Test case TC_Hover_026 passed successfully.");
    }
}
