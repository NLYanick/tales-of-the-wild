package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
		String query = "INSERT INTO player (name, x, y, game_id) VALUES(?, ?, ?, ?)";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, player.getName());
			stmt.setInt(2, player.getX());
			stmt.setInt(3, player.getY());
			stmt.setInt(4, player.getGameId());
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
	
	public ArrayList<Player> getAllPlayers() {
		ArrayList<Player> players = new ArrayList<Player>();
		
		String query = "SELECT * FROM player";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Location location = new Location(rs.getInt("x"), rs.getInt("y"));
				Player player = new Player(Player.DEFAULT_URL, location, rs.getString("name"), rs.getInt("game_id"));
				players.add(player);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return players;
	}
	
	public ArrayList<String> getAllPlayerNames() {
		ArrayList<String> playerNames = new ArrayList<String>();
		
		String query = "SELECT * FROM player";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				playerNames.add(rs.getString("name"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return playerNames;
	}
	
	public void deletePlayer(String playerName) {
		String query = "DELETE FROM player WHERE name = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, playerName);
			stmt.execute();
			
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public Player getPlayer(String playerName) {
		Player player = null;
		String query = "SELECT * FROM player WHERE name = '" + playerName + "';";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Location location = new Location(rs.getInt("x"), rs.getInt("y"));
				player = new Player(Player.DEFAULT_URL, location, rs.getString("name"), rs.getInt("game_id"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return player;
	}
	
}
