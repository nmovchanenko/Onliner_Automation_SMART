package webdriver;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import webdriver.Browser.Browsers;
import webdriver.annotations.Jira;
import webdriver.utils.ExcelUtils;
import webdriver.utils.HttpUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import static org.testng.AssertJUnit.assertTrue;

/**
 * BaseEntity
 */
public abstract class BaseEntity {

	protected static int stepNumber = 1;
	protected static Logger logger = Logger.getInstance();
	protected static Browser browser = Browser.getInstance();
	protected static boolean isLogged = false;
	protected static int screenIndex = 0;
	protected ITestContext context;
    static final String PROPERTY_KEY_PREFIX = "org.uncommons.reportng.";
    static final String GLOBAL_LOGSTEP_PART = PROPERTY_KEY_PREFIX + "global-logstep-part";

	/**
	 * Get locale
	 * 
	 * @param key
	 *            key
	 * @return value
	 */
	protected static String getLoc(final String key) {
		return Logger.getLoc(key);
	}

	// ==============================================================================================
	// Methods for logging

	/**
	 * Format message.
	 * 
	 * @param message
	 *            message
	 * @return null
	 */
	protected abstract String formatLogMsg(String message);

	/**
	 * Message for debugging.
	 * 
	 * @param message
	 *            Message
	 */
	protected final void debug(final String message) {
		logger.debug(String.format("[%1$s] %2$s", this.getClass().getSimpleName(), formatLogMsg(message)));
	}

	/**
	 * Informative message.
	 * 
	 * @param message
	 *            Message
	 */
	protected void info(final String message) {
		logger.info(formatLogMsg(message));
	}

	/**
	 * Warning.
	 * 
	 * @param message
	 *            Message
	 */
	protected void warn(final String message) {
		logger.warn(formatLogMsg(message));
	}

	/**
	 * Error message without stopping the test.
	 * 
	 * @param message
	 *            Message
	 */
	protected void error(final String message) {
		logger.error(formatLogMsg(message));
	}

	/**
	 * Fatal error message.
	 * 
	 * @param message
	 *            Message
	 */
	protected void fatal(final String message) {
		logger.fatal(formatLogMsg(message));
		assertTrue(formatLogMsg(message), false);
	}

	/**
	 * Logging a step number.
	 * 
	 * @param step
	 *            - step number
	 */
	public static void logStep(final int step) {
		logger.step(step);
	}

	/**
	 * Logging a several steps in a one action
	 * 
	 * @param fromStep
	 *            - the first step number to be logged
	 * @param toStep
	 *            - the last step number to be logged
	 */
	public void logStep(final int fromStep, final int toStep) {
		logger.step(fromStep, toStep);
	}

	// ==============================================================================================
	// Asserts

	/**
	 * Universal method
	 * 
	 * @param isTrue
	 *            Condition
	 * @param passMsg
	 *            Positive message
	 * @param failMsg
	 *            Negative message
	 */
	public void doAssert(final Boolean isTrue, final String passMsg,
			final String failMsg) {
		if (isTrue) {
			info(passMsg);
		} else {
			fatal(failMsg);
		}
	}

	/**
	 * Assert Objects are Equal
	 * 
	 * @param expected
	 *            Expected Value
	 * @param actual
	 *            Actual Value
	 */
	public void assertEquals(final Object expected, final Object actual) {
		if (!expected.equals(actual)) {
			fatal("Expected value: '" + expected + "', but was: '" + actual
					+ "'");
		}
	}

	/**
	 * Assert Objects are Equal, doesn't fail the test
	 * 
	 * @param passMessage
	 *            Pass Message
	 * @param failMessage
	 *            Fail Message
	 * @param expected
	 *            Expected Value
	 * @param actual
	 *            Actual Value
	 */
	public void assertEqualsNoFail(final String passMessage,
			final String failMessage, final Object expected, final Object actual) {
		if (expected.equals(actual)) {
			info(passMessage);
		} else {
			error(failMessage);
			error("Expected value: '" + expected + "', but was: '" + actual
					+ "'");
		}
	}

	/**
	 * Assert Objects are Equal
	 * 
	 * @param message
	 *            Fail Message
	 * @param expected
	 *            Expected Value
	 * @param actual
	 *            Actual Value
	 */
	public void assertEquals(final String message, final Object expected,
			final Object actual) {
		if (!expected.equals(actual)) {
			fatal(message);
		}
	}

	/**
	 * Assert current page's URL.
	 * 
	 * @param url
	 *            expected URL
	 * */
	public void assertCurrentURL(String url) {
		String actualUrl = browser.getLocation();
		assertEquals(url, actualUrl);
		info("Page has corret URL.");
	}

	// ==============================================================================================
	//
	/**
	 * screenshot.
	 * 
	 * @param name
	 *            Name of class
	 * @return String path to screen
	 */
	protected String makeScreen(final Class<? extends BaseEntity> name) {
		return makeScreen(name,true);
	}

	
	// ==============================================================================================
	//
	/**
	 * screenshot.
	 * 
	 * @param name
	 *            Name of class
	 * @param additionalInfo
	 *            additionalInfo
	 * @return String path to screen
	 */
	protected String makeScreen(final Class<? extends BaseEntity> name, final boolean additionalInfo) {
		String fileName;
		if (context==null) {
			fileName = name.getPackage().getName() + "." + name.getSimpleName();
		} else {
			fileName = name.getPackage().getName() + "." + name.getSimpleName()+context.getName();
		}
		try {
			String pageSource = browser.getDriver().getPageSource();
			FileUtils.writeStringToFile(new File(String.format("surefire-reports\\html\\Screenshots/%1$s.txt", fileName)),pageSource);
		} catch (Exception e1) {
			warn("Failed to save page source");
		}
		try {
			File screen = ((TakesScreenshot) browser.getDriver()).getScreenshotAs(OutputType.FILE);
			File addedNewFile = new File(String.format("surefire-reports\\html\\Screenshots/%1$s.png", fileName));
			FileUtils.copyFile(screen, addedNewFile);
		} catch (Exception e) {
			warn("Failed to save screenshot");
		}
		if (additionalInfo) {
			String formattedName = String.format(
					"<a href='Screenshots/%1$s.png'>ScreenShot</a>", fileName);
			String formattedNamePageSource = String.format(
					"<a href='Screenshots/%1$s.txt'>Page Source</a>", fileName);
			logger.info(formattedName);
			logger.info(formattedNamePageSource);
			logger.printDots(formattedName.length());
		}
		return new File(String.format(
				"surefire-reports\\html\\Screenshots/%1$s.png", fileName))
				.getAbsolutePath();
	}

	/**
	 * killing process by Image name
	 */
	public void checkAndKill() {
		logger.info("killing processes");
		try {
			String line;
			Process p = Runtime.getRuntime().exec(
					String.format("taskkill /IM %1$s.exe /F",
							Browser.currentBrowser.toString()));
			BufferedReader input = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			while (((line = input.readLine()) != null)) {
				logger.info(line);
			}
			input.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	/**
	 * Only for IE
	 */
	public void acceptUnsignedSertificate() {
		if (Browser.currentBrowser == Browsers.IEXPLORE) {
			browser.navigate("javascript:document.getElementById('overridelink').click()");
		}
	}

	/**
	 * Before Class method
	 */
	@BeforeClass
	public void before(ITestContext context) {
		this.context = context;
		browser = Browser.getInstance();
		browser.windowMaximise();
		browser.navigate(browser.getStartBrowserURL());
		stepNumber = 1;
		if (Browser.getAnalyzeTraffic()) {
			browser.startAnalyzeTraffic();
		}
	}

	/**
	 * Close browser after each test Class
	 */
	@AfterClass
	public void after() {
		ProxyServ.stopProxyServer();
		if (browser.isBrowserAlive()) {
			browser.exit();
			checkAndKill();
		}
	}

	/**
	 * Logging steps
	 */
	protected void LogStep() {
		logStep(stepNumber++);
	}

    /**
     * Makes LogStep collapsing in html.
     * Log message comes from \testcases\*.xlsx file.
     * Excel file must be *.xlsx, first(A) column used for row numbers,
     * second(B) used for test-cases messages.
     * If file for test
     * @param step step number
     */
    public void LogHeaderStep(int step){
        Class<?> currentClass = this.getClass();

        File excelLogFile = new File(System.getProperty("basedir")+"\\src\\test\\resources\\testcases\\"+currentClass.getName()+".xlsx");

        if (!excelLogFile.exists()) {
            ExcelUtils newFile = new ExcelUtils(ExcelUtils.FileFormat.NEW);
            newFile.addSheet("Test");
            newFile.addCell("Test", "B12", "Hello from SMART");
            newFile.save(System.getProperty("basedir")+"\\src\\test\\resources\\testcases\\"+currentClass.getName()+".xlsx");
            logger.info(System.getProperty("basedir")+"\\src\\test\\resources\\testcases\\"+currentClass.getName()+".xlsx"+
                                            " Created from default template");
        }

        ExcelUtils eu = new ExcelUtils(excelLogFile.getAbsolutePath());
        String stepMessage = eu.getCellValue(0,"B"+step);
        logger.info(String.format("%1$s %2$s: %3$s",(System.getProperty(GLOBAL_LOGSTEP_PART)), step, stepMessage));
        logger.info(stepMessage);
    }

	/**
	 * Logging steps with info
	 */
	protected void LogStep(final String info) {
		logStep(stepNumber++);
		logger.info(String.format("----==[ %1$s ]==----", info));
	}

	/**
	 * Check if assigned jira issue is already fixed
	 * 
	 * @param clazz
	 */
	protected void checkJiraIssue(AnnotatedElement clazz) {
		Annotation[] annotations = clazz.getDeclaredAnnotations();
		if (annotations.length>0) {
			analyzeJiraAnnotations(clazz, annotations);
		}
	}

    /**
     * Analize Jira annotations
     * @param type type
     * @param annotations annotations
     */
	private void analyzeJiraAnnotations(AnnotatedElement type, Annotation[] annotations) {
		String url = null,issue = null, login = null;
		for (Annotation annotation : annotations) {
			try {
				Annotation classInfo = type.getAnnotation(Jira.class);
				if (classInfo==null) {
					continue;
				}
				Method valueMethod = classInfo.getClass().getMethod("value",null);
				Method urlMethod = classInfo.getClass().getMethod("url",null);
				Method issueMethod = classInfo.getClass().getMethod("issue",null);
				Method loginMethod = classInfo.getClass().getMethod("login",null);
				Method passwordMethod = classInfo.getClass().getMethod("password",null);
				String value = (String) valueMethod.invoke(classInfo);
				url = (String) urlMethod.invoke(classInfo);
				issue = (String) issueMethod.invoke(classInfo);
				login = (String) loginMethod.invoke(classInfo);
				String password = (String) passwordMethod.invoke(classInfo);
				if (url.isEmpty()) {
					url = Browser.getJiraUrl();
				}
				if (url.isEmpty()) {
					warn("Jira url is empty");
					return;
				}
				if (issue.isEmpty()) {
					issue = value;
				}
				if (login.isEmpty()) {
					login = Browser.getJiraLogin();
				}
				if (password.isEmpty()) {
					password = Browser.getJiraPassword();
				}
				if (login.isEmpty()||password.isEmpty()) {
					warn("Jira login or password is not defined");
				}
				HttpUtils http = new HttpUtils();
				http.executeGetRequest(String.format("%1$srest/gadget/latest/login?os_captcha=&os_password=%3$s&os_username=%2$s",url,login,password),true);
				http.executeGetRequest(String.format("%1$srest/api/latest/issue/%2$s",url,issue),true);
				String json = http.getResponse();
				JSONObject jsonObj = new JSONObject(json);
				String status = (String) jsonObj.getJSONObject("fields").getJSONObject("status").get("name");
				if (!(status.equalsIgnoreCase("resolved")||status.equalsIgnoreCase("closed"))){
					throw new SkipException(String.format("Jira issue %1$s has status %2$s",issue,status));
				}
				Class params[] = new Class[1];
				params[0] = String.class;
			} catch (SkipException e) {
				warn(e.getMessage());
				throw e;
			}
			catch (Exception e) {
				warn("Jira checking failed : " + e.getMessage());
				warn("URL: " + String.format("%1$srest/api/latest/issue/%2$s",url,issue));
				warn("Login: " + login);
			}

		}
	}

}
