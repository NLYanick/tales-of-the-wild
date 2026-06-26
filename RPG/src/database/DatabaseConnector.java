package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

	private static Connection connection;
	private static String url;
	
	/*
	 * For database connection, download mysql-connector-j-8.3.0.jar 
	 * or another version and add it to the library
	 */
	public DatabaseConnector() {
		String host = System.getenv("DB_HOST");
		url = "jdbc:mysql://" + host + "/tales_of_the_wild";
		makeConnection();
	}
	
	// For environment variables go to: Run > Run configurations > Java Applications > Main (7) > Environment
	private static boolean makeConnection() {
		try {
			String username = System.getenv("DB_USERNAME");
			String password = System.getenv("DB_PASSWORD");
			connection = DriverManager.getConnection(url, username, password);
            
			System.out.println("Connected to Database");
			return true;
		} catch(SQLException s) {
			System.out.println("Database connection failed");
			System.out.println(s);
			return false;
		}
	}
	
	 public static Connection getConn() {
    	if(connection == null) {
    		makeConnection();
    	}
        return connection;
    }
	
}
