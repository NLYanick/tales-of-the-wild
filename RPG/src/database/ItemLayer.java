package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Item;
import model.Location;
import model.Player;

public class ItemLayer {

	private Connection conn;
	
	public ItemLayer() {
		conn = DatabaseConnector.getConn();
	}
	
	public ArrayList<Item> getAllItemsOfPlayer(Player player) {
		ArrayList<Item> items = new ArrayList<Item>();
		
		String query = "SELECT * FROM item WHERE player_name = '" + player.getName() + "'";
		
		try {
			Statement stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Item item = new Item(new Location(rs.getInt("x"), rs.getInt("y")), rs.getString("name"), rs.getString("image_url"), 
						rs.getInt("building_id"), rs.getInt("id"), rs.getInt("npc_id"));
				items.add(item);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}
	
	public ArrayList<Item> getAllItems() {
		ArrayList<Item> items = new ArrayList<Item>();
		
		String query = "SELECT * FROM item";
		
		try {
			Statement stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Item item = new Item(new Location(rs.getInt("x"), rs.getInt("y")), rs.getString("name"), rs.getString("image_url"), 
						rs.getInt("building_id"), rs.getInt("id"), rs.getInt("npc_id"));
				items.add(item);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}
	
	public void savePlayerItems(Player player, ArrayList<Item> items) {
		String query = "UPDATE item SET player_name = ? WHERE id = ?";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			
		    for (Item item : items) {
		        stmt.setString(1, player.getName());
		        stmt.setInt(2, item.getId());
		        stmt.addBatch(); 
		        stmt.execute();
		    }
			
		    stmt.executeBatch();
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setItemLocation(Item item, Location location) {
		
		String query = "UPDATE item SET x = ?, y = ? WHERE id = ?";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, location.getX());
			stmt.setInt(2, location.getY());
			stmt.setInt(3, item.getId());
		    
			stmt.execute();
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addItemToPlayer(Item item, Player player) {
		
		String added = "";
		if(item.getNPCId() > 0) {
			added += ", npc_id = NULL";
		}
		if(item.getBuildingId() > 0) {
			added += ", building_id = NULL";
		}
		
		Location location = new Location(item.getX(), item.getY());
		
		String query = "UPDATE item SET x = ?, y = ?, player_name = ? " + added + " WHERE id = ?";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, location.getX());
			stmt.setInt(2, location.getY());
			stmt.setString(3, player.getName());
			stmt.setInt(4, item.getId());
		    
			stmt.execute();
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Location getOriginalItemBuildingLocation(Item item) {
		String query = "SELECT x, y FROM item WHERE building_id = " + item.getBuildingId() + ";";
		
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
	
}
