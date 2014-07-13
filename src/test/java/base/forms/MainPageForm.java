package base.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Link;

public class MainPageForm extends BaseForm {

    public Link lnkFirstAvailableArticle = new Link(By.xpath("//div[@id='widget-11']/a[1]"), "Main news");


    public MainPageForm() {
        super(By.className("b-cat-line"), "Home Page");
    }
}