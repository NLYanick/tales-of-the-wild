package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Location;

public class PlayerLayer {

	public Location getLocation() {
		Connection conn = DatabaseConnector.getConn();
		Location location = new Location();
		
		String query = "SELECT x, y FROM player";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				int x = rs.getInt("x");
				int y = rs.getInt("y");
				location = new Location(x, y);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return location;
	}
	
}
