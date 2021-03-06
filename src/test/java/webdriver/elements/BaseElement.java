package webdriver.elements;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.api.ScreenRegion;
import org.sikuli.webdriver.SikuliDriver;
import webdriver.BaseEntity;
import webdriver.Browser;
import webdriver.CommonFunctions;
import webdriver.Logger;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Abstract class, describing forms element.
 */
public abstract class BaseElement extends BaseEntity {

	private static final String LINK = "link=";
	private static final String HTTP = "http://";
	private static final String HTTPS = "https://";
	private static final String FILE = "file:///";
	private static final String RESOURCE = "resource:///";
	private static final String ID = "id=";
	private static final String CSS = "css=";
	private static final int TIMEOUT_IS_PRESENT = 5;
	private static final int TIMEOUT_WAIT_IS_NOT_PRESENT = 1;
	private static final int TIMEOUT_WAIT_0 = 0;
	private static final int SHIFT_5 = 5;
	private static final int SHIFT_0 = 0;

	/**
	 * @uml.property name="name"
	 */
	protected String name;
	/**
	 * @uml.property name="locator"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	protected By locator;
	/**
	 * @uml.property name="element"
	 * @uml.associationEnd
	 */
	protected RemoteWebElement element;

	/**
	 * @uml.property name="element"
	 * @uml.associationEnd
	 */
	protected SikuliDriver sikuli;
	protected ScreenRegion sikuliScreenRegion;
	protected URL sikuliLocator;
	protected String similarity = Browser.getSimilarityForSikuli();

	/**
	 * @return
	 * @uml.property name="element"
	 * @return RemoteWebElement
	 */
	public RemoteWebElement getElement() {
		waitForIsElementPresent();
		return element;
	}

	/**
	 * @param elementToSet RemoteWebElement
	 * @uml.property name="element"
	 */
	public void setElement(final RemoteWebElement elementToSet) {
		element = elementToSet;
	}

	/**
	 * Verify that the drop-down element is minimized (performed by a class member)
	 * @return true if collapsed
	 */
	public boolean isCollapsed() {
		waitAndAssertIsPresent();
		String elementClass = element.getAttribute("class");
		return elementClass.toLowerCase().contains("collapse");
	}

	/**
	 * Check that the element is enabled (performed by a class member)
	 * @return true if enabled
	 */
	public boolean isEnabled() {
		waitAndAssertIsPresent();
		String elementClass = element.getAttribute("class");
		return element.isEnabled() && (!elementClass.toLowerCase().contains("disabled"));
	}

	/**
	 * Check that the item is checked (performed by a class member)
	 * @return true if checked
	 */
	public boolean isChecked() {
		waitAndAssertIsPresent();
		String elementClass = element.getAttribute("class");
		return !elementClass.toLowerCase().contains("unchecked");
	}

	/**
	 * The simple constructor, name will be extracted
	 * @param loc By Locator
	 */
	protected BaseElement(final By loc) {
		locator = loc;
	}

	/**
	 * The runTest constructor
	 * @param loc By Locator
	 * @param nameOf Output in logs
	 */
	protected BaseElement(final By loc, final String nameOf) {
		locator = loc;
		name = nameOf;
	}

	/**
	 * Set similarity
	 * @param similarity similarity
	 * @return BaseElement
	 */
	public BaseElement setSimilar(final String similarity) {
		this.similarity = similarity;
		return this;
	}

	/**
	 * Get similarity
	 * @return String
	 */
	public String getSimilarity() {
		return similarity;
	}

	/**
	 * using different locators for different stages DON'T FORGET TO INIT {@link BaseEntity#stageController}
	 * @param locatorStageA First locator
	 * @param locatorStageB Second locator
	 * @param nameOfElement Name
	 */
	protected BaseElement(final By locatorStageA, final By locatorStageB, final String nameOfElement) {
		locator = (Browser.stageController.getStage() == webdriver.stage.Stages.STAGE_A) ? locatorStageA
				: locatorStageB;
		name = nameOfElement + " " + Browser.stageController.getStage();
	}

	/**
	 * easy adapting from Selenium RC locators. CSS, ID, LINK
	 * @param stringLocator String locator
	 * @param nameOfElement Name
	 */
	protected BaseElement(String stringLocator, final String nameOfElement) {
		String clearLoc = null;
		if (stringLocator.contains(CSS)) {
			clearLoc = stringLocator.replaceFirst(CSS, "");
			locator = By.cssSelector(clearLoc);
			name = nameOfElement;
		} else if (stringLocator.contains(ID)) {
			clearLoc = stringLocator.replaceFirst(ID, "");
			locator = By.id(clearLoc);
			name = nameOfElement;
		} else if (stringLocator.contains(LINK)) {
			clearLoc = stringLocator.replaceFirst(LINK, "");
			locator = By.linkText(clearLoc);
			name = nameOfElement;
		} else if (stringLocator.contains(RESOURCE) || stringLocator.contains(FILE) || stringLocator.contains(HTTP)
				|| stringLocator.contains(HTTPS)) {
				sikuliLocator = getSikuliLocator(stringLocator);
			name = nameOfElement;
		} else {
			logger.fatal("UNKNOWN LOCATOR's TYPE. Change to 'By'");
		}
	}

	/**
	 * Get locator for sikuli
	 * @param stringLocator path or url
	 * @return URL
	 */
	public URL getSikuliLocator(String stringLocator) {
		String fullPath = null;
		fullPath = ClassLoader.getSystemResource(stringLocator.replaceAll(RESOURCE, "")).getPath().substring(1);
		if (stringLocator.contains(RESOURCE)) {
			stringLocator = FILE + fullPath;
		}
		try {
			return new URL(stringLocator);
		} catch (MalformedURLException e) {
		}
		return null;
	}

	/**
	 * @return
	 * @uml.property name="locator"
	 * @return Locator
	 */
	public By getLocator() {
		return locator;
	}

	/**
	 * @return
	 * @uml.property name="name"
	 * @return Name of element
	 */
	public String getName() {
		try {
			if (name == null) {
				name = browser.getDriver().findElement(locator).getText();
			}
		} catch (Exception e) {
			name = "unknown";
		}
		return name;
	}

	/**
	 * The implementation of an abstract method for logging of BaseEntity
	 * @param message Message to display in the log
	 * @return Formatted message (containing the name and type of item)
	 */
	protected String formatLogMsg(final String message) {
		return String.format("%1$s '%2$s' %3$s %4$s", getElementType(), name, Logger.LOG_DELIMITER, message);
	}

	/**
	 * The method returns the element type (used for logging)
	 * @uml.property name="elementType"
	 * @return Type of element
	 */
	protected abstract String getElementType();

	/**
	 * Send keys.
	 */
	public void sendKeys(Keys key) {
		waitForIsElementPresent();
		assertIsPresent();
		browser.getDriver().findElement(locator).sendKeys(key);
	}

	/**
	 * Click via Coordinates.
	 */
	public void clickViaCoordinates() {
		waitForIsElementPresent();
		clickViaCoordinates(sikuli.getRectangle().width/2,sikuli.getRectangle().height/2);
	}
	
	/**
	 * Click via Coordinates in specified screen region.
	 */
	public void clickViaCoordinates(ScreenRegion region) {
		isPresentSikuli(TIMEOUT_IS_PRESENT, region);
		clickViaCoordinates(sikuli.getRectangle().width/2,sikuli.getRectangle().height/2);
	}

	
	/**
	 * Click via Coordinates.
	 */
	public void clickIfPresentViaCoordinates(int timeoutMiliSecs) {
		if (isPresentSikuli(timeoutMiliSecs)) {
			clickViaCoordinates(sikuli.getRectangle().width/2,sikuli.getRectangle().height/2);
		}
	}

	/**
	 * Click via Coordinates.
	 */
	public void clickIfPresentViaCoordinates() {
		clickIfPresentViaCoordinates(Integer.parseInt(Browser.getTimeoutForSikuli())/2);
	}
	
	/**
	 * Click via Coordinates.
	 * @param x relatively
	 * @param y relatively
	 */
	public void clickViaCoordinates(final int x, final int y) {
		int shift = SHIFT_0;
		if (Browser.currentBrowser.toString().equalsIgnoreCase("iexplore")) {
			shift = SHIFT_5;
		}
		if (sikuli==null) {
			waitForIsElementPresent();
		}
		Point point = sikuli.getPoint();
		info(getLoc("loc.clicking"));
		browser.getDriver().getMouse().mouseMove(
				((Locatable) browser.getDriver().findElementByTagName("html")).getCoordinates(),
				point.getX() + shift + x, point.getY() + shift + y);
		new Actions(browser.getDriver()).click().perform();
	}

	
	/**
	 * Get Screenshot of found element
	 * @param x
	 * @param y
	 * @return
	 */
	public BufferedImage getScreenShotOfSikuliElement(final int x, final int y) {
		waitForIsElementPresent();
		Point point = sikuli.getPoint();
		info(getLoc("Get screanshot of sikuli element"));
		return sikuliScreenRegion.getRelativeScreenRegion(point.getX(), point.getY(), x, y).capture();
	}
	
	/**
	 * Get Screanshot of found element
	 * @param x
	 * @param y
	 * @return
	 */
	public BufferedImage getScreenShotOfSikuliElement() {
		if (sikuli==null) {
			waitForIsElementPresent();
		}
		return getScreenShotOfSikuliElement(sikuli.getRectangle().width,sikuli.getRectangle().height);
	}
	
	
	/**
	 * Click via Action.
	 */
	public void clickViaAction() {
		waitForIsElementPresent();
		info(getLoc("loc.clicking"));
		Actions action = new Actions(browser.getDriver());
		action.click(getElement());
		action.perform();
	}

	/**
	 * Click via JS.
	 */
	public void clickViaJS() {
		waitForIsElementPresent();
		assertIsPresent();
		info(getLoc("loc.clicking"));
		((JavascriptExecutor) browser.getDriver()).executeScript("arguments[0].click();", getElement());
	}

	/**
	 * Wait for the item and click on it
	 */
	public void waitAndClick() {
		waitForElementClickable();
		info(getLoc("loc.clicking"));
		click();
	}

	/**
	 * ext click through .sendKeys("\n");
	 */
	public void clickExt() {
		info(getLoc("loc.clicking.extended"));
		waitForIsElementPresent();
		if (browser.getDriver() instanceof JavascriptExecutor) {
	        ((JavascriptExecutor)browser.getDriver()).executeScript("arguments[0].style.border='3px solid red'", element);
	    }
		browser.getDriver().findElement(locator).sendKeys("\n");
	}

	/**
	 * Move mouse to this element.
	 */
	public void moveMouseToElement() {
		waitForIsElementPresent();
		CommonFunctions.centerMouse();
		Actions action = new Actions(browser.getDriver());
		action.moveToElement(getElement());
		action.perform();
	}

	/**
	 * Wait for element is present.
	 */
	public void waitForIsElementPresent() {
		if (sikuliLocator != null) {
			isPresent();
			return;
		}
		isPresent(Integer.valueOf(browser.getTimeoutForCondition()));
		// troubleshooting if element is not found
		if (Browser.getTroubleShooting()&&!element.isDisplayed()) {
			performTroubleShooting();
		}
		Assert.assertTrue(formatLogMsg(getLoc("loc.is.absent")), element.isDisplayed());
	}
	
	/**
	 * Wait until element is clickable.
	 */
	public void waitForElementClickable(){
		new WebDriverWait(browser.getDriver(), Long.parseLong(browser.getTimeoutForCondition())).until(ExpectedConditions.elementToBeClickable(locator));
	}

	/**
	 * Performing troubleshooting via changing active locator, output log and report.
	 */
	private void performTroubleShooting(){
		int length = decrementLocator(locator).toString().length();
		try {
			info("---------------- Troubleshooting starting --------------------");
			for (int i = 0; i < length; i++) {
				decrementLocator(locator);
				Boolean result = isPresent(TIMEOUT_WAIT_0);
				info("Re-try with locator: \t" + locator.toString() + String.format(new String(new char[i]).replace('\0', ' ') + " :%s",result? "FOUND!":"NOT FOUND"));
				if (result) {
					break;
				}
			}
		} catch (Exception e) {
			warn(e.getMessage());
		}
		finally {
			info("---------------- Troubleshooting finished --------------------");
		}
	}

	/**
	 * Decrement type of locator (troubleshooting).
	 * @param locator
	 * @return By
	 */
	private By decrementLocator(By locator){
		for (Field field : locator.getClass().getDeclaredFields()) {
	        	field.setAccessible(true);
				try {
	        		String strLocator = (String)field.get(locator);
					field.set(locator,strLocator.substring(0, strLocator.length()-1));
					} catch (IllegalArgumentException e) {
						warn(e.getMessage());
					} catch (IllegalAccessException e) {
						warn(e.getMessage());
					}
	    }
		return locator;
	}

	/**
	 * Does an element exist in html (no check for display)
	 */
	public final boolean exists() {
		browser.getDriver().manage().timeouts().implicitlyWait(TIMEOUT_WAIT_0, TimeUnit.SECONDS);
		Boolean result = browser.getDriver().findElements(locator).size()>0;
		element = (RemoteWebElement) browser.getDriver().findElement(locator);
		browser.getDriver().manage().timeouts().implicitlyWait(Integer.valueOf(browser.getTimeoutForCondition()), TimeUnit.SECONDS);
		return result;
	}

	/**
	 * Click on the item.
	 */
	public void click() {
		waitForIsElementPresent();
		info(getLoc("loc.clicking"));
		browser.getDriver().getMouse().mouseMove(element.getCoordinates());
		if (browser.getDriver() instanceof JavascriptExecutor) {
	        ((JavascriptExecutor)browser.getDriver()).executeScript("arguments[0].style.border='3px solid red'", element);
	    }
		element.click();
	};

	/**
	 * Click on an item and wait for the page is loaded
	 */
	public void clickAndWait() {
		click();
		browser.waitForPageToLoad();
	}

	/**
	 * Click on an item ext click through .sendKeys("\n") and wait for the page is loaded.
	 */
	public void clickExtAndWait() {
		clickExt();
		browser.waitForPageToLoad();
	}

	/**
	 * Click on an item js click and wait for the page is loaded.
	 */
	public void clickViaJsAndWait() {
		clickViaJS();
		browser.waitForPageToLoad();
	}

	/**
	 * Click and look forward to the emergence of a new window.
	 */
	public void clickAndWaitForNewWindow() {
		Set<String> set = browser.getDriver().getWindowHandles();
		int count = browser.getDriver().getWindowHandles().size();
		click();
		info(getLoc("loc.select.next.window"));
		browser.waitForNewWindow(count);
		Iterator<String> cIter = browser.getDriver().getWindowHandles().iterator();
		String handle = null;
		while (cIter.hasNext()) {
			String temp = cIter.next();
			if (!set.contains(temp)) {
				handle = temp;
				break;
			}
		}
		browser.getDriver().switchTo().window(handle);
		browser.windowMaximise();
	}

	/**
	 * Click and look forward to closing the current window
	 */
	public void clickAndWaitForCloseWindow() {
		int count = browser.getDriver().getWindowHandles().size();
		click();
		info(getLoc("loc.select.previous.window"));
		browser.waitForNewWindow(count - 2);
		browser.getDriver().switchTo().window((String) browser.getDriver().getWindowHandles().toArray()[count - 2]);
	}

	/**
	 * Get the item text (inner text).
	 * @return Text of element
	 */
	public String getText() {
		waitForIsElementPresent();
		return element.getText();
	}

	/**
	 * Check for an element on the page If the element is absent, then the test is aborted
	 */
	public void assertIsPresent() {
		if (!isPresent()) {
			fatal(getLoc("loc.is.absent"));
		}
	}

	/**
	 * Check for an element on the page If the element is present, then the test is aborted
	 */
	public void assertIsAbsent() {
		if (isPresent()) {
			fatal(getLoc("loc.is.absent"));
		}
	}

	/**
	 * Wait for the item and abort the test, if the item does not appear.
	 */
	public void waitAndAssertIsPresent() {
		waitForIsElementPresent();
	}

	
	protected String id;
	
	/**
	 * Check for is element present on the page.
	 * @return Is element present
	 */
	public boolean isPresent() {
		
		return isPresent(TIMEOUT_WAIT_0);
	}
	/**
	 * Check for is element present on the page.
	 * @return Is element present
	 */
	public boolean isPresent(int timeout) {
		if (sikuliLocator != null) {
			sikuli = new SikuliDriver(sikuliLocator);
			sikuliScreenRegion = sikuli.getRegion();
			return sikuli.findImageElement(Double.parseDouble(similarity)) != null;
		}
		WebDriverWait wait = new WebDriverWait(Browser.getInstance().getDriver(), timeout);
		browser.getDriver().manage().timeouts().implicitlyWait(TIMEOUT_WAIT_0, TimeUnit.SECONDS);
		try {
			wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
				public Boolean apply(final WebDriver driver) {
					try {
						List<WebElement> list = driver.findElements(locator);
						for (WebElement el : list) {
							if (el instanceof RemoteWebElement && el.isDisplayed()) {
								element = (RemoteWebElement) el;
								return element.isDisplayed();
							}
						}
						element = (RemoteWebElement) driver.findElement(locator);
					} catch (Exception e) {
						return false;
					}
					return element.isDisplayed();
				}
			});
		} catch (Exception e) {
			return false;
		}
		try {
			browser.getDriver().manage().timeouts().implicitlyWait(Integer.valueOf(browser.getTimeoutForCondition()), TimeUnit.SECONDS);
			return element.isDisplayed();
		} catch (Exception e) {
			warn(e.getMessage());
		}
		return false;
	}

	/**
	 * Check for is element present on the page.
	 * @return Is element present
	 */
	public boolean isPresentSikuli(int timeout) {
		if (sikuliLocator != null) {
			sikuli = new SikuliDriver(sikuliLocator);
			sikuliScreenRegion = sikuli.getRegion();
			return sikuli.findImageElement(Double.parseDouble(similarity),timeout) != null;
		}
		return false;
	}

	/**
	 * Check for is element present in the specified screen region.
	 * @return Is element present
	 */
	public boolean isPresentSikuli(int timeout, ScreenRegion region) {
		if (sikuliLocator != null) {
			sikuli = new SikuliDriver(sikuliLocator);
			sikuliScreenRegion = sikuli.getRegion();
			return sikuli.findImageElement(
					Double.parseDouble(similarity),timeout,
					region.getBounds().x, region.getBounds().y,
					region.getBounds().width, region.getBounds().height)!= null;
		}
		return false;
	}

	/**
	 * Double click on the item. Waiting for the end of renderiga
	 */
	public void doubleClick() {
		waitForIsElementPresent();
		info(getLoc("loc.clicking.double"));
		browser.getDriver().getMouse().mouseMove(element.getCoordinates());
		Actions builder = new Actions(browser.getDriver());
		Action dClick = builder.doubleClick(element).build();
		dClick.perform();

	}

	/**
	 * another implementation of waiting.
	 */
	public void waitForExists() {
		new WebDriverWait(browser.getDriver(), Long.parseLong(browser.getTimeoutForCondition())) {}.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(final WebDriver driver) {
				Boolean isExist = driver.findElements(locator).size() > 0;
				if (isExist) {
					element = (RemoteWebElement) driver.findElement(locator);
				}
				return isExist;
			}
		});
	}

	/**
	 * another implementation of waiting for not exists.
	 */
	public void waitForDoesNotExist() {
		browser.getDriver().manage().timeouts().implicitlyWait(TIMEOUT_WAIT_IS_NOT_PRESENT, TimeUnit.SECONDS);
		new WebDriverWait(browser.getDriver(), Long.parseLong(browser.getTimeoutForCondition())) {
		}.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(final WebDriver driver) {
				return (driver.findElements(locator).size() < 1);
			}
		});
		browser.getDriver().manage().timeouts().implicitlyWait(TIMEOUT_IS_PRESENT, TimeUnit.SECONDS);
	}

	/**
	 * Gets attribute value of the element.
	 * @param attr Attribute name
	 * @return Attribute value
	 */
	public String getAttribute(final String attr) {
		waitForIsElementPresent();
		return browser.getDriver().findElement(locator).getAttribute(attr);
	}

	/**
	 * set value via javascript <b>document.getElementById('%1$s').value='%2$s' </b>
	 * @param elementId Element Id
	 * @param value Value
	 */
	public void setValueViaJS(final String elementId, final String value) {
		try {
			((JavascriptExecutor) browser.getDriver()).executeScript(String.format(
					"document.getElementById('%1$s').value='%2$s'", elementId, value), element);
		} catch (Exception r) {
			Logger.getInstance().warn(r.getMessage());
		}
	}

	/**
	 * set innerHtml via javascript <b>arguments[0].innerHTML='%1$s' </b>.
	 * @param value value
	 */
	public void setInnerHtml(final String value) {
		waitAndAssertIsPresent();
		element.click();
		info("Ввод текста '" + value + "'");
		((JavascriptExecutor) browser.getDriver()).executeScript("arguments[0].innerHTML=\"\";", element);
		((JavascriptExecutor) browser.getDriver()).executeScript("arguments[0].innerHTML=\"" + value + "\";", element);
	}

	/**
	 * Check if element is "disabled" or "readonly".
	 */
	public void assertDisabled() {
		waitAndAssertIsPresent();
		String[] attributes = new String[] { "disabled", "readonly", "readOnly" };
		String disabled;
		for (int i = 0; i < attributes.length; i++) {
			disabled = element.getAttribute(attributes[i]);
			if (disabled.equals("") || disabled.equals("disabled") || disabled.equals("true")
					|| disabled.equals("readonly")) {
				return;
			}
		}
		fatal(getElementType() + " " + name + getLoc("loc.is.disabled"));
	}

	/**
	 * Set value via javascript <b>arguments[0].value='%1$s' </b>.
	 * @param value value
	 */
	public void setValueViaJs(final String value) {
		waitAndAssertIsPresent();
		element.click();
		info("Ввод текста '" + value + "'");
		((JavascriptExecutor) browser.getDriver()).executeScript("arguments[0].value=\"\";", element);
		((JavascriptExecutor) browser.getDriver()).executeScript("arguments[0].value=\"" + value + "\";", element);
	}

	/**
	 * Click if present.
	 */
	public void clickIfPresent() {
		if (isPresent()) {
			click();
		}
	}

	/**
	 * Right Click.
	 */
	public void clickRight() {
		waitForIsElementPresent();
		info("Clicking Right");
		browser.getDriver().getMouse().mouseMove(element.getCoordinates());
		browser.getDriver().getMouse().contextClick(element.getCoordinates());
	}

	/**
	 * Focuses the element.
	 */
	public void focus() {
		browser.getDriver().getMouse().mouseMove(element.getCoordinates());
	}

	/**
	 * Get Sikuli Driver.
	 * @return SikuliDriver
	 */
	public ScreenRegion getSikuliRegion() {
		return sikuliScreenRegion;
	}

	/**
	 * AssertIsPresentFromCache (sikuli).
	 * @param screen ScreenRegion
	 * @return is present
	 */
	public boolean assertIsPresentFromCache(ScreenRegion screen) {
		if (sikuliLocator != null) {
			sikuli = new SikuliDriver(sikuliLocator,screen);
			sikuliScreenRegion = sikuli.getRegion();
			return sikuli.findImageElement(Double.parseDouble(similarity)) != null;
		}
		return false;
	}
	
	/**
	 * wait the item does not exist yet (there is no check on Visibility)
	 * @deprecated  As of release 1.2.7, replaced by {@link #waitForDoesNotExist()}
	 */
	@Deprecated
	public void waitNotVisible() {
		WebDriverWait wait = new WebDriverWait(browser.getDriver(), Long.parseLong(browser.getTimeoutForCondition()));

		try {
			wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
				public Boolean apply(final WebDriver driver) {
					try {
						List<WebElement> list = driver.findElements(locator);
						for (WebElement el : list) {
							if (el instanceof RemoteWebElement) {
								element = (RemoteWebElement) el;
								return true;
							}
						}
						element = (RemoteWebElement) driver.findElement(locator);
						return true;
					} catch (Exception e) {
						return false;
					}
				}
			});
		} catch (Exception e) {
			boolean res = false;
			try {
				res = (element != null);
			} catch (Exception e1) {
				e1.toString();
			}
			Assert.assertTrue(formatLogMsg(getLoc("loc.is.absent")), res);
		}
		Assert.assertTrue(formatLogMsg(getLoc("loc.is.absent")), element != null);
	}
	

}
