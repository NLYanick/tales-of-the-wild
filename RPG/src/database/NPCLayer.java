package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
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
	
	public ArrayList<NPC> getBuildingNPCs(int buildingId) {
		ArrayList<NPC> npcs = new ArrayList<NPC>();
		
		String query = "SELECT * FROM npc WHERE building_id = " + buildingId + ";";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				npcs.add(makeNPC(rs));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return npcs;
	}
	
	private NPC makeNPC(ResultSet rs) {
		NPC npc = null;
		try {
			Location location = new Location(rs.getInt("x"), rs.getInt("y"));
			ArrayList<Dialog> dialog = getDialog(rs.getString("dialog"));
			
			String directionString = rs.getString("direction");
			
			if(directionString != null) {
				npc = new NPC(rs.getString("url"), location, Direction.valueOf(directionString), rs.getString("name"), dialog, 
						rs.getInt("id"), rs.getBoolean("shop_seller"));
			} else {
				npc = new NPC(rs.getString("url"), location, rs.getString("name"), dialog, rs.getInt("id"), rs.getBoolean("shop_seller"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return npc;
	}
	
	public ArrayList<NPC> getAllNPCs(int gameId) {
		ArrayList<NPC> npcs = new ArrayList<NPC>();
		
		String query = "SELECT * FROM npc WHERE game_id = " + gameId + " AND building_id IS NULL;";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				npcs.add(makeNPC(rs));
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
	
		if(dialogData.isEmpty() || dialogData == null) return dialogs;
		
		JSONObject obj = new JSONObject(dialogData);
		JSONArray dialogArray = obj.getJSONArray("dialogs");
		
		for(Object dialogObj : dialogArray) {
			JSONObject dialogJSON = new JSONObject(dialogObj.toString());
			
			String text = "";
			int item = -1;
			int optionChosen = -1;
			HashMap<Integer, String> options = new HashMap<Integer, String>();
			String action = "";
			
			if(dialogJSON.has("text")) text = dialogJSON.getString("text");
			if(dialogJSON.has("item")) item = dialogJSON.getInt("item");
			if(dialogJSON.has("option-chosen")) optionChosen = dialogJSON.getInt("option-chosen");
			if(dialogJSON.has("options")) options = getOptionsArray(dialogJSON.getJSONArray("options"));
			if(dialogJSON.has("action")) action = dialogJSON.getString("action");
			
			Dialog dialog = new Dialog(text, dialogJSON.getBoolean("skip"), item, 
					dialogJSON.getBoolean("interactive"), options, optionChosen, action);
			
			if(dialogJSON.has("played")) dialog.setHasPlayed(dialogJSON.getBoolean("played"));
			
			dialogs.add(dialog);
		}
		
		return dialogs;
	}
	
	private HashMap<Integer, String> getOptionsArray(JSONArray optionsJSON) {
		if(optionsJSON.length() > 4) return null;
		
		HashMap<Integer, String> options = new HashMap<Integer, String>();
		
		int i = 1;
		for(Object key : optionsJSON) {
			options.put(i, key.toString());
			i++;
		}
		
		return options;
	}

	public Location getOriginalNPCBuildingLocation(NPC npc, int buildingId) {
		String query = "SELECT x, y FROM npc WHERE building_id = " + buildingId + ";";
		
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
		String fullString = "{\"dialogs\": [ ";
		
		for(Dialog dialog : dialogs) {
			fullString += "{ " + dialog.toJSON() + " }, ";
		}
		
		String suffix = ", ";
		if (fullString.endsWith(suffix))
			fullString = fullString.substring(0, fullString.length() - suffix.length());
		
		fullString += "] }";
		
		return fullString;
	}
	
}
