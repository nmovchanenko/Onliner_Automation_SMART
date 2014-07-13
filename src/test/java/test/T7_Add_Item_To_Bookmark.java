package test;

import base.BaseOnlinerFunctions;
import base.forms.*;
import webdriver.BaseTest;

public class T7_Add_Item_To_Bookmark extends BaseTest {

    @Override
    public void runTest() {

        BaseOnlinerFunctions.logInToSystem();

        logger.logStep(1, "Perform quick search to open list of found items");
        BaseOnlinerFunctions.performQuickSearchInCatalog(BaseOnlinerFunctions.PRODUCT_IPOD);

        logger.logStep(2, "Open an item, which is not added to bookmarks yet");
        ProductListForm productListForm = new ProductListForm();
        productListForm.openNotAddedToBookmarkItem();

        logger.logStep(3, "Click 'Add to bookmark' link");
        ProductDetailsForm productDetailsForm = new ProductDetailsForm();
        productDetailsForm.lnkAddToBookmark.click();

        logger.logStep(4, "Open bookmarks");
        BaseOnlinerForm baseOnlinerForm = new BaseOnlinerForm();
        baseOnlinerForm.lnkBookmarks.clickAndWait();

        logger.logStep(5, "Open catalog bookmarks: click 'Каталог' tab");
        BookmarksForm bookmarksForm = new BookmarksForm();
        bookmarksForm.lnkCatalogBookmarks.click();

        logger.logStep(6, "Check that item was added to bookmarks");
        bookmarksForm.validateItemWasAddedToBookmarks(productListForm.getProductName());
    }

    @Override
    public boolean shouldAnalys() {
        return false;
    }

    @Override
    public void invokeAnalys(Throwable exc, String bodyText) throws Throwable {

    }
}
