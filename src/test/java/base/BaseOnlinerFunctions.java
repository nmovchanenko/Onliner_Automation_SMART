package base;

import base.forms.BaseOnlinerForm;
import base.forms.LoginForm;
import base.forms.ProductDetailsForm;
import base.forms.ProductListForm;
import webdriver.BaseTestParam;
import webdriver.PropertiesResourceManager;

public class BaseOnlinerFunctions extends BaseTestParam {

    // User properties
    static PropertiesResourceManager credentials = new PropertiesResourceManager("credentials.properties");
    public static final String USER_NAME = credentials.getProperty("simpleUserName");
    public static final String USER_PASSWORD = credentials.getProperty("simpleUserPassword");

    // Common product's data
    static PropertiesResourceManager productData = new PropertiesResourceManager("productsData.properties");
    public static final String MANUFACTURER_APPLE = productData.getProperty("manufacturerApple");
    public static final String MANUFACTURER_SAMSUNG = productData.getProperty("manufacturerSamsung");

    public static final String PRODUCT_IPOD = productData.getProperty("productIpod");
    public static final String PRODUCT_GALAXY = productData.getProperty("productGalaxy");

    @Override
    public void runTest() {

    }

    @Override
    public boolean shouldAnalys() {
        return false;
    }

    @Override
    public void invokeAnalys(Throwable exc, String bodyText) throws Throwable {

    }

    /**
     * Login to system
     */
    public static void logInToSystem() {
        logger.info("Open login form:");
        BaseOnlinerForm baseOnlinerForm = new BaseOnlinerForm();
        baseOnlinerForm.lnkLogIn.clickAndWait();

        logger.info("Enter credentials and submit:");
        LoginForm loginForm = new LoginForm();
        loginForm.txbUserName.type(USER_NAME);
        loginForm.txbPassword.type(USER_PASSWORD);
        loginForm.btnLogin.clickAndWait();
    }

    /**
     * Performs quick search from any page
     * @param itemName Item name to search
     */
    public static void performQuickSearchInCatalog(String itemName) {
        logger.info("Select a place to search:");
        BaseOnlinerForm baseOnlinerForm = new BaseOnlinerForm();
        baseOnlinerForm.lnkCatalogTab.click();

        logger.info("Type item name and perform search:");
        baseOnlinerForm.txbQuickSearch.type(itemName);
        baseOnlinerForm.btnSearch.clickAndWait();
    }

    /**
     * Add item to bookmark. Skip adding if selected item is already added to bookmarks
     * @param itemName Name of item
     */
    public static void addItemToBookmark(String itemName) {
        logger.info("Search for item");
        performQuickSearchInCatalog(itemName);

        logger.info("Open item details page");
        ProductListForm productListForm = new ProductListForm();
        if (productListForm.lnkFirstFoundProduct.isPresent()) {
            productListForm.lnkFirstFoundProduct.clickAndWait();
        }

        logger.info("Add item to bookmarks if it's not added yet");
        ProductDetailsForm productDetailsForm = new ProductDetailsForm();
        if (productDetailsForm.lnkDeleteFromBookmark.isPresent()) {
            logger.info("\"" + itemName + "\"" + " is already added to bookmarks");
        } else {
            logger.info("Click Add to bookmark link");
            productDetailsForm.lnkAddToBookmark.click();
        }
    }


    /**
     * Logout from system
     */
    public static void logout() {
        logger.info("Open main page page:");
        BaseOnlinerForm baseOnlinerForm = new BaseOnlinerForm();
        baseOnlinerForm.lnkHomeOnliner.clickAndWait();

        logger.info("Click Logout link:");
        baseOnlinerForm.lnkLogout.clickAndWait();
    }
}
