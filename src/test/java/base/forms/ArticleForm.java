package base.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.TableCell;

public class ArticleForm extends BaseForm {

    public TableCell tbcNeedToRegisterForComment = new TableCell(By.xpath("//*[@class='b-comments-register-hint']"), "Чтобы оставить свое мнение, необходимо войти или зарегистрироваться");

    public ArticleForm() {
        super(By.className("b-content-grid-1"), "Article Form");
    }
}
