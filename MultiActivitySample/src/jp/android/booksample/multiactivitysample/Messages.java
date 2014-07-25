package jp.android.booksample.multiactivitysample;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * é©ìÆê∂ê¨ÉNÉâÉX
 * @author îÕçs
 *
 */
public class Messages
{
	private static final String BUNDLE_NAME = "jp.android.booksample.multiactivitysample.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	/**
	 *
	 */
	private Messages()
	{
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public static String getString(String key)
	{
		try
		{
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}
}
