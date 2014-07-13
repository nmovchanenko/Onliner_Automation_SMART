package test;

import base.BaseOnlinerFunctions;
import base.forms.CommentsForm;
import base.forms.MainPageForm;
import webdriver.BaseTest;

public class T3_Available_To_Comment extends BaseTest {

    @Override
    public void runTest() {

        BaseOnlinerFunctions.logInToSystem();

        logger.logStep(1, "Open any available article");
        MainPageForm mainPageForm = new MainPageForm();
        mainPageForm.lnkFirstAvailableArticle.clickAndWait();

        logger.logStep(2, "Check that commenting is available when user logged in: comments block should be displayed");
        CommentsForm commentsForm = new CommentsForm();
        commentsForm.assertIsOpen();
    }

    @Override
    public boolean shouldAnalys() {
        return false;
    }

    @Override
    public void invokeAnalys(Throwable exc, String bodyText) throws Throwable {

    }
}
