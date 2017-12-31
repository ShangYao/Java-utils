package com.jinanlongen.flume.interceptors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * get ResourceBundle
 * 
 * @author shangyao
 * @date 2017年11月17日
 * @scope test
 */
public class GetProp {
	public static ResourceBundle getARTDBprops() {
		File artdbfile = new File("D:\\flumes\\conf\\ARTD_B.properties");
		return getprops(artdbfile);
	}

	public static ResourceBundle getARTDWprops() {
		File artdwfile = new File("D:\\flumes\\conf\\ARTD_W.properties");
		return getprops(artdwfile);
	}

	public static ResourceBundle getARTDBIDprops() {
		File artdbfile = new File("D:\\flumes\\conf\\ARTD_B_id.properties");
		return getprops(artdbfile);
	}

	public static ResourceBundle getARTDWIDprops() {
		File artdwfile = new File("D:\\flumes\\conf\\ARTD_W_id.properties");
		return getprops(artdwfile);
	}

	public static ResourceBundle getCOSMOIDprops() {
		// File file = new File("D:/flumes/conf/COSMO_id.properties");
		File file = new File("D:\\flumes\\conf\\COSMO_id.properties");
		return getprops(file);
	}

	public static ResourceBundle getCOSMOprops() {
		// File file = new File("D:/flumes/conf/COSMO.properties");
		File file = new File("D:\\flumes\\conf\\COSMO.properties");
		return getprops(file);
	}

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
