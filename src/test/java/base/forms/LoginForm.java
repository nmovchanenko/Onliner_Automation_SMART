package base.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Button;
import webdriver.elements.TextBox;

public class LoginForm extends BaseForm {

    public TextBox txbUserName = new TextBox(By.id("username"), "User name");
    public TextBox txbPassword = new TextBox(By.id("password"), "Password");
    public Button btnLogin = new Button(By.xpath("//input[@alt='Войти и быть как дома']"), "Login button");

    public LoginForm() {
        super(By.xpath("//*[@id='minWidth']/h1"), "Здравствуйте");
    }
}
