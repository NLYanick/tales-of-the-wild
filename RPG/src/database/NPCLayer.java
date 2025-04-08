package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Direction;
import model.Location;
import model.NPC;

public class NPCLayer {
	
	private Connection conn;
	
	public NPCLayer() {
		conn = DatabaseConnector.getConn();
	}
	
	public ArrayList<NPC> getAllNPCs() {
		ArrayList<NPC> npcs = new ArrayList<NPC>();
		
		String query = "SELECT * FROM npc";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Location location = new Location(rs.getInt("x"), rs.getInt("y"));
				List<String> dialog = getDialog(rs.getString("dialog"));
				
				String directionString = rs.getString("direction");
				NPC npc = null;
				if(directionString != null) {
					npc = new NPC(rs.getString("url"), location, Direction.valueOf(directionString), rs.getString("name"), dialog, rs.getInt("building_id"));					
				} else {					
					npc = new NPC(rs.getString("url"), location, rs.getString("name"), dialog, rs.getInt("building_id"));
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

	public static List<String> getDialog(String dialogData) {
		List<String> dialog = new ArrayList<String>();
		
		for(String text : dialogData.split(", ")) {
			dialog.add(text);
		}
		
		return dialog;
	}
	
}
