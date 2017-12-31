package com.jinanlongen.pinyin;

import java.util.regex.Pattern;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年12月19日
 */
public class ZZtest {
	public static void main(String[] args) {
		// String pattern = "[\u4e00-\u9fa5\\w]+";
		String str = "1233445";
		String str1 = "汉语拼音";
		String str2 = "hanyupinyin";
		System.out.println(isHanZi(str1));
		System.out.println(isInteger(str));
		System.out.println(isZiMu(str2));
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isZiMu(String str) {
		String regex = "^\\w+$";
		return str.matches(regex);
	}

	public static boolean isNumeric2(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static boolean vd(String str) {

		char[] chars = str.toCharArray();
		boolean isGB2312 = false;
		for (int i = 0; i < chars.length; i++) {
			byte[] bytes = ("" + chars[i]).getBytes();
			if (bytes.length == 2) {
				int[] ints = new int[2];
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;

				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40 && ints[1] <= 0xFE) {
					isGB2312 = true;
					break;
				}
			}
		}
		return isGB2312;
	}

	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static boolean isHanZi(String str1) {
		String reg = "[\\u4e00-\\u9fa5]+";
		return str1.matches(reg);
	}
}
