package com.ftu.admin.consumer.utils;

import com.ftu.utils.ResourceBundleUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.jackson.JacksonFeature;

/**
 * @version 1.0 09/15/2015
 * @author haitx3
 */

public class ConsumerPropeties {
	public static String resourceUrl;
	static {
		InputStream is = null;
		try {
			resourceUrl = ResourceBundleUtil.getString("resource.url");
		} catch (Throwable ex) {
			System.err.println("Initial WebResource failed." + ex);
			throw new ExceptionInInitializerError(ex);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignored) {
					ignored.printStackTrace();
				}
			}
		}
	}

	public static String getResourceUrl() {
		return resourceUrl;
	}

	public static void setResourceUrl(String resourceUrl) {
		ConsumerPropeties.resourceUrl = resourceUrl;
	}

	public static String getPath(String path) {
		Object objPath = ResourceBundleUtil.getString(path);
		if (objPath != null) {
			return objPath.toString();
		} else {
			return path;
		}
	}

	public static String getProperty(String path) {
		return getPath(path);
	}

	public static String getUrlByPath(String path) {
		String fullPath = resourceUrl;
		if (ResourceBundleUtil.getString(path) != null) {
			fullPath += ResourceBundleUtil.getString(path);
		} else {
			fullPath += path;
		}
		return fullPath;
	}
}
