package base.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Button;
import webdriver.elements.ComboBox;
import webdriver.elements.TextBox;

public class ProductListAdvancedSortingForm extends BaseForm {

    public ComboBox cmbManufactor = new ComboBox(By.id("lmfr0"), "Производитель");
    public TextBox txbPriceFrom = new TextBox(By.id("smth2"), "Цена от");
    public TextBox txbPriceTo = new TextBox(By.id("smth3"), "Цена до");
    public TextBox txbBirthday = new TextBox(By.id("lbirthday"), "Дата выхода на рынок не ранее");

    public Button btnSearch = new Button(By.name("search"), "Подобрать");
    public Button btnClearForm = new Button(By.xpath("//a[contains(text(),'Очистить форму')]"), "Очистить форму");

    public ProductListAdvancedSortingForm() {
        super(By.name("filter"), "Подбор по параметрам");
    }
}
