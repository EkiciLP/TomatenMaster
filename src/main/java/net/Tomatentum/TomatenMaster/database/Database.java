package net.Tomatentum.TomatenMaster.database;

import net.Tomatentum.TomatenMaster.TomatenMaster;

import java.sql.*;

public class Database{

	private static final String database = TomatenMaster.getINSTANCE().getConfig().getYML().getString("db");
	private static final String user = TomatenMaster.getINSTANCE().getConfig().getYML().getString("dbuser");
	private static final String password = TomatenMaster.getINSTANCE().getConfig().getYML().getString("dbpassword");
	private static final String host = TomatenMaster.getINSTANCE().getConfig().getYML().getString("dbhost");

	private static Connection connection;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?useSSL=false", user, password);
		} catch (SQLException | ClassNotFoundException e) {
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
