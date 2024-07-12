package api.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class readConfig {

    private Properties properties;
    private final String path = "C:\\Users\\Shaik2.Amjad\\Desktop\\EnhancingSkills\\SIT_Automation\\Configuration\\Config.properties";

    public readConfig() {
        properties = new Properties();
        try (FileInputStream IStream = new FileInputStream(path)) {
            properties.load(IStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Properties configuration is not setup");
        }
    }

    public String getURL() {
        String URL = properties.getProperty("SIT_URL");
        if (URL != null) {
            return URL;
        } else {
            throw new RuntimeException("baseURL not specified in the config file.");
        }
    }

    public String getBrowser() {
        String value = properties.getProperty("browser");
        if (value != null) {
            return value;
        } else {
            throw new RuntimeException("browser not specified in the config file.");
        }
    }

    public String getTestName() {
        String value = properties.getProperty("Test_Name");
        if (value != null) {
            return value;
        } else {
            throw new RuntimeException("TestName is not specified in the config file.");
        }
    }
}
