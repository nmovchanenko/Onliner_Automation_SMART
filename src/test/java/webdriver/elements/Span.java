package webdriver.elements;

import org.openqa.selenium.By;

public class Span extends BaseElement {

    public Span(final By locator, final String name) {
        super(locator, name);
    }

    public Span(String string, String name) {
        super(string, name);
    }

    protected String getElementType() {
        return "Span";
    }

}
