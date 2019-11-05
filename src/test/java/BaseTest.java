import com.applitools.eyes.selenium.Eyes;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.stqa.selenium.factory.WebDriverPool;

import java.util.List;

public class BaseTest {

    protected final String hackathonV1 = "https://demo.applitools.com/hackathon.html";

    protected final String hackathonV2 = "https://demo.applitools.com/hackathonV2.html";

    protected String baseUrl = hackathonV1;

    protected WebDriver driver;

    protected Eyes eyes;

    protected SoftAssertions softAssert;

    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void openLoginPage() {
        driver.get(hackathonV1);
        sleep(2000);
    }

    protected void login() {
        enterText(findElement(By.id("username")), "a");
        enterText(findElement(By.id("password")), "b");
        findElement(By.id("log-in")).click();
        sleep(1500);
    }

    protected WebElement findElement(By by) {
        try {
            return driver.findElement(by);
        } catch (NoSuchElementException e) {
            System.out.println("e = " + e.getMessage());
            return null;
        } catch (TimeoutException e) {
            System.out.println("e = " + e.getMessage());
            return null;
        }
    }

    protected List<WebElement> findElements(By by) {
        try {
            return driver.findElements(by);
        } catch (NoSuchElementException e) {
            System.out.println("e = " + e.getMessage());
            return null;
        } catch (TimeoutException e) {
            System.out.println("e = " + e.getMessage());
            return null;
        }
    }

    public WebElement waitFor(By locatorBy) {
        return new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(locatorBy));
    }

    protected void enterText(WebElement element, String value) {
        element.click();
        element.clear();
        if (value.isEmpty())
            return;
        element.sendKeys(value);
    }


    @BeforeSuite
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = WebDriverPool.DEFAULT.getDriver(new ChromeOptions());
        driver.manage().window().fullscreen();
        softAssert = new SoftAssertions();

        eyes = new Eyes();
        eyes.setApiKey("9QF54IRHkSfE109GSWNLMWlB5cYOLPEwplwoBnd8dSxLo110");

    }

    @AfterSuite
    public void tearDown() {
        //softAssert.assertAll();
        WebDriverPool.DEFAULT.dismissDriver(driver);
        eyes.abortIfNotClosed();
    }

}
