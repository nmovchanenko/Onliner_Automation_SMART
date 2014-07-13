package test;

import base.forms.BaseOnlinerForm;
import base.BaseOnlinerFunctions;
import webdriver.BaseTest;

import static org.junit.Assert.assertFalse;

public class T10_Logout extends BaseTest {

    @Override
    public void runTest() {

        BaseOnlinerFunctions.logInToSystem();

        logger.logStep(1, "Click logout link");
        BaseOnlinerForm baseOnlinerForm = new BaseOnlinerForm();
        baseOnlinerForm.lnkLogout.clickAndWait();

        logger.logStep(2, "Validate logout was successful");
        assertFalse("User is logged in!", baseOnlinerForm.lnkUserLogin.isPresent());
    }

    @Override
    public boolean shouldAnalys() {
        return false;
    }

    @Override
    public void invokeAnalys(Throwable exc, String bodyText) throws Throwable {

    }
}