package com.jinanlongen.flume.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年11月16日
 */
public class IOTest {
	public static void main(String[] args) {
		File file = new File("D:\\a.txt");
		try {

			FileInputStream fi = new FileInputStream(file);

			BufferedReader input = new BufferedReader(new InputStreamReader(fi));

			String dtr = input.readLine();
			System.out.println(dtr);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
