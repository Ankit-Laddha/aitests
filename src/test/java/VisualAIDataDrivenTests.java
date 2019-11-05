import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.selenium.ClassicRunner;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class VisualAIDataDrivenTests extends BaseTest {

    final String hackathonV1 = "https://demo.applitools.com/hackathon.html";

    final String hackathonV2 = "https://demo.applitools.com/hackathonV2.html";

    BatchInfo batch;

    ClassicRunner runner;

    private void openLoginPage() {
        driver.get(hackathonV1);
        sleep(2000);
    }

    private void login() {
        enterText(findElement(By.id("username")), "a");
        enterText(findElement(By.id("password")), "b");
        findElement(By.id("log-in")).click();
        sleep(1500);
    }

    @BeforeClass
    public void setupThisClass() {
        batch = new BatchInfo("Data-Driven-Batch");
        eyes.setBatch(batch);
    }

    @DataProvider(name = "loginData")
    private static Object[][] credentials() {

        return new Object[][]{
                {"", "", "Both Username and Password must be present"},
                {"username1", "", "Password must be present"},
                {"", "password", "Username must be present"},
                {"username1", "password1", ""}};
    }

    @Test(dataProvider = "loginData")
    public void dataDrivenTest(String username, String password, String errorMsg) {
        openLoginPage();
        String generatedString = RandomStringUtils.randomAlphanumeric(3);
        eyes.open(driver, "Hackathon App", "Data-Driven-login-Test-name");

        enterText(findElement(By.id("username")), username);
        enterText(findElement(By.id("password")), password);
        findElement(By.id("log-in")).click();

        if (!username.isEmpty() && !password.isEmpty()) {
            assertTrue(findElement(By.id("showExpensesChart")) instanceof WebElement);
            return;
        }

        eyes.checkWindow("Error Messages");
        eyes.close();
    }

}
