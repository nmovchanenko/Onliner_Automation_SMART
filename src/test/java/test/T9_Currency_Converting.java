package test;

import base.forms.BaseOnlinerForm;
import base.forms.CurrencyConverterForm;
import webdriver.BaseTest;

public class T9_Currency_Converting extends BaseTest {

    private static final String CURRENCY_AMOUNT = "1000";

    @Override
    public void runTest() {

        logger.logStep(1, "Navigate to currency converter: click on currency link");
        BaseOnlinerForm baseOnlinerForm = new BaseOnlinerForm();
        baseOnlinerForm.lnkCurrency.clickAndWait();

        logger.logStep(2, "Click 'Купить' button");
        CurrencyConverterForm currencyConverterForm = new CurrencyConverterForm();
        currencyConverterForm.btnBuy.click();

        logger.logStep(3, "Enter currency amount");
        currencyConverterForm.txbCurrencyAmount.setText(CURRENCY_AMOUNT);

        logger.logStep(4, "Check converting");
        currencyConverterForm.validateCurrencyConverting(CURRENCY_AMOUNT);
    }


    @Override
    public boolean shouldAnalys() {
        return false;
    }

    @Override
    public void invokeAnalys(Throwable exc, String bodyText) throws Throwable {

    }
}
