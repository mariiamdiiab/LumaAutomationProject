package tests.checkOut;

import data.ExcelReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import tests.TestBase;
import utilites.GlobalVariable;
import java.io.IOException;
import java.util.Objects;

@Epic("Order History")
@Feature("Verify Order History")
public class OrderHistoryTest extends TestBase {
    private HomePage homePage;
    private SignInPage signInPage;
    private MyAccountPage myAccountPage;
    private ExcelReader excel;

    @BeforeMethod
    public void pageSetup() {
        homePage = new HomePage(driver);
        signInPage = new SignInPage(driver);
        myAccountPage = new MyAccountPage(driver);
        excel = new ExcelReader();
    }

    @DataProvider(name = "OrderIDData")
    public Object[][] orderIdDataProvider() throws IOException {
        String lastOrderId = excel.getLastOrderId();
        return new Object[][]{{lastOrderId}};
    }

    @Test
    @Story("User Login")
    @Description("Verify that a user can sign in successfully before accessing order history")
    public void userCanSignInSuccessfully() {
        Allure.step("Navigate to Sign In Page", homePage::openSignInPage);

        Allure.step("Login using valid credentials", () ->
                signInPage.userSignIn(GlobalVariable.EMAIL, GlobalVariable.PASSWORD));

        Allure.step("Verify Welcome Message", () -> {
            String welcomeMessage = homePage.getWelcomeMessage();
            Assert.assertTrue(welcomeMessage.contains("Welcome"),
                    "Expected 'Welcome' in message, but got: " + welcomeMessage);
        });
    }

    @Test(priority = 1, dependsOnMethods = "userCanSignInSuccessfully", dataProvider = "OrderIDData",groups = "ValidTests")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Order History Verification")
    @Description("Verify that the most recent order appears in the user's order history")
    public void userCheckIfOrderAppearsInOrderHistory(String orderId) {
        Allure.step("Ensure user is on the My Account page", () -> {
            if (!Objects.equals(driver.getTitle(), "My Account")) {
                homePage.openMyAccountPage();
            }
        });

        Allure.step("Fetch list of order IDs from account", () -> {
            var orderIDs = myAccountPage.getOrderIDs();
            Allure.addAttachment("Orders Found", String.join(", ", orderIDs));
            Assert.assertTrue(orderIDs.contains(orderId), "Order ID not found: " + orderId);
        });

        Allure.addAttachment("Test Case Result", "Order ID " + orderId + " appears in order history.");
        System.out.println("Order ID is: " + orderId);
    }
}