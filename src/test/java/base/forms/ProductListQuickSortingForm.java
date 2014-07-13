package base.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Link;

public class ProductListQuickSortingForm extends BaseForm {

    public Link lnkSortByWindowsPhone = new Link(By.linkText("Windows Phone"), "Windows Phone");


    public ProductListQuickSortingForm() {
        super(By.xpath("//td[text()='БЫСТРЫЙ ВЫБОР']"), "Quick Sorting Form");

    }
}
