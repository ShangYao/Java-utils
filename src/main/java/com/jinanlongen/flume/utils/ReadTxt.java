package com.jinanlongen.flume.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.lang.StringUtils;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年12月7日
 */
public class ReadTxt {
	/**
	 * 读取txt文件的内容
	 * 
	 * @param file
	 *            想要读取的文件对象
	 * @return 返回文件内容
	 */
	public static String txt2String(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				if (!"".equals(s)) {
					s = StringUtils.deleteWhitespace(s);
					System.out.println(s.substring(0, 6));
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static void main(String[] args) {
		File file = new File("D:/xzqh.txt");
		System.out.println(txt2String(file));
	}
}
