package api.utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import api.testcases.TA_SIT_AUTH;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.annotations.BeforeClass;

public class ExtentListenerClass implements ITestListener{

    ExtentSparkReporter  htmlReporter;
    public ExtentReports reports;
    public ExtentTest test;
    public ExtentTest infoSheet;

    public void configureReport()
    {
        readConfig readConfig = new readConfig();
        String timestamp = new SimpleDateFormat("MMM dd,yyyy hh_mm_ss a").format(new Date());
        String reportName = readConfig.getTestName() + timestamp + ".html";
        htmlReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "//Reports//" + reportName);

        reports = new ExtentReports();
        reports.attachReporter(htmlReporter);

        test = reports.createTest(readConfig.getTestName());
        infoSheet = test.createNode("Automation Report");

        //add system information/environment info to reports
        reports.setSystemInfo("Environment", "SIT");
        reports.setSystemInfo("browser:", readConfig.getBrowser());
        reports.setSystemInfo("user name:", "Amjad");

        //configuration to change look and feel of report
        htmlReporter.config().setDocumentTitle(readConfig.getTestName());
        htmlReporter.config().setReportName(readConfig.getTestName());
        htmlReporter.config().setTheme(Theme.STANDARD);
    }

    @BeforeClass
    //OnStart method is called when any Test starts.
    public void onStart(ITestContext Result)
    {
        configureReport();
    }

    //onFinish method is called after all Tests are executed
    public void onFinish(ITestContext Result)
    {
        System.out.println("On Finished method invoked....");
        reports.flush();//it is mandatory to call flush method to ensure information is written to the started reporter.

    }
    // When Test case get failed, this method is called.

    public void onTestFailure(ITestResult Result)
    {
        System.out.println("Name of test method failed:" + Result.getName() );
        String screenShotPath = System.getProperty("user.dir") + "\\ScreenShots\\" + Result.getName() + ".png";

        File screenShotFile = new File(screenShotPath);

        if(screenShotFile.exists())
        {
            test.fail("Captured Screenshot is below:" + test.addScreenCaptureFromPath(screenShotPath));

        }
    }

    // When Test case get Skipped, this method is called.

    public void onTestSkipped(ITestResult Result)
    {
        System.out.println("Name of test method skipped:" + Result.getName() );

        test = reports.createTest(Result.getName());
        test.log(Status.SKIP, MarkupHelper.createLabel("Name of the skip test case is: " + Result.getName() ,ExtentColor.YELLOW));
    }

    // When Test case get Started, this method is called.

    public void onTestStart(ITestResult Result)
    {
        System.out.println("Name of test method started:" + Result.getName() );
    }

    // When Test case get passed, this method is called.
    public void onTestSuccess(ITestResult Result)
    {
        for(int i = 1;i<TA_SIT_AUTH.testInfoNumber;i++){
            switch (TA_SIT_AUTH.testStatus[i]){
                case "click":
                    infoSheet.log(Status.PASS,TA_SIT_AUTH.testInfo[i]);
                    break;
                case "send":
                    infoSheet.log(Status.INFO,TA_SIT_AUTH.testInfo[i]);
                    break;
            }
        }
    }
}