package com.ftu.admin.consumer.utils;

import java.security.MessageDigest;

/**
 * <p>
 * Title: StringUtil
 * </p>
 * <p>
 * Description: Utility for string processing
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001
 * </p>
 * <p>
 * Company: FPT
 * </p>
 * 
 * @author Thai Hoang Hiep
 * @version 1.0
 */

public class ConsumerUtil {

	// //////////////////////////////////////////////////////
	/**
	 * Encrypt a string
	 * 
	 * @param strValue
	 *            string to encrypt
	 * @param strAlgorithm
	 *            ecrypt algorithm
	 * @return encrypted string
	 * @throws Exception
	 *             if error occured
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String encrypt(String strValue, String strAlgorithm) throws Exception {
		return encrypt(strValue.getBytes(), strAlgorithm);
	}

	// //////////////////////////////////////////////////////
	/**
	 * Encrypt a byte array
	 * 
	 * @param btValue
	 *            array to encrypt
	 * @param strAlgorithm
	 *            ecrypt algorithm
	 * @return encrypted string
	 * @throws Exception
	 *             if error occured
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String encrypt(byte[] btValue, String strAlgorithm) throws Exception {
		BASE64Encoder enc = new BASE64Encoder();
		MessageDigest md = MessageDigest.getInstance(strAlgorithm);
		return enc.encodeBuffer(md.digest(btValue));
	}

	// //////////////////////////////////////////////////////
	/**
	 * Replace null string
	 * 
	 * @param objInput
	 *            object value
	 * @param strNullValue
	 *            object to replace if objInput is null
	 * @return String value of objInput after replace
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String nvl(Object objInput, String strNullValue) {
		if (objInput == null)
			return strNullValue;
		return objInput.toString();
	}

	public static boolean stringIsNullOrEmty(String str) {
		if (str == null)
			return true;
		else {
			if (str.length() <= 0)
				return true;
		}
		return false;
	}

	public static boolean stringIsNullOrEmty(Object str) {
		if (str == null)
			return true;
		else {
			if (str.toString().length() <= 0)
				return true;
		}
		return false;
	}

}
