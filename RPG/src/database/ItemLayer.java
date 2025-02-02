package database;

import java.sql.Connection;
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
		
		String query = "SELECT * FROM item WHERE player_name = ?";
		
		try {
			Statement stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Item item = new Item(new Location(rs.getInt("x"), rs.getInt("y")), rs.getString("name"), "");
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
				Item item = new Item(new Location(rs.getInt("x"), rs.getInt("y")), rs.getString("name"), rs.getString("image_url"));
				items.add(item);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}
	
}
