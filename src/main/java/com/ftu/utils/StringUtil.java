package com.ftu.utils;

import java.security.MessageDigest;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

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

public class StringUtil {
	public static final String CHARFORM_NOHORN = "aaaaaaaaaaaaaaaaaeeeeeeeeeeeiiiiiooooooooooooooooouuuuuuuuuuuyyyyyd"
			+ "AAAAAAAAAAAAAAAAAEEEEEEEEEEEIIIIIOOOOOOOOOOOOOOOOOUUUUUUUUUUUYYYYYD";
	public static final String CHARFORM_UNICODE = "àáảãạâầấẩẫậăằắẳẵặèéẻẽẹêềếểễệìíỉĩịòóỏõọôồốổỗộơờớởỡợùúủũụưừứửữựỳýỷỹỵđ"
			+ "ÀÁẢÃẠÂẦẤẨẪẬĂẰẮẲẴẶÈÉẺẼẸÊỀẾỂỄỆÌÍỈĨỊÒÓỎÕỌÔỒỐỔỖỘƠỜỚỞỠỢÙÚỦŨỤƯỪỨỬỮỰỲÝỶỸỴĐ";
	public static final String CHARFORM_TCVN = "µ¸¶·¹©ÇÊÈÉË¨»¾¼½ÆÌÐÎÏÑªÒÕÓÔÖ×ÝØÜÞßãáâä«åèæçé¬êíëìîïóñòô­õøö÷ùúýûüþ®"
			+ "µ¸¶·¹©ÇÊÈÉË¨»¾¼½ÆÌÐÎÏÑªÒÕÓÔÖ×ÝØÜÞßãáâä«åèæçé¬êíëìîïóñòô­õøö÷ùúýûüþ®";
	public static final int ALIGN_CENTER = 0;
	public static final int ALIGN_LEFT = 1;
	public static final int ALIGN_RIGHT = 2;

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
//	public static String encrypt(String strValue, String strAlgorithm) throws Exception {
//		return encrypt(strValue.getBytes(), strAlgorithm);
//	}

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
//	public static String encrypt(byte[] btValue, String strAlgorithm) throws Exception {
//		BASE64Encoder enc = new BASE64Encoder();
//		MessageDigest md = MessageDigest.getInstance(strAlgorithm);
//		return enc.encodeBuffer(md.digest(btValue));
//	}

	// //////////////////////////////////////////////////////
	/**
	 * Format long number
	 * 
	 * @param lngNumber
	 *            number to format
	 * @param strPattern
	 *            format pattern
	 * @return formatted string
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String format(long lngNumber, String strPattern) {
		java.text.DecimalFormat fmt = new java.text.DecimalFormat(strPattern);
		return fmt.format(lngNumber);
	}

	// //////////////////////////////////////////////////////
	/**
	 * Format double number
	 * 
	 * @param dblNumber
	 *            number to format
	 * @param strPattern
	 *            format pattern
	 * @return formatted string
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String format(double dblNumber, String strPattern) {
		java.text.DecimalFormat fmt = new java.text.DecimalFormat(strPattern);
		return fmt.format(dblNumber);
	}

	// //////////////////////////////////////////////////////
	/**
	 * Replace all occurred of string with another
	 * 
	 * @param strSrc
	 *            main string
	 * @param strFind
	 *            string to find
	 * @param strReplace
	 *            string to replace
	 * @return main string after replace
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String replaceAll(String strSrc, String strFind, String strReplace) {
		if (strFind == null || strFind.length() == 0)
			return strSrc;
		int iLocation = 0;
		int iPrevLocation = 0;
		StringBuffer strResult = new StringBuffer();
		while ((iLocation = strSrc.indexOf(strFind, iLocation)) >= 0) {
			strResult.append(strSrc.substring(iPrevLocation, iLocation));
			strResult.append(strReplace);
			iLocation += strFind.length();
			iPrevLocation = iLocation;
		}
		strResult.append(strSrc.substring(iPrevLocation, strSrc.length()));
		return strResult.toString();
	}

	// //////////////////////////////////////////////////////
	/**
	 * Replace all occurred of string with another
	 * 
	 * @param strSrc
	 *            main string
	 * @param strFind
	 *            string to find
	 * @param strReplace
	 *            string to replace
	 * @param iMaxReplacement
	 *            max replacement allowed
	 * @return main string after replace
	 * @author Nguyen Thi Thu Trang
	 */
	// //////////////////////////////////////////////////////
	public static String replaceAll(String strSrc, String strFind, String strReplace, int iMaxReplacement) {
		int iLocation = 0;
		if (strFind == null || strFind.length() == 0)
			return strSrc;
		int iPrevLocation = 0;
		int iCount = 0;
		StringBuffer strResult = new StringBuffer();
		while ((iLocation = strSrc.indexOf(strFind, iLocation)) >= 0 && iCount < iMaxReplacement) {
			strResult.append(strSrc.substring(iPrevLocation, iLocation));
			strResult.append(strReplace);
			iCount++;
			iLocation += strFind.length();
			iPrevLocation = iLocation;
		}
		strResult.append(strSrc.substring(iPrevLocation, strSrc.length()));
		return strResult.toString();
	}

	// //////////////////////////////////////////////////////
	/**
	 * Replace all occurred of string ignore case with another
	 * 
	 * @param strSrc
	 *            main string
	 * @param strFind
	 *            string to find
	 * @param strReplace
	 *            string to replace
	 * @return main string after replace
	 * @author SonNT
	 */
	// //////////////////////////////////////////////////////
	public static String replaceAllIgnoreCase(String strSrc, String strFind, String strReplace) {
		if (strFind == null || strFind.length() == 0)
			return strSrc;
		String strSrcUpper = strSrc.toUpperCase();
		strFind = strFind.toUpperCase();

		int iLocation = 0;
		int iPrevLocation = 0;
		StringBuffer strResult = new StringBuffer();
		while ((iLocation = strSrcUpper.indexOf(strFind, iLocation)) >= 0) {
			strResult.append(strSrc.substring(iPrevLocation, iLocation));
			strResult.append(strReplace);
			iLocation += strFind.length();
			iPrevLocation = iLocation;
		}
		strResult.append(strSrc.substring(iPrevLocation, strSrc.length()));
		return strResult.toString();
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

	// //////////////////////////////////////////////////////
	/**
	 * Return first position of letter (printable character) in String
	 * 
	 * @param strSource
	 *            String to search
	 * @param iOffset
	 *            start offset
	 * @return first position of letter (printable character) in strSource, -1
	 *         if not found
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static int indexOfLetter(String strSource, int iOffset) {
		char c;
		while (iOffset < strSource.length()) {
			c = strSource.charAt(iOffset);
			if (c > ' ')
				return iOffset;
			else
				iOffset++;
		}
		return -1;
	}

	// //////////////////////////////////////////////////////
	/**
	 * Return first position of space in String
	 * 
	 * @param strSource
	 *            String to search
	 * @param iOffset
	 *            start offset
	 * @return first position of space (unprintable character) in strSource, -1
	 *         if not found
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static int indexOfSpace(String strSource, int iOffset) {
		char c;
		while (iOffset < strSource.length()) {
			c = strSource.charAt(iOffset);
			if (c > ' ')
				iOffset++;
			else
				return iOffset;
		}
		return -1;
	}

	// //////////////////////////////////////////////////////
	/**
	 * Return number occurence of symbol in a string
	 * 
	 * @param strSource
	 *            String to search
	 * @param chrSymbol
	 *            symbol to count
	 * @param iOffset
	 *            start offset
	 * @return number occurence of symbol in strSource
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static int countSymbol(String strSource, String chrSymbol, int iOffset) {
		if (chrSymbol == null || chrSymbol.length() == 0)
			return 0;
		int iCount = 0;
		while ((iOffset = strSource.indexOf(chrSymbol, iOffset) + 1) > 0)
			iCount++;
		return iCount;
	}

	// //////////////////////////////////////////////////////
	/**
	 * Convert a string to string array base on separator
	 * 
	 * @param strSource
	 *            String to convert
	 * @param strSeparator
	 *            separator symbol
	 * @return String array after convert
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String[] toStringArray(String strSource, String strSeparator) {
		Vector vtReturn = toStringVector(strSource, strSeparator);
		String[] strReturn = new String[vtReturn.size()];
		for (int iIndex = 0; iIndex < strReturn.length; iIndex++)
			strReturn[iIndex] = (String) vtReturn.elementAt(iIndex);
		return strReturn;
	}

	// //////////////////////////////////////////////////////
	/**
	 * Convert a string to string array base on separator
	 * 
	 * @param strSource
	 *            String to convert
	 * @param strSeparator
	 *            separator symbol
	 * @return String array after convert
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static Vector toStringVector(String strSource, String strSeparator) {
		Vector vtReturn = new Vector();
		int iIndex = 0;
		int iLastIndex = 0;
		while ((iIndex = strSource.indexOf(strSeparator, iLastIndex)) >= 0) {
			vtReturn.addElement(strSource.substring(iLastIndex, iIndex).trim());
			iLastIndex = iIndex + strSeparator.length();
		}
		if (iLastIndex <= strSource.length())
			vtReturn.addElement(strSource.substring(iLastIndex, strSource.length()).trim());
		return vtReturn;
	}

	// //////////////////////////////////////////////////////
	/**
	 * Convert a string to string array base on ',' symbol
	 * 
	 * @param strSource
	 *            String to convert
	 * @return String array after convert
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String[] toStringArray(String strSource) {
		return toStringArray(strSource, ",");
	}

	// //////////////////////////////////////////////////////
	/**
	 * Convert a string to string array base on ',' symbol
	 * 
	 * @param strSource
	 *            String to convert
	 * @return String array after convert
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static Vector toStringVector(String strSource) {
		return toStringVector(strSource, ",");
	}

	// //////////////////////////////////////////////////////
	/**
	 * Convert char form
	 * 
	 * @param strSource
	 *            String to convert
	 * @param strCharformSource
	 *            source charform
	 * @param strCharformDestination
	 *            destination charform
	 * @return String after convert
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String convertCharForm(String strSource, String strCharformSource, String strCharformDestination) {
		if (strSource == null)
			return null;
		int iLength = strSource.length();
		int iResult = 0;
		StringBuffer strReturn = new StringBuffer();
		for (int iIndex = 0; iIndex < iLength; iIndex++) {
			char c = strSource.charAt(iIndex);
			if ((iResult = strCharformSource.indexOf(c)) >= 0)
				strReturn.append(strCharformDestination.charAt(iResult));
			else
				strReturn.append(c);
		}
		return strReturn.toString();
	}

	// //////////////////////////////////////////////////////
	/**
	 * Convert an unicode string to tcvn string
	 * 
	 * @param strSource
	 *            String to convert
	 * @return String array after convert
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String unicodeToTCVN(String strSource) {
		return convertCharForm(strSource, CHARFORM_UNICODE, CHARFORM_TCVN);
	}

	// //////////////////////////////////////////////////////
	/**
	 * Convert an tcvn string to unicode string
	 * 
	 * @param strSource
	 *            String to convert
	 * @return String array after convert
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String tcvnToUnicode(String strSource) {
		return convertCharForm(strSource, CHARFORM_TCVN, CHARFORM_UNICODE);
	}

	// //////////////////////////////////////////////////////
	/**
	 * Clear all horn in unicode string
	 * 
	 * @param strSource
	 *            String to convert
	 * @return String array after convert
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String clearHornUnicode(String strSource) {
		return convertCharForm(strSource, CHARFORM_UNICODE, CHARFORM_NOHORN);
	}

	// //////////////////////////////////////////////////////
	/**
	 * Clear all horn in unicode string
	 * 
	 * @param strSource
	 *            String to convert
	 * @return String array after convert
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String clearHornTCVN(String strSource) {
		return convertCharForm(strSource, CHARFORM_TCVN, CHARFORM_NOHORN);
	}

	// //////////////////////////////////////////////////////
	/**
	 * Pronounce a number to vietnamese
	 * 
	 * @param lNumber
	 *            nuber to pronoun
	 * @return String contain sound of number
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static String pronounceVietnameseNumber(long lNumber) {
		String strUnit[] = new String[] { "", "nghìn", "triệu", "tỷ", "nghìn tỷ", "triệu tỷ", "nghìn triệu tỷ", "tỷ tỷ" };

		// Analyse the number to array
		byte btDecimalNumber[] = new byte[30];
		byte btDecimalCount = 0;
		boolean bNegative = (lNumber < 0);
		if (bNegative)
			lNumber = -lNumber;
		while (lNumber > 0) {
			byte btValue = (byte) (lNumber - 10 * (lNumber / 10));
			lNumber /= 10;
			btDecimalNumber[btDecimalCount++] = btValue;
		}

		// Pronounce array
		String strReturn = "";
		int iUnitIndex = 0;
		while (iUnitIndex < strUnit.length && iUnitIndex * 3 < btDecimalCount) {
			String str = pronounceVietnameseNumber(btDecimalNumber[iUnitIndex * 3],
					btDecimalNumber[iUnitIndex * 3 + 1], btDecimalNumber[iUnitIndex * 3 + 2],
					iUnitIndex * 3 + 2 < btDecimalCount);
			if (str.length() > 0) {
				if (strReturn.length() > 0)
					strReturn = str + " " + strUnit[iUnitIndex] + " " + strReturn;
				else
					strReturn = str + " " + strUnit[iUnitIndex];
			}
			iUnitIndex++;
		}
		if (bNegative)
			strReturn = "âm " + strReturn;
		return strReturn;
	}

	// //////////////////////////////////////////////////////
	/**
	 * Pronounce a number to vietnamese
	 * 
	 * @param bUnit
	 *            unit part
	 * @param bTen
	 *            tens part
	 * @param bHundred
	 *            hundred part
	 * @param
	 *            --if hundred part = 0
	 * @return String contain sound of number
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	private static String pronounceVietnameseNumber(byte bUnit, byte bTen, byte bHundred, boolean bMax) {
		// Return zero
		if (bUnit == 0 && bTen == 0 && bHundred == 0)
			return "";

		String strUnitSuffix[] = new String[] { "", "một", "hai", "ba", "tư", "lăm", "sáu", "bảy", "tám", "chín" };
		String strUnitTen[] = new String[] { "", "mười một", "mười hai", "mười ba", "mười bốn", "mười lăm", "mười sáu",
				"mười bảy", "mười tám", "mười chín" };
		String strUnit[] = new String[] { "", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín" };
		String strTenFirst[] = new String[] { "", "mười một", "hai mươi mốt", "ba mươi mốt", "bốn mươi mốt",
				"năm mươi mốt", "sáu mươi mốt", "bảy mươi mốt", "tám mươi mốt", "chín mươi mốt" };
		String strTen[] = new String[] { "", "mười", "hai mươi", "ba mươi", "bốn mươi", "năm mươi", "sáu mươi",
				"bảy mươi", "tám mươi", "chín mươi" };
		String strHundred[] = new String[] { "không trăm", "một trăm", "hai trăm", "ba trăm", "bốn trăm", "năm trăm",
				"sáu trăm", "bảy trăm", "tám trăm", "chín trăm" };
		String strReturn = "";

		if (bMax || bHundred > 0)
			strReturn = strHundred[bHundred];
		if (bTen > 0) {
			if (strReturn.length() > 0)
				strReturn += " ";
			if (bUnit > 0) {
				if (bTen == 1)
					strReturn += strUnitTen[bUnit];
				else {
					if (bUnit == 1)
						strReturn += strTenFirst[bTen];
					else
						strReturn += strTen[bTen] + " " + strUnitSuffix[bUnit];
				}
			} else
				strReturn += strTen[bTen];
		} else {
			if (bUnit > 0) {
				if (strReturn.length() > 0)
					strReturn += " linh " + strUnit[bUnit];
				else
					strReturn = strUnit[bUnit];
			}
		}
		return strReturn;
	}

	// //////////////////////////////////////////////////////
	/**
	 * @param str
	 *            String
	 * @param iAlignment
	 *            int
	 * @param iLength
	 *            int
	 * @return String
	 */
	// //////////////////////////////////////////////////////
	public static String align(String str, int iAlignment, int iLength) {
		if (str == null)
			return null;
		if (str.length() > iLength)
			return str.substring(0, iLength);
		StringBuffer buf = new StringBuffer();
		if (iAlignment == ALIGN_CENTER) {
			int iFirstLength = (iLength - str.length()) / 2;
			for (int iIndex = 0; iIndex < iFirstLength; iIndex++)
				buf.append(" ");
			buf.append(str);
			for (int iIndex = str.length() + iFirstLength; iIndex < iLength; iIndex++)
				buf.append(" ");
		} else if (iAlignment == ALIGN_RIGHT) {
			iLength = iLength - str.length();
			for (int iIndex = 0; iIndex < iLength; iIndex++)
				buf.append(" ");
			buf.append(str);
		} else {
			buf.append(str);
			for (int iIndex = str.length(); iIndex < iLength; iIndex++)
				buf.append(" ");
		}
		return buf.toString();
	}

	public static int compareVietnameseString(String o1, String o2) {
		return compareString(o1, o2, new Locale("vi"));
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

	public static int compareString(String strObj1, String strObj2, Locale locale) {
		final String DELIMITERS = "\\p{Cntrl}\\s\\p{Punct}\u0080-\u00BF\u2000-\uFFFF";
		Collator primary = null;
		Collator secondary = null;
		if (primary == null) {
			primary = Collator.getInstance(locale);
			secondary = (Collator) primary.clone();
			secondary.setStrength(Collator.SECONDARY);
		}

		int result;
		// String o1 = str1;
		// String o2 = str;
		String[] s1 = (" " + strObj1).split("[" + DELIMITERS + "]+");
		String[] s2 = (" " + strObj2).split("[" + DELIMITERS + "]+");
		for (int i = 1; i < s1.length && i < s2.length; i++) {
			result = secondary.compare(s1[i], s2[i]);
			if (result != 0) {
				return result;
			}
		}

		if (s1.length > s2.length) {
			return 1;
		} else if (s1.length < s2.length) {
			return -1;
		}

		for (int i = 1; i < s1.length; i++) {
			result = primary.compare(s1[i], s2[i]);
			if (result != 0) {
				return result;
			}

		}

		s1 = (strObj1 + " ").split("[^" + DELIMITERS + "]+");
		s2 = (strObj2 + " ").split("[^" + DELIMITERS + "]+");

		for (int i = 1; i < s1.length - 1 && i < s2.length - 1; i++) {
			result = primary.compare(s1[i], s2[i]);
			if (result != 0) {
				return result;
			}
		}

		result = primary.compare(s1[0], s2[0]);

		if (result != 0) {
			return result;
		}

		return primary.compare(strObj1, strObj2);
	}

	public static ArrayList<String> stringToArrayList(String strData, String chSplit) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			String[] arr = strData.split(chSplit);
			for (String string : arr) {
				list.add(string);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

    public static boolean  isEmpty(String str){
        if(str == null || str.equals("") || str.trim().length() <= 0){
            return true;
        }

        return false;
    }
	public static boolean isNumber(String value) {
    	try {
    		Long.parseLong(value);
    		return  true;
		}catch (Exception ex){
    		return false;
		}
	}
	public static String compound2Unicode(String str) {
    	if(str==null || str.isEmpty()) return "";

		str = str.replaceAll("\u0065\u0309", "\u1EBB"); //ẻ
		str = str.replaceAll("\u0065\u0301", "\u00E9"); //é
		str = str.replaceAll("\u0065\u0300", "\u00E8"); //è
		str = str.replaceAll("\u0065\u0323", "\u1EB9"); //ẹ
		str = str.replaceAll("\u0065\u0303", "\u1EBD"); //ẽ
		str = str.replaceAll("\u00EA\u0309", "\u1EC3"); //ể
		str = str.replaceAll("\u00EA\u0301", "\u1EBF"); //ế
		str = str.replaceAll("\u00EA\u0300", "\u1EC1"); //ề
		str = str.replaceAll("\u00EA\u0323", "\u1EC7"); //ệ
		str = str.replaceAll("\u00EA\u0303", "\u1EC5"); //ễ
		str = str.replaceAll("\u0079\u0309", "\u1EF7"); //ỷ
		str = str.replaceAll("\u0079\u0301", "\u00FD"); //ý
		str = str.replaceAll("\u0079\u0300", "\u1EF3"); //ỳ
		str = str.replaceAll("\u0079\u0323", "\u1EF5"); //ỵ
		str = str.replaceAll("\u0079\u0303", "\u1EF9"); //ỹ
		str = str.replaceAll("\u0075\u0309", "\u1EE7"); //ủ
		str = str.replaceAll("\u0075\u0301", "\u00FA"); //ú
		str = str.replaceAll("\u0075\u0300", "\u00F9"); //ù
		str = str.replaceAll("\u0075\u0323", "\u1EE5"); //ụ
		str = str.replaceAll("\u0075\u0303", "\u0169"); //ũ
		str = str.replaceAll("\u01B0\u0309", "\u1EED"); //ử
		str = str.replaceAll("\u01B0\u0301", "\u1EE9"); //ứ
		str = str.replaceAll("\u01B0\u0300", "\u1EEB"); //ừ
		str = str.replaceAll("\u01B0\u0323", "\u1EF1"); //ự
		str = str.replaceAll("\u01B0\u0303", "\u1EEF"); //ữ
		str = str.replaceAll("\u0069\u0309", "\u1EC9"); //ỉ
		str = str.replaceAll("\u0069\u0301", "\u00ED"); //í
		str = str.replaceAll("\u0069\u0300", "\u00EC"); //ì
		str = str.replaceAll("\u0069\u0323", "\u1ECB"); //ị
		str = str.replaceAll("\u0069\u0303", "\u0129"); //ĩ
		str = str.replaceAll("\u006F\u0309", "\u1ECF"); //ỏ
		str = str.replaceAll("\u006F\u0301", "\u00F3"); //ó
		str = str.replaceAll("\u006F\u0300", "\u00F2"); //ò
		str = str.replaceAll("\u006F\u0323", "\u1ECD"); //ọ
		str = str.replaceAll("\u006F\u0303", "\u00F5"); //õ
		str = str.replaceAll("\u01A1\u0309", "\u1EDF"); //ở
		str = str.replaceAll("\u01A1\u0301", "\u1EDB"); //ớ
		str = str.replaceAll("\u01A1\u0300", "\u1EDD"); //ờ
		str = str.replaceAll("\u01A1\u0323", "\u1EE3"); //ợ
		str = str.replaceAll("\u01A1\u0303", "\u1EE1"); //ỡ
		str = str.replaceAll("\u00F4\u0309", "\u1ED5"); //ổ
		str = str.replaceAll("\u00F4\u0301", "\u1ED1"); //ố
		str = str.replaceAll("\u00F4\u0300", "\u1ED3"); //ồ
		str = str.replaceAll("\u00F4\u0323", "\u1ED9"); //ộ
		str = str.replaceAll("\u00F4\u0303", "\u1ED7"); //ỗ
		str = str.replaceAll("\u0061\u0309", "\u1EA3"); //ả
		str = str.replaceAll("\u0061\u0301", "\u00E1"); //á
		str = str.replaceAll("\u0061\u0300", "\u00E0"); //à
		str = str.replaceAll("\u0061\u0323", "\u1EA1"); //ạ
		str = str.replaceAll("\u0061\u0303", "\u00E3"); //ã
		str = str.replaceAll("\u0103\u0309", "\u1EB3"); //ẳ
		str = str.replaceAll("\u0103\u0301", "\u1EAF"); //ắ
		str = str.replaceAll("\u0103\u0300", "\u1EB1"); //ằ
		str = str.replaceAll("\u0103\u0323", "\u1EB7"); //ặ
		str = str.replaceAll("\u0103\u0303", "\u1EB5"); //ẵ
		str = str.replaceAll("\u00E2\u0309", "\u1EA9"); //ẩ
		str = str.replaceAll("\u00E2\u0301", "\u1EA5"); //ấ
		str = str.replaceAll("\u00E2\u0300", "\u1EA7"); //ầ
		str = str.replaceAll("\u00E2\u0323", "\u1EAD"); //ậ
		str = str.replaceAll("\u00E2\u0303", "\u1EAB"); //ẫ
		str = str.replaceAll("\u0045\u0309", "\u1EBA"); //Ẻ
		str = str.replaceAll("\u0045\u0301", "\u00C9"); //É
		str = str.replaceAll("\u0045\u0300", "\u00C8"); //È
		str = str.replaceAll("\u0045\u0323", "\u1EB8"); //Ẹ
		str = str.replaceAll("\u0045\u0303", "\u1EBC"); //Ẽ
		str = str.replaceAll("\u00CA\u0309", "\u1EC2"); //Ể
		str = str.replaceAll("\u00CA\u0301", "\u1EBE"); //Ế
		str = str.replaceAll("\u00CA\u0300", "\u1EC0"); //Ề
		str = str.replaceAll("\u00CA\u0323", "\u1EC6"); //Ệ
		str = str.replaceAll("\u00CA\u0303", "\u1EC4"); //Ễ
		str = str.replaceAll("\u0059\u0309", "\u1EF6"); //Ỷ
		str = str.replaceAll("\u0059\u0301", "\u00DD"); //Ý
		str = str.replaceAll("\u0059\u0300", "\u1EF2"); //Ỳ
		str = str.replaceAll("\u0059\u0323", "\u1EF4"); //Ỵ
		str = str.replaceAll("\u0059\u0303", "\u1EF8"); //Ỹ
		str = str.replaceAll("\u0055\u0309", "\u1EE6"); //Ủ
		str = str.replaceAll("\u0055\u0301", "\u00DA"); //Ú
		str = str.replaceAll("\u0055\u0300", "\u00D9"); //Ù
		str = str.replaceAll("\u0055\u0323", "\u1EE4"); //Ụ
		str = str.replaceAll("\u0055\u0303", "\u0168"); //Ũ
		str = str.replaceAll("\u01AF\u0309", "\u1EEC"); //Ử
		str = str.replaceAll("\u01AF\u0301", "\u1EE8"); //Ứ
		str = str.replaceAll("\u01AF\u0300", "\u1EEA"); //Ừ
		str = str.replaceAll("\u01AF\u0323", "\u1EF0"); //Ự
		str = str.replaceAll("\u01AF\u0303", "\u1EEE"); //Ữ
		str = str.replaceAll("\u0049\u0309", "\u1EC8"); //Ỉ
		str = str.replaceAll("\u0049\u0301", "\u00CD"); //Í
		str = str.replaceAll("\u0049\u0300", "\u00CC"); //Ì
		str = str.replaceAll("\u0049\u0323", "\u1ECA"); //Ị
		str = str.replaceAll("\u0049\u0303", "\u0128"); //Ĩ
		str = str.replaceAll("\u004F\u0309", "\u1ECE"); //Ỏ
		str = str.replaceAll("\u004F\u0301", "\u00D3"); //Ó
		str = str.replaceAll("\u004F\u0300", "\u00D2"); //Ò
		str = str.replaceAll("\u004F\u0323", "\u1ECC"); //Ọ
		str = str.replaceAll("\u004F\u0303", "\u00D5"); //Õ
		str = str.replaceAll("\u01A0\u0309", "\u1EDE"); //Ở
		str = str.replaceAll("\u01A0\u0301", "\u1EDA"); //Ớ
		str = str.replaceAll("\u01A0\u0300", "\u1EDC"); //Ờ
		str = str.replaceAll("\u01A0\u0323", "\u1EE2"); //Ợ
		str = str.replaceAll("\u01A0\u0303", "\u1EE0"); //Ỡ
		str = str.replaceAll("\u00D4\u0309", "\u1ED4"); //Ổ
		str = str.replaceAll("\u00D4\u0301", "\u1ED0"); //Ố
		str = str.replaceAll("\u00D4\u0300", "\u1ED2"); //Ồ
		str = str.replaceAll("\u00D4\u0323", "\u1ED8"); //Ộ
		str = str.replaceAll("\u00D4\u0303", "\u1ED6"); //Ỗ
		str = str.replaceAll("\u0041\u0309", "\u1EA2"); //Ả
		str = str.replaceAll("\u0041\u0301", "\u00C1"); //Á
		str = str.replaceAll("\u0041\u0300", "\u00C0"); //À
		str = str.replaceAll("\u0041\u0323", "\u1EA0"); //Ạ
		str = str.replaceAll("\u0041\u0303", "\u00C3"); //Ã
		str = str.replaceAll("\u0102\u0309", "\u1EB2"); //Ẳ
		str = str.replaceAll("\u0102\u0301", "\u1EAE"); //Ắ
		str = str.replaceAll("\u0102\u0300", "\u1EB0"); //Ằ
		str = str.replaceAll("\u0102\u0323", "\u1EB6"); //Ặ
		str = str.replaceAll("\u0102\u0303", "\u1EB4"); //Ẵ
		str = str.replaceAll("\u00C2\u0309", "\u1EA8"); //Ẩ
		str = str.replaceAll("\u00C2\u0301", "\u1EA4"); //Ấ
		str = str.replaceAll("\u00C2\u0300", "\u1EA6"); //Ầ
		str = str.replaceAll("\u00C2\u0323", "\u1EAC"); //Ậ
		str = str.replaceAll("\u00C2\u0303", "\u1EAA"); //Ẫ

		str = str.replaceAll("ỳ", "y"); //xu ly chu y
		str = str.replaceAll("Ỳ", "Y"); //xu ly chu y
		str = str.replaceAll("ỹ", "y"); //xu ly chu y
		str = str.replaceAll("Ỹ", "Y"); //xu ly chu y
		str = str.replaceAll("ỵ", "y"); //xu ly chu y
		str = str.replaceAll("Ỵ", "Y"); //xu ly chu y
		str = str.replaceAll("ỷ", "y"); //xu ly chu y
		str = str.replaceAll("Ỷ", "Y"); //xu ly chu y
		return str;
	}


}
