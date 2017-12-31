package com.jinanlongen.flume.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jinanlongen.pgsql.PgConnect;

/**
 * Read Excel File To Properties
 * 
 * @author shangyao
 * @date 2017年11月17日
 */
public class ReadExcelToProp {
	public static void main(String[] args) {
		getId();
		getCode();

	}

	public static String replacespace(String s) {
		return s.replace(" ", "\\ ");
	}

	public static void getCode() {
		File sourcefile = new File("D:\\zzz\\areatred品牌补充.xlsx");
		try {
			FileInputStream f = new FileInputStream(sourcefile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<ArrayList<Object>> list = ReadExcle.readExcel(sourcefile);
		for (ArrayList<Object> l : list) {
			String key = replacespace((String) l.get(0));
			String line = key + "=" + l.get(1);
			System.out.println(line);
		}
	}

	public static void getId() {
		Connection connection = PgConnect.getPgConnect();
		PreparedStatement prst = null;
		File sourcefile = new File("D:\\zzz\\areatred品牌补充.xlsx");
		String sql = "select id from brands where code=?";
		ArrayList<ArrayList<Object>> list = ReadExcle.readExcel(sourcefile);
		System.out.println(list.size());
		for (ArrayList<Object> l : list) {
			try {
				prst = connection.prepareStatement(sql);
				prst.setString(1, (String) l.get(1));
				// System.out.println(prst.toString());
				ResultSet resultSet = prst.executeQuery();
				String key = replacespace((String) l.get(0));
				while (resultSet.next()) {
					// System.out.println("source=" + l.get(0) + "code=" + l.get(1) + "id=" +
					// resultSet.getString("id"));
					String line = key + "=" + resultSet.getString("id");
					System.out.println(line);
				}
			} catch (SQLException e) {
				System.out.println("jjj");
				e.printStackTrace();
			}

		}

	}

}
