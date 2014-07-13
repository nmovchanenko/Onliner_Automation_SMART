package webdriver.report;

import org.openqa.selenium.By;

import webdriver.Logger;
import webdriver.elements.Label;

public class ExampleException extends AssertionError {

	private static final Label lblDataError = new Label(By.xpath("//td/div[contains(.,'Data error')]"), "Data error");
	private static final long serialVersionUID = 354899061504456795L;
	private static ExampleException customException;

	private ExampleException(Errors error, String detailMessage) {
		super(error.toString() + " " + detailMessage);
		Logger.getInstance().info("mark as " + error.toString());
	}

	public static void analyzeAndThrowException(Throwable error, String browserBody) {

		String message = error.getLocalizedMessage();

		Logger.getInstance().info("error message is " + message);
		if (message != null) {
			if (browserBody.contains(Errors.SERVICE_UNAVAILABLE.message)) {
				customException = new ExampleException(Errors.SERVICE_UNAVAILABLE, message);
				customException.setStackTrace(error.getStackTrace());
				throw customException;
			} else if (message.contains(Errors.DATA_ERROR.message)) {
				customException = new ExampleException(Errors.SERVER_ERROR, message);
				customException.setStackTrace(error.getStackTrace());
				throw customException;
			} else if (lblDataError.isPresent()) {
				// if data error occured
				customException = new ExampleException(Errors.DATA_ERROR, message);
				customException.setStackTrace(error.getStackTrace());
				throw customException;
			}else if (message.contains(Errors.ELEMENT_ABSENT.message)) {
				customException = new ExampleException(Errors.ELEMENT_ABSENT, message);
				customException.setStackTrace(error.getStackTrace());
				throw customException;
			}

		}
		// if not found custom exception or message is null
		customException = new ExampleException(Errors.UNKNOWN_ERROR, message);
		customException.setStackTrace(error.getStackTrace());
		throw customException;

	}

}
