package org.lambdatest;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;

import static org.lambdatest.Constants.*;


public class AssignmentTests {
    public static RemoteWebDriver driver = null;
    String expectedUrl = "https://www.lambdatest.com/integrations";

    @BeforeMethod
    @Parameters(value = {"browser", "version", "platform"})
    public void setUp(String browser, String version, String platform) throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("version", version);
        capabilities.setCapability("platform", platform);
        capabilities.setCapability("build", "lambdatestbuild");
        capabilities.setCapability("name", "Test01");
        capabilities.setCapability("console", true);
        capabilities.setCapability("network", true);
        capabilities.setCapability("visual", true);

        try {
            driver = new RemoteWebDriver(new URL("https://" + USERNAME + ":" + ACCESS_KEY + LAMBDA_URL), capabilities);
        } catch (MalformedURLException e) {
            throw new Exception("LAMBDA_URL is not valid");
        } catch (Exception e) {
            throw new Exception("Exception raised during the remotewebdrive object creation! " + e);
        }
    }


    @Test
    @Parameters(value = {"platform"})
    public void firstTest(String platform) throws Exception {
        driver.get("https://www.lambdatest.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='Logo']")));

        WebElement element = driver.findElement(By.xpath("//h2[text()='Seamless Collaboration']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        WebElement lambdaIntegrations = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='See All Integrations']")));
        if (platform.contains("mac")) {
            new Actions(driver).keyDown(Keys.COMMAND).click(lambdaIntegrations).keyUp(Keys.COMMAND).build().perform();
        } else {
            new Actions(driver).keyDown(Keys.CONTROL).click(lambdaIntegrations).keyUp(Keys.CONTROL).build().perform();
        }
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("search")));

        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl);
    }


    @AfterMethod
    public void tearDown() throws Exception {
        driver.quit();
    }
}
