package net.Tomatentum.TomatenMaster.database;

import java.sql.*;

public class Database{

	private static final String database = "sus";
	private static final String user = "no";
	private static final String password = "you";

	private static Connection connection;

	static {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://116.202.92.16:3306/" + database + "?useSSL=false", user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Database() {}



	/**
	 *
	 * @param code SQL code to execute
	 * @return	The ResultSet or null if an error occurred
	 */

	public static ResultSet executeQuery(String code) {
		try {
		PreparedStatement statement = connection.prepareStatement(code);
		return statement.executeQuery(code);

		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * @param sql code which updates the database
	 * @return number of rows affected
	 */

	public static int executeUpdate(String sql) {
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			return statement.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return 0;
		}
	}

	public static void close() {
		try {
			if (!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
