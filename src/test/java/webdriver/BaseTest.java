package webdriver;

import org.testng.annotations.Test;

/**
 * An abstract class that describes the basic test application contains
 * methods for logging and field test settings (options)
 */
public abstract class BaseTest extends BaseEntity implements IAnalys {

	/**
	 * To override.
	 */
	public abstract void runTest();

	/**
	 * Test
	 * @throws Throwable Throwable
	 */
	@Test
	public void xTest() throws Throwable {
		Class<? extends BaseTest> currentClass = this.getClass();
		checkJiraIssue(currentClass);
		checkJiraIssue(currentClass.getMethod("runTest", null));
		try {
			logger.logTestName(currentClass.getName());
			runTest();
			logger.logTestEnd(currentClass.getName());
		} catch (Throwable e) {
			if (shouldAnalys()) {
				invokeAnalys(e, browser.getDriver().getPageSource());
			} else {
				logger.warn("");
				logger.warnRed(getLoc("loc.test.failed"));
				logger.warn("");
				throw e;
			}
		} finally {
			if (Browser.getAnalyzeTraffic()){
				browser.assertAnalyzedTrafficResponseCode();
				browser.assertAnalyzedTrafficLoadTime();
				ProxyServ.saveToFile(this.getClass().getName());
			}
			makeScreen(currentClass);
		}
	}

	/**
	 * Format logging
	 * @param message Message
	 * @return Message
	 */
	protected String formatLogMsg(final String message) {
		return message;
	}

}
