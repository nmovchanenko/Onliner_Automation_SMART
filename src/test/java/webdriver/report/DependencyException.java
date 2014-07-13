package webdriver.report;

import org.testng.SkipException;

public class DependencyException extends SkipException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1805319980683714503L;

	/**
	 * @param caseStatus - status
	 */
	public DependencyException(String caseStatus) {
		super(String.format("Dependency: case with status '%1$s' was not created by previous autotest.",caseStatus));
	}

//	/**
//	 * @param skipMessage
//	 * @param cause
//	 */
//	public DependencyException(String skipMessage, Throwable cause) {
//		super(skipMessage, cause);
//	}

}
