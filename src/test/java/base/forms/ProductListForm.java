package base.forms;

import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.Browser;
import webdriver.elements.Link;

public class ProductListForm extends BaseForm {

    private static final int MAX_ITEM_ROWS_AMOUNT_ON_PAGE = 30;
    private static final int SECOND_ITEM_ROW = 4;
    private static final int STEP_TO_NEXT_ITEM_ROW = 2;
    private static String productName;

    public Link lnkFirstFoundProduct = new Link(By.cssSelector(".pname>a"), "Item link");

    public ProductListForm() {
        super(By.id("unline"), "Каталог выбранной продукции");
    }


    /**
     * Open an item which is not added to bookmarks
     */
    public void openNotAddedToBookmarkItem() {
        ProductListForm productListForm = new ProductListForm();
        productName = productListForm.lnkFirstFoundProduct.getText();
        productListForm.lnkFirstFoundProduct.clickAndWait();

        if (isAddedToBookmarks()) {
            openNextItem();
        }
    }

    /**
     * Find an item which is not added to bookmarks. When such item is found, stores it's name to 'productName' variable
     */
    private void openNextItem() {
        for (int i = SECOND_ITEM_ROW; isAddedToBookmarks(); i += STEP_TO_NEXT_ITEM_ROW) {
            Browser.getDriver().navigate().back();
            Link lnkNextItem = new Link(By.xpath("//tr[" + i + "]/td[3]/strong/a"), "Next found item: №" + i);
            productName = lnkNextItem.getText();
            lnkNextItem.clickAndWait();
        }
    }

    /**
     * Check if current item is added to bookmarks
     *
     * @return Boolean
     */
    private boolean isAddedToBookmarks() {
        ProductDetailsForm productDetailsForm = new ProductDetailsForm();
        if (productDetailsForm.lnkDeleteFromBookmark.isPresent()) {
            return true;
        }
        return false;
    }

    public String getProductName() {
        return productName;
    }
}
