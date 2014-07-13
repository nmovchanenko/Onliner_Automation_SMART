package test;

import base.forms.ArticleForm;
import base.forms.MainPageForm;
import webdriver.BaseTest;

public class T2_Unavailable_To_Comment extends BaseTest {

    @Override
    public void runTest() {

        MainPageForm mainPageForm = new MainPageForm();

        logger.logStep(1, "Don't login and open any available article");
        mainPageForm.lnkFirstAvailableArticle.clickAndWait();

        logger.logStep(2, "Check that commenting is not available when user is not logged in");
        ArticleForm articleForm = new ArticleForm();
        articleForm.tbcNeedToRegisterForComment.assertIsPresent();
    }


    @Override
    public boolean shouldAnalys() {
        return false;
    }

    @Override
    public void invokeAnalys(Throwable exc, String bodyText) throws Throwable {

    }
}
