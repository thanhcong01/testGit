package com.ftu.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ConvertUtil {

	public static final String stringShortDateFormat = "dd/MM/yyyy";
	public static final String stringTimeFormat = "HH:MM:SS";
	public static final SimpleDateFormat simpleShortDateFormat = new SimpleDateFormat(stringShortDateFormat);
	public static final String stringDateFormat = "dd/MM/yyyy HH:mm:ss";
	public static final String stringSQLShortDateFormatSysDate = "yyyy-MM-dd";
	public static final String stringSQLDateFormatSysDate = "yyyy-MM-dd HH:mm:ss";
	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(stringDateFormat);

	public static String convertHourToDateAndHour(int hour) {

		int day = Math.round(hour / 24);
		int hour2 = hour % 24;

		String dateAndHour = "";

		if (day != 0) {
			dateAndHour = day + "d " + hour2 + "h";

		} else {
			dateAndHour = hour2 + "h";
		}

		return dateAndHour;
	}

	public static int convertDateAndHourToHour(int day, int hour) {
		int rs = 0;

		rs = day * 24 + hour;

		return rs;
	}

	public static String formatStringToHHmmss(int strHMS) {
		int h = strHMS / (3600);
		int m = (strHMS % (3600)) / 60;
		int s = (strHMS % (3600)) % 60;
		String strH = String.format("%02d", h);
		String strM = String.format("%02d", m);
		String strS = String.format("%02d", s);
		return strH + ":" + strM + ":" + strS;
	}

	public static Date timestampToDate(Timestamp stamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(stamp.getTime());
		return cal.getTime();
	}

	public static String DateToString(Date date, String strFormat) {
		if (date != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormat);
			return simpleDateFormat.format(date);
		} else
			return null;
	}

	public static Date StringToDate(String dateString, String strFormat) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormat);
			return simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
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

	public static Long stringHMSToMillis(String strHMS) {
		if (stringIsNullOrEmty(strHMS))
			return null;

		String[] arrStr = strHMS.split(":");
		if (arrStr.length == 3) {
			int h = Integer.parseInt(arrStr[0]);
			int m = Integer.parseInt(arrStr[1]);
			int s = Integer.parseInt(arrStr[2]);
			long mH = h * (60 * (60 * 1000));
			long mM = m * (60 * 1000);
			long mS = s * 1000;
			return (mH + mM + mS);
		} else
			return null;
	}

	public static String convertToUTF8(String val) {

		try {
			return new String(val.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	
	public static List<String> strCodeToList(String str) {
		List<String> list = new ArrayList<String>();
		String[] arrStr = str.split(",");
		for (String string : arrStr) {
			list.add(string);
		}
		return list;
	}

	public static String convertListToString(List<Object> list) {
		String data = "";
		if (list != null) {
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					if (i == list.size() - 1) {
						data += "'" + list.get(i) + "'";
						// data += list.get(i);
					} else {
						data += "'" + list.get(i) + "',";
						// data += list.get(i)+",";
					}
				}
			}
		}
		return data;
	}

	public static String listToString(List<String> list, String strSeparator) {
		String str = null;
		for (Object string : list) {
			if (str == null) {
				if (string instanceof BigDecimal) {
					str = String.valueOf(string);
				} else {
					str = string.toString();
				}
			} else {
				if (string instanceof BigDecimal) {
					str += strSeparator + String.valueOf(string);
				} else {
					str += strSeparator + string.toString();
				}
			}
		}
		return str;
	}

	public static String listToString(List<String> list, String strSeparator, String strChar) {
		String str = null;
		for (String string : list) {
			if (str == null) {
				str = strChar + string + strChar;
			} else {
				str += strSeparator + strChar + string + strChar;
			}
		}
		return str;
	}
}
