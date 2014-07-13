package webdriver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * DataProviderUtils
 */
public class DataProviderUtils {
	/**
	 * Mapping
	 * @param testMethod testMethod
	 * @return arguments
	 * @throws Exception Exception
	 */
	protected static Map<String, String> resolveDataProviderArguments(final Method testMethod) throws Exception {
		if (testMethod == null) {
			throw new IllegalArgumentException("Test Method context cannot be null.");
		}

		DataProviderArguments args = testMethod.getAnnotation(DataProviderArguments.class);
		if (args == null) {
			throw new IllegalArgumentException("Test Method context has no DataProviderArguments annotation.");
		}

		if (args.value() == null || args.value().length == 0) {
			throw new IllegalArgumentException("Test Method context has a malformed DataProviderArguments annotation.");
		}

		Map<String, String> arguments = new HashMap<String, String>();
		for (int i = 0; i < args.value().length; i++) {
			String[] parts = args.value()[i].split("=");
			arguments.put(parts[0], parts[1]);
		}
		return arguments;
	}
}
