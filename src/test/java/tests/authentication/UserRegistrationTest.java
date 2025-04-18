package tests.authentication;

import com.github.javafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.MyAccountPage;
import pages.UserRegistrationPage;
import tests.TestBase;


public class UserRegistrationTest extends TestBase {

   Faker fakeDate=new Faker();
   String firstName=fakeDate.name().firstName();
   String lastName=fakeDate.name().lastName();
   String email=fakeDate.internet().emailAddress();
   String password="SecurePass12@";


    @Test
        public void userCanRegisterSuccessfully() {
            HomePage homePage = new HomePage(driver);
            UserRegistrationPage userRegistrationPage = new UserRegistrationPage(driver);
            MyAccountPage myAccountPage = new MyAccountPage(driver);

            homePage.openRegistrationPage();
            userRegistrationPage.userRegistration(
                    firstName,
                    lastName,
                    email,
                    password
            );

            String actualMessage = myAccountPage.getSuccessMessage();
            Assert.assertTrue(actualMessage.contains("Thank you for registering"),
                    "Expected success message not found. Actual: " + actualMessage);

        System.out.println("first name is: "+firstName
        +" last name is: "+lastName
        +" email is: "+email);
        System.out.println("tc id is: TC_Reg_001 passed");

        homePage.SignOut();
        }
    }

