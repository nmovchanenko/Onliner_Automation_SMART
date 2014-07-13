package test;

import base.forms.BaseOnlinerForm;
import base.BaseOnlinerFunctions;
import base.forms.BookmarksForm;
import base.forms.ProductDetailsForm;
import webdriver.BaseTest;

public class T8_Delete_Item_From_Bookmark extends BaseTest {

    @Override
    public void runTest() {

        logger.info("To perform the test, at least one item should be added to bookmarks");
        BaseOnlinerFunctions.logInToSystem();
        BaseOnlinerFunctions.addItemToBookmark(BaseOnlinerFunctions.PRODUCT_IPOD);
        BaseOnlinerFunctions.logout();

        logger.logStep(1, "Login to system");
        BaseOnlinerFunctions.logInToSystem();

        logger.logStep(2, "Open bookmarks");
        BaseOnlinerForm baseOnlinerForm = new BaseOnlinerForm();
        baseOnlinerForm.lnkBookmarks.clickAndWait();

        logger.logStep(3, "Open the list of items from catalog");
        BookmarksForm bookmarksForm = new BookmarksForm();
        bookmarksForm.lnkCatalogBookmarks.click();

        logger.logStep(4, "Open any item detail page");
        String firstBookmarksItem = bookmarksForm.tbcFirstAddedItemToBookmark.getText();
        bookmarksForm.tbcFirstAddedItemToBookmark.clickAndWait();

        logger.logStep(5, "Click 'Удалить из закладок'");
        ProductDetailsForm productDetailsForm = new ProductDetailsForm();
        productDetailsForm.lnkDeleteFromBookmark.click();

        logger.logStep(6, "Open bookmarks");
        baseOnlinerForm.lnkBookmarks.clickAndWait();

        logger.logStep(7, "Check that item was deleted from bookmarks");
        bookmarksForm.lnkCatalogBookmarks.click();
        bookmarksForm.validateItemWasDeletedFromBookmarks(firstBookmarksItem);
    }

    @Override
    public boolean shouldAnalys() {
        return false;
    }

    @Override
    public void invokeAnalys(Throwable exc, String bodyText) throws Throwable {

    }
}
