package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.paint.Color;
import model.Building;
import model.BuildingTile;
import model.BuildingTileType;
import model.BuildingType;
import model.Direction;
import model.Location;
import model.Shop;
import model.Size;

public class BuildingLayer {

	private Connection conn;
	
	public BuildingLayer() {
		conn = DatabaseConnector.getConn();
	}
	
	public ArrayList<Building> getAllBuildings(int gameId) {
		ArrayList<Building> buildings = new ArrayList<Building>();
		
		String query = "SELECT * FROM building WHERE game_id = " + gameId + ";";
		
		try {
			Statement stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(query);
						
			while(rs.next()) {
				String layout = rs.getString("layout");
				ArrayList<BuildingTile> tiles = getTiles(layout);
				HashMap<String, String> tileSettings = getSettings(layout);
				
				Building building;
				if(rs.getString("type").equals("SHOP")) {
					building = makeShop(rs, tiles, tileSettings);
				} else {
					building = makeBuilding(rs, tiles, tileSettings);
				}
				buildings.add(building);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return buildings;
	}
	
	private Shop makeShop(ResultSet rs, ArrayList<BuildingTile> tiles, HashMap<String, String> tileSettings) throws SQLException {
		return new Shop(new Location(rs.getInt("x"), rs.getInt("y")), rs.getBoolean("canPass"), 
				new Size(rs.getInt("width"), rs.getInt("height")), BuildingType.valueOf(rs.getString("type")), 
				Direction.valueOf(rs.getString("exit")), new Location(rs.getInt("leaveX"), rs.getInt("leaveY")), 
				rs.getInt("id"), new Location(rs.getInt("entranceX"), rs.getInt("entranceY")), tiles, 
				tileSettings, Color.valueOf(rs.getString("color")), (int) (Math.random() * 3));
	}
	
	private Building makeBuilding(ResultSet rs, ArrayList<BuildingTile> tiles, HashMap<String, String> tileSettings) throws SQLException {
		String colorString = rs.getString("Color");
		if(colorString == null) colorString = "TRANSPARENT";
		return new Building(new Location(rs.getInt("x"), rs.getInt("y")), rs.getBoolean("canPass"), 
				new Size(rs.getInt("width"), rs.getInt("height")), BuildingType.valueOf(rs.getString("type")), 
				Direction.valueOf(rs.getString("exit")), new Location(rs.getInt("leaveX"), rs.getInt("leaveY")), 
				rs.getInt("id"), new Location(rs.getInt("entranceX"), rs.getInt("entranceY")), 
				tiles, tileSettings, Color.valueOf(colorString));
	}
	
	private ArrayList<BuildingTile> getTiles(String layout) {
		ArrayList<BuildingTile> tiles = new ArrayList<BuildingTile>();
		
		JSONObject json = new JSONObject(layout);
		JSONArray tilesArray = json.getJSONArray("tiles");
		
		for(Object obj : tilesArray) {
			JSONObject tileJSON = new JSONObject(obj.toString());
			
			boolean spawn = false;
			if(tileJSON.has("spawn")) spawn = tileJSON.getBoolean("spawn");
			
			BuildingTileType type = BuildingTileType.valueOf(tileJSON.getString("type").toUpperCase());
			int x = tileJSON.getInt("x");
			int y = tileJSON.getInt("y");
			
			tiles.add(new BuildingTile(type, new Location(x, y), spawn));
		}
		
		return tiles;
	}
	
	private HashMap<String, String> getSettings(String layout) {
		HashMap<String, String> settings = new HashMap<String, String>();
		
		JSONObject json = new JSONObject(layout);
		
		if(!json.has("settings")) return settings;
		JSONObject settingsObject = json.getJSONObject("settings");
		
		for(String key : settingsObject.keySet()) {			
			settings.put(key, settingsObject.getString(key));
		}
		
		return settings;
	}
	
}
