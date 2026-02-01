package model;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.paint.Color;

public class Shop extends Building {

	private ArrayList<ShopItem> shopItems;
	private int floorPattern;
	
	public Shop(boolean canPass, BuildingType type, Direction exit, Location location, Location leaveLocation, int id, 
			Location entranceLocation, ArrayList<BuildingTile> tiles, HashMap<String, String> tileSettings, Color color, int floorPattern) {
		super(canPass, type, exit, location, leaveLocation, id, entranceLocation, tiles, tileSettings, color);
		
		this.floorPattern = floorPattern;
	}
	
	public void buy(Item item, Player player) {
		ShopItem shopItem = findShopItem(item);
		if(shopItem == null) return;
		
		if(player.canAffordPrice(shopItem.getPrice())) {			
			player.addItem(item);
			shopItems.remove(shopItem);
		} else {
			game.setShopError("Not enough coins");
		}
	}
	
	public ShopItem findShopItem(Item item) {
		if(item == null) return null;
		
		return shopItems.stream().filter(si -> si.getItem().getId() == item.getId()).findFirst().orElse(null);
	}
	
	public int getFloorPattern() { return floorPattern; }
	
	public void setShopItems(ArrayList<ShopItem> shopItems) { 
		this.shopItems = shopItems; 
	}
	public ArrayList<ShopItem> getShopItems() { return shopItems; }
	
}
