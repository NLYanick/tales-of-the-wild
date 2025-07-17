package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import controller.MainController;
import javafx.scene.paint.Color;
import model.Building;
import model.BuildingType;
import model.Direction;
import model.Location;
import model.Shop;
import model.Size;

public class BuildingLayer {

	private Connection conn;
	private MainController controller;
	
	public BuildingLayer(MainController controller) {
		conn = DatabaseConnector.getConn();
		this.controller = controller;
	}
	
	public ArrayList<Building> getAllBuildings(int gameId) {
		ArrayList<Building> buildings = new ArrayList<Building>();
		
		String query = "SELECT * FROM building WHERE game_id = " + gameId + ";";
		
		try {
			Statement stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Building building;
				if(rs.getString("type").equals("SHOP")) {
					building = makeShop(rs);
				} else {
					building = makeBuilding(rs);
				}
				buildings.add(building);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return buildings;
	}
	
	private Shop makeShop(ResultSet rs) throws SQLException {
		return new Shop(new Location(rs.getInt("x"), rs.getInt("y")), rs.getBoolean("canPass"), 
				new Size(rs.getInt("width"), rs.getInt("height")), BuildingType.valueOf(rs.getString("type")), 
				Direction.valueOf(rs.getString("exit")), new Location(rs.getInt("leaveX"), rs.getInt("leaveY")), 
				controller, rs.getInt("id"), new Location(rs.getInt("entranceX"), rs.getInt("entranceY")), Color.valueOf(rs.getString("color")));
	}
	
	private Building makeBuilding(ResultSet rs) throws SQLException {
		return new Building(new Location(rs.getInt("x"), rs.getInt("y")), rs.getBoolean("canPass"), 
				new Size(rs.getInt("width"), rs.getInt("height")), BuildingType.valueOf(rs.getString("type")), 
				Direction.valueOf(rs.getString("exit")), new Location(rs.getInt("leaveX"), rs.getInt("leaveY")), 
				controller, rs.getInt("id"), new Location(rs.getInt("entranceX"), rs.getInt("entranceY")));
	}
	
}
