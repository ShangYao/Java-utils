package com.jinanlongen.pgsql;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jinanlongen.flume.utils.ReadExcle;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年11月24日
 */
public class PgTest {
	public static void main(String[] args) {
		res();
	}

	public static void write() {
		File sourcefile = new File("D:\\店铺app信息.xls");
		ArrayList<ArrayList<Object>> list = ReadExcle.readExcel(sourcefile);

		Connection connection = PgConnect.getPgConnect();
		String sql = "insert into shop values(?,?,?,?,?)";
		try {
			PreparedStatement pst = connection.prepareStatement(sql);
			for (ArrayList<Object> l : list) {
				// System.out.println(((String) l.get(0)).s);
				// pst.setString(1, ((String) l.get(0)).substring(0, 2));
				// pst.setString(2, (String) l.get(1));
				// pst.setString(3, (String) l.get(2));
				// pst.setString(4, (String) l.get(3));
				// pst.setString(5, (String) l.get(4));
				// boolean f = pst.execute();
				// System.out.println(f);
			}
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void res() {
		Connection connection = PgConnect.getPgConnect();
		String sql = "select shop_id,name,app_key,app_secret,access_token from shop";
		try {
			PreparedStatement pst = connection.prepareStatement(sql);
			ResultSet re = pst.executeQuery();
			while (re.next()) {
				System.out.println(re.getInt(1));
				System.out.println(re.getString(2));
			}
			connection.close();
			re.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
