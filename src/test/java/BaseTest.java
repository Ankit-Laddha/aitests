import com.applitools.eyes.StdoutLogHandler;
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
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import ru.stqa.selenium.factory.WebDriverPool;

import java.util.List;

public class BaseTest {

    protected final String hackathonV1 = "https://demo.applitools.com/hackathon.html";

    protected final String hackathonV2 = "https://demo.applitools.com/hackathonV2.html";

    final String appName = "Hackathon-app-v2";

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
        driver.get(baseUrl);
        driver.manage().window().maximize();
        sleep(500);
    }

    protected void openPage(String relativeUrl) {
        driver.get(baseUrl + relativeUrl);
        driver.manage().window().maximize();
        sleep(500);
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
    public void oneTimeSetup() {
        // Using softAssertions as traditional tests has lot of ui assertions and we do not want to fail our test
        // on first failing assertions
        softAssert = new SoftAssertions();

        //Setup eyes
        eyes = new Eyes();
        eyes.setApiKey(System.getenv("APPILIKEY"));
        eyes.setLogHandler(new StdoutLogHandler(true));
    }

    @BeforeTest
    public void setup() {

        // This manages chrome driver
        WebDriverManager.chromedriver().setup();
        driver = WebDriverPool.DEFAULT.getDriver(new ChromeOptions());
        driver.manage().window().maximize();
        System.out.println("= Before Test = ");
    }

    @AfterTest
    public void tearDown() {
        WebDriverPool.DEFAULT.dismissDriver(driver);
        System.out.println("= After Test = ");
        softAssert.assertAll();
        if (eyes.getIsOpen()) {
            eyes.close();
            eyes.abortIfNotClosed();
        }
    }
}
//  Do eyes.open in tests
