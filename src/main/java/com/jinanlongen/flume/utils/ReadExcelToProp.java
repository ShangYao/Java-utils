package com.jinanlongen.flume.utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Read Excel File To Properties
 * 
 * @author shangyao
 * @date 2017年11月17日
 */
public class ReadExcelToProp {
	public static void main(String[] args) {
		File sourcefile = new File("D:\\zzz\\ARTD_W_品牌补充.xlsx");
		ArrayList<ArrayList<Object>> list = ReadExcle.readExcel(sourcefile);
		for (ArrayList<Object> l : list) {
			String key = replacespace((String) l.get(0));
			String line = key + "=" + l.get(1);
			System.out.println(line);
		}
	}

	public static String replacespace(String s) {
		return s.replace(" ", "\\ ");
	}

}
