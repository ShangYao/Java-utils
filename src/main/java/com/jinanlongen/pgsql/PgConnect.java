package com.jinanlongen.pgsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * postgresql connection
 * 
 * @author shangyao
 * @date 2017年11月18日
 */
public class PgConnect {
	public void pg() {
		Connection connection = null;
		Statement statement = null;
		try {
			String url = "jdbc:postgresql://192.168.200.152:5432/idcard";
			String user = "postgres";
			String password = "postgres";
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("是否成功连接pg数据库" + connection);
			String sql = "select name from users";
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String name = resultSet.getString(1);
				System.out.println(name);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static Connection getPgConnect() {
		Connection connection = null;
		String url = "jdbc:postgresql://192.168.200.152:5432/taz_production";
		String user = "taz";
		String password = "taz";
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
}
