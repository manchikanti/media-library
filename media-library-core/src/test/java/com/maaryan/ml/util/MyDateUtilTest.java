package com.maaryan.ml.util;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.maaryan.ml.MediaLibraryTest;

public class MyDateUtilTest extends MediaLibraryTest {
	private static Calendar testDate = Calendar.getInstance(TimeZone.getTimeZone("IST"));
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testDate.clear();
		testDate.setTime(MyDateUtil.parseDate("yyyyMMdd", "19470815"));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testDate = null;
	}

	@Test
	public final void testGetYearStr() {
		Assert.assertTrue("1947".equals(MyDateUtil.getYearStr(testDate)));
	}

	@Test
	public final void testGetMonthStr() {
		Assert.assertTrue("08".equals(MyDateUtil.getMonthStr(testDate)));
	}

	@Test
	public final void testGetDayStr() {
		Assert.assertTrue("15".equals(MyDateUtil.getDayStr(testDate)));
	}

	@Test
	public final void testToStringCalendarString() {
		Assert.assertTrue("19470815".equals(MyDateUtil.toString(testDate,"yyyyMMdd")));
	}

}
