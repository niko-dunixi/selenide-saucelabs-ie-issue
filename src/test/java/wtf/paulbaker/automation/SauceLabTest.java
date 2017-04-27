package wtf.paulbaker.automation;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import wtf.paulbaker.automation.pages.GoogleResultsPage;

import java.io.IOException;
import java.util.*;

import static com.codeborne.selenide.Selenide.open;

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
        GoogleResultsPage googleResultsPage = open("search?q=" + searchTerm, GoogleResultsPage.class);
        googleResultsPage.getResults().forEach(System.out::println);
        Selenide.close();
    }
}
