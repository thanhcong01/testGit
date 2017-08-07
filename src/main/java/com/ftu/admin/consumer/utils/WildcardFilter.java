package com.ftu.admin.consumer.utils;

import java.io.File;
import java.io.FilenameFilter;

public class WildcardFilter implements FilenameFilter {

	public static void main(String[] args) {
		System.out.println(WildcardFilter.match("10.*", "10.15.139.72"));
	}

	private String mstrWildcard;

	public WildcardFilter(String strWildcard) {
		mstrWildcard = strWildcard;
	}

	public static boolean match(String strWild, String strVal) {
		return match(strWild, strVal, true);
	}

	public static boolean match(String strWild, String strVal, boolean bIgnoreCase) {
		if (bIgnoreCase) {
			strVal = strVal.toUpperCase();
			strWild = strWild.toUpperCase();
		}

		int iWild = 0;
		int iVal = 0;
		int iWildCount = strWild.length();
		int iValCount = strVal.length();

		while ((iVal < iValCount) && (iWild < iWildCount)) {
			if ((strWild.charAt(iWild) == strVal.charAt(iVal)) || (strWild.charAt(iWild) == '?')) {
				iWild++;
				iVal++;
			} else {
				if (strWild.charAt(iWild) == '*') {
					iWild++;
					if (iWild >= iWildCount)
						return true;
					for (;;) {
						if ((iVal < iValCount) && (strWild.charAt(iWild) != strVal.charAt(iVal))) {
							iVal++;
						} else {
							if (iVal >= iValCount)
								return false;
							String strNewWild = strWild.substring(iWild, iWildCount);
							String strNewVal = strVal.substring(iVal, iValCount);
							if (match(strNewWild, strNewVal, bIgnoreCase)) {
								return true;
							}
							iVal++;
						}
					}
				}
				return false;
			}
		}
		if ((iVal >= iValCount) && ((iWild >= iWildCount) || (isOptional(strWild.substring(iWild, strWild.length()))))) {
			return true;
		}
		return false;
	}

	public static boolean isOptional(String str) {
		for (int iIndex = 0; iIndex < str.length(); iIndex++) {
			if (str.charAt(iIndex) != '*')
				return false;
		}
		return true;
	}

	public boolean accept(File dir, String name) {
		return match(mstrWildcard, name);
	}
}
