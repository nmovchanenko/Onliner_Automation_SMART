package base.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Link;
import webdriver.elements.TableCell;

import static org.junit.Assert.assertTrue;

public class ProductDetailsForm extends BaseForm {

    public Link lnkAddToBookmark = new Link(By.id("bookmark_link"), "Add to Bookmarks");
    public Link lnkDeleteFromBookmark = new Link(By.xpath("//a[contains(text(),'Удалить из закладок')]"), "Удалить из закладок");
    public TableCell tbcProductName = new TableCell(By.xpath("//*[@class='product_h1']//span"), "Полное название продукта");

    public ProductDetailsForm() {
        super(By.className("pdtable"), "Информация о продукте");
    }

    protected ProductDetailsForm(By locator, String title) {
        super(locator, title);
    }

    /**
     * Validate header product name
     *
     * @param itemName Product name
     */
    public void validateProductName(String itemName) {
        logger.info("Expected product name should contain: " + itemName);
        logger.info("Actual product name: " + tbcProductName.getText());

        assertTrue("Found item doesn't match to search query", tbcProductName.getText().contains(itemName));
    }
}
