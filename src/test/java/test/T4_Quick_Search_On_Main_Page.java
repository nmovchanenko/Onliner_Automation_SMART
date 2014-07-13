package test;

import base.forms.BaseOnlinerForm;
import base.BaseOnlinerFunctions;
import base.forms.ProductDetailsForm;
import base.forms.ProductListForm;
import webdriver.BaseTest;

import static org.junit.Assert.assertTrue;

public class T4_Quick_Search_On_Main_Page extends BaseTest {

    @Override
    public void runTest() {

        BaseOnlinerForm baseOnlinerForm = new BaseOnlinerForm();

        logger.logStep(1, "Select a place to search: click 'Каталог и цены' tab");
        baseOnlinerForm.lnkCatalogTab.click();

        logger.logStep(2, "Type the name of item");
        baseOnlinerForm.txbQuickSearch.type(BaseOnlinerFunctions.PRODUCT_IPOD);

        logger.logStep(3, "Click 'Найти' button");
        baseOnlinerForm.btnSearch.clickAndWait();

        logger.logStep(4, "Open details page of first found item");
        ProductListForm productListForm = new ProductListForm();
        productListForm.lnkFirstFoundProduct.clickAndWait();

        logger.logStep(5, "Validate product name");
        ProductDetailsForm productDetailsForm = new ProductDetailsForm();
        logger.info("Expected product name should contain: " + BaseOnlinerFunctions.PRODUCT_IPOD);
        logger.info("Actual product name: " + productDetailsForm.tbcProductName.getText());
        assertTrue("Found item doesn't match to search query!", productDetailsForm.tbcProductName.getText().contains(BaseOnlinerFunctions.PRODUCT_IPOD));
    }

    @Override
    public boolean shouldAnalys() {
        return false;
    }

    @Override
    public void invokeAnalys(Throwable exc, String bodyText) throws Throwable {

    }
}
