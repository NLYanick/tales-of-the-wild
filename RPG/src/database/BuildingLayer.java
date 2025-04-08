package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import controller.MainController;
import model.Building;
import model.BuildingType;
import model.Direction;
import model.Location;
import model.Size;

public class BuildingLayer {

	private Connection conn;
	private MainController controller;
	
	public BuildingLayer(MainController controller) {
		conn = DatabaseConnector.getConn();
		this.controller = controller;
	}
	
	public ArrayList<Building> getAllBuildings() {
		ArrayList<Building> buildings = new ArrayList<Building>();
		
		String query = "SELECT * FROM building";
		
		try {
			Statement stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Building building = new Building(new Location(rs.getInt("x"), rs.getInt("y")), rs.getBoolean("canPass"), 
						new Size(rs.getInt("width"), rs.getInt("height")), BuildingType.valueOf(rs.getString("type")), 
						Direction.valueOf(rs.getString("exit")), new Location(rs.getInt("leaveX"), rs.getInt("leaveY")), 
						controller, rs.getInt("id"));
				buildings.add(building);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return buildings;
	}
	
}
