package com.jinanlongen.flume.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * read properties
 * 
 * @author shangyao
 * @date 2017年11月11日
 */
public class ReadProps {

	/**
	 * 获取指定配置文件中所以的数据
	 * 
	 * @param propertyName
	 *            调用方式： 1.配置文件放在resource源包下，不用加后缀
	 *            PropertiesUtil.getAllMessage("message"); 2.放在包里面的
	 *            PropertiesUtil.getAllMessage("com.test.message");
	 * @return
	 */
	public static List<String> getAllMessage(String propertyName) {
		// 获得资源包
		ResourceBundle rb = ResourceBundle.getBundle(propertyName.trim());
		System.out.println("rb=" + rb.toString());
		System.out.println("rb=" + rb.containsKey("name"));

		// 通过资源包拿到所有的key
		Enumeration<String> allKey = rb.getKeys();
		// 遍历key 得到 value
		List<String> valList = new ArrayList<String>();
		while (allKey.hasMoreElements()) {
			String key = allKey.nextElement();
			String value = rb.getString(key);
			valList.add(value);
		}
		return valList;
	}

	/**
	 * 
	 * 
	 * @param file
	 *            流方式读取配置文件
	 * @return ResourceBundle
	 */
	public static ResourceBundle getprops(File file) {
		InputStream inStream = null;
		ResourceBundle resource = null;
		// 获得资源包
		try {
			inStream = new FileInputStream(file);
			resource = new PropertyResourceBundle(inStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inStream != null)
					inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resource;

	}
}
