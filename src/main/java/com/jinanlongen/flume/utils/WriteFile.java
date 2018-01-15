package com.jinanlongen.flume.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年11月9日
 */
public class WriteFile {
	public void write(File outfile, String line) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(outfile, true);
			out.write(line.getBytes());
			out.write("\r\n".getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void mkdir() {
		String strPath = "E:\\a\\aa\\aaa.txt";
		File file = new File(strPath);
		File fileParent = file.getParentFile();
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String changeFileName(String filename, String idnumber, String basebath, String sign) {
		File file = new File(basebath + filename);
		String newfilename = null;
		if (file.exists()) {
			SimpleDateFormat df = new SimpleDateFormat("HHmmss");// 设置日期格式
			String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
			newfilename = idnumber + "_" + date + sign + filename.substring(filename.lastIndexOf('.'));

			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DATE);
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);

			File newFile = new File(basebath + year + "/" + month + "/" + day + "/" + newfilename);
			System.out.println(newFile.getAbsolutePath());
			// File newFile = new File(strPath);
			File fileParent = newFile.getParentFile();
			if (!fileParent.exists()) {
				fileParent.mkdirs();
			}
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			file.renameTo(newFile);
		}
		return newfilename;

	}

	public static void downOne(String sourceface) {
		try {
			String id = URLEncoder.encode("身份证", "UTF-8");
			String year = URLEncoder.encode("年", "UTF-8");
			String month = URLEncoder.encode("月", "UTF-8");
			String day = URLEncoder.encode("日", "UTF-8");
			// String blank = URLEncoder.encode(" ", "UTF-8");
			String filename = URLEncoder.encode(sourceface.substring(sourceface.lastIndexOf("/") + 1), "UTF-8");
			String face = sourceface.replace("身份证", id);
			face = face.replace("年", year);
			face = face.replace("月", month);
			face = face.replace("日", day);
			// face = face.replace(" ", blank);
			face = face.replace(sourceface.substring(sourceface.lastIndexOf("/") + 1), filename);
			face = face.replace("~", "");
			String preUrl = "http://192.168.200.62:6630";

			String path = "E:/idcard" + sourceface.substring(0, sourceface.lastIndexOf("/"));
			path = path.replace("年", "");
			path = path.replace("月", "");
			path = path.replace("日", "");
			path = path.replace(sourceface.substring(sourceface.lastIndexOf("/") + 1), "");
			path = path.replace("~", "");

			String fileName = sourceface.substring(sourceface.lastIndexOf("/") + 1);
			if (fileName.contains("反面")) {
				fileName = fileName.replace("反面", "_b");
			} else {
				fileName = fileName.replace("正面", "_f");
			}
			if (!path.endsWith("/")) {

				path += "/";

			}
			System.out.println(path + fileName);

			// saveUrlAs((preUrl + face).replace("+", "%20"), path, fileName.replace(" ",
			// ""));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println(sourceface);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		File excelFile = new File("D:/idcard2.xlsx");
		ArrayList<ArrayList<Object>> list = ReadExcle.readExcel(excelFile);
		for (ArrayList<Object> arrayList : list) {
			String face = (String) arrayList.get(0);
			String back = (String) arrayList.get(1);
			if (face.contains("UpFiles")) {
				downOne(face);
				downOne(back);
			}
		}

	}

	/**
	 * @功能 下载临时素材接口
	 * @param filePath
	 *            文件将要保存的目录
	 * @param method
	 *            请求方法，包括POST和GET
	 * @param url
	 *            请求的路径
	 * @return
	 */

	public static File saveUrlAs(String url, String filePath, String filename) {
		// System.out.println("fileName---->"+filePath);
		// 创建不同的文件夹目录
		File file = new File(filePath);
		// 判断文件夹是否存在
		if (!file.exists()) {
			// 如果文件夹不存在，则创建新的的文件夹
			file.mkdirs();
		}
		FileOutputStream fileOut = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		try {
			// 建立链接
			URL httpUrl = new URL(url);
			conn = (HttpURLConnection) httpUrl.openConnection();
			// 以Post方式提交表单，默认get方式
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			// post方式不能使用缓存
			conn.setUseCaches(false);
			// 连接指定的资源
			conn.connect();
			// 获取网络输入流
			inputStream = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			// 判断文件的保存路径后面是否以/结尾
			if (!filePath.endsWith("/")) {

				filePath += "/";

			}
			// 写入到文件（注意文件保存路径的后面一定要加上文件的名称）
			fileOut = new FileOutputStream(filePath + filename);
			BufferedOutputStream bos = new BufferedOutputStream(fileOut);

			byte[] buf = new byte[4096 * 2];
			int length = bis.read(buf);
			// System.out.println(length);
			// 保存文件
			while (length != -1) {
				bos.write(buf, 0, length);
				length = bis.read(buf);
			}
			bos.close();
			bis.close();
			conn.disconnect();
		} catch (Exception e) {
			System.out.println(url + "----" + filePath + "-----" + filename);
			e.printStackTrace();
		}

		return file;

	}
}
