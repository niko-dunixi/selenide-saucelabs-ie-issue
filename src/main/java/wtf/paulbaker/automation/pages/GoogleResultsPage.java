package wtf.paulbaker.automation.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by paul.baker on 4/27/17.
 */
public class GoogleResultsPage {

    @FindAll({@FindBy(css = "#search h3.r a")})
    private ElementsCollection results;

    public List<String> getResults() {
        return results.stream().map(SelenideElement::text).collect(Collectors.toList());
    }
}
