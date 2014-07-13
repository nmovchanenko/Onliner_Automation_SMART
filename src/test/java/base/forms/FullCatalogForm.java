package base.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Link;

public class FullCatalogForm extends BaseForm {

    // Links to product lists
    public Link lnkMobilePhones = new Link(By.linkText("Мобильные телефоны"), "Мобильные телефоны");


    public FullCatalogForm() {
        super(By.cssSelector(".textmain.main_page"), "Каталог и цены");
    }
}
