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
				Item item = new Item(new Location(rs.getInt("x"), rs.getInt("y")), rs.getString("name"), rs.getString("image_url"), rs.getInt("id"));
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
				Item item = new Item(new Location(rs.getInt("x"), rs.getInt("y")), rs.getString("name"), rs.getString("image_url"), rs.getInt("id"));
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
	
}
