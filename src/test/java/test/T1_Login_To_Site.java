package test;

import base.forms.BaseOnlinerForm;
import base.BaseOnlinerFunctions;
import base.forms.LoginForm;
import webdriver.BaseTest;

public class T1_Login_To_Site extends BaseTest {

    @Override
    public void runTest() {

        BaseOnlinerForm baseOnlinerForm = new BaseOnlinerForm();
        baseOnlinerForm.lnkLogIn.clickAndWait();

        logger.logStep(1, "Enter user name");
        LoginForm loginForm = new LoginForm();
        loginForm.txbUserName.type(BaseOnlinerFunctions.USER_NAME);

        logger.logStep(2, "Enter password");
        loginForm.txbPassword.type(BaseOnlinerFunctions.USER_PASSWORD);

        logger.logStep(3, "Submit login");
        loginForm.btnLogin.clickAndWait();

        logger.logStep(4, "Check that login was successful");
        baseOnlinerForm.validateUserAccount(BaseOnlinerFunctions.USER_NAME);
    }

    @Override
    public boolean shouldAnalys() {
        return false;
    }

    @Override
    public void invokeAnalys(Throwable exc, String bodyText) throws Throwable {

    }
}
