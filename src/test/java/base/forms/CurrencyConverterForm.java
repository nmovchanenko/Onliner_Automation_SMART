package base.forms;

import base.CurrencyConverter;
import org.openqa.selenium.By;
import webdriver.BaseForm;
import webdriver.elements.Button;
import webdriver.elements.ComboBox;
import webdriver.elements.TableCell;
import webdriver.elements.TextBox;

public class CurrencyConverterForm extends BaseForm {

    public TextBox txbCurrencyAmount = new TextBox(By.id("amount-in"), "Сумма");
    public Button btnBuy = new Button(By.className("state-2"), "Купить");
    public TableCell tbcConvertingResult = new TableCell(By.className("js-cur-result"), "Стоимость");
    public TableCell tbcBestUSDBuyPrice = new TableCell(By.xpath("//table[@class='b-currency-table__best']//tbody//td[3]//b"), "Лучший курс продажи USD");
    public ComboBox cmbCurrencyIn = new ComboBox(By.id("currency-in"), "Валюта, которая конвертируется");
    public ComboBox cmbCurrencyOut = new ComboBox(By.id("currency-out"), "Валюта, в которую конвертируется");

    public CurrencyConverterForm() {
        super(By.className("b-currency-converter-i"), "Конвертер валют");
    }


    /**
     * Validate USD to BYR converting
     *
     * @param currencyAmount Currency amount to convert
     */
    public void validateCurrencyConverting(String currencyAmount) {

        //storing converting result, which is displayed on page, and delete spaces from it using regExp
        String actualConverting = tbcConvertingResult.getText().replaceAll("[ ]", "");

        //manual calculating of converting result:
        CurrencyConverter currencyConverter = new CurrencyConverter();
        String currencyPrice = tbcBestUSDBuyPrice.getText();
        String expectedConverting = currencyConverter.convertCurrencyToBYR(currencyAmount, currencyPrice);

        logger.info("Expected converting: " + expectedConverting);
        logger.info("Actual converting: " + actualConverting);

        assertEquals("Wrong currency converting!", expectedConverting, actualConverting);
    }

}
