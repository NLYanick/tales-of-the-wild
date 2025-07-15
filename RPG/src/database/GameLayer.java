package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Game;

public class GameLayer {

	Connection conn;

	public GameLayer() {
		conn = DatabaseConnector.getConn();
	}
	
	public Game createGame() {
		int newGameId = 0;
		String query = "INSERT INTO game VALUES();";
		String queryTwo = "CALL InsertGameObjects(?);";
		
		try {
			
			PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate(); 

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
			    newGameId = rs.getInt(1);
			}
			rs.close();
			stmt.close();
			
			
			PreparedStatement stmtTwo = conn.prepareStatement(queryTwo);
			stmtTwo.setInt(1, newGameId);
			
			stmtTwo.execute();
			stmtTwo.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		Game game = new Game(newGameId);
		
		return game;
	}

	public Game getGameByPlayerName(String playerName) {
		Game game = null;
		String query = "SELECT game_id FROM vw_player_with_game WHERE player_name = '" + playerName + "';";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				game = new Game(rs.getInt("game_id"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return game;
	}

}
