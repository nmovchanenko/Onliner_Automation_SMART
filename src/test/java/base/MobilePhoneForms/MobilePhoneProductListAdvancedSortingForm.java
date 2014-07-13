package base.MobilePhoneForms;

import base.forms.ProductListAdvancedSortingForm;
import org.openqa.selenium.By;
import webdriver.elements.ComboBox;

public class MobilePhoneProductListAdvancedSortingForm extends ProductListAdvancedSortingForm {


    public ComboBox cmbDisplayDiagonalFrom = new ComboBox(By.id("lmob_displaydiag"), "Размер экрана От");
    public ComboBox cmbDisplayDiagonalTo = new ComboBox(By.id("lmob_displaydiag_2"), "Размер экрана До");
    public ComboBox cmbType = new ComboBox(By.id("lmobile_type0"), "Тип");
    public ComboBox cmbSensorScreen = new ComboBox(By.id("lsens_screen"), "Сенсорный экран");


    public ComboBox cmbOperatingSystem = new ComboBox(By.id("lsmart0"), "Operating system");
}
