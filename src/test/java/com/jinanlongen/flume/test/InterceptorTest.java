package com.jinanlongen.flume.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

public class InterceptorTest {

	@Test
	public void test() {
		File file = new File("D:\\flumes\\erro.txt");
		String origBody = "hfahfh,hajfhds,hfafdsk,fahf,fhfh";
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(origBody.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
