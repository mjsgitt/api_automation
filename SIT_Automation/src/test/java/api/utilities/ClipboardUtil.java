package api.utilities;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

public class ClipboardUtil {

    public static String getClipboardContent() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String result = "";
        try {
            result = (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void pasteFromClipboard(WebElement element) {
        String clipboardText = getClipboardContent();
        element.sendKeys(clipboardText);
    }
}