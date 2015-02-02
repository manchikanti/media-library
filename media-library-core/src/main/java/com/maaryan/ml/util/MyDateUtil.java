package com.maaryan.ml.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MyDateUtil {
	private MyDateUtil() {
	}

	public static TimeZone IST = TimeZone.getTimeZone("IST");

	public static String getYearStr(Calendar cal) {
		return cal.get(Calendar.YEAR) + "";
	}

	public static String getMonthStr(Calendar cal) {
		int month = cal.get(Calendar.MONTH) + 1;
		return month > 9 ? "" + month : "0" + month;
	}

	public static String getDayStr(Calendar cal) {
		return cal.get(Calendar.DATE) > 9 ? "" + cal.get(Calendar.DATE) : "0"
				+ cal.get(Calendar.DATE);
	}

	public static Date parseDate(String dateFormat, String value) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			formatter.setTimeZone(IST);
			return formatter.parse(value);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String toString(Calendar modifiedTime, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		formatter.setTimeZone(IST);
		return formatter.format(modifiedTime.getTime());
	}
}
