package com.ftu.admin.consumer.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Load all properties from properties file
 * 
 * @version 1.0 09/15/2015
 * @author haitx3
 */

public class PropertyLoader {
	// private static final boolean THROW_ON_LOAD_FAILURE = true;
	// private static final boolean LOAD_AS_RESOURCE_BUNDLE = false;
	private static final String SUFFIX = ".properties";

	public static Properties loadProperties(String strName, ClassLoader loader) {
		if (strName == null) {
			throw new IllegalArgumentException("loadProperties(String name, ClassLoader loader): name is null");
		}

		final char ch = '/';
		if (strName.charAt(0) == ch) {
			strName = strName.substring(1);
		}
		if (strName.endsWith(SUFFIX)) {
			strName = strName.substring(0, strName.length() - SUFFIX.length());
		}
		Properties result = null;

		InputStream in = null;
		try {
			if (loader == null) {
				loader = ClassLoader.getSystemClassLoader();
			}

			strName = strName.replace('.', '/');

			if (!strName.endsWith(SUFFIX)) {
				strName = strName.concat(SUFFIX);
			}

			in = loader.getResourceAsStream(strName);
			if (in != null) {
				result = new Properties();
				result.load(in);
			}
		} catch (Exception e) {
			result = null;

			if (in != null)
				try {
					in.close();
				} catch (Throwable localThrowable) {
				}
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Throwable localThrowable1) {
				}
		}
		if (result == null) {
			throw new IllegalArgumentException("could not load [" + strName + "]" + " as " + "a classloader resource");
		}

		return result;
	}

	public static Properties loadProperties(String strName) {
		return loadProperties(strName, Thread.currentThread().getContextClassLoader());
	}
}