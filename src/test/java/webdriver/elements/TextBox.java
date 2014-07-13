package webdriver.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.testng.AssertJUnit;

/**
 * The class that describes an input field
 */
public class TextBox extends BaseElement {

	/**
	 * Constructor
	 * @param locator locator
	 * @param name name
	 */
	public TextBox(final By locator, final String name) {
		super(locator, name);
	}

	/**
	 * Constructor
	 * @param string locator
	 * @param name name
	 */
	public TextBox(final String string, final String name) {
		super(string, name);
	}

	/**
	 * Returns Element type
	 * @return Element type
	 */
	protected String getElementType() {
		return getLoc("loc.text.field");
	}

	/**
	 * Constructor
	 * @param locatorStageA locator A
	 * @param locatorStageB locator B
	 * @param name name
	 */
	public TextBox(final By locatorStageA, final By locatorStageB, final String name) {
		super(locatorStageA, locatorStageB, name);
	}

	/**
	 * Constructor
	 * @param locator locator
	 */
	public TextBox(final By locator) {
		super(locator);
	}

	/**
	 * Enter the text in the box
	 * @param value text
	 */
	public void type(final String value) {
		waitAndAssertIsPresent();
		info(String.format(getLoc("loc.text.typing") + " '%1$s'", value));
		if (browser.getDriver() instanceof JavascriptExecutor) {
	        ((JavascriptExecutor)browser.getDriver()).executeScript("arguments[0].style.border='3px solid red'", element);
	    }
		element.sendKeys(value);
	}
	
	/**
	 * Enter the text in the Sikuli box
	 * @param value text
	 */
	public void typeSikuli(final String value) {
		clickViaCoordinates();
		info(String.format(getLoc("loc.text.typing") + " '%1$s'", value));
		new Actions(browser.getDriver().getKeyboard()).sendKeys(value).perform();
	}

	/**
	 * File browsing
	 * @param value Filepath
	 */
	public void typeFile(final String value) {
		waitForExists();
		info(String.format(getLoc("loc.text.typing") + " '%1$s'", value));
		element.sendKeys(value);
	}

	/**
	 * Clear field and type text
	 * @param value text
	 */
	public void setText(final String value) {
		waitAndAssertIsPresent();
		element.clear();
		type(value);
	}

	/**
	 * Submits the field
	 */
	public void submit() {
		element.submit();
	}

	/**
	 * Mouse Up
	 */
	public void mouseUp() {
		browser.getDriver().getMouse().mouseUp(element.getCoordinates());
	}

	/**
	 * Gets value of field
	 * @return value
	 */
	public String getValue() {
		waitAndAssertIsPresent();
		return element.getAttribute("value");
	}

	/**
	 * Asserts if Text is Present in field
	 * @param value Text
	 */
	public void assertIsTextPresent(final String value) {
		String text = getValue();
		AssertJUnit.assertTrue(value + " " + getLoc("loc.text.not.present"), text.contains(value));
	}

	/**
	 * Asserts max field length
	 * @param max value
	 */
	public void assertMaxLenght(final int max) {
		String attribute = getAttribute("maxlength");
		if (attribute.isEmpty() || attribute.equals(null)) {
			AssertJUnit.fail(getLoc("loc.text.not.limited"));
		}
		int current = Integer.parseInt(attribute);
		AssertJUnit.assertTrue(String.format(getLoc("loc.text.expected.template"), max, current), current == max);
		info(getLoc("loc.text.length.correct"));
	}

	/**
	 * Focuses on the element using send keys
	 */
	public void focus() {
		browser.getDriver().findElement(locator).sendKeys("");
	}

	/**
	 * Type using Java Script
	 * @param value Text
	 */
	public void jsType(final String value) {
		assertIsPresent();
		info(String.format(getLoc("loc.text.typing") + " '%1$s'", value));
		((JavascriptExecutor) browser.getDriver()).executeScript(String.format("arguments[0].value='%1$s'", value), element);
	}

}
