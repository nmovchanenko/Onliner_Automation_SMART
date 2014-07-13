package base.MobilePhoneForms;

import base.forms.ProductListForm;
import org.openqa.selenium.By;
import webdriver.elements.TableCell;

public class MobilePhoneListForm extends ProductListForm {

    public TableCell tbcOStype = new TableCell(By.xpath("//*[@class='pdtable']//tr[7]//td[2]"), "Операционная система");


}
