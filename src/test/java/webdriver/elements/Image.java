package webdriver.elements;

import org.openqa.selenium.Point;
import org.openqa.selenium.internal.Locatable;


public class Image extends BaseElement {

	public Image(String url, String name) {
		super(url, name);
	}

	protected String getElementType() {
		return getLoc("loc.image");
	}


	/*
	 * Click to image
	 */
	@Override
	public void click() {
	    info(getLoc("loc.clicking"));
	    Point point = sikuli.getPoint();
	    browser.getDriver().getMouse().mouseMove(((Locatable)browser.getDriver().findElementByTagName("html")).getCoordinates(),point.getX(),point.getY());
		sikuli.findImageElement(Double.parseDouble(similarity)).click();
	}

	/*
	 * DoubleClick to image
	 */
	@Override
	public void doubleClick() {
		sikuli.findImageElement(Double.parseDouble(similarity)).doubleClick();
	}

}
