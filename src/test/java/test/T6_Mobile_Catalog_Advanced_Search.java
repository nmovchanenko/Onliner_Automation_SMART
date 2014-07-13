package test;

import base.BaseOnlinerFunctions;
import base.MobilePhoneForms.MobilePhoneProductListAdvancedSortingForm;
import base.forms.*;
import org.testng.annotations.Test;
import webdriver.BaseTestDataDriven;
import webdriver.DataProviderArguments;

public class T6_Mobile_Catalog_Advanced_Search extends BaseTestDataDriven {

    @Test(dataProvider = "DataProv", dataProviderClass = BaseTestDataDriven.class)
    @DataProviderArguments("filename=OnlinerTestData")
    @Override
    public void runTest(String... args) {
        logger.logTestName("T6_Mobile_Catalog_Advanced_Search");
        String galaxyS5 = args[0];
        String bithday = args[1];
        String operatingSystem = args[2];
        String diaganalFrom = args[3];
        String diaganalTo = args[4];
        String type = args[5];
        String sensorScreen = args[6];


        logger.logStep(1, "Open catalog list: click 'Каталог и цены' tab");
        BaseOnlinerForm baseOnlinerForm = new BaseOnlinerForm();
        baseOnlinerForm.lnkCatalogAndPrice.clickAndWait();

        logger.logStep(2, "Open mobile phones list");
        FullCatalogForm fullCatalogForm = new FullCatalogForm();
        fullCatalogForm.lnkMobilePhones.clickAndWait();

        logger.logStep(3, "Fill out search parameters ");
        MobilePhoneProductListAdvancedSortingForm advancedSortingForm = new MobilePhoneProductListAdvancedSortingForm();
        advancedSortingForm.cmbManufactor.selectByText(BaseOnlinerFunctions.MANUFACTURER_SAMSUNG);
        advancedSortingForm.txbBirthday.type(bithday);
        advancedSortingForm.cmbOperatingSystem.selectByText(operatingSystem);
        advancedSortingForm.cmbDisplayDiagonalFrom.selectByText(diaganalFrom);
        advancedSortingForm.cmbDisplayDiagonalTo.selectByText(diaganalTo);
        advancedSortingForm.cmbType.selectByText(type);
        advancedSortingForm.cmbSensorScreen.selectByText(sensorScreen);

        logger.logStep(4, "Click Search button");
        advancedSortingForm.btnSearch.clickAndWait();

        logger.logStep(5, "Open first found item");
        ProductListForm productListForm = new ProductListForm();
        productListForm.lnkFirstFoundProduct.clickAndWait();

        logger.logStep(6, "Validate that found item satisfy each search criteria");
        ProductInfoTable productInfoTable = new ProductInfoTable();
        productInfoTable.validateDetailsInfo(args);
    }


    @Override
    public boolean shouldAnalys() {
        return false;
    }

    @Override
    public void invokeAnalys(Throwable exc, String bodyText) throws Throwable {

    }
}
