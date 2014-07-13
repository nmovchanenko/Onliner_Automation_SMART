package webdriver;

/**
 * IAnalys interface
 */
public interface IAnalys {

	public abstract boolean shouldAnalys();
	public abstract void invokeAnalys(Throwable exc, String bodyText) throws Throwable;

}