package TestBase;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

public class TestBase {

    protected static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
    public static String remote_url = System.getProperty("remoteUrl", "http://localhost:4445/"); // Configurable URL
    public Capabilities capabilities;

    @Parameters({ "browser" })
    @BeforeMethod // Using @BeforeMethod to ensure each test gets its own driver
    public void setDriver(String browser) throws MalformedURLException {

        System.out.println("Test is running on " + browser);

        // Initialize the correct browser options
        switch (browser.toLowerCase()) {
            case "firefox":
                capabilities = new FirefoxOptions();
                break;
            case "chrome":
                capabilities = new ChromeOptions();
                break;
            case "edge":
                capabilities = new EdgeOptions();
                break;
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        // Set up the driver for RemoteWebDriver with the desired capabilities
        driver.set(new RemoteWebDriver(new URL(remote_url), capabilities));
        driver.get().get("https://opensource-demo.orangehrmlive.com/");
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        // Adding wait for page load
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='divUsername']/span")));
    }

    public WebDriver getDriver() {
        return driver.get();
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
