package com.shining3d.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @Date: 2014年3月10日 下午9:44:31<br>
 * @Copyright (c) 2014 udai.com <br> *
 * @since 1.0
 * @author coral
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

	private static final Logger  logger                                        = LoggerFactory.getLogger(DateUtils.class);

	public static final String   DATE_FORMAT_YEAR_MONTH_DAY                    = "yyyy-MM-dd";
	public static final String   DATE_FORMAT_HOUR_MINUTE_SECOND                = "HH:mm:ss";
	public static final String   DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";
	public static final String   DATE_FORMAT_PACK                              = "yyyyMMddHHmmss";
	public static final String[] DATE_FORMAT_PACK_ARR                          = { "yyyyMMddHHmmss" };
	public static final TimeZone DEFAULT_TIME_ZONE                             = TimeZone.getTimeZone("GMT+8");

	public static Date format(String dates) {
		Date d = null;
		try {
			d = new SimpleDateFormat(DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND).parse(dates);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	public static Timestamp now() {
		return new Timestamp(current().getTime());
	}


	public static Date current() {
		Calendar ca = Calendar.getInstance(DEFAULT_TIME_ZONE, Locale.CHINA);

		Date now;
		try {
			now = parseDate(DateFormatUtils.format(ca, DATE_FORMAT_PACK), DATE_FORMAT_PACK_ARR);

		} catch (ParseException e) {
			logger.error("获取东8区时间出错", e);
			now = new Date();
		}

		return now;
	}

	public static String formatNow(String format) {
		DateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

	public static String formatTime(Timestamp date, String format) {
		return formatTime(date.getTime(), format);
	}

	public static String formatTime(Date date, String format) {
		return formatTime(date.getTime(), format);
	}

	public static String formatTime(long timeInMills, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeInMills);
		return dateFormat.format(calendar.getTime());
	}

	public static Timestamp truncate(Timestamp date, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		Date retDate = truncate(calendar.getTime(), field);
		return new Timestamp(retDate.getTime());
	}

	/**
	 * ceil(t1-t2)
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static int dayDiff(Date t1, Date t2) {
		t1 = truncateTime(t1);
		t2 = truncateTime(t2);
		return (int) ((t1.getTime() - t2.getTime()) / (1000 * 60 * 60 * 24));
	}

	public static Date truncateTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
}
