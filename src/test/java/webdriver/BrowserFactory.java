package webdriver;

import static webdriver.Logger.getLoc;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.naming.NamingException;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

/*import com.opera.core.systems.OperaDriver;
import com.opera.core.systems.OperaProfile;*/

import webdriver.Browser.Browsers;

/**
 * The class-initializer-based browser string parameter.
 */
public abstract class BrowserFactory {

	/**
	 * Setting up Driver
	 * @param type Browser type
	 * @return RemoteWebDriver
	 */
	public static RemoteWebDriver setUp(final Browsers type) {
		
		DesiredCapabilities capabilitiesProxy = null;
		Proxy proxy = null;
		if (Browser.getAnalyzeTraffic()){
			//browsermob proxy
			//-----------------------------------------------------------
		    //captures the moouse movements and navigations
			ProxyServ.getProxyServer().setCaptureHeaders(true);
			ProxyServ.getProxyServer().setCaptureContent(true); 
		    // get the Selenium proxy object
		    proxy = null;
			try {
				proxy = ProxyServ.getProxyServer().seleniumProxy();
			} catch (Exception e1) {
				Logger.getInstance().debug(e1.getMessage());
			}
		    // configure it as a desired capability
		    capabilitiesProxy = new DesiredCapabilities();
		    capabilitiesProxy.setCapability(CapabilityType.PROXY, proxy);
		    //-----------------------------------------------------------
		}
		
		RemoteWebDriver driver = null;
		File myFile = null;
		switch (type) {
		case CHROME:
			ChromeOptions options = null;
			if (Browser.getDetectJsErrors()){
				options = new ChromeOptions();
				options.addExtensions(new File(ClassLoader.getSystemResource("Chrome_JSErrorCollector.crx").getPath()));
			}
			DesiredCapabilities cp1 = DesiredCapabilities.chrome();
			cp1.setCapability("chrome.switches", Arrays.asList("--disable-popup-blocking"));
			URL myTestURL = ClassLoader.getSystemResource("chromedriver.exe");
			try {
				myFile = new File(myTestURL.toURI());
			} catch (URISyntaxException e1) {
				Logger.getInstance().debug(e1.getMessage());
			}
			System.setProperty("webdriver.chrome.driver", myFile.getAbsolutePath());
			if (Browser.getAnalyzeTraffic()){
				cp1.setCapability(CapabilityType.PROXY, proxy);
			}
			if (options!=null) {
				cp1.setCapability(ChromeOptions.CAPABILITY, options);
			}
			driver = new ChromeDriver(cp1);
			driver.manage().window().maximize();
			break;
		case FIREFOX:
			if (Browser.getDetectJsErrors()) {
				FirefoxProfile ffProfile = new FirefoxProfile();
				try {
					JavaScriptError.addExtension(ffProfile);
				} catch (IOException e) {
					Logger.getInstance().warn("Error during initializing of FF (JavaScriptError) webdriver");
				}
				driver = new FirefoxDriver(new FirefoxBinary(),ffProfile,capabilitiesProxy);
				break;
			}
			driver = new FirefoxDriver(capabilitiesProxy);
			break;
		case IEXPLORE:
			//local security request flag INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS
			//(but this flag may cause appearing "skipped" tests)
			if(new PropertiesResourceManager(Browser.PROPERTIES_FILE).getProperty("localrun").equalsIgnoreCase("true")){
				DesiredCapabilities cp = DesiredCapabilities.internetExplorer();
				cp.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				URL myTestURL2 = ClassLoader.getSystemResource("IEDriverServer.exe");
				try {
					myFile = new File(myTestURL2.toURI());
				} catch (URISyntaxException e1) {
					Logger.getInstance().debug(e1.getMessage());
				}
				System.setProperty("webdriver.ie.driver", myFile.getAbsolutePath());
				if (Browser.getAnalyzeTraffic()){
					cp.setCapability(CapabilityType.PROXY, proxy);
				}
				driver = new InternetExplorerDriver(cp);
			// better to avoid
			}else{
				// now remote connection will be refused, so use selenium server instead
				driver = new InternetExplorerDriver();
			}
			break;
		/*case OPERA:
			//work on v.11-12 (Presto engine)
			if (capabilitiesProxy!=null){
				driver = new OperaDriver(capabilitiesProxy);
				break;
			}
			driver = new OperaDriver();
			break;*/
		case SAFARI:
			//work on v.5.1+
			if (capabilitiesProxy!=null){
				driver = new SafariDriver(capabilitiesProxy);
				break;
			}
			driver = new SafariDriver();
			break;
		default:
			break;
		}
		return driver;
	}

	/**
	 * Setting up Driver
	 * @param type Browser type
	 * @return RemoteWebDriver
	 * @throws NamingException NamingException
	 */
	public static RemoteWebDriver setUp(final String type) throws NamingException {
		for (Browsers t : Browsers.values()) {
			if (t.toString().equalsIgnoreCase(type)) {
				return setUp(t);
			}
		}
		throw new NamingException(getLoc("loc.browser.name.wrong")+":\nchrome\nfirefox\niexplore\nopera\nsafari");
	}
}
