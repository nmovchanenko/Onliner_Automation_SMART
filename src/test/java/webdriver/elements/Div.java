package webdriver.elements;

import org.openqa.selenium.By;

public class Div extends BaseElement {

    public Div(final By locator, final String name) {
        super(locator, name);
    }

    public Div(String string, String name) {
        super(string, name);
    }

    protected String getElementType() {
        return "Div";
    }

}
