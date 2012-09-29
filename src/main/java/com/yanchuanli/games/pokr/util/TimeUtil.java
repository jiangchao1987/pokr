package com.yanchuanli.games.pokr.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeUtil {

	public static int unixtime() {
        return (int) (System.currentTimeMillis() / 1000L);
    }
	
	public static int year() {
		return calGenerator(Calendar.YEAR);
	}

	public static int month() {
		return calGenerator(Calendar.MONTH) + 1;
	}

	public static int day() {
		return calGenerator(Calendar.DAY_OF_MONTH);
	}
	
	private static int calGenerator(int type) {
		GregorianCalendar g = new GregorianCalendar();
		return (int) g.get(type);
	}
	
}
