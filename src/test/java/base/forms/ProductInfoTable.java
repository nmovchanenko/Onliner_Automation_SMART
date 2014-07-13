package base.forms;

import org.openqa.selenium.By;
import webdriver.table.Table;

import static org.junit.Assert.assertTrue;

public class ProductInfoTable extends Table {

    public ProductInfoTable() {
        super(By.className("pdtable"), "Detail info");
    }


    /**
     * Check that parameters are displayed on Product Detail table
     * @param params Parameters which should be displayed
     */
    public void validateDetailsInfo(String[] params) {
        ProductInfoTable productInfoTable = new ProductInfoTable();

        for (int i = 0; i < params.length; i++) {
            assertTrue("Parameter was not found!", productInfoTable.getElement().getText().contains(params[i]));

            // Log each found element. If smth is not found, we will know which one
            if (productInfoTable.getElement().getText().contains(params[i])) {
                logger.info("Parameter \'" + params[i] + "\' is displayed");
            } else {
                logger.info("Parameter \'" + params[i] + "\' was not found!");
            }
        }
    }
}
