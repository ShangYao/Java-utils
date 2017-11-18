package com.jinanlongen.flume.utils;

/**  
*   one utils to read csv file
* @author shangyao  
* @date 2017年11月8日  
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class Readcsv {

	public List<String[]> redcsv(File sourcefile) {
		FileReader fReader = null;
		CSVReader csvReader = null;
		List<String[]> list = null;
		try {
			fReader = new FileReader(sourcefile);
			csvReader = new CSVReader(fReader);
			list = csvReader.readAll();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fReader != null)
					fReader.close();
				if (csvReader != null)
					csvReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

}
