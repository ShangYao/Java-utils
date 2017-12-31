package com.jinanlongen.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年11月22日
 */
public class TimeTest {
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());

		System.out.println(stampToDate("1512368815661"));
		System.out.println(stampToDate("1511670832950"));
		Date d = new Date();
		System.out.println(d.toString());
		System.out.println(d.getTime());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(simpleDateFormat.format(d));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

		String s = "1 3, ";
		String[] str = s.split(",");
		for (String ss : str) {
			System.out.println(ss);
		}
	}

	/*
	 * 将时间转换为时间戳
	 */
	public static String dateToStamp(String s) throws ParseException {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}

	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}
}
