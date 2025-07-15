package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Building;
import model.Item;
import model.Location;
import model.NPC;
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
						 rs.getString("description"), rs.getInt("id"));
				items.add(item);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}
	
	public ArrayList<Item> getAllWorldItems(int gameId) {
		ArrayList<Item> items = new ArrayList<Item>();
		
		String query = "SELECT * FROM item WHERE game_id = " + gameId + " AND npc_id IS NULL AND building_id IS NULL;";
		
		try {
			Statement stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Item item = new Item(new Location(rs.getInt("x"), rs.getInt("y")), rs.getString("name"), rs.getString("image_url"), 
						rs.getString("description"), rs.getInt("id"));
				items.add(item);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}
	
	public void savePlayerItems(Player player, ArrayList<Item> items) {
		String query = "UPDATE item SET player_name = ?, x = 0, y = 0 WHERE id = ?";
		
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
		    
			stmt.executeUpdate();
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void dropItem(Item item, Location location) {
		
		String query = "UPDATE item SET x = ?, y = ?, player_name = NULL WHERE id = ?";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, location.getX());
			stmt.setInt(2, location.getY());
			stmt.setInt(3, item.getId());
		    
			stmt.executeUpdate();
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addItemToPlayer(Item item, Player player) {
				
		String query = "UPDATE item SET x = ?, y = ?, player_name = ? WHERE id = ?";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, item.getX());
			stmt.setInt(2, item.getY());
			stmt.setString(3, player.getName());
			stmt.setInt(4, item.getId());
		    
			stmt.execute();
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeItemFromNPC(Item item) {
				
		String query = "UPDATE item SET npc_id = NULL WHERE id = ?";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, item.getId());
			
			stmt.execute();
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeItemFromBuilding(Item item) {
		
		String query = "UPDATE item SET building_id = NULL WHERE id = ?";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, item.getId());
			
			stmt.execute();
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Location getOriginalItemBuildingLocation(Item item, int buildingId) {
		String query = "SELECT x, y FROM item WHERE building_id = " + buildingId + ";";
		
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
	
	public ArrayList<Item> getBuildingItems(Building building) {
		ArrayList<Item> items = new ArrayList<Item>();
		
		String query = "SELECT * FROM item WHERE building_id = " + building.getId() + ";";
		
		try {
			Statement stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Item item = new Item(new Location(rs.getInt("x"), rs.getInt("y")), rs.getString("name"), rs.getString("image_url"), 
						rs.getString("description"), rs.getInt("id"));
				items.add(item);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}
	
	public ArrayList<Item> getNPCItems(NPC npc) {
		ArrayList<Item> items = new ArrayList<Item>();
		
		String query = "SELECT * FROM item WHERE npc_id = " + npc.getId() + ";";
		
		try {
			Statement stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Item item = new Item(new Location(rs.getInt("x"), rs.getInt("y")), rs.getString("name"), rs.getString("image_url"), 
						rs.getString("description"), rs.getInt("id"));
				items.add(item);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}
	
}
