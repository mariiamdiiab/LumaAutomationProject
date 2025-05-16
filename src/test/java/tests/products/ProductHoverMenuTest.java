package tests.products;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import tests.TestBase;

@Epic("Products")
@Feature("Hover Functionality")
public class ProductHoverMenuTest extends TestBase {
    private HomePage homePage;

    @Test(groups = "ValidTests")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate that user can navigate to jackets using hover menu")
    @Story("Menu Navigation")
    public void userCanSelectSubCategoryFromMenu() {
        homePage = new HomePage(driver);

        Allure.step("1. Hover over Women > Tops > Jackets menu", () -> homePage.selectJacketMenu());

        Allure.step("2. Verify the URL contains 'jackets'", () -> {
            String currentUrl = driver.getCurrentUrl();
            Assert.assertNotNull(currentUrl);
            Assert.assertTrue(currentUrl.contains("jackets"),
                    String.format("URL validation failed. Expected to contain 'jackets', Actual URL: %s", currentUrl));
        });

        Allure.step("3. Verify the page title is 'Jackets'", () -> {
            String actualTitle = homePage.getPageTitle();
            Assert.assertEquals(actualTitle, "Jackets",
                    String.format("Title mismatch. Expected: 'Jackets', Actual: '%s'", actualTitle));
        });

        Allure.addAttachment("Test Verification", "text/plain",
                """
                        Verified hover navigation to Jackets:
                        - URL contains 'jackets'
                        - Page title is 'Jackets'""");
    }
}