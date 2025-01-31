package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

	private static Connection connection;
	private static String url;
	
	public DatabaseConnector() {
		url = "jdbc:mysql://localhost:3306/tales_of_the_wild";
		makeConnection();
	}
	
	private static boolean makeConnection() {
		try {
			String username = "root";
			String password = "kr2fWa73S9!";
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
