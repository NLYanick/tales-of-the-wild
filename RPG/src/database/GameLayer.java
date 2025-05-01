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

	public Game getGame(int playerGameId) {
		String query = "SELECT * FROM game WHERE id = " + playerGameId + ";";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			Game game = null;
			while (rs.next()) {
				game = new Game(rs.getInt("id"));
			}

			rs.close();
			stmt.close();

			return game;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private int getLastGameId() {
		String query = "SELECT * FROM game ORDER BY id DESC LIMIT 1;";

		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			int gameId = 0;
			while (rs.next()) {
				gameId = rs.getInt("id");
			}

			rs.close();
			stmt.close();

			return gameId;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Game createGame() {
		int newGameId = getLastGameId() + 1;
		String query = "INSERT INTO game VALUES(?);";
		String queryTwo = "CALL InsertGameObjects(?);";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, newGameId);
			
			stmt.execute();
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
