package wtf.paulbaker.automation;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import wtf.paulbaker.automation.pages.GoogleResultsPage;

import java.io.IOException;
import java.util.*;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

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

    private void setBrowser(String browser) {
        if (INTERNET_EXPLORER.equals(browser)) {
            browser = "internet explorer";
        } else if (EDGE.equals(browser)) {
            browser = "MicrosoftEdge";
        }
        Configuration.browser = browser;
        setCapability("browserName", browser);
    }

    @DataProvider(name = "searchTerms")
    public Iterator<Object[]> getSearchTerms() {
        List<Object[]> objects = new ArrayList<>();
        Set<String> browsers = new TreeSet<>();
        browsers.add(CHROME);
        browsers.add(INTERNET_EXPLORER);
        browsers.add(EDGE);
        browsers.add(FIREFOX);
        for (String browser : browsers) {
            objects.add(new Object[]{browser, "Asimov"});
            objects.add(new Object[]{browser, "Brandon Sanderson"});
        }
        return objects.iterator();
    }

    @Test(dataProvider = "searchTerms")
    public void testSearch(String browser, String searchTerm) {
        setBrowser(browser);
        GoogleResultsPage googleResultsPage = open("search?q=" + searchTerm, GoogleResultsPage.class);
        List<String> results = googleResultsPage.getResults();
        assertNotNull(results, "Page object didn't load elements correctly");
        assertFalse(results.isEmpty(), "Page object didn't proxy correctly and no google results were found");
        results.forEach(System.out::println);
        Selenide.close();
    }
}
