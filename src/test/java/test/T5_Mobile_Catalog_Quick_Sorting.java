package test;

import base.BaseOnlinerFunctions;
import base.MobilePhoneForms.MobilePhoneDetailsForm;
import base.forms.*;
import webdriver.BaseTest;

public class T5_Mobile_Catalog_Quick_Sorting extends BaseTest {

    @Override
    public void runTest() {

        logger.logStep(1, "Open catalog list: click 'Каталог и цены' tab");
        BaseOnlinerForm baseOnlinerForm = new BaseOnlinerForm();
        baseOnlinerForm.lnkCatalogAndPrice.clickAndWait();

        logger.logStep(2, "Open mobile phones list");
        FullCatalogForm fullCatalogForm = new FullCatalogForm();
        fullCatalogForm.lnkMobilePhones.clickAndWait();

        logger.logStep(3, "Perform filter by manufacturer 'Apple'");
        ProductListManufacturerSortForm manufacturerSortForm = new ProductListManufacturerSortForm();
        manufacturerSortForm.lnkSortByApple.clickAndWait();

        logger.logStep(4, "Open any found item");
        ProductListForm productListForm = new ProductListForm();
        productListForm.lnkFirstFoundProduct.clickAndWait();

        logger.logStep(5, "Validate that found item satisfy performed filter");
        MobilePhoneDetailsForm mobilePhoneDetailsForm = new MobilePhoneDetailsForm(BaseOnlinerFunctions.MANUFACTURER_APPLE);
        mobilePhoneDetailsForm.validateProductName(BaseOnlinerFunctions.MANUFACTURER_APPLE);
    }

    @Override
    public boolean shouldAnalys() {
        return false;
    }

    @Override
    public void invokeAnalys(Throwable exc, String bodyText) throws Throwable {

    }
}
