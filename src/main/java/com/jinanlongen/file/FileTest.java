package com.jinanlongen.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2018年1月6日
 */
public class FileTest {
	public static List<File> getFileList(String strPath) {
		File dir = new File(strPath);
		File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
		List filelist = new ArrayList();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String fileName = files[i].getName();
				if (files[i].isDirectory()) { // 判断是文件还是文件夹
					getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
				} else if (fileName.endsWith("avi")) { // 判断文件名是否以.avi结尾
					String strFileName = files[i].getAbsolutePath();
					System.out.println("---" + strFileName);
					filelist.add(files[i]);
				} else {
					continue;
				}
			}

		}
		return filelist;
	}

	public static List<String> traverseFolder2(String path) {

		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				// System.out.println("文件夹是空的!");
				return null;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						// System.out.println("文件夹:" + file2.getAbsolutePath());
						traverseFolder2(file2.getAbsolutePath());
					} else {
						String filename = file2.getAbsolutePath();
						if (!filename.contains("_C")) {
							filelist.add(filename);
						}
					}
				}
			}
		} else {
			// System.out.println("文件不存在!");
			return null;
		}
		// System.out.println(filelist.size());
		return filelist;
	}

	public static List<String> filelist = new ArrayList<String>();

	public static void main(String[] args) {
		List<String> filelist = traverseFolder2("E:\\idcard\\UpFiles\\身份证\\");
		System.out.println(filelist.size());
		int a = 0;
		for (String string : filelist) {
			System.out.println(string);
		}
	}
}
