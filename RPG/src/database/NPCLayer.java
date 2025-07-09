package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONObject;

import model.Dialog;
import model.Direction;
import model.Location;
import model.NPC;

public class NPCLayer {
	
	private Connection conn;
	
	public NPCLayer() {
		conn = DatabaseConnector.getConn();
	}
	
	public ArrayList<NPC> getAllNPCs(int gameId) {
		ArrayList<NPC> npcs = new ArrayList<NPC>();
		
		String query = "SELECT * FROM npc WHERE game_id = " + gameId + ";";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Location location = new Location(rs.getInt("x"), rs.getInt("y"));
				ArrayList<Dialog> dialog = getDialog(rs.getString("dialog"));
				
				String directionString = rs.getString("direction");
				NPC npc = null;
				if(directionString != null) {
					npc = new NPC(rs.getString("url"), location, Direction.valueOf(directionString), rs.getString("name"), dialog, 
							rs.getInt("building_id"), rs.getInt("id"), rs.getInt("game_id"));					
				} else {					
					npc = new NPC(rs.getString("url"), location, rs.getString("name"), dialog, rs.getInt("building_id"), 
							rs.getInt("id"), rs.getInt("game_id"));
				}
				
				npcs.add(npc);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return npcs;
	}

	private ArrayList<Dialog> getDialog(String dialogData) {
		ArrayList<Dialog> dialogs = new ArrayList<Dialog>();
	
		JSONObject obj = new JSONObject(dialogData);
		JSONObject dialogsJSON = obj.getJSONObject("dialogs");
		
		ArrayList<String> sortedKeys = new ArrayList<>(dialogsJSON.keySet());
		Collections.sort(sortedKeys);
		
		for(String key : sortedKeys) {
			JSONObject dialogJSON = dialogsJSON.getJSONObject(key);
			
			int item = -1;
			int optionChosen = -1;
			HashMap<Integer, String> options = new HashMap<Integer, String>();
			
			if(dialogJSON.has("item")) item = dialogJSON.getInt("item");
			if(dialogJSON.has("option-chosen")) optionChosen = dialogJSON.getInt("option-chosen");
			if(dialogJSON.has("options")) options = getOptionsArray(dialogJSON.getJSONObject("options"));
			
			Dialog dialog = new Dialog(dialogJSON.getString("text"), dialogJSON.getBoolean("skip"), item, 
					dialogJSON.getBoolean("interactive"), options, optionChosen);
			
			if(dialogJSON.has("played")) dialog.setHasPlayed(dialogJSON.getBoolean("played"));
			
			dialogs.add(dialog);
		}
		
		return dialogs;
	}
	
	private HashMap<Integer, String> getOptionsArray(JSONObject optionsJSON) {
		HashMap<Integer, String> options = new HashMap<Integer, String>();
		
		ArrayList<String> sortedValues = new ArrayList<>(optionsJSON.keySet());
		Collections.sort(sortedValues);
		
		int i = 1;
		for(String key : sortedValues) {
			options.put(i, optionsJSON.getString(key));
			i++;
		}
		
		return options;
	}

	public Location getOriginalNPCBuildingLocation(NPC npc) {
		String query = "SELECT x, y FROM npc WHERE building_id = " + npc.getBuildingId() + ";";
		
		Location location = new Location();
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
		} catch(SQLException s) {
			s.printStackTrace();
		}
		return location;
	}
	
	public void saveDialogs(NPC npc) {
		String query = "UPDATE npc SET dialog = ? WHERE id = ?";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, getFullDialogString(npc.getDialogs()));
			stmt.setInt(2, npc.getId());
			stmt.execute();
			
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getFullDialogString(ArrayList<Dialog> dialogs) {
		String fullString = "{\"dialogs\": { ";
		int i = 1;
		
		for(Dialog dialog : dialogs) {
			fullString += "\"dialog-" + i + "\": { " + dialog.toJSON() + " }, ";
			i++;
		}
		fullString += " }}";
		
		return fullString;
	}
	
}
