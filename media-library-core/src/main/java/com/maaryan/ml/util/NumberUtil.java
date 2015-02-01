package com.maaryan.ml.util;

public class NumberUtil {
	private static String REGEX_CLEAN_NUMBER = "[\\s,]";

	public static double nthRoot(double x, int nth) {
		double nthRoot = Double.NaN;
		nthRoot = nthRootItr(1, x, nth);
		return nthRoot;
	}

	private static double nthRootItr(double guess, double x, int nth) {
		if (isGoodEnough(guess, x, nth))
			return guess;
		return nthRootItr(improve(guess, x, nth), x, nth);
	}

	private static boolean isGoodEnough(double guess, double x, int nth) {
		return Math.abs(Math.pow(guess, nth) - x) / x < 0.001;
	}

	private static double improve(double guess, double x, int nth) {
		return (guess + x / guess) / nth;
	}

	public static boolean isNumber(String value) {
		return value.matches("^(\\+|-)?([0-9]+(\\.[0-9]+))");
	}

	public static double parseDouble(String value) {
		double d;
		try {
			value = value.replaceAll(REGEX_CLEAN_NUMBER, "");
			d = Double.parseDouble(value);
			return roundDouble(d);
		} catch (Exception e) {
			return Double.NaN;
		}

	}

	public static double roundDouble(double d) {
		return roundDouble(d, 2);
	}

	public static double roundDouble(double d, int decimals) {
		long tempD = (long) (d * Math.pow(10, decimals));
		return tempD / (Math.pow(10, decimals));
	}

	public static long parseLong(String value) {
		long i;
		try {
			value = value.replaceAll(REGEX_CLEAN_NUMBER, "");
			i = Long.parseLong(value);
			return i;
		} catch (Exception e) {
			return Long.MIN_VALUE;
		}

	}

	public static double percentChange(double prev, double present) {
		double d = (present - prev) * 100 / prev;
		return roundDouble(d);
	}
}
