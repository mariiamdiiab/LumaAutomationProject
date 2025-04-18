package utilites;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Helper {
    public static void attachScreenshotToAllure(WebDriver driver, String screenshotName) {
        Allure.addAttachment(screenshotName,
                new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    // Optional: Keep local file saving for debugging (but add to .gitignore)
    public static void saveScreenshotLocally(WebDriver driver, String screenshotName) {
        Path dest = Paths.get("./target/screenshots", screenshotName + ".png");
        try {
            Files.createDirectories(dest.getParent());
            Files.write(dest, ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
        } catch (IOException e) {
            System.out.println("Exception while saving screenshot: " + e.getMessage());
        }
    }
}
