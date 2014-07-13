package base.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Link;
import webdriver.elements.TableCell;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BookmarksForm extends BaseForm {

    public Link lnkCatalogBookmarks = new Link(By.xpath("//*[@class='gray-btn-segmented']//li[5]/a"), "'Каталог' tab");
    public TableCell tbcFirstAddedItemToBookmark = new TableCell(By.xpath("//table[@id='bookmarksList']//tr//td[3]/strong/a"), "First Added Item To Bookmark");

    public BookmarksForm() {
        super(By.className("b-hdtopic"), "Bookmarks");
    }

    /**
     * Check that item is displayed in bookmarks
     * @param expectedProductName Product name which should be displayed
     */
    public void validateItemWasAddedToBookmarks(String expectedProductName) {
        TableCell tbcAddedToBookmark = new TableCell(By.xpath("//a[contains(text(),'" + expectedProductName + "')]"), "Added item");

        logger.info("Expected product name: " + expectedProductName);
        logger.info("Actual product name: " + tbcAddedToBookmark.getText());
        assertTrue("Item is not added!", tbcAddedToBookmark.isPresent());
    }

    /**
     * Check that item is NOT displayed in bookmarks
     *
     * @param deletedProductName Product name which should not be displayed
     */
    public void validateItemWasDeletedFromBookmarks(String deletedProductName) {
        TableCell tbcDeletedFromBookmark = new TableCell(By.xpath("//a[contains(text(),'" + deletedProductName + "')]"), "Deleted item");

        logger.info("Deleted item: " + deletedProductName);
        assertFalse("Item is not deleted!", tbcDeletedFromBookmark.isPresent());
    }
}
