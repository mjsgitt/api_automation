package api.testcases;

import api.utilities.ExtentListenerClass;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.RestAssured;
import static io.restassured.config.SSLConfig.sslConfig;


import api.utilities.readConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;



import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class baseClass extends ExtentListenerClass {
    // setup the config for the url's
    readConfig config = new readConfig();
    // getting the url and browser
    String URL = config.getURL();
    String browser = config.getBrowser();

    public WebDriver driver;
    public static Logger logger;

    SeleniumProgressTracker tracker;



    @BeforeClass
    public void setup(){
        //launch browser
        switch(browser.toLowerCase())
        {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;

            case "msedge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = new EdgeOptions();
//                 Add arguments for headless mode
//                options.addArguments("--headless");
//                options.addArguments("--disable-gpu");  // Recommended for headless mode
//                options.addArguments("--no-sandbox");   // Recommended for running in Docker
//                options.addArguments("--window-size=1366,784");
//                driver = new EdgeDriver(options);
                driver = new EdgeDriver();
                tracker = new SeleniumProgressTracker(5);

                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            default:
                driver = null;
                break;

        }
        // maximizing windows

        driver.manage().window().maximize();
        //implicit wait of 10 secs
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        //for logging
        logger = LogManager.getLogger("SIT_Automation");

        RestAssured.config = RestAssured.config().sslConfig(sslConfig().relaxedHTTPSValidation());

        //open url
        driver.get(URL);
        logger.info("url opened");
    }
    public static WebElement waitForElementByXPath(WebDriver driver, String xpath, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }
    //user method to capture screen shot
    public void captureScreenShot(WebDriver driver,String testName) throws IOException
    {
        TakesScreenshot screenshot = ((TakesScreenshot)driver);
        File src = screenshot.getScreenshotAs(OutputType.FILE);
        File dest = new File(System.getProperty("user.dir") + "//ScreenShots//" + testName + ".png");
        FileUtils.copyFile(src, dest);
    }
    @AfterClass
    public void tearDown()
    {
        driver.close();
        driver.quit();
    }
}
