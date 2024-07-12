package api.testcases;

import api.utilities.ClipboardUtil;
import api.utilities.ReadExcelFile;
import api.utilities.readConfig;

import org.openqa.selenium.*;
import org.testng.annotations.Test;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.awt.*;
import java.util.List;
import java.time.Duration;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.datatransfer.StringSelection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TA_SIT_AUTH extends baseClass {
    // 0,1,2,3,4,5,6,7,8
    private String[] excelValues;  // String ID, String field, String Annotate, String AnnotateValue, String Status, String InputValue, String IndexNumber, String findClass, String Flow
    public static String[] testInfo;
    public static String[] testStatus;
    public static int testInfoNumber;


    private final String FILE_PATH = System.getProperty("user.dir") + "\\TestDataResources\\TestData2.xlsx";
    private final String SHEET_NAME = "ATAP_API_Template";

    private int indexNumber;
    private int EleValue;
    private int numericIndex;
    private String StringIndexNumber;
    private String ElementIndex;
    private String stringIndex;

    enum ActionType {
        SEND, CLICK, SENDS, CLICKS, LASTSENDS, LASTCLICKS, ACTION, ACTION_KEY_A, ACTION_KEY_BACKSPACE, COPYTEXT,ACTION_KEYS_A,ACTION_KEYS_BACKSPACE,COPYTEXTS, PASTETEXT, SENDINT, SENDDECI, SENDDECIS, COPYSENDS, SENDSINT,ITERATIONSENDS, ITERATIONCLICKS, ITERATIONSENDSINT, SCROLLTOTOP, SCROLLTOBOTTOM, WAIT
    }
    @Test
    public void TS_HCMP_LogAnalytics_01_Groupping() throws IOException {
        int ttlRows = ReadExcelFile.rowCount(FILE_PATH, SHEET_NAME);
        int ttlColumns = ReadExcelFile.colCount(FILE_PATH, SHEET_NAME);

        excelValues = new String[ttlColumns]; // len 8
        testInfo = new String[ttlRows];
        testStatus = new String[ttlRows];
        testInfoNumber = ttlRows;

        for (int i = 1; i < ttlRows; i++) {
            // row select r - 1
            for (int j = 0; j < ttlColumns; j++) { // 0,1..8
                // coloum - cell itration
                excelValues[j] = ReadExcelFile.GetCellValue(FILE_PATH, SHEET_NAME, i, j);
            }
            // ignore -
//            if (!excelValues[5].isEmpty()) {
//                waitForElement(Duration.ofSeconds(10), By.className(excelValues[5]));
//            }
            // r-1
            // row select

//            String _xpath = getXpath(excelValues[1],excelValues[2],excelValues[3]); for SIT_AUTOMATION

            String _xpath = excelValues[1];
            logger.info(excelValues[0]);
            testInfo[i] = excelValues[0];
            testStatus[i] = excelValues[2];

            ActionType actionType = ActionType.valueOf(excelValues[2].toUpperCase());

            PerformAction(actionType,_xpath,excelValues[3],excelValues[4],excelValues[5],excelValues[6],excelValues[10],excelValues[0]);

            if(excelValues[7].equals("_true")){
                stringIndex = excelValues[10].replaceAll("[^0-9]", "");
                numericIndex = Integer.parseInt(stringIndex);
                waitForElementToDisappear(Duration.ofSeconds(numericIndex),By.xpath(excelValues[8]));
            }

            if(excelValues[9].equals("wait")){
                try {
                    stringIndex = excelValues[10].replaceAll("[^0-9]", "");
                    numericIndex = Integer.parseInt(stringIndex);
                    numericIndex = numericIndex * 100;
                    Thread.sleep(numericIndex);
                } catch (Exception e) {
                    System.out.println("Element not found within the specified timeout.");
                    throw new RuntimeException(e);
                    }
            }
            tracker.incrementProgress();
        }
    }

    // method for perform
    private void PerformAction(ActionType type,String locator,String inputValue,String indexValue,String selectedIndex,String ElementCount,String waitime,String logMessage){
        try{
            WebElement element;
            List<WebElement> elements;
            switch (type){
                case SEND :
                    element = findElement(By.xpath(locator));
                    element.sendKeys(inputValue);
                    break;
                case CLICK:
                    element = findElement(By.xpath(locator));
                    element.click();
                    captureScreenShot(driver,logMessage);
                    break;
                case SENDS:
                    parsingIndexNumber(indexValue);
                    elements = findElements(By.xpath(locator));
                    elements.get(indexNumber).sendKeys(inputValue);
                    break;
                case CLICKS:
                    parsingIndexNumber(indexValue);
                    elements = findElements(By.xpath(locator));
                    elements.get(indexNumber).click();
                    captureScreenShot(driver,logMessage);
                    break;
                case LASTSENDS:
                    elements = findElements(By.xpath(locator));
                    elements.get(elements.size()-1).sendKeys(inputValue);
                    break;
                case LASTCLICKS:
                    elements = findElements(By.xpath(locator));
                    elements.get(elements.size()-1).click();
                    captureScreenShot(driver,logMessage);
                    break;
                case ACTION:
                    element = findElement(By.xpath(locator));
                    new Actions(driver).contextClick(element).perform();
                    captureScreenShot(driver,logMessage);
                    break;
                case ACTION_KEY_A:
                    element = findElement(By.xpath(locator));
                    element.sendKeys(Keys.CONTROL + "A");
                    break;
                case ACTION_KEY_BACKSPACE:
                    element = findElement(By.xpath(locator));
                    element.sendKeys(Keys.BACK_SPACE);
                    break;
                case COPYTEXT:
                    element = findElement(By.xpath(locator));
                    String copiedTxt = element.getText();
                    copyTextToClipboard(copiedTxt);
                    break;
                case PASTETEXT:
                    pasteTextFromClipboard();
                    break;
                case SENDINT:
                    // Remove non-numeric characters
                    String numericString = inputValue.replaceAll("[^0-9]", "");
                    element = findElement(By.xpath(locator));
                    element.sendKeys(numericString);
                    break;
                case SENDSINT:
                    // Remove non-numeric characters
                    parsingIndexNumber(indexValue);
                    String numericsString = inputValue.replaceAll("[^0-9]", "");
                    elements = findElements(By.xpath(locator));
                    elements.get(indexNumber).sendKeys(numericsString);
                    break;
                case SENDDECI:
                    element = findElement(By.xpath(locator));
                    Pattern pattern = Pattern.compile("\"(\\d+\\.\\d+\\.\\d+)\"");
                    Matcher matcher = pattern.matcher(inputValue);

                    if (matcher.find()) {
                        String matched = matcher.group(1); // Extracting the matched group without quotes
                        element.sendKeys(String.valueOf(matched));
                    } else {
                        System.out.println("No match found");
                    }
                break;
                case ACTION_KEYS_A:
                    parsingIndexNumber(indexValue);
                    stringIndex = selectedIndex.replaceAll("[^0-9]", "");
                    numericIndex = Integer.parseInt(stringIndex);
                    ElementIndex = ElementCount.replaceAll("[^0-9]", "");
                    EleValue = Integer.parseInt(ElementIndex);
                    elements = findElements(By.xpath(locator));
                    elements.get( (indexNumber + ( EleValue * numericIndex))-1).sendKeys(Keys.CONTROL + "A");
                    break;
                case ACTION_KEYS_BACKSPACE:
                    parsingIndexNumber(indexValue);
                    stringIndex = selectedIndex.replaceAll("[^0-9]", "");
                    numericIndex = Integer.parseInt(stringIndex);
                    ElementIndex = ElementCount.replaceAll("[^0-9]", "");
                    EleValue = Integer.parseInt(ElementIndex);
                    elements = findElements(By.xpath(locator));
                    elements.get( (indexNumber + ( EleValue * numericIndex))-1).sendKeys(Keys.BACK_SPACE);
                    break;
                case COPYTEXTS:
                    parsingIndexNumber(indexValue);
                    stringIndex = selectedIndex.replaceAll("[^0-9]", "");
                    numericIndex = Integer.parseInt(stringIndex);
                    ElementIndex = ElementCount.replaceAll("[^0-9]", "");
                    EleValue = Integer.parseInt(ElementIndex);
                    elements = findElements(By.xpath(locator));
                    String copiedTxts = elements.get( (indexNumber + ( EleValue * numericIndex))-1).getText();
                    copyTextToClipboard(copiedTxts);
                    break;
                case ITERATIONSENDSINT:
                    parsingIndexNumber(indexValue);
                    String numString = inputValue.replaceAll("[^0-9]", "");
                    stringIndex = selectedIndex.replaceAll("[^0-9]", "");
                    numericIndex = Integer.parseInt(stringIndex);
                    ElementIndex = ElementCount.replaceAll("[^0-9]", "");
                    EleValue = Integer.parseInt(ElementIndex);
                    elements = findElements(By.xpath(locator));
                    elements.get((indexNumber + ( EleValue * numericIndex))-1).sendKeys(numString);
                break;
                case ITERATIONSENDS :
                    parsingIndexNumber(indexValue);
                    stringIndex = selectedIndex.replaceAll("[^0-9]", "");
                    numericIndex = Integer.parseInt(stringIndex);
                    ElementIndex = ElementCount.replaceAll("[^0-9]", "");
                    EleValue = Integer.parseInt(ElementIndex);
                    elements = findElements(By.xpath(locator));
                    elements.get((indexNumber + ( EleValue * numericIndex))-1).sendKeys(inputValue);
                    break;
                case ITERATIONCLICKS :
                    parsingIndexNumber(indexValue);
                    stringIndex = selectedIndex.replaceAll("[^0-9]", "");
                    numericIndex = Integer.parseInt(stringIndex);
                    ElementIndex = ElementCount.replaceAll("[^0-9]", "");
                    EleValue = Integer.parseInt(ElementIndex);
                    elements = findElements(By.xpath(locator));
                    elements.get((indexNumber + ( EleValue * numericIndex))-1).click();
                    break;
                case SENDDECIS:
                    Pattern pattern1 = Pattern.compile("\"(\\d+\\.\\d+\\.\\d+)\"");
                    Matcher matcher1 = pattern1.matcher(inputValue);
                    parsingIndexNumber(indexValue);
                    if (matcher1.find()) {
                        String matched = matcher1.group(1); // Extracting the matched group without quotes
                        elements = findElements(By.xpath(locator));
                        elements.get(indexNumber).sendKeys(String.valueOf(matched));
                    } else {
                        System.out.println("No match found");
                    }
                    break;
                case COPYSENDS:
                    parsingIndexNumber(indexValue);
                    elements = findElements(By.xpath(locator));
                    elements.get(indexNumber).sendKeys(ClipboardUtil.getClipboardContent());
//                    pasteTextFromClipboard();
//                    ClipboardUtil.pasteFromClipboard(elements.get(indexNumber));
//                    elements.get(indexNumber).click();
                    break;
                case SCROLLTOTOP:
                    // Execute JavaScript to scroll to the top of the page
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
                    break;
                case SCROLLTOBOTTOM:
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                    break;
                case WAIT:
                    try {
                        stringIndex = waitime.replaceAll("[^0-9]", "");
                        numericIndex = Integer.parseInt(stringIndex);
                        WebElement waitingElement = waitForElementByXPath(driver,locator,numericIndex);
                    } catch (Exception e) {
                        System.out.println("Element not found within the specified timeout.");
                        throw new RuntimeException(e);
                    }
                default:
                    System.out.println("Unknown Status: " + type);
                    break;
            }
        }
        catch (Exception e) {
            System.out.println("Error handling action: " + type + ", Message: " + e.getMessage());
        }
    }


    private void parsingIndexNumber(String indexValue){
        StringIndexNumber = indexValue.replaceAll("[^0-9]", "");
        indexNumber = Integer.parseInt(StringIndexNumber);
    }
    private void waitForElement(Duration duration, By locator) {
        new WebDriverWait(driver, duration).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    private void waitForElementToDisappear(Duration duration,By locator){
        new WebDriverWait(driver, duration).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    private WebElement findElement(By locator){
        return driver.findElement(locator);
    }
    private List<WebElement> findElements(By locator){
        return driver.findElements(locator);
    }
    private static void copyTextToClipboard(String text) {
        // Create a StringSelection object with the text
        StringSelection stringSelection = new StringSelection(text);
        // Get the system clipboard
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }
    private static void pasteTextFromClipboard() throws AWTException {
        // Create a Robot instance
        Robot robot = new Robot();
        // Use Robot to press and release CTRL+V
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }
}