package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Location;
import model.Player;

public class PlayerLayer {
	
	private Connection conn;
	
	public PlayerLayer() {
		conn = DatabaseConnector.getConn();
	}

	public Location getLocation(String name) {
		Location location = new Location();
		
		String query = "SELECT x, y FROM player WHERE name = '" + name + "'";
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
	
	public void saveNewPlayer(Player player) {
		String query = "INSERT INTO player (name, x, y) VALUES(?, ?, ?)";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, player.getName());
			stmt.setInt(2, player.getX());
			stmt.setInt(3, player.getY());
			stmt.execute();
			
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void saveLocation(Player player) {
		String query = "UPDATE player SET x = ?, y = ? WHERE name = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, player.getX());
			stmt.setInt(2, player.getY());
			stmt.setString(3, player.getName());
			stmt.execute();
			
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
}
