import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.Region;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class VisualAITests extends BaseTest {

    @DataProvider(name = "loginData")
    private static Object[][] credentials() {

        return new Object[][]{
                {"", "", "Both Username and Password must be present"},
                {"username1", "", "Password must be present"},
                {"", "password", "Username must be present"},
                {"username1", "password1", ""}};
    }

    public void openEyes(String testName) {
        eyes.setBatch(new BatchInfo(String.format("%s-Batch", testName)));
        eyes.open(driver, appName, testName);
    }

    //Using this specifically for data-driven tests for better naming and understanding purpose
    public void openEyes(String testName, String id) {
        BatchInfo batch = new BatchInfo("Login-Test-Batch");
        batch.setId(id);
        eyes.setBatch(batch);
        eyes.open(driver, appName, testName);
    }
    // start-region All Tests

    @Test
    public void loginPageUITest() {
        try {
            openEyes("loginPageUITest");
            openLoginPage();
            eyes.checkWindow("Check-login-page-layout");
            eyes.close();
        } finally {
            eyes.abortIfNotClosed();
        }

    }

    @Test(dataProvider = "loginData")
    public void dataDrivenLoginTests(String username, String password, String errorMsg) {
        try {
            openEyes("TestLogin-" + errorMsg.replace(" ", "-"), "param");
            openLoginPage();

            enterText(findElement(By.id("username")), username);
            enterText(findElement(By.id("password")), password);
            findElement(By.id("log-in")).click();

            if (!username.isEmpty() && !password.isEmpty()) {
                // Check-successful-login
                eyes.checkRegion(new Region(14, 62, 172, 58), -1, "Check-successful-login");
                return;
            }
            eyes.checkWindow("Check-Error-Messages");
            eyes.close();
        } finally {
            eyes.abortIfNotClosed();
        }
    }

    @Test
    public void tableSortTest() {
        try {
            openEyes("tableSortTest");
            eyes.setForceFullPageScreenshot(true);
            openLoginPage();
            login();
            eyes.checkWindow("Transaction-table-view-before-sorting");

            WebElement amountCol = waitFor(By.id("amount"));
            amountCol.click();
            sleep(2000);

            eyes.checkWindow("Transaction-table-view-after-sorting");
            eyes.close();
        } finally {
            eyes.abortIfNotClosed();
        }

    }

    @Test
    public void canvasTest() {
        try {
            openEyes("canvasTest");
            openLoginPage();
            login();

            WebElement expenseLink = waitFor(By.id("showExpensesChart"));
            expenseLink.click();

            eyes.checkWindow("Charts-before-adding-data-set");

            findElement(By.id("addDataset")).click();

            sleep(1000);

            eyes.checkWindow("Charts-after-adding-data-set");
            eyes.close();

        } finally {
            eyes.abortIfNotClosed();
        }

    }

    @Test
    public void dynamicAdTest() {
        try {
            openEyes("dynamicAdTest");
            //driver.get(baseUrl + "?showAd=true");
            openPage("?showAd=true");
            login();
            eyes.checkWindow("Check-Dynamic-Add-window");
            eyes.close();
        } finally {
            eyes.abortIfNotClosed();
        }
    }

    // end-region All Tests
}
