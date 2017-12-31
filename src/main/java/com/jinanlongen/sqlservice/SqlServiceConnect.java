package com.jinanlongen.sqlservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年11月29日
 */
public class SqlServiceConnect {
	public static void main(String[] args) {
		// Connection connection = PgConnect.getPgConnect();
		Connection connection2 = getSqlServiceConnect();
		// String sql = "select shop_id,name,app_key,app_secret,access_token from shop";
		// String sql2 = "insert into shop(shop_id,name,app_key,app_secret,access_token)
		// values(?,?,?,?,?)";
		// Statement statement;
		// PreparedStatement pstm;
		// try {
		// statement = connection.createStatement();
		// ResultSet resultSet = statement.executeQuery(sql);
		// while (resultSet.next()) {
		// pstm = connection2.prepareStatement(sql2);
		// pstm.setString(1, resultSet.getString(1));
		// pstm.setString(2, resultSet.getString(2));
		// pstm.setString(3, resultSet.getString(3));
		// pstm.setString(4, resultSet.getString(4));
		// pstm.setString(5, resultSet.getString(5));
		// System.out.println(pstm.toString());
		// Boolean b = pstm.execute();
		// System.out.println(b);
		// }
		// connection.close();
		// connection2.close();
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	public static Connection getSqlServiceConnect() {
		Connection connection = null;
		Statement statement = null;
		String url = "jdbc:sqlserver://192.168.200.66:1433; DatabaseName=JD_test";
		String user = "NewPeople";
		String password = "cpz0410LHL";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("是否成功连接pg数据库" + connection);

			String sql = "select * from categorys";
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String name = resultSet.getString(1);
				System.out.println(name);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
}
