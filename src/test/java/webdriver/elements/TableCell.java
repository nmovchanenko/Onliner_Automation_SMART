package webdriver.elements;

import org.openqa.selenium.By;

public class TableCell extends BaseElement {

    public TableCell(final By locator, final String name) {
        super(locator, name);
    }

    @Override
    protected String getElementType() {
        return "tablecell";
    }
}
