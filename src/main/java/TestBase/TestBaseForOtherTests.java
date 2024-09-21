package TestBase;

import java.net.MalformedURLException;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBaseForOtherTests {

    public WebDriver driver;
    ChromeOptions chromeOptions;
    EdgeOptions edgeOptions;

    @BeforeTest(alwaysRun = true)
    @Parameters({ "browser" })
    public void getDriver(String browser) throws MalformedURLException {
        try {
            System.out.println("Test is running on " + browser);

            if (browser.equalsIgnoreCase("firefox")) {
                // Use WebDriverManager to manage the FirefoxDriver
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();

            } else if (browser.equalsIgnoreCase("chrome")) {
                // Use WebDriverManager to manage the ChromeDriver and set the correct version
                WebDriverManager.chromedriver().clearDriverCache();
                WebDriverManager.chromedriver().setup();
                chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOptions);

            } else if (browser.equalsIgnoreCase("edge")) {
                // Use WebDriverManager to manage the EdgeDriver
                WebDriverManager.edgedriver().setup();
                edgeOptions=new EdgeOptions();
                edgeOptions.addArguments("--remote-allow-origins=*");
                driver = new EdgeDriver(edgeOptions);
            } else {
                throw new IllegalArgumentException("Browser not supported: " + browser);
            }

            driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
            driver.get("https://www.saucedemo.com/");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @AfterTest
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
