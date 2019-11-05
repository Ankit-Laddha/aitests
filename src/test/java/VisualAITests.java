import com.applitools.eyes.BatchInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class VisualAITests extends BaseTest {

    final String hackathonV2 = "https://demo.applitools.com/hackathon.html";

    final String hackathonV1 = "https://demo.applitools.com/hackathonV2.html";

    BatchInfo batch;

    private void openLoginPage() {
        driver.get(hackathonV1);
        sleep(2000);
    }

    @BeforeClass
    public void setupThisClass() {
        batch = new BatchInfo("Hackathon-Batch");
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

    private void login() {
        enterText(findElement(By.id("username")), "a");
        enterText(findElement(By.id("password")), "b");
        findElement(By.id("log-in")).click();
        sleep(1500);
    }

    @Test
    public void loginUITest() {

        openLoginPage();
        eyes.open(driver, "Hackathon App", "login-UI-Test-name");

        eyes.checkWindow("Home view");
        eyes.close();
    }

    @Test
    public void tableSortTest() {
        openLoginPage();
        login();
        eyes.open(driver, "tableSortTest App", "tableSortTest");
        eyes.checkWindow("Window with transaction table view before Sorting");

        WebElement amountCol = waitFor(By.id("amount"));
        amountCol.click();
        sleep(2000);

        eyes.checkWindow("Window with transaction table view after Sorting");

        eyes.close();

    }

    @Test
    public void cavasTest() {
        openLoginPage();
        login();

        WebElement expenseLink = waitFor(By.id("showExpensesChart"));

        expenseLink.click();

        eyes.open(driver, "cavasTest App", "cavasTest");
        eyes.checkWindow("Charts before adding data set");

        findElement(By.id("addDataset")).click();

        sleep(1000);

        eyes.checkWindow("Charts after adding data set");
        eyes.close();

    }

    @Test
    public void dynamicAdTest() {
        driver.get(hackathonV1 + "?showAd=true");
        sleep(2000);

        login();

        eyes.open(driver, "dynamicAdTest App", "dynamicAdTest");
        eyes.checkWindow("Dyanamic Add window");

        eyes.close();
    }
}
