package model;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.paint.Color;

public class Shop extends Building {

	private Inventory inventory;
	private int floorPattern;
	
	public Shop(boolean canPass, BuildingType type, Direction exit,
			Location leaveLocation, int id, Location entranceLocation, ArrayList<BuildingTile> tiles, 
			HashMap<String, String> tileSettings, Color color, int floorPattern) {
		super(canPass, type, exit, leaveLocation, id, entranceLocation, tiles, 
				tileSettings, color);
		
		this.floorPattern = floorPattern;

		inventory = new Inventory();
	}
	
	public void buy(Item item) {
		// if(player.currency > item.cost)
		inventory.removeItem(item);
		// player.addItem(item)
	}
	
	public void addShopItem(Item item) {
		inventory.addItem(item);
	}
	
	public int getFloorPattern() {
		return floorPattern;
	}
	
}
