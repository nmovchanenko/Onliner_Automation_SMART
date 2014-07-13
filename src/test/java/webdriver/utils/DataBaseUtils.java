package webdriver.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import webdriver.BaseEntity;
import webdriver.PropertiesResourceManager;

/**
 * Describes functions for working with Data Bases.
 */
public class DataBaseUtils extends BaseEntity {

	private static PropertiesResourceManager props = new PropertiesResourceManager("database.properties");

	private String driverName;
	private String ip;
	private String port;
	private String baseName;
	private String serverUrl;
	private String userName;
	private String password;
	private Connection connection;

	/**
	 * Constructor.
	 */
	public DataBaseUtils() {
		initializeDB();
		connection = connectToDb(userName, password);
	}

	/**
	 * Variables initialization.
	 */
	private void initializeDB() {
		driverName = props.getProperty("driverName");
		ip = props.getProperty("ip");
		port = props.getProperty("port");
		baseName = props.getProperty("baseName");
		String serverUrlProp = props.getProperty("serverUrl");
		serverUrl = String.format(serverUrlProp, ip, port, baseName);
		userName = props.getProperty("userName");
		password = props.getProperty("password");
	}

	/**
	 * Connects to DB
	 * @param user User
	 * @param pass Password
	 * @return Connection connection to database
	 */
	private Connection connectToDb(final String user, final String pass) {
		Connection connect = null;
		try {
			// Load the JDBC driver
			Class.forName(driverName);
			// Create a connection to the database
			connect = DriverManager.getConnection(serverUrl, user, pass);
		} catch (ClassNotFoundException e) {
			error("Unable to connect to the Database " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			error("Unable to connect to the Database " + e.getMessage());
			e.printStackTrace();
		}
		info("DB connection established");
		return connect;
	}

	/**
	 * Refreshes connection.
	 */
	private void refreshConnection() {
		try {
			if (connection.isClosed()) {
				info("Connection was closed. Connecting...");
				connection = connectToDb(userName, password);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Performs request.
	 * @param request Request.
	 * @return ResultSet results of request
	 */
	public ResultSet doRequest(final String request){
		ResultSet result = null;
		refreshConnection();
		try {
			Statement statement = connection.createStatement();
			result = statement.executeQuery(request);

		} catch (SQLException e) {
			fatal(e.getMessage());
		}

		if (result == null) {
			fatal("Database error. Used request: " + request);
		}
		return result;
	}

	/**
	 * Performs query which may be an INSERT, UPDATE, or DELETE statement.
	 * @param query Query
	 * @return Integer result
	 */
	public int doUpdate(final String query){
		int result = 0;
		refreshConnection();
		try {
			Statement statement = connection.createStatement();
			result = statement.executeUpdate(query);

		} catch (SQLException e) {
			fatal(e.getMessage());
		}
		return result;
	}

	@Override
	protected String formatLogMsg(final String message) {
		String formattedMessage = String.format("Database: '%1$s'.", message);
		return formattedMessage;
	}
}
