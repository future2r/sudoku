package name.ulbricht.sudoku;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

final class Messages {

	private static final String BUNDLE_NAME = "name.ulbricht.sudoku.messages";

	private static ResourceBundle getBundle() {
		return ResourceBundle.getBundle(BUNDLE_NAME);
	}

	private static ResourceBundle getBundle(final Locale locale) {
		return ResourceBundle.getBundle(BUNDLE_NAME, locale);
	}

	public static String msg(final String key) {
		return getString(getBundle(), key);
	}

	public static String msg(final Locale locale, final String key) {
		return getString(getBundle(locale), key);
	}

	public static String msg(final String key, final Object... args) {
		return String.format(msg(key), args);
	}

	public static String msg(final Locale locale, final String key, final Object... args) {
		return String.format(msg(locale, key), args);
	}

	private static String getString(final ResourceBundle bundle, final String key) {
		try {
			return bundle.getString(key);
		} catch (final MissingResourceException ex) {
			return '!' + key + '!';
		}
	}

	private Messages() {
		// hidden
	}
}