package base.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Link;

public class ProductListManufacturerSortForm extends BaseForm {

    public Link lnkSortByApple = new Link(By.xpath("//a[contains(@href, 'mobile/apple/')]"), "Apple");

    public ProductListManufacturerSortForm() {
        super(By.className("menuleftline"), "Left Menu");
    }
}
