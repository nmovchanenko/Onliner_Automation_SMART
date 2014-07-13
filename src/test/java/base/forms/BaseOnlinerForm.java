package base.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Button;
import webdriver.elements.Link;
import webdriver.elements.TextBox;

public class BaseOnlinerForm extends BaseForm {

    public Link lnkHomeOnliner = new Link(By.xpath("//img[@alt='Onliner']"), "Link to main page");

    // elements of navigation bar
    public Link lnkCatalogAndPrice = new Link(By.xpath("//*[@class='b-main-navigation']/li[1]/a"), "Каталог и цены");
    public Link lnkCurrency = new Link(By.xpath("//*[@class='top-informer-currency']//a"), "Курс валюты");

    // elements of quick search
    public TextBox txbQuickSearch = new TextBox(By.id("g-search-input"), "Введите текст для поиска");
    public Link lnkCatalogTab = new Link(By.linkText("Каталог и цены"), "Закладка 'Каталог и цены'");
    public Button btnSearch = new Button(By.cssSelector(".top-search-button"), "Найти");

    // elements of user profile block
    public Link lnkBookmarks = new Link(By.xpath("//a[contains(text(),'Закладки')]"), "Закладки");
    public Link lnkLogIn = new Link(By.linkText("Войти"), "Войти");
    public Link lnkLogout = new Link(By.className("exit"), "Log out");
    public Link lnkUserLogin = new Link(By.cssSelector(".user-name>a"), "User login link");


    public BaseOnlinerForm() {
        super(By.xpath("//*[@class='g-top-i'] | //*[@class='onliner-top']"), "User profile");
    }


    /**
     * Compares displayed user name with entered name
     * @param userAccount User name that was entered
     */
    public void validateUserAccount(String userAccount) {
        String actualUser = lnkUserLogin.getText(); //Storing user name which is displayed on page

        logger.info("Expected user name: " + userAccount);
        logger.info("Actual user name: " + actualUser);
        assertEquals("Invalid user is displayed!", userAccount, actualUser);
    }
}