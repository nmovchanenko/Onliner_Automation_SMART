package base.MobilePhoneForms;

import base.forms.ProductDetailsForm;
import org.openqa.selenium.By;

public class MobilePhoneDetailsForm extends ProductDetailsForm {

    public MobilePhoneDetailsForm(String manufactor) {
        super(By.linkText(manufactor), "Mobile phone details page");
    }
}
