import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.AssertJUnit.assertTrue;

public class TraditionalTests extends BaseTest {

    @Test
    public void loginUITest() {

        openLoginPage();

        //1
        softAssert.assertThat(findElement(By.cssSelector("img[src='img/logo-big.png']"))).isInstanceOf(WebElement.class);

        //2
        softAssert.assertThat(findElement(By.cssSelector(".auth-header")).getText().trim().equals("Login " +
                "Form"));

        //3
        //The small horizontal bar is not identifiable and it's presence could not be recognized

        //4
        softAssert.assertThat(findElement(By.cssSelector("div.form-group:nth-child(1) > label")).getText().trim().equals(
                "Username"));

        //5
        softAssert.assertThat(findElement(By.cssSelector(".os-icon-user-male-circle")) instanceof WebElement);

        //6
        assertTrue(findElement(By.id("username")) instanceof WebElement);
        softAssert.assertThat(findElement(By.id("username")).getAttribute("placeholder").equals("Enter your username"));

        //7
        softAssert.assertThat(findElement(By.cssSelector("div.form-group:nth-child(2) > label")).getText().trim().equals(
                "Password"));

        //8
        softAssert.assertThat(findElement(By.cssSelector(".os-icon-fingerprint")) instanceof WebElement);

        //9
        assertTrue(findElement(By.id("password")) instanceof WebElement);
        softAssert.assertThat(findElement(By.id("password")).getAttribute("placeholder").equals("Enter your password"));

        //10
        softAssert.assertThat(findElement(By.id("log-in")) instanceof WebElement);

        //11
        softAssert.assertThat(findElement(By.cssSelector(".form-check-input")) instanceof WebElement);

        //12
        softAssert.assertThat(findElement(By.cssSelector(".form-check-label")).getText().trim().equals("Remember Me"));

        //13
        softAssert.assertThat(findElement(By.cssSelector("img[src='img/social-icons/twitter.png']")) instanceof WebElement);

        //14
        softAssert.assertThat(findElement(By.cssSelector("img[src='img/social-icons/facebook.png']")) instanceof WebElement);

        //15
        softAssert.assertThat(findElement(By.cssSelector("img[src='img/social-icons/linkedin.png']")) instanceof WebElement);
        softAssert.assertAll();
    }
    // 1 hour - along with initial framework setup

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

        enterText(findElement(By.id("username")), username);
        enterText(findElement(By.id("password")), password);
        findElement(By.id("log-in")).click();

        if (!username.isEmpty() && !password.isEmpty()) {
            assertTrue(findElement(By.id("showExpensesChart")) instanceof WebElement);
            return;
        }

        WebElement errorElement = waitFor(By.cssSelector("div.alert-warning"));
        assertTrue(errorElement instanceof WebElement);
        System.out.println(String.format("Error Msg: [%s]", errorElement.getText().trim()));
        assertTrue(errorElement.getText().trim().equals(errorMsg));
    }

    private List<Double> getAmountsAsList() {
        List<WebElement> amountCol = findElements(By.cssSelector("table#transactionsTable tbody " +
                "tr td:nth-child(5)"));

        return amountCol
                .stream().map(e -> Double.valueOf(e.getText()
                        .replace("+ ", "").replace(",", "")
                        .replace("- ", "-").split(" ")[0])).collect(Collectors.toList());

    }

    private List<String> getRowAsList() {
        List<WebElement> rows = findElements(By.cssSelector("table#transactionsTable tbody tr"));

        List myList = new ArrayList();

        for (WebElement row : rows) {
            String temp = "";
            for (WebElement cell : row.findElements(By.cssSelector("td"))) {
                temp += "-" + cell.getText();
            }
            myList.add(temp);
        }

        System.out.println("myList = " + myList);
        return myList;
    }

    @Test
    public void tableSortTest() {
        openLoginPage();
        login();

        List beforeSorting = getRowAsList();

        List<Double> amount = getAmountsAsList();

        System.out.println("amount before sorting = " + amount);
        Collections.sort(amount);
        System.out.println("amount before sorting through collection = " + amount);

        WebElement amountCol = waitFor(By.id("amount"));
        amountCol.click();
        sleep(1000);

        List afterSorting = getRowAsList();

        List<Double> amountNew = getAmountsAsList();

        System.out.println("amount after sorting through App = " + amount);

        assertTrue(amount.equals(amountNew));

        // Make sure sorting is across row and not only applied to the column
        afterSorting.forEach(p ->
        {
            if (!beforeSorting.contains(p))
                assertTrue("Table is not sorted correctly", false);
        });

    }

    @Test
    public void cavasTest() {
        openLoginPage();
        login();

        WebElement expenseLink = waitFor(By.id("showExpensesChart"));

        expenseLink.click();

        assertTrue(waitFor(By.id("canvas")) instanceof WebElement);

        // Can't access the chart bars from the DOM in traditional way as everything is wrapped under single canvas
        // element

        //Though one can use supporting libraries like ocular to take snapshot of charts to create baseline and then
        // re run tests to compare with them
        //https://github.com/vinsguru/ocular

        findElement(By.id("addDataset")).click();
        softAssert.assertAll();

    }

    @Test
    public void dynamicAdTest() {
        driver.get(hackathonV1 + "?showAd=true");
        sleep(2000);

        login();

        softAssert.assertThat(findElement(By.id("flashSale")) instanceof WebElement);
        softAssert.assertThat(findElement(By.cssSelector("div#flashSale > img")) instanceof WebElement);
        softAssert.assertThat(findElement(By.cssSelector("div#flashSale > img")).getAttribute("src").isEmpty());

        softAssert.assertThat(findElement(By.id("flashSale2")) instanceof WebElement);
        softAssert.assertThat(findElement(By.cssSelector("div#flashSale2 > img")) instanceof WebElement);
        softAssert.assertThat(!findElement(By.cssSelector("div#flashSale2 > img")).getAttribute("src").isEmpty());

        softAssert.assertAll();

    }
}
