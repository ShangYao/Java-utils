package com.jinanlongen.flume.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
}
