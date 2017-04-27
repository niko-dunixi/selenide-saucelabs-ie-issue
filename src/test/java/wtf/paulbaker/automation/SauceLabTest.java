package wtf.paulbaker.automation;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

/**
 * Created by paul.baker on 4/27/17.
 */
public class SauceLabTest {

    @BeforeClass
    public void initialize() throws IOException {
        Configuration.browserVersion = "latest";
        setCapability("platform", "Windows 10");
        Configuration.baseUrl = "https://www.google.com/";

        String user = System.getProperty("user");
        String key = System.getProperty("key");
        Configuration.remote = String.format("http://%s:%s@ondemand.saucelabs.com:80/wd/hub", user, key);
    }

    private static void setCapability(String capability, String value) {
        System.setProperty("capabilities." + capability, value);
    }

    @DataProvider(name = "searchTerms")
    public Iterator<Object[]> getSearchTerms() {
        List<Object[]> objects = new ArrayList<>();
        Set<String> browsers = new TreeSet<>();
        browsers.add(WebDriverRunner.CHROME);
        browsers.add(WebDriverRunner.INTERNET_EXPLORER);
        browsers.add(WebDriverRunner.EDGE);
        browsers.add(WebDriverRunner.FIREFOX);
        for (String browser : browsers) {
            objects.add(new Object[]{browser, "Asimov"});
            objects.add(new Object[]{browser, "Brandon Sanderson"});
        }
        return objects.iterator();
    }

    @Test(dataProvider = "searchTerms")
    public void testSearch(String browser, String searchTerm) {
        Configuration.browser = browser;
        Selenide.open("search?q=" + searchTerm);
        Selenide.sleep(5 * 1000); // Hold long enough to see the instance open in the browser
        Selenide.close();
    }
}
