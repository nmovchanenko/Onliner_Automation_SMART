package webdriver.report;

public enum Errors {
	ELEMENT_ABSENT("is absent"),SERVICE_UNAVAILABLE ("SERVICE UNAVAILABLE"), SERVER_ERROR("ПРОВЕРКА"), UNKNOWN_ERROR(""), DATA_ERROR("connection refused");
	
	/**
	 * @uml.property  name="message"
	 */
	public String message;
	Errors(String message){
		this.message = message;
	}
}
