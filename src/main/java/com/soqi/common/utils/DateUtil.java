package com.soqi.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能说明: Date工具类<br>
 * 系统版本: v1.0<br>
 * 开发人员: @author yejg<br>
 * 开发时间: 2015年8月3日<br>
 */
public class DateUtil extends DateUtils {

	private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_NO_DELIMITER = "yyyyMMdd";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String TIME_FORMAT_NO_SEC = "HH:mm";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
	public static final String DATE_TIME_FORMAT_NO_SEC = "yyyy-MM-dd HH:mm";
	public static final String DATE_TIME_FORMAT_NO_DELIMITER = "yyyyMMddHHmmss";
	public static final String DATE_PRECISE_TO_MINUTE = "yyyyMMddHHmm";
	public static final String[] WEEKS={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
	public static final String[] DEFAULT_FORMATS = {TIMESTAMP_FORMAT, DATE_TIME_FORMAT, DATE_FORMAT, TIME_FORMAT, DATE_PRECISE_TO_MINUTE};

	/**
	 * 自动判断日期字符串的格式，返回Date对象
	 * @param dateString 日期字符串
	 * @param dateFormat 格式字符串数组。为空时使用<code>DateUtil.DEFAULT_FORMATS</code>
	 * @see DateUtils#parseDate
	 * @return 日期Date对象
	 * @throws ParseException
	 */
	public static Date parse(String dateString, String ... dateFormat) throws ParseException {
		if (StringUtils.isEmpty(dateString)) {
			return null;
		}

		if (dateFormat == null || dateFormat.length == 0) {
			return DateUtils.parseDate(dateString, DEFAULT_FORMATS);
		} else {
			return DateUtils.parseDate(dateString, dateFormat);
		}
	}

	/**
	 * 取指定格式的当前时间字符串
	 * @param dateFormat
	 * @return
	 */
	public static String getCurrentTime(String dateFormat) {
		Date date = new Date();
		return format(date, dateFormat);
	}

	/**
	 * 将字符串转换成Date类型
	 * @param dateString
	 * @param dateFormat
	 * @return
	 */
	public static Date parse(String dateString, String dateFormat) {
		if (StringUtils.isEmpty(dateString)) {
			return null;
		}
		try {
			return DateUtils.parseDate(dateString, dateFormat);
		} catch (ParseException e) {
			logger.error("日期格式化异常", e);
			return null;
		}
	}

	/**
	 * 将Date类型转化成字符串
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String format(Date date, String dateFormat) {
		if (date == null) {
			return "";
		} else {
			return DateFormatUtils.format(date, dateFormat);
		}
	}

	/**
	 * 在传入的日期基础上往后加n天
	 * @param date
	 * @param n 要加的天数
	 * @return
	 */
	public static Date addDay(Date date, int n) {
		return DateUtils.addDays(date, n);
	}

	/**
	 * 判断当前时间是否在开始时间与结束时间之间
	 * @param time 当前时间
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @return boolen类型，true表示在两者间，false表示不在两者之间
	 */
	public static boolean isTimeIn(Date time, Date begin, Date end) {
		return time.getTime() >= begin.getTime() && time.getTime() <= end.getTime();
	}

	/**
	 * 判断指定日期是星期几
	 * @param time 要判断的日期
	 * @param format 输入的日期格式
	 * @return 返回数字[1:星期一，2：星期二，....，7：星期日]
	 * @throws ParseException
	 */
	public static int getWeek(String time, String format) throws ParseException {
		return getWeek(DateUtils.parseDate(time, format));
	}

	/**
	 * 判断指定日期是星期几
	 * @param Date 要判断的日期
	 * @return 返回数字[1:星期一，2：星期二，....，7：星期日]
	 * @throws ParseException
	 */
	public static int getWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int week = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			week = 7;
		} else {
			week = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return week;
	}

	/**
	 * 判断是否为有效的身份证日期
	 * @param date
	 * @return
	 */
	public static boolean isIdDate(String date) {
		return isDateFormat(date, "yyyyMMdd");
	}

	/**
	 * 判断传入的字符串dateStr是否是日期格式patternStr的字符串 @author yejg
	 * @param dateStr
	 * @param patternStr
	 * @return
	 */
	public static boolean isDateFormat(String dateStr, String patternStr) {
		Date date = null;
		try {
			date = parse(dateStr, patternStr);
		} catch (Exception e) {}

		return date == null ? false : true;
	}

	/**
	 * 将字符串日期转成Timestamp类型
	 * @param dateString 字符串类型的时间
	 * @param format 字符串类型的时间要转换的格式
	 * @return Timestamp类型的时间戳
	 * @throws ParseException
	 */
	public static java.sql.Timestamp parse2Timestamp(String dateString, String format) throws ParseException {
		return new java.sql.Timestamp(DateUtils.parseDate(dateString, format).getTime());
	}

	public static void main(String args[]){
		try {
			System.out.println(parse2Timestamp(getCurrentTime(DATE_TIME_FORMAT_NO_DELIMITER),DATE_TIME_FORMAT_NO_DELIMITER));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获取两个时间的间隔,字符串表示
	 * @author huadi
	 * @param start
	 * @param end
	 * @return
	 */
	public static String getDiffTimeStr(Date start, Date end) {
		String time = "";
		if (start != null && end != null) {
			int t = (int)(end.getTime() - start.getTime()) / 1000;
			String h = "";
			String m = "";
			String s = "";
			h = (int)t / 3600 + "";
			m = (int)(t % 3600) / 60 + "";
			s = t % 60 + "";
			if (h.length() <= 1) {
				h = "0" + h;
			}
			if (m.length() <= 1) {
				m = "0" + m;
			}
			if (s.length() <= 1) {
				s = "0" + s;
			}
			time = h + ":" + m + ":" + s;
		}
		return time;
	}

	/**
	 * 获取两个日期之间间隔的分钟数
	 * @author zhougz
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getIntervalMinute(Date startDate, Date endDate) {
		int min = 0;
		if (null != startDate && null != endDate) {
			long end = endDate.getTime();
			long start = startDate.getTime();
			long betweenDate = (end - start) / (60 * 1000);
			min = Long.valueOf(betweenDate).intValue();
		}
		return min;
	}

	/**
	 * 获取两个日期之间间隔的天数
	 * @author sunyy
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getIntervalDay(Date start_date, Date end_date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			start_date = sdf.parse(sdf.format(start_date));
			end_date = sdf.parse(sdf.format(end_date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(start_date);
		long time1 = cal.getTimeInMillis();
		cal.setTime(end_date);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2-time1)/(1000*3600*24);
		return Integer.parseInt(String.valueOf(between_days));
	}
	
	
	/**
	 * 根据类型参数返回不同的日期
	 * @param type
	 *        <pre>
	 *  today:当天
	 *  yesterday：前一天
	 *  less7：前6天 （近7天）
	 *  less30：前29天 （近30天）
	 *  all：前29年 （近30年）
	 * </pre>
	 * @author zhouzc
	 * @return 返回yyyy-MM-dd格式字符串
	 */
	public static String getSpecifiedDay(String type) {
		String time = "";
		if ("today".equals(type)) {
			time = getCurrentTime(DATE_FORMAT);
		} else if ("yesterday".equals(type)) {
			Date yesterday = addDay(new Date(), -1);
			time = format(yesterday, DATE_FORMAT);
		} else if ("less7".equals(type)) {
			Date yesterday = addDay(new Date(), -6);
			time = format(yesterday, DATE_FORMAT);
		} else if ("less30".equals(type)) {
			Date yesterday = addDay(new Date(), -29);
			time = format(yesterday, DATE_FORMAT);
		} else if ("all".equals(type)) {
			// 取全部就设置截至时间为30年以前
			Date yesterday = addYears(new Date(), -29);
			time = format(yesterday, DATE_FORMAT);
		}
		return time;
	}
	
	/**
	 * 星期转换为星期索引
	 * @author guanhui
	 * @param week
	 * @return
	 */
	public static int weekToNum(String week){
		int weekNum=-1;
		for(int i=0,j=WEEKS.length;i<j;i++){
			if(week!=null && WEEKS[i].toLowerCase().contains(week.toLowerCase())){
				weekNum=i+1;
				break;
			}
		}
		return weekNum;
	}
	
	/**获取时间秒数
	 * @param date
	 * @return
	 */
	public static int getSecondTimestampTwo(Date date){
		if(null == date){
			return 0;
		}
		String timestamp = String.valueOf(date.getTime()/1000);
		return Integer.valueOf(timestamp);
	}
}
