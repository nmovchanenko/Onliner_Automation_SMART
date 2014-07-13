package base.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;

public class CommentsForm extends BaseForm {

    public CommentsForm() {
        super(By.cssSelector(".b-comments-form"), "Блок для комментария");
    }
}
