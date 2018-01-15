package com.jinanlongen.flume.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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
			int a = 0;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				if (!"".equals(s)) {
					File newFile = new File(s);
					String filename = s.replace(".", "_C.");
					File f = new File(filename);

					if (!f.exists()) {
						System.out.println(filename);
						System.out.println(s + "--" + a++);
						// if (newFile.length() >= 200 * 1024) {
						// ImagesUtils.compressImage(s, filename, 50);
						// System.out.println(filename + "--" + "50--" + a++);
						//
						// } else if (newFile.length() < 200 * 1024 && newFile.length() >= 50 * 1024) {
						// ImagesUtils.compressImage(s, filename, 75);
						// System.out.println(filename + "--" + "75--" + a++);
						// } else {
						//
						// newFile.renameTo(f);
						// System.out.println(filename + "--" + "100--" + a++);
						// }

					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static void main(String[] args) {
		File file = new File("D:/allFile.txt");
		txt2String(file);
		// System.out.println(txt2String(file));
	}
}
